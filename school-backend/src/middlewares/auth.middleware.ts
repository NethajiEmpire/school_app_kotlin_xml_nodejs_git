/// <reference path="../types/express.d.ts" />
import { NextFunction, Request, Response } from 'express';
import { AppError } from '../utils/AppError';
import { verifyAccessToken } from '../utils/jwt';

/** Requires a valid "Authorization: Bearer <token>" header. Must run AFTER tenantMiddleware. */
export function authMiddleware(req: Request, _res: Response, next: NextFunction) {
  const header = req.header('Authorization');
  if (!header || !header.startsWith('Bearer ')) {
    return next(new AppError(401, 'Missing or invalid Authorization header'));
  }

  const token = header.replace('Bearer ', '').trim();
  try {
    const payload = verifyAccessToken(token);

    // Make sure the token actually belongs to the school in the request context
    if (req.school && payload.schoolCode !== req.school.school_code) {
      return next(new AppError(403, 'Token does not belong to this school'));
    }

    req.user = payload;
    next();
  } catch {
    return next(new AppError(401, 'Session expired or invalid token. Please log in again.'));
  }
}

/** Restrict a route to one or more role names, e.g. requireRole('admin', 'teacher') */
export function requireRole(...roles: string[]) {
  return (req: Request, _res: Response, next: NextFunction) => {
    if (!req.user) return next(new AppError(401, 'Not authenticated'));
    if (!roles.map((r) => r.toLowerCase()).includes(req.user.roleName.toLowerCase())) {
      return next(new AppError(403, `This action requires one of these roles: ${roles.join(', ')}`));
    }
    next();
  };
}
