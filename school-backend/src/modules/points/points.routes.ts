import { Router, Request, Response } from 'express';
import { authMiddleware, requireRole } from '../../middlewares/auth.middleware';
import { asyncHandler } from '../../utils/asyncHandler';
import { created, ok } from '../../utils/apiResponse';
import { AppError } from '../../utils/AppError';
import { insertRow, query } from '../../core/dbHelpers';

const router = Router();

/** Teacher/admin awards points to a student (for good homework, behaviour, etc.) */
router.post(
  '/award',
  authMiddleware,
  requireRole('teacher', 'admin'),
  asyncHandler(async (req: Request, res: Response) => {
    const { studentId, points, reason } = req.body;
    if (!studentId || points === undefined) throw new AppError(400, 'studentId and points are required');
    const id = await insertRow(req.db!, 'points_history', {
      student_id: studentId, points, reason: reason || null, awarded_by: req.user!.userId,
    });
    return created(res, { id, studentId, points, reason }, 'Points awarded');
  }),
);

/** A student's full points history + running total */
router.get(
  '/history/:studentId',
  authMiddleware,
  asyncHandler(async (req: Request, res: Response) => {
    const rows = await query(
      req.db!,
      'SELECT * FROM points_history WHERE student_id = ? ORDER BY awarded_at DESC',
      [req.params.studentId],
    );
    const total = rows.reduce((sum: number, r: any) => sum + r.points, 0);
    return ok(res, { history: rows, total });
  }),
);

/** Leaderboard: top students by total points, optionally scoped to a class/section */
router.get(
  '/leaderboard',
  authMiddleware,
  asyncHandler(async (req: Request, res: Response) => {
    const { standardId, sectionId, limit } = req.query;
    const conditions: string[] = [];
    const params: unknown[] = [];
    if (standardId) { conditions.push('s.standard_id = ?'); params.push(standardId); }
    if (sectionId) { conditions.push('s.section_id = ?'); params.push(sectionId); }
    const where = conditions.length ? `WHERE ${conditions.join(' AND ')}` : '';
    const rows = await query(
      req.db!,
      `SELECT s.id as student_id, u.name, COALESCE(SUM(ph.points),0) as total_points
       FROM students s
       JOIN users u ON u.id = s.user_id
       LEFT JOIN points_history ph ON ph.student_id = s.id
       ${where}
       GROUP BY s.id
       ORDER BY total_points DESC
       LIMIT ?`,
      [...params, Number(limit) || 20],
    );
    return ok(res, rows);
  }),
);

/** Scoreboard: combined academic performance (avg exam %) + points, for a class */
router.get(
  '/scoreboard',
  authMiddleware,
  asyncHandler(async (req: Request, res: Response) => {
    const { standardId, sectionId } = req.query;
    if (!standardId || !sectionId) throw new AppError(400, 'standardId and sectionId are required');
    const rows = await query(
      req.db!,
      `SELECT s.id as student_id, u.name,
              COALESCE(AVG(er.marks_obtained / NULLIF(es.total_marks,0) * 100), 0) as avg_exam_percentage,
              COALESCE((SELECT SUM(points) FROM points_history WHERE student_id = s.id), 0) as total_points
       FROM students s
       JOIN users u ON u.id = s.user_id
       LEFT JOIN exam_results er ON er.student_id = s.id
       LEFT JOIN exam_subjects es ON es.id = er.exam_subject_id
       WHERE s.standard_id = ? AND s.section_id = ?
       GROUP BY s.id
       ORDER BY avg_exam_percentage DESC`,
      [standardId, sectionId],
    );
    return ok(res, rows);
  }),
);

export default router;
