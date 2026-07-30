import { Pool } from 'mysql2/promise';
import { AppError } from '../../utils/AppError';
import { exec, insertRow, query, queryOne } from '../../core/dbHelpers';
import { comparePassword, generateOtp, hashPassword } from '../../utils/hash';
import { env } from '../../config/env';
import { SchoolRow } from '../../core/tenantManager';
import { signAccessToken, signRefreshToken, verifyRefreshToken } from '../../utils/jwt';

interface RoleRow { id: number; name: string }
interface UserRow {
  id: number;
  role_id: number;
  name: string;
  mobile: string;
  email: string | null;
  password_hash: string;
  status: 'active' | 'inactive';
}

export async function sendOtp(db: Pool, mobile: string, purpose: 'register' | 'login' | 'forgot_password') {
  const otp = generateOtp();
  const expiresAt = new Date(Date.now() + env.otpExpiryMinutes * 60 * 1000);
  await insertRow(db, 'otp_verifications', {
    mobile,
    otp,
    purpose,
    expires_at: expiresAt.toISOString().slice(0, 19).replace('T', ' '),
  });

  // NOTE: plug in an SMS gateway (MSG91, Twilio, etc.) here in production.
  // Returned in the response only so the app is testable without an SMS provider.
  return { otp, expiresInMinutes: env.otpExpiryMinutes };
}

export async function verifyOtp(db: Pool, mobile: string, otp: string) {
  const row = await queryOne<{ id: number; expires_at: string }>(
    db,
    `SELECT id, expires_at FROM otp_verifications
     WHERE mobile = ? AND otp = ? AND is_verified = 0
     ORDER BY id DESC LIMIT 1`,
    [mobile, otp],
  );
  if (!row) throw new AppError(400, 'Invalid OTP');
  if (new Date(row.expires_at).getTime() < Date.now()) throw new AppError(400, 'OTP expired. Please request a new one.');

  await exec(db, 'UPDATE otp_verifications SET is_verified = 1 WHERE id = ?', [row.id]);
  return true;
}

export async function register(
  db: Pool,
  school: SchoolRow,
  input: { name: string; mobile: string; email?: string; password: string; roleName: string },
) {
  const role = await queryOne<RoleRow>(db, 'SELECT id, name FROM roles WHERE name = ?', [input.roleName.toLowerCase()]);
  if (!role) throw new AppError(400, `Unknown role "${input.roleName}"`);

  const existing = await queryOne<UserRow>(db, 'SELECT id FROM users WHERE mobile = ?', [input.mobile]);
  if (existing) throw new AppError(409, 'An account with this mobile number already exists');

  const passwordHash = await hashPassword(input.password);
  const userId = await insertRow(db, 'users', {
    role_id: role.id,
    name: input.name,
    mobile: input.mobile,
    email: input.email || null,
    password_hash: passwordHash,
    status: 'active',
  });

  return buildAuthResponse(db, school, userId);
}

export async function login(db: Pool, school: SchoolRow, mobileOrEmail: string, password: string) {
  const user = await queryOne<UserRow>(
    db,
    'SELECT * FROM users WHERE (mobile = ? OR email = ?) LIMIT 1',
    [mobileOrEmail, mobileOrEmail],
  );
  if (!user) throw new AppError(401, 'Invalid credentials');
  if (user.status !== 'active') throw new AppError(403, 'Your account is inactive. Contact school admin.');

  const matches = await comparePassword(password, user.password_hash);
  if (!matches) throw new AppError(401, 'Invalid credentials');

  await exec(db, 'UPDATE users SET last_login_at = NOW() WHERE id = ?', [user.id]);

  return buildAuthResponse(db, school, user.id);
}

export async function forgotPassword(db: Pool, mobile: string, newPassword: string) {
  const user = await queryOne<UserRow>(db, 'SELECT id FROM users WHERE mobile = ?', [mobile]);
  if (!user) throw new AppError(404, 'No account found for this mobile number');
  const passwordHash = await hashPassword(newPassword);
  await exec(db, 'UPDATE users SET password_hash = ? WHERE id = ?', [passwordHash, user.id]);
  return true;
}

export async function refreshSession(db: Pool, school: SchoolRow, refreshToken: string) {
  let payload: { userId: number; schoolCode: string };
  try {
    payload = verifyRefreshToken(refreshToken);
  } catch {
    throw new AppError(401, 'Invalid or expired refresh token. Please log in again.');
  }
  if (payload.schoolCode !== school.school_code) throw new AppError(403, 'Refresh token does not belong to this school');
  return buildAuthResponse(db, school, payload.userId);
}

async function buildAuthResponse(db: Pool, school: SchoolRow, userId: number) {
  const user = await queryOne<UserRow & { role_name: string }>(
    db,
    `SELECT u.*, r.name as role_name FROM users u JOIN roles r ON r.id = u.role_id WHERE u.id = ?`,
    [userId],
  );
  if (!user) throw new AppError(404, 'User not found');

  const accessToken = signAccessToken({
    userId: user.id,
    roleId: user.role_id,
    roleName: user.role_name,
    schoolCode: school.school_code,
    dbName: school.db_name,
  });
  const refreshToken = signRefreshToken({ userId: user.id, schoolCode: school.school_code });

  return {
    accessToken,
    refreshToken,
    user: {
      id: user.id,
      name: user.name,
      mobile: user.mobile,
      email: user.email,
      role: user.role_name,
    },
    school: { schoolCode: school.school_code, schoolName: school.school_name, logoUrl: school.logo_url },
  };
}

export async function getRoles(db: Pool) {
  return query(db, 'SELECT id, name FROM roles ORDER BY id');
}
