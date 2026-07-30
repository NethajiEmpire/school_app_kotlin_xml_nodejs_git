import { Router, Request, Response } from 'express';
import { authMiddleware, requireRole } from '../../middlewares/auth.middleware';
import { upload, fileUrl } from '../../middlewares/upload.middleware';
import { asyncHandler } from '../../utils/asyncHandler';
import { created, ok } from '../../utils/apiResponse';
import { AppError } from '../../utils/AppError';
import { exec, insertRow, paginate, query, queryOne } from '../../core/dbHelpers';

const router = Router();

/** Teacher creates homework for a class/section/subject */
router.post(
  '/',
  authMiddleware,
  requireRole('teacher', 'admin'),
  upload.single('attachment'),
  asyncHandler(async (req: Request, res: Response) => {
    const b = req.body;
    if (!b.subjectId || !b.standardId || !b.sectionId || !b.teacherId || !b.title || !b.dueDate) {
      throw new AppError(400, 'subjectId, standardId, sectionId, teacherId, title, dueDate are required');
    }
    const attachment = req.file ? fileUrl(req, req.file.filename) : null;
    const id = await insertRow(req.db!, 'homework', {
      subject_id: b.subjectId, standard_id: b.standardId, section_id: b.sectionId,
      teacher_id: b.teacherId, title: b.title, description: b.description || null,
      attachment, due_date: b.dueDate,
    });

    // auto-create a pending submission row for every student in that class/section
    await exec(
      req.db!,
      `INSERT INTO homework_submissions (homework_id, student_id, status)
       SELECT ?, id, 'pending' FROM students WHERE standard_id = ? AND section_id = ?`,
      [id, b.standardId, b.sectionId],
    );

    const row = await queryOne(req.db!, 'SELECT * FROM homework WHERE id = ?', [id]);
    return created(res, row, 'Homework posted');
  }),
);

/** List homework for a class/section (teacher/admin view) */
router.get(
  '/',
  authMiddleware,
  asyncHandler(async (req: Request, res: Response) => {
    const { standardId, sectionId, subjectId, page, limit } = req.query;
    const conditions: string[] = [];
    const params: unknown[] = [];
    if (standardId) { conditions.push('h.standard_id = ?'); params.push(standardId); }
    if (sectionId) { conditions.push('h.section_id = ?'); params.push(sectionId); }
    if (subjectId) { conditions.push('h.subject_id = ?'); params.push(subjectId); }
    const where = conditions.length ? `WHERE ${conditions.join(' AND ')}` : '';
    const baseSql = `SELECT h.*, sub.name as subject_name FROM homework h JOIN subjects sub ON sub.id = h.subject_id ${where} ORDER BY h.id DESC`;
    const countSql = `SELECT COUNT(*) as total FROM homework h ${where}`;
    const result = await paginate(req.db!, baseSql, countSql, params, Number(page) || 1, Number(limit) || 20);
    return ok(res, result);
  }),
);

/** Single homework with submission stats */
router.get(
  '/:id',
  authMiddleware,
  asyncHandler(async (req: Request, res: Response) => {
    const hw = await queryOne(req.db!, 'SELECT * FROM homework WHERE id = ?', [req.params.id]);
    if (!hw) throw new AppError(404, 'Homework not found');
    const submissions = await query(
      req.db!,
      `SELECT hs.*, u.name as student_name FROM homework_submissions hs
       JOIN students s ON s.id = hs.student_id JOIN users u ON u.id = s.user_id
       WHERE hs.homework_id = ?`,
      [req.params.id],
    );
    return ok(res, { ...hw, submissions });
  }),
);

/** Student's homework list (their standard/section) */
router.get(
  '/student/:studentId',
  authMiddleware,
  asyncHandler(async (req: Request, res: Response) => {
    const rows = await query(
      req.db!,
      `SELECT h.*, hs.status, hs.submitted_file, hs.submitted_at, hs.remarks, sub.name as subject_name
       FROM homework_submissions hs
       JOIN homework h ON h.id = hs.homework_id
       JOIN subjects sub ON sub.id = h.subject_id
       WHERE hs.student_id = ? ORDER BY h.due_date DESC`,
      [req.params.studentId],
    );
    return ok(res, rows);
  }),
);

/** Student submits homework */
router.post(
  '/:id/submit',
  authMiddleware,
  requireRole('student'),
  upload.single('file'),
  asyncHandler(async (req: Request, res: Response) => {
    const { studentId } = req.body;
    if (!studentId) throw new AppError(400, 'studentId is required');
    if (!req.file) throw new AppError(400, 'A file is required for submission');
    const fileLoc = fileUrl(req, req.file.filename);
    await exec(
      req.db!,
      `UPDATE homework_submissions SET status = 'submitted', submitted_file = ?, submitted_at = NOW()
       WHERE homework_id = ? AND student_id = ?`,
      [fileLoc, req.params.id, studentId],
    );
    return ok(res, { file: fileLoc }, 'Homework submitted successfully');
  }),
);

/** Teacher updates a submission's status/remarks/points */
router.put(
  '/submissions/:submissionId',
  authMiddleware,
  requireRole('teacher', 'admin'),
  asyncHandler(async (req: Request, res: Response) => {
    const { status, remarks, points } = req.body;
    const values: Record<string, unknown> = {};
    if (status) values.status = status;
    if (remarks !== undefined) values.remarks = remarks;
    if (points !== undefined) values.points = points;
    const columns = Object.keys(values);
    if (!columns.length) throw new AppError(400, 'Nothing to update');
    const setClause = columns.map((c) => `\`${c}\` = ?`).join(', ');
    await exec(req.db!, `UPDATE homework_submissions SET ${setClause} WHERE id = ?`, [...Object.values(values), req.params.submissionId]);
    return ok(res, null, 'Submission updated');
  }),
);

export default router;
