import { Router, Request, Response } from 'express';
import { authMiddleware, requireRole } from '../../middlewares/auth.middleware';
import { upload, fileUrl } from '../../middlewares/upload.middleware';
import { asyncHandler } from '../../utils/asyncHandler';
import { created, ok } from '../../utils/apiResponse';
import { AppError } from '../../utils/AppError';
import { exec, insertRow, paginate, queryOne } from '../../core/dbHelpers';

const router = Router();

router.post(
  '/',
  authMiddleware,
  requireRole('admin', 'teacher'),
  upload.single('attachment'),
  asyncHandler(async (req: Request, res: Response) => {
    const b = req.body;
    if (!b.title) throw new AppError(400, 'title is required');
    const attachment = req.file ? fileUrl(req, req.file.filename) : null;
    const id = await insertRow(req.db!, 'notices', {
      title: b.title,
      description: b.description || null,
      target_role: b.targetRole || 'all',
      standard_id: b.standardId || null,
      section_id: b.sectionId || null,
      attachment,
      created_by: req.user!.userId,
    });
    const row = await queryOne(req.db!, 'SELECT * FROM notices WHERE id = ?', [id]);
    return created(res, row, 'Notice posted');
  }),
);

router.get(
  '/',
  authMiddleware,
  asyncHandler(async (req: Request, res: Response) => {
    const { targetRole, standardId, sectionId, page, limit } = req.query;
    const conditions: string[] = [];
    const params: unknown[] = [];
    if (targetRole) { conditions.push('(target_role = ? OR target_role = "all")'); params.push(targetRole); }
    if (standardId) { conditions.push('(standard_id = ? OR standard_id IS NULL)'); params.push(standardId); }
    if (sectionId) { conditions.push('(section_id = ? OR section_id IS NULL)'); params.push(sectionId); }
    const where = conditions.length ? `WHERE ${conditions.join(' AND ')}` : '';
    const baseSql = `SELECT * FROM notices ${where} ORDER BY created_at DESC`;
    const countSql = `SELECT COUNT(*) as total FROM notices ${where}`;
    const result = await paginate(req.db!, baseSql, countSql, params, Number(page) || 1, Number(limit) || 20);
    return ok(res, result);
  }),
);

router.delete(
  '/:id',
  authMiddleware,
  requireRole('admin', 'teacher'),
  asyncHandler(async (req: Request, res: Response) => {
    const result = await exec(req.db!, 'DELETE FROM notices WHERE id = ?', [req.params.id]);
    if (!result.affectedRows) throw new AppError(404, 'Notice not found');
    return ok(res, null, 'Notice deleted');
  }),
);

export default router;
