import mysql, { Pool } from 'mysql2/promise';
import { env } from '../config/env';

/**
 * The MASTER database only stores the registry of schools (tenants) and
 * super-admin accounts. It never stores students/teachers/attendance/etc.
 * Each school's actual data lives in its own isolated database
 * (see core/tenantManager.ts).
 */
let masterPool: Pool | null = null;

export function getMasterPool(): Pool {
  if (!masterPool) {
    masterPool = mysql.createPool({
      host: env.db.host,
      port: env.db.port,
      user: env.db.user,
      password: env.db.password,
      database: env.masterDbName,
      waitForConnections: true,
      connectionLimit: 10,
      queueLimit: 0,
      dateStrings: true,
    });
  }
  return masterPool;
}

/** Pool without a selected database - used only to create the master DB itself on first run. */
export function getRootPool(): Pool {
  return mysql.createPool({
    host: env.db.host,
    port: env.db.port,
    user: env.db.user,
    password: env.db.password,
    waitForConnections: true,
    connectionLimit: 5,
  });
}
