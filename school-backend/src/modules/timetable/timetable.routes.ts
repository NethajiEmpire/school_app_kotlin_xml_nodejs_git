import { Router, Request, Response } from 'express';
import { authMiddleware, requireRole } from '../../middlewares/auth.middleware';
import { asyncHandler } from '../../utils/asyncHandler';
import { created, ok } from '../../utils/apiResponse';
import { AppError } from '../../utils/AppError';
import { insertRow, query } from '../../core/dbHelpers';

const router = Router();

router.post(
  '/',
  authMiddleware,
  requireRole('admin'),
  asyncHandler(async (req: Request, res: Response) => {
    const b = req.body;
    if (!b.standardId || !b.sectionId || !b.subjectId || !b.teacherId || !b.dayOfWeek || !b.periodNo || !b.startTime || !b.endTime) {
      throw new AppError(400, 'standardId, sectionId, subjectId, teacherId, dayOfWeek, periodNo, startTime, endTime are required');
    }
    const id = await insertRow(req.db!, 'timetable', {
      standard_id: b.standardId, section_id: b.sectionId, subject_id: b.subjectId, teacher_id: b.teacherId,
      day_of_week: b.dayOfWeek, period_no: b.periodNo, start_time: b.startTime, end_time: b.endTime,
    });
    return created(res, { id, ...b }, 'Timetable slot created');
  }),
);

/** Class timetable (student view) */
router.get(
  '/class',
  authMiddleware,
  asyncHandler(async (req: Request, res: Response) => {
    const { standardId, sectionId } = req.query;
    if (!standardId || !sectionId) throw new AppError(400, 'standardId and sectionId are required');
    const rows = await query(
      req.db!,
      `SELECT tt.*, sub.name as subject_name, u.name as teacher_name
       FROM timetable tt
       JOIN subjects sub ON sub.id = tt.subject_id
       JOIN teachers t ON t.id = tt.teacher_id
       JOIN users u ON u.id = t.user_id
       WHERE tt.standard_id = ? AND tt.section_id = ?
       ORDER BY tt.day_of_week, tt.period_no`,
      [standardId, sectionId],
    );
    return ok(res, rows);
  }),
);

/** Teacher's personal schedule across all classes */
router.get(
  '/teacher/:teacherId',
  authMiddleware,
  asyncHandler(async (req: Request, res: Response) => {
    const rows = await query(
      req.db!,
      `SELECT tt.*, sub.name as subject_name, st.name as standard_name, sec.name as section_name
       FROM timetable tt
       JOIN subjects sub ON sub.id = tt.subject_id
       JOIN standards st ON st.id = tt.standard_id
       JOIN sections sec ON sec.id = tt.section_id
       WHERE tt.teacher_id = ?
       ORDER BY tt.day_of_week, tt.period_no`,
      [req.params.teacherId],
    );
    return ok(res, rows);
  }),
);

export default router;
