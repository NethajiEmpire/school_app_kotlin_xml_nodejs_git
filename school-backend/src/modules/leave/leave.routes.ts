import { Router, Request, Response } from 'express';
import { authMiddleware, requireRole } from '../../middlewares/auth.middleware';
import { asyncHandler } from '../../utils/asyncHandler';
import { created, ok } from '../../utils/apiResponse';
import { AppError } from '../../utils/AppError';
import { exec, insertRow, paginate, queryOne } from '../../core/dbHelpers';

const router = Router();

/** Any user (student/teacher/staff) raises a leave request */
router.post(
  '/',
  authMiddleware,
  asyncHandler(async (req: Request, res: Response) => {
    const b = req.body;
    if (!b.leaveTypeId || !b.fromDate || !b.toDate || !b.reason) {
      throw new AppError(400, 'leaveTypeId, fromDate, toDate, reason are required');
    }
    const id = await insertRow(req.db!, 'leave_requests', {
      user_id: req.user!.userId, leave_type_id: b.leaveTypeId,
      from_date: b.fromDate, to_date: b.toDate, reason: b.reason,
    });
    const row = await queryOne(req.db!, 'SELECT * FROM leave_requests WHERE id = ?', [id]);
    return created(res, row, 'Leave request submitted');
  }),
);

/** My leave requests */
router.get(
  '/mine',
  authMiddleware,
  asyncHandler(async (req: Request, res: Response) => {
    const baseSql = `SELECT lr.*, lt.name as leave_type FROM leave_requests lr
      JOIN leave_types lt ON lt.id = lr.leave_type_id WHERE lr.user_id = ? ORDER BY lr.applied_at DESC`;
    const countSql = `SELECT COUNT(*) as total FROM leave_requests WHERE user_id = ?`;
    const result = await paginate(req.db!, baseSql, countSql, [req.user!.userId], Number(req.query.page) || 1, Number(req.query.limit) || 20);
    return ok(res, result);
  }),
);

/** All leave requests (admin view, filterable by status) */
router.get(
  '/',
  authMiddleware,
  requireRole('admin'),
  asyncHandler(async (req: Request, res: Response) => {
    const { status, page, limit } = req.query;
    const conditions: string[] = [];
    const params: unknown[] = [];
    if (status) { conditions.push('lr.status = ?'); params.push(status); }
    const where = conditions.length ? `WHERE ${conditions.join(' AND ')}` : '';
    const baseSql = `SELECT lr.*, lt.name as leave_type, u.name as applicant_name
      FROM leave_requests lr JOIN leave_types lt ON lt.id = lr.leave_type_id JOIN users u ON u.id = lr.user_id
      ${where} ORDER BY lr.applied_at DESC`;
    const countSql = `SELECT COUNT(*) as total FROM leave_requests lr ${where}`;
    const result = await paginate(req.db!, baseSql, countSql, params, Number(page) || 1, Number(limit) || 20);
    return ok(res, result);
  }),
);

/** Approve/reject a leave request */
router.put(
  '/:id/decision',
  authMiddleware,
  requireRole('admin'),
  asyncHandler(async (req: Request, res: Response) => {
    const { status } = req.body;
    if (!['approved', 'rejected'].includes(status)) throw new AppError(400, 'status must be approved or rejected');
    await exec(req.db!, 'UPDATE leave_requests SET status = ?, approved_by = ? WHERE id = ?', [status, req.user!.userId, req.params.id]);
    const row = await queryOne(req.db!, 'SELECT * FROM leave_requests WHERE id = ?', [req.params.id]);
    return ok(res, row, `Leave request ${status}`);
  }),
);

/** Available leave balance (simple: fixed quota minus approved days taken this year) */
router.get(
  '/balance',
  authMiddleware,
  asyncHandler(async (req: Request, res: Response) => {
    const ANNUAL_QUOTA = 12;
    const row = await queryOne<{ used_days: number }>(
      req.db!,
      `SELECT COALESCE(SUM(DATEDIFF(to_date, from_date) + 1), 0) as used_days
       FROM leave_requests WHERE user_id = ? AND status = 'approved' AND YEAR(from_date) = YEAR(CURDATE())`,
      [req.user!.userId],
    );
    const used = row?.used_days || 0;
    return ok(res, { annualQuota: ANNUAL_QUOTA, used, remaining: Math.max(0, ANNUAL_QUOTA - used) });
  }),
);

export default router;
