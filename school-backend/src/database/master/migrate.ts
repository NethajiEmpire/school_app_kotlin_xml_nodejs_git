/* eslint-disable no-console */
import fs from 'fs';
import path from 'path';
import mysql from 'mysql2/promise';
import { env } from '../../config/env';
import { hashPassword } from '../../utils/hash';

async function run() {
  console.log('Connecting to MySQL...');
  const root = await mysql.createConnection({
    host: env.db.host,
    port: env.db.port,
    user: env.db.user,
    password: env.db.password,
    multipleStatements: true,
  });

  console.log(`Creating master database "${env.masterDbName}" if it doesn't exist...`);
  await root.query(`CREATE DATABASE IF NOT EXISTS \`${env.masterDbName}\` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci`);
  await root.changeUser({ database: env.masterDbName });

  const schemaPath = path.join(__dirname, 'schema.sql');
  const schemaSql = fs.readFileSync(schemaPath, 'utf-8');
  console.log('Running master schema.sql...');
  await root.query(schemaSql);

  // Seed the first super admin if none exists yet
  const [rows] = await root.query('SELECT COUNT(*) as cnt FROM super_admins');
  const count = (rows as { cnt: number }[])[0].cnt;
  if (count === 0) {
    const passwordHash = await hashPassword(env.superAdmin.password);
    await root.query('INSERT INTO super_admins (name, email, password_hash) VALUES (?, ?, ?)', [
      'Platform Super Admin',
      env.superAdmin.email,
      passwordHash,
    ]);
    console.log(`Seeded super admin -> email: ${env.superAdmin.email}  password: ${env.superAdmin.password}`);
    console.log('IMPORTANT: change this password after first login.');
  } else {
    console.log('Super admin already exists, skipping seed.');
  }

  await root.end();
  console.log('Master database is ready.');
}

run().catch((err) => {
  console.error('Master migration failed:', err);
  process.exit(1);
});
