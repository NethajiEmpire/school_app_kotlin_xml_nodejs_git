import { Router, Request, Response } from 'express';
import { authMiddleware, requireRole } from '../../middlewares/auth.middleware';
import { asyncHandler } from '../../utils/asyncHandler';
import { created, ok } from '../../utils/apiResponse';
import { AppError } from '../../utils/AppError';
import { exec, insertRow, query, queryOne } from '../../core/dbHelpers';

const router = Router();

/** Create an exam (e.g. "Mid Term 2026") for a standard */
router.post(
  '/',
  authMiddleware,
  requireRole('admin'),
  asyncHandler(async (req: Request, res: Response) => {
    const b = req.body;
    if (!b.name || !b.standardId || !b.startDate || !b.endDate) {
      throw new AppError(400, 'name, standardId, startDate, endDate are required');
    }
    const id = await insertRow(req.db!, 'exams', {
      name: b.name, standard_id: b.standardId, academic_year_id: b.academicYearId || null,
      start_date: b.startDate, end_date: b.endDate,
    });
    const row = await queryOne(req.db!, 'SELECT * FROM exams WHERE id = ?', [id]);
    return created(res, row, 'Exam created');
  }),
);

/** Add a subject/date/time slot to an exam's schedule */
router.post(
  '/:examId/subjects',
  authMiddleware,
  requireRole('admin'),
  asyncHandler(async (req: Request, res: Response) => {
    const b = req.body;
    if (!b.subjectId || !b.examDate) throw new AppError(400, 'subjectId and examDate are required');
    const id = await insertRow(req.db!, 'exam_subjects', {
      exam_id: req.params.examId, subject_id: b.subjectId, exam_date: b.examDate,
      start_time: b.startTime || null, end_time: b.endTime || null, total_marks: b.totalMarks || 100,
    });

    // auto-create pending result rows for every student in that standard
    const exam = await queryOne<{ standard_id: number }>(req.db!, 'SELECT standard_id FROM exams WHERE id = ?', [req.params.examId]);
    if (exam) {
      await exec(
        req.db!,
        `INSERT INTO exam_results (exam_subject_id, student_id)
         SELECT ?, id FROM students WHERE standard_id = ?`,
        [id, exam.standard_id],
      );
    }
    return created(res, { id, ...b }, 'Exam subject scheduled');
  }),
);

router.get(
  '/',
  authMiddleware,
  asyncHandler(async (req: Request, res: Response) => {
    const { standardId } = req.query;
    const conditions: string[] = [];
    const params: unknown[] = [];
    if (standardId) { conditions.push('standard_id = ?'); params.push(standardId); }
    const where = conditions.length ? `WHERE ${conditions.join(' AND ')}` : '';
    const rows = await query(req.db!, `SELECT * FROM exams ${where} ORDER BY start_date DESC`, params);
    return ok(res, rows);
  }),
);

router.get(
  '/:id',
  authMiddleware,
  asyncHandler(async (req: Request, res: Response) => {
    const exam = await queryOne(req.db!, 'SELECT * FROM exams WHERE id = ?', [req.params.id]);
    if (!exam) throw new AppError(404, 'Exam not found');
    const subjects = await query(
      req.db!,
      `SELECT es.*, sub.name as subject_name FROM exam_subjects es JOIN subjects sub ON sub.id = es.subject_id WHERE es.exam_id = ?`,
      [req.params.id],
    );
    return ok(res, { ...exam, subjects });
  }),
);

/** Enter/update a student's marks for one exam subject */
router.put(
  '/results/:resultId',
  authMiddleware,
  requireRole('teacher', 'admin'),
  asyncHandler(async (req: Request, res: Response) => {
    const { marksObtained, grade, remarks } = req.body;
    const values: Record<string, unknown> = {};
    if (marksObtained !== undefined) values.marks_obtained = marksObtained;
    if (grade !== undefined) values.grade = grade;
    if (remarks !== undefined) values.remarks = remarks;
    const columns = Object.keys(values);
    if (!columns.length) throw new AppError(400, 'Nothing to update');
    const setClause = columns.map((c) => `\`${c}\` = ?`).join(', ');
    await exec(req.db!, `UPDATE exam_results SET ${setClause} WHERE id = ?`, [...Object.values(values), req.params.resultId]);
    return ok(res, null, 'Result saved');
  }),
);

/** A student's full result sheet for one exam */
router.get(
  '/:examId/students/:studentId/results',
  authMiddleware,
  asyncHandler(async (req: Request, res: Response) => {
    const rows = await query(
      req.db!,
      `SELECT er.*, sub.name as subject_name, es.total_marks, es.exam_date
       FROM exam_results er
       JOIN exam_subjects es ON es.id = er.exam_subject_id
       JOIN subjects sub ON sub.id = es.subject_id
       WHERE es.exam_id = ? AND er.student_id = ?`,
      [req.params.examId, req.params.studentId],
    );
    return ok(res, rows);
  }),
);

export default router;
