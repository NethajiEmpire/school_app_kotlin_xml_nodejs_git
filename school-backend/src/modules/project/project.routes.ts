import { Router, Request, Response } from 'express';
import { authMiddleware, requireRole } from '../../middlewares/auth.middleware';
import { upload, fileUrl } from '../../middlewares/upload.middleware';
import { asyncHandler } from '../../utils/asyncHandler';
import { created, ok } from '../../utils/apiResponse';
import { AppError } from '../../utils/AppError';
import { exec, insertRow, paginate, query, queryOne } from '../../core/dbHelpers';

const router = Router();

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
    const id = await insertRow(req.db!, 'projects', {
      subject_id: b.subjectId, standard_id: b.standardId, section_id: b.sectionId,
      teacher_id: b.teacherId, title: b.title, description: b.description || null,
      attachment, total_marks: b.totalMarks || 0, due_date: b.dueDate,
    });
    await exec(
      req.db!,
      `INSERT INTO project_submissions (project_id, student_id, status)
       SELECT ?, id, 'pending' FROM students WHERE standard_id = ? AND section_id = ?`,
      [id, b.standardId, b.sectionId],
    );
    const row = await queryOne(req.db!, 'SELECT * FROM projects WHERE id = ?', [id]);
    return created(res, row, 'Project posted');
  }),
);

router.get(
  '/',
  authMiddleware,
  asyncHandler(async (req: Request, res: Response) => {
    const { standardId, sectionId, subjectId, page, limit } = req.query;
    const conditions: string[] = [];
    const params: unknown[] = [];
    if (standardId) { conditions.push('p.standard_id = ?'); params.push(standardId); }
    if (sectionId) { conditions.push('p.section_id = ?'); params.push(sectionId); }
    if (subjectId) { conditions.push('p.subject_id = ?'); params.push(subjectId); }
    const where = conditions.length ? `WHERE ${conditions.join(' AND ')}` : '';
    const baseSql = `SELECT p.*, sub.name as subject_name FROM projects p JOIN subjects sub ON sub.id = p.subject_id ${where} ORDER BY p.id DESC`;
    const countSql = `SELECT COUNT(*) as total FROM projects p ${where}`;
    const result = await paginate(req.db!, baseSql, countSql, params, Number(page) || 1, Number(limit) || 20);
    return ok(res, result);
  }),
);

router.get(
  '/:id',
  authMiddleware,
  asyncHandler(async (req: Request, res: Response) => {
    const item = await queryOne(req.db!, 'SELECT * FROM projects WHERE id = ?', [req.params.id]);
    if (!item) throw new AppError(404, 'Project not found');
    const submissions = await query(
      req.db!,
      `SELECT ps.*, u.name as student_name FROM project_submissions ps
       JOIN students s ON s.id = ps.student_id JOIN users u ON u.id = s.user_id
       WHERE ps.project_id = ?`,
      [req.params.id],
    );
    return ok(res, { ...item, submissions });
  }),
);

router.get(
  '/student/:studentId',
  authMiddleware,
  asyncHandler(async (req: Request, res: Response) => {
    const rows = await query(
      req.db!,
      `SELECT p.*, ps.status, ps.submitted_file, ps.marks_obtained, ps.submitted_at, ps.remarks, sub.name as subject_name
       FROM project_submissions ps
       JOIN projects p ON p.id = ps.project_id
       JOIN subjects sub ON sub.id = p.subject_id
       WHERE ps.student_id = ? ORDER BY p.due_date DESC`,
      [req.params.studentId],
    );
    return ok(res, rows);
  }),
);

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
      `UPDATE project_submissions SET status = 'submitted', submitted_file = ?, submitted_at = NOW()
       WHERE project_id = ? AND student_id = ?`,
      [fileLoc, req.params.id, studentId],
    );
    return ok(res, { file: fileLoc }, 'Project submitted successfully');
  }),
);

router.put(
  '/submissions/:submissionId',
  authMiddleware,
  requireRole('teacher', 'admin'),
  asyncHandler(async (req: Request, res: Response) => {
    const { status, remarks, marksObtained } = req.body;
    const values: Record<string, unknown> = {};
    if (status) values.status = status;
    if (remarks !== undefined) values.remarks = remarks;
    if (marksObtained !== undefined) values.marks_obtained = marksObtained;
    const columns = Object.keys(values);
    if (!columns.length) throw new AppError(400, 'Nothing to update');
    const setClause = columns.map((c) => `\`${c}\` = ?`).join(', ');
    await exec(req.db!, `UPDATE project_submissions SET ${setClause} WHERE id = ?`, [...Object.values(values), req.params.submissionId]);
    return ok(res, null, 'Submission updated');
  }),
);

export default router;
