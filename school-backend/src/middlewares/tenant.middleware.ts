/// <reference path="../types/express.d.ts" />
import { NextFunction, Request, Response } from 'express';
import { resolveTenant } from '../core/tenantManager';
import { AppError } from '../utils/AppError';
import { asyncHandler } from '../utils/asyncHandler';

/**
 * Every school-scoped request must identify which school it belongs to.
 * The Android app sends this as a header: "X-School-Code: STMARY001"
 * (captured once at login/school-selection screen and cached on device).
 * Falls back to req.body.schoolCode / req.query.schoolCode for convenience.
 */
export const tenantMiddleware = asyncHandler(async (req: Request, _res: Response, next: NextFunction) => {
  const schoolCode =
    (req.header('X-School-Code') as string) ||
    (req.body && req.body.schoolCode) ||
    (req.query.schoolCode as string) ||
    (req.params.schoolCode as string);

  if (!schoolCode) {
    throw new AppError(400, 'Missing school code. Send it in the "X-School-Code" header.');
  }

  const { school, pool } = await resolveTenant(schoolCode);
  req.school = school;
  req.db = pool;
  next();
});
