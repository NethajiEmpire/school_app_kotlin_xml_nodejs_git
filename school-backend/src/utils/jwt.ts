import jwt, { SignOptions } from 'jsonwebtoken';
import { env } from '../config/env';

export interface AuthTokenPayload {
  userId: number;
  roleId: number;
  roleName: string;
  schoolCode: string;
  dbName: string;
}

export function signAccessToken(payload: AuthTokenPayload): string {
  return jwt.sign(payload, env.jwt.secret, { expiresIn: env.jwt.expiresIn } as SignOptions);
}

export function signRefreshToken(payload: Pick<AuthTokenPayload, 'userId' | 'schoolCode'>): string {
  return jwt.sign(payload, env.jwt.refreshSecret, { expiresIn: env.jwt.refreshExpiresIn } as SignOptions);
}

export function verifyAccessToken(token: string): AuthTokenPayload {
  return jwt.verify(token, env.jwt.secret) as AuthTokenPayload;
}

export function verifyRefreshToken(token: string): { userId: number; schoolCode: string } {
  return jwt.verify(token, env.jwt.refreshSecret) as { userId: number; schoolCode: string };
}

/** Separate, short-lived token type used only for the super-admin (master DB) panel. */
export function signSuperAdminToken(payload: { superAdminId: number; email: string }): string {
  return jwt.sign({ ...payload, isSuperAdmin: true }, env.jwt.secret, { expiresIn: '1d' });
}
