import { NextFunction, Request, Response } from 'express';
import jwt from 'jsonwebtoken';
import { env } from '../config/env';
import { AppError } from '../utils/AppError';

export interface SuperAdminPayload {
  superAdminId: number;
  email: string;
  isSuperAdmin: true;
}

declare global {
  namespace Express {
    interface Request {
      superAdmin?: SuperAdminPayload;
    }
  }
}

/** Guards the platform-level super-admin routes (school onboarding, etc.) - separate from tenant auth. */
export function superAdminAuth(req: Request, _res: Response, next: NextFunction) {
  const header = req.header('Authorization');
  if (!header || !header.startsWith('Bearer ')) return next(new AppError(401, 'Missing Authorization header'));
  try {
    const payload = jwt.verify(header.replace('Bearer ', ''), env.jwt.secret) as SuperAdminPayload;
    if (!payload.isSuperAdmin) return next(new AppError(403, 'Not a super admin token'));
    req.superAdmin = payload;
    next();
  } catch {
    return next(new AppError(401, 'Invalid or expired token'));
  }
}
