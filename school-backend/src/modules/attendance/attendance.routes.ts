import { Router, Request, Response } from 'express';
import { authMiddleware, requireRole } from '../../middlewares/auth.middleware';
import { asyncHandler } from '../../utils/asyncHandler';
import { ok } from '../../utils/apiResponse';
import { AppError } from '../../utils/AppError';
import { exec, query, queryOne } from '../../core/dbHelpers';

const router = Router();

/** Teacher marks attendance for a whole class/section on one date in a single call. */
router.post(
  '/mark',
  authMiddleware,
  requireRole('teacher', 'admin'),
  asyncHandler(async (req: Request, res: Response) => {
    const { standardId, sectionId, date, markedBy, records } = req.body;
    // records: [{ studentId, status, remarks }]
    if (!standardId || !sectionId || !date || !Array.isArray(records) || !records.length) {
      throw new AppError(400, 'standardId, sectionId, date and a non-empty records[] are required');
    }
    for (const r of records) {
      await exec(
        req.db!,
        `INSERT INTO attendance (student_id, standard_id, section_id, date, status, marked_by, remarks)
         VALUES (?, ?, ?, ?, ?, ?, ?)
         ON DUPLICATE KEY UPDATE status = VALUES(status), remarks = VALUES(remarks), marked_by = VALUES(marked_by)`,
        [r.studentId, standardId, sectionId, date, r.status, markedBy || null, r.remarks || null],
      );
    }
    return ok(res, null, 'Attendance marked successfully');
  }),
);

/** Attendance for a class/section on one date (for teacher's daily view) */
router.get(
  '/class',
  authMiddleware,
  asyncHandler(async (req: Request, res: Response) => {
    const { standardId, sectionId, date } = req.query;
    if (!standardId || !sectionId || !date) throw new AppError(400, 'standardId, sectionId and date are required');
    const rows = await query(
      req.db!,
      `SELECT s.id as student_id, u.name, s.roll_no, a.status, a.remarks
       FROM students s
       JOIN users u ON u.id = s.user_id
       LEFT JOIN attendance a ON a.student_id = s.id AND a.date = ?
       WHERE s.standard_id = ? AND s.section_id = ?
       ORDER BY s.roll_no`,
      [date, standardId, sectionId],
    );
    return ok(res, rows);
  }),
);

/** A single student's attendance history + percentage */
router.get(
  '/student/:studentId',
  authMiddleware,
  asyncHandler(async (req: Request, res: Response) => {
    const { studentId } = req.params;
    const { from, to } = req.query;
    const conditions = ['student_id = ?'];
    const params: unknown[] = [studentId];
    if (from) { conditions.push('date >= ?'); params.push(from); }
    if (to) { conditions.push('date <= ?'); params.push(to); }
    const where = `WHERE ${conditions.join(' AND ')}`;

    const history = await query(req.db!, `SELECT * FROM attendance ${where} ORDER BY date DESC`, params);
    const stats = await queryOne<{ total: number; present: number }>(
      req.db!,
      `SELECT COUNT(*) as total, SUM(status = 'present') as present FROM attendance ${where}`,
      params,
    );
    const total = stats?.total || 0;
    const present = Number(stats?.present || 0);
    const percentage = total > 0 ? Math.round((present / total) * 10000) / 100 : 0;

    return ok(res, { history, total, present, percentage });
  }),
);

/** Overall attendance progress for a class (used for admin/teacher dashboards) */
router.get(
  '/overall',
  authMiddleware,
  asyncHandler(async (req: Request, res: Response) => {
    const { standardId, sectionId, from, to } = req.query;
    const conditions: string[] = [];
    const params: unknown[] = [];
    if (standardId) { conditions.push('standard_id = ?'); params.push(standardId); }
    if (sectionId) { conditions.push('section_id = ?'); params.push(sectionId); }
    if (from) { conditions.push('date >= ?'); params.push(from); }
    if (to) { conditions.push('date <= ?'); params.push(to); }
    const where = conditions.length ? `WHERE ${conditions.join(' AND ')}` : '';
    const stats = await queryOne<{ total: number; present: number; absent: number; leave: number }>(
      req.db!,
      `SELECT COUNT(*) as total, SUM(status='present') as present, SUM(status='absent') as absent, SUM(status='leave') as leave
       FROM attendance ${where}`,
      params,
    );
    return ok(res, stats);
  }),
);

export default router;
