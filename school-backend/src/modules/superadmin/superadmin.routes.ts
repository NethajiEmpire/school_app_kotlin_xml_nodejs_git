import { Router, Request, Response } from 'express';
import { asyncHandler } from '../../utils/asyncHandler';
import { created, ok } from '../../utils/apiResponse';
import { AppError } from '../../utils/AppError';
import { getMasterPool } from '../../core/masterDb';
import { provisionSchoolDatabase } from '../../core/tenantManager';
import { exec, insertRow, query, queryOne } from '../../core/dbHelpers';
import { comparePassword, hashPassword } from '../../utils/hash';
import { signSuperAdminToken } from '../../utils/jwt';
import { superAdminAuth } from '../../middlewares/superAdminAuth.middleware';

const router = Router();

interface SuperAdminRow { id: number; name: string; email: string; password_hash: string }

router.post(
  '/login',
  asyncHandler(async (req: Request, res: Response) => {
    const { email, password } = req.body;
    if (!email || !password) throw new AppError(400, 'email and password are required');
    const pool = getMasterPool();
    const admin = await queryOne<SuperAdminRow>(pool, 'SELECT * FROM super_admins WHERE email = ?', [email]);
    if (!admin || !(await comparePassword(password, admin.password_hash))) throw new AppError(401, 'Invalid credentials');
    const token = signSuperAdminToken({ superAdminId: admin.id, email: admin.email });
    return ok(res, { token, name: admin.name, email: admin.email }, 'Login successful');
  }),
);

router.post(
  '/change-password',
  superAdminAuth,
  asyncHandler(async (req: Request, res: Response) => {
    const { newPassword } = req.body;
    if (!newPassword) throw new AppError(400, 'newPassword is required');
    const pool = getMasterPool();
    const passwordHash = await hashPassword(newPassword);
    await exec(pool, 'UPDATE super_admins SET password_hash = ? WHERE id = ?', [passwordHash, req.superAdmin!.superAdminId]);
    return ok(res, null, 'Password changed');
  }),
);

function toDbName(schoolCode: string): string {
  return `school_${schoolCode.toLowerCase().replace(/[^a-z0-9]/g, '')}`;
}

/** Onboard a brand-new school: creates its isolated database + registers it in master */
router.post(
  '/schools',
  superAdminAuth,
  asyncHandler(async (req: Request, res: Response) => {
    const b = req.body;
    if (!b.schoolName || !b.schoolCode) throw new AppError(400, 'schoolName and schoolCode are required');
    const schoolCode = String(b.schoolCode).toUpperCase();
    const dbName = toDbName(schoolCode);

    const pool = getMasterPool();
    const existing = await queryOne(pool, 'SELECT id FROM schools WHERE school_code = ?', [schoolCode]);
    if (existing) throw new AppError(409, 'A school with this code already exists');

    await provisionSchoolDatabase(dbName);

    const id = await insertRow(pool, 'schools', {
      school_code: schoolCode,
      school_name: b.schoolName,
      db_name: dbName,
      address: b.address || null,
      city: b.city || null,
      state: b.state || null,
      pincode: b.pincode || null,
      contact_email: b.contactEmail || null,
      contact_phone: b.contactPhone || null,
      logo_url: b.logoUrl || null,
      status: 'active',
    });

    const row = await queryOne(pool, 'SELECT * FROM schools WHERE id = ?', [id]);
    return created(res, row, 'School onboarded successfully. Give the school_code to the mobile app team.');
  }),
);

router.get(
  '/schools',
  superAdminAuth,
  asyncHandler(async (_req: Request, res: Response) => {
    const pool = getMasterPool();
    const rows = await query(pool, 'SELECT * FROM schools ORDER BY id DESC');
    return ok(res, rows);
  }),
);

router.get(
  '/schools/:id',
  superAdminAuth,
  asyncHandler(async (req: Request, res: Response) => {
    const pool = getMasterPool();
    const row = await queryOne(pool, 'SELECT * FROM schools WHERE id = ?', [req.params.id]);
    if (!row) throw new AppError(404, 'School not found');
    return ok(res, row);
  }),
);

router.put(
  '/schools/:id/status',
  superAdminAuth,
  asyncHandler(async (req: Request, res: Response) => {
    const { status } = req.body;
    if (!['active', 'inactive', 'suspended'].includes(status)) throw new AppError(400, 'Invalid status');
    const pool = getMasterPool();
    await exec(pool, 'UPDATE schools SET status = ? WHERE id = ?', [status, req.params.id]);
    return ok(res, null, 'School status updated');
  }),
);

export default router;
