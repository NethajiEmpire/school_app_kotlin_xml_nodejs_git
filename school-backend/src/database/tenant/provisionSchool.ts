/* eslint-disable no-console */
import readline from 'readline';
import { getMasterPool } from '../../core/masterDb';
import { provisionSchoolDatabase } from '../../core/tenantManager';

const rl = readline.createInterface({ input: process.stdin, output: process.stdout });
const ask = (q: string): Promise<string> => new Promise((resolve) => rl.question(q, resolve));

function toDbName(schoolCode: string): string {
  return `school_${schoolCode.toLowerCase().replace(/[^a-z0-9]/g, '')}`;
}

async function run() {
  console.log('=== Provision a new school (tenant) ===\n');

  const schoolName = await ask('School name: ');
  const schoolCode = (await ask('School code (short, unique, e.g. STMARY001): ')).trim().toUpperCase();
  const contactEmail = await ask('Contact email: ');
  const contactPhone = await ask('Contact phone: ');
  const city = await ask('City: ');
  rl.close();

  const dbName = toDbName(schoolCode);

  console.log(`\nCreating database "${dbName}" and running tenant schema...`);
  await provisionSchoolDatabase(dbName);

  console.log('Registering school in master database...');
  const pool = getMasterPool();
  await pool.query(
    `INSERT INTO schools (school_code, school_name, db_name, contact_email, contact_phone, city, status)
     VALUES (?, ?, ?, ?, ?, ?, 'active')`,
    [schoolCode, schoolName, dbName, contactEmail, contactPhone, city],
  );

  console.log('\nSchool provisioned successfully!');
  console.log(`  school_code : ${schoolCode}   <-- give this to the school; the app sends it as X-School-Code`);
  console.log(`  database    : ${dbName}`);
  console.log('\nNext: create the first admin user for this school via POST /api/:schoolCode/auth/register (roleName="admin")');
  process.exit(0);
}

run().catch((err) => {
  console.error('Provisioning failed:', err);
  process.exit(1);
});
