import { Router, Request, Response } from 'express';
import { authMiddleware, requireRole } from '../../middlewares/auth.middleware';
import { asyncHandler } from '../../utils/asyncHandler';
import { created, ok } from '../../utils/apiResponse';
import { AppError } from '../../utils/AppError';
import { exec, insertRow, paginate, queryOne } from '../../core/dbHelpers';

const router = Router();

router.post(
  '/',
  authMiddleware,
  asyncHandler(async (req: Request, res: Response) => {
    const b = req.body;
    if (!b.complaintTypeId || !b.subject) throw new AppError(400, 'complaintTypeId and subject are required');
    const id = await insertRow(req.db!, 'complaints', {
      raised_by: req.user!.userId, complaint_type_id: b.complaintTypeId,
      subject: b.subject, description: b.description || null,
    });
    const row = await queryOne(req.db!, 'SELECT * FROM complaints WHERE id = ?', [id]);
    return created(res, row, 'Complaint raised');
  }),
);

router.get(
  '/',
  authMiddleware,
  requireRole('admin'),
  asyncHandler(async (req: Request, res: Response) => {
    const { status, page, limit } = req.query;
    const conditions: string[] = [];
    const params: unknown[] = [];
    if (status) { conditions.push('c.status = ?'); params.push(status); }
    const where = conditions.length ? `WHERE ${conditions.join(' AND ')}` : '';
    const baseSql = `SELECT c.*, ct.name as complaint_type, u.name as raised_by_name
      FROM complaints c JOIN complaint_types ct ON ct.id = c.complaint_type_id JOIN users u ON u.id = c.raised_by
      ${where} ORDER BY c.created_at DESC`;
    const countSql = `SELECT COUNT(*) as total FROM complaints c ${where}`;
    const result = await paginate(req.db!, baseSql, countSql, params, Number(page) || 1, Number(limit) || 20);
    return ok(res, result);
  }),
);

router.get(
  '/mine',
  authMiddleware,
  asyncHandler(async (req: Request, res: Response) => {
    const baseSql = `SELECT c.*, ct.name as complaint_type FROM complaints c
      JOIN complaint_types ct ON ct.id = c.complaint_type_id WHERE c.raised_by = ? ORDER BY c.created_at DESC`;
    const countSql = `SELECT COUNT(*) as total FROM complaints WHERE raised_by = ?`;
    const result = await paginate(req.db!, baseSql, countSql, [req.user!.userId], Number(req.query.page) || 1, Number(req.query.limit) || 20);
    return ok(res, result);
  }),
);

router.get(
  '/:id',
  authMiddleware,
  asyncHandler(async (req: Request, res: Response) => {
    const row = await queryOne(
      req.db!,
      `SELECT c.*, ct.name as complaint_type, u.name as raised_by_name
       FROM complaints c JOIN complaint_types ct ON ct.id = c.complaint_type_id JOIN users u ON u.id = c.raised_by
       WHERE c.id = ?`,
      [req.params.id],
    );
    if (!row) throw new AppError(404, 'Complaint not found');
    return ok(res, row);
  }),
);

router.put(
  '/:id/resolve',
  authMiddleware,
  requireRole('admin'),
  asyncHandler(async (req: Request, res: Response) => {
    const { status, resolvedRemarks } = req.body;
    if (!['in_progress', 'resolved'].includes(status)) throw new AppError(400, 'status must be in_progress or resolved');
    await exec(
      req.db!,
      `UPDATE complaints SET status = ?, resolved_by = ?, resolved_remarks = ?, resolved_at = ${status === 'resolved' ? 'NOW()' : 'NULL'} WHERE id = ?`,
      [status, req.user!.userId, resolvedRemarks || null, req.params.id],
    );
    const row = await queryOne(req.db!, 'SELECT * FROM complaints WHERE id = ?', [req.params.id]);
    return ok(res, row, 'Complaint updated');
  }),
);

/** Simple stats: counts by status (for admin dashboard) */
router.get(
  '/stats/summary',
  authMiddleware,
  requireRole('admin'),
  asyncHandler(async (req: Request, res: Response) => {
    const row = await queryOne(
      req.db!,
      `SELECT COUNT(*) as total, SUM(status='open') as open, SUM(status='in_progress') as in_progress, SUM(status='resolved') as resolved
       FROM complaints`,
    );
    return ok(res, row);
  }),
);

export default router;
