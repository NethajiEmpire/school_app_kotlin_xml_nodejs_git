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
    if (!b.subjectId || !b.standardId || !b.sectionId || !b.teacherId || !b.title || !b.testDate) {
      throw new AppError(400, 'subjectId, standardId, sectionId, teacherId, title, testDate are required');
    }
    const attachment = req.file ? fileUrl(req, req.file.filename) : null;
    const id = await insertRow(req.db!, 'class_tests', {
      subject_id: b.subjectId, standard_id: b.standardId, section_id: b.sectionId,
      teacher_id: b.teacherId, title: b.title, description: b.description || null,
      attachment, total_marks: b.totalMarks || 0, test_date: b.testDate,
    });
    await exec(
      req.db!,
      `INSERT INTO class_test_results (class_test_id, student_id, status)
       SELECT ?, id, 'pending' FROM students WHERE standard_id = ? AND section_id = ?`,
      [id, b.standardId, b.sectionId],
    );
    const row = await queryOne(req.db!, 'SELECT * FROM class_tests WHERE id = ?', [id]);
    return created(res, row, 'Class test created');
  }),
);

router.get(
  '/',
  authMiddleware,
  asyncHandler(async (req: Request, res: Response) => {
    const { standardId, sectionId, subjectId, page, limit } = req.query;
    const conditions: string[] = [];
    const params: unknown[] = [];
    if (standardId) { conditions.push('ct.standard_id = ?'); params.push(standardId); }
    if (sectionId) { conditions.push('ct.section_id = ?'); params.push(sectionId); }
    if (subjectId) { conditions.push('ct.subject_id = ?'); params.push(subjectId); }
    const where = conditions.length ? `WHERE ${conditions.join(' AND ')}` : '';
    const baseSql = `SELECT ct.*, sub.name as subject_name FROM class_tests ct JOIN subjects sub ON sub.id = ct.subject_id ${where} ORDER BY ct.id DESC`;
    const countSql = `SELECT COUNT(*) as total FROM class_tests ct ${where}`;
    const result = await paginate(req.db!, baseSql, countSql, params, Number(page) || 1, Number(limit) || 20);
    return ok(res, result);
  }),
);

router.get(
  '/:id',
  authMiddleware,
  asyncHandler(async (req: Request, res: Response) => {
    const item = await queryOne(req.db!, 'SELECT * FROM class_tests WHERE id = ?', [req.params.id]);
    if (!item) throw new AppError(404, 'Class test not found');
    const results = await query(
      req.db!,
      `SELECT ctr.*, u.name as student_name FROM class_test_results ctr
       JOIN students s ON s.id = ctr.student_id JOIN users u ON u.id = s.user_id
       WHERE ctr.class_test_id = ?`,
      [req.params.id],
    );
    return ok(res, { ...item, results });
  }),
);

router.get(
  '/student/:studentId',
  authMiddleware,
  asyncHandler(async (req: Request, res: Response) => {
    const rows = await query(
      req.db!,
      `SELECT ct.*, ctr.status, ctr.marks_obtained, ctr.remarks, sub.name as subject_name
       FROM class_test_results ctr
       JOIN class_tests ct ON ct.id = ctr.class_test_id
       JOIN subjects sub ON sub.id = ct.subject_id
       WHERE ctr.student_id = ? ORDER BY ct.test_date DESC`,
      [req.params.studentId],
    );
    return ok(res, rows);
  }),
);

/** Teacher enters marks/status for one student's class test result */
router.put(
  '/results/:resultId',
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
    await exec(req.db!, `UPDATE class_test_results SET ${setClause} WHERE id = ?`, [...Object.values(values), req.params.resultId]);
    return ok(res, null, 'Result updated');
  }),
);

export default router;
