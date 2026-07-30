import mysql, { Pool } from 'mysql2/promise';
import fs from 'fs';
import path from 'path';
import { env } from '../config/env';
import { getMasterPool, getRootPool } from './masterDb';
import { AppError } from '../utils/AppError';

/**
 * Caches one MySQL connection pool per school database so we don't open a
 * fresh pool on every request. Key = db_name (e.g. "school_stmarys").
 */
const tenantPools = new Map<string, Pool>();

export interface SchoolRow {
  id: number;
  school_code: string;
  school_name: string;
  db_name: string;
  status: 'active' | 'inactive' | 'suspended';
  contact_email: string | null;
  contact_phone: string | null;
  logo_url: string | null;
  created_at: string;
}

/** Look up a school by its unique school_code (from master DB). */
export async function getSchoolByCode(schoolCode: string): Promise<SchoolRow | null> {
  const pool = getMasterPool();
  const [rows] = await pool.query('SELECT * FROM schools WHERE school_code = ? LIMIT 1', [schoolCode]);
  const list = rows as SchoolRow[];
  return list.length ? list[0] : null;
}

export async function getSchoolById(id: number): Promise<SchoolRow | null> {
  const pool = getMasterPool();
  const [rows] = await pool.query('SELECT * FROM schools WHERE id = ? LIMIT 1', [id]);
  const list = rows as SchoolRow[];
  return list.length ? list[0] : null;
}

/** Get (and cache) the connection pool for one school's database. */
export function getTenantPool(dbName: string): Pool {
  const cached = tenantPools.get(dbName);
  if (cached) return cached;

  const pool = mysql.createPool({
    host: env.db.host,
    port: env.db.port,
    user: env.db.user,
    password: env.db.password,
    database: dbName,
    waitForConnections: true,
    connectionLimit: 10,
    queueLimit: 0,
    dateStrings: true,
  });

  tenantPools.set(dbName, pool);
  return pool;
}

/** Convenience: resolve school by code + return its ready-to-use pool. Throws 404 if not found/inactive. */
export async function resolveTenant(schoolCode: string): Promise<{ school: SchoolRow; pool: Pool }> {
  const school = await getSchoolByCode(schoolCode);
  if (!school) throw new AppError(404, `No school found for code "${schoolCode}"`);
  if (school.status !== 'active') throw new AppError(403, `School "${school.school_name}" is ${school.status}. Contact the platform admin.`);
  const pool = getTenantPool(school.db_name);
  return { school, pool };
}

/**
 * Creates a brand-new isolated database for a school and runs the tenant
 * schema (schema.sql) against it. Called from the super-admin
 * "create school" endpoint and from the `provision:school` CLI script.
 */
export async function provisionSchoolDatabase(dbName: string): Promise<void> {
  const root = getRootPool();
  const safeName = dbName.replace(/[^a-zA-Z0-9_]/g, '');
  if (!safeName || safeName !== dbName) {
    throw new AppError(400, 'Invalid database name. Use letters, numbers and underscores only.');
  }

  await root.query(`CREATE DATABASE IF NOT EXISTS \`${safeName}\` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci`);

  const schemaPath = path.join(__dirname, '..', 'database', 'tenant', 'schema.sql');
  const schemaSql = fs.readFileSync(schemaPath, 'utf-8');

  // mysql2 needs multipleStatements enabled to run a whole .sql file at once
  const conn = await mysql.createConnection({
    host: env.db.host,
    port: env.db.port,
    user: env.db.user,
    password: env.db.password,
    database: safeName,
    multipleStatements: true,
  });

  try {
    await conn.query(schemaSql);
  } finally {
    await conn.end();
  }
}

export function closeAllTenantPools(): Promise<void[]> {
  const closers = Array.from(tenantPools.values()).map((p) => p.end());
  tenantPools.clear();
  return Promise.all(closers);
}
