import { Router, Request, Response } from 'express';
import { authMiddleware, requireRole } from '../../middlewares/auth.middleware';
import { asyncHandler } from '../../utils/asyncHandler';
import { ok } from '../../utils/apiResponse';
import { queryOne, query } from '../../core/dbHelpers';

const router = Router();

/** High-level admin dashboard: total students/teachers/staff/classes + today's attendance snapshot */
router.get(
  '/admin',
  authMiddleware,
  requireRole('admin'),
  asyncHandler(async (req: Request, res: Response) => {
    const [students, teachers, staff, standards, todayAttendance, openComplaints, pendingLeaves] = await Promise.all([
      queryOne<{ cnt: number }>(req.db!, 'SELECT COUNT(*) as cnt FROM students'),
      queryOne<{ cnt: number }>(req.db!, 'SELECT COUNT(*) as cnt FROM teachers'),
      queryOne<{ cnt: number }>(req.db!, 'SELECT COUNT(*) as cnt FROM staff'),
      queryOne<{ cnt: number }>(req.db!, 'SELECT COUNT(*) as cnt FROM standards'),
      queryOne<{ total: number; present: number }>(
        req.db!,
        `SELECT COUNT(*) as total, SUM(status='present') as present FROM attendance WHERE date = CURDATE()`,
      ),
      queryOne<{ cnt: number }>(req.db!, `SELECT COUNT(*) as cnt FROM complaints WHERE status != 'resolved'`),
      queryOne<{ cnt: number }>(req.db!, `SELECT COUNT(*) as cnt FROM leave_requests WHERE status = 'pending'`),
    ]);

    return ok(res, {
      totalStudents: students?.cnt || 0,
      totalTeachers: teachers?.cnt || 0,
      totalStaff: staff?.cnt || 0,
      totalStandards: standards?.cnt || 0,
      todayAttendance: {
        total: todayAttendance?.total || 0,
        present: todayAttendance?.present || 0,
      },
      openComplaints: openComplaints?.cnt || 0,
      pendingLeaveRequests: pendingLeaves?.cnt || 0,
    });
  }),
);

/** Academic stats for a standard: subject count, avg attendance %, avg exam % */
router.get(
  '/academic',
  authMiddleware,
  asyncHandler(async (req: Request, res: Response) => {
    const { standardId } = req.query;
    const subjectCount = await queryOne<{ cnt: number }>(req.db!, 'SELECT COUNT(*) as cnt FROM subjects WHERE standard_id = ?', [standardId]);
    const attendance = await queryOne<{ total: number; present: number }>(
      req.db!,
      `SELECT COUNT(*) as total, SUM(status='present') as present FROM attendance WHERE standard_id = ?`,
      [standardId],
    );
    const examAvg = await queryOne<{ avg_pct: number }>(
      req.db!,
      `SELECT AVG(er.marks_obtained / NULLIF(es.total_marks,0) * 100) as avg_pct
       FROM exam_results er
       JOIN exam_subjects es ON es.id = er.exam_subject_id
       JOIN exams ex ON ex.id = es.exam_id
       WHERE ex.standard_id = ?`,
      [standardId],
    );
    const attTotal = attendance?.total || 0;
    const attPresent = Number(attendance?.present || 0);
    return ok(res, {
      subjectCount: subjectCount?.cnt || 0,
      attendancePercentage: attTotal > 0 ? Math.round((attPresent / attTotal) * 10000) / 100 : 0,
      averageExamPercentage: Math.round((examAvg?.avg_pct || 0) * 100) / 100,
    });
  }),
);

/** Teacher's own workload stats: classes handled, pending gradings */
router.get(
  '/teacher/:teacherId',
  authMiddleware,
  requireRole('teacher', 'admin'),
  asyncHandler(async (req: Request, res: Response) => {
    const { teacherId } = req.params;
    const [classesHandled, pendingHomework, pendingAssignments, pendingClassTests, pendingProjects] = await Promise.all([
      queryOne<{ cnt: number }>(req.db!, 'SELECT COUNT(DISTINCT standard_id, section_id) as cnt FROM teacher_subjects WHERE teacher_id = ?', [teacherId]),
      queryOne<{ cnt: number }>(
        req.db!,
        `SELECT COUNT(*) as cnt FROM homework_submissions hs JOIN homework h ON h.id = hs.homework_id WHERE h.teacher_id = ? AND hs.status = 'submitted'`,
        [teacherId],
      ),
      queryOne<{ cnt: number }>(
        req.db!,
        `SELECT COUNT(*) as cnt FROM assignment_submissions asub JOIN assignments a ON a.id = asub.assignment_id WHERE a.teacher_id = ? AND asub.status = 'submitted'`,
        [teacherId],
      ),
      queryOne<{ cnt: number }>(
        req.db!,
        `SELECT COUNT(*) as cnt FROM class_test_results ctr JOIN class_tests ct ON ct.id = ctr.class_test_id WHERE ct.teacher_id = ? AND ctr.status = 'pending'`,
        [teacherId],
      ),
      queryOne<{ cnt: number }>(
        req.db!,
        `SELECT COUNT(*) as cnt FROM project_submissions ps JOIN projects p ON p.id = ps.project_id WHERE p.teacher_id = ? AND ps.status = 'submitted'`,
        [teacherId],
      ),
    ]);

    return ok(res, {
      classesHandled: classesHandled?.cnt || 0,
      pendingGrading: {
        homework: pendingHomework?.cnt || 0,
        assignments: pendingAssignments?.cnt || 0,
        classTests: pendingClassTests?.cnt || 0,
        projects: pendingProjects?.cnt || 0,
      },
    });
  }),
);

/** A single student's overall progress snapshot (attendance %, avg exam %, pending work counts) */
router.get(
  '/student/:studentId/overall-progress',
  authMiddleware,
  asyncHandler(async (req: Request, res: Response) => {
    const { studentId } = req.params;
    const attendance = await queryOne<{ total: number; present: number }>(
      req.db!,
      `SELECT COUNT(*) as total, SUM(status='present') as present FROM attendance WHERE student_id = ?`,
      [studentId],
    );
    const examAvg = await queryOne<{ avg_pct: number }>(
      req.db!,
      `SELECT AVG(marks_obtained / NULLIF((SELECT total_marks FROM exam_subjects WHERE id = exam_subject_id),0) * 100) as avg_pct
       FROM exam_results WHERE student_id = ? AND marks_obtained IS NOT NULL`,
      [studentId],
    );
    const pendingCounts = await query<{ label: string; cnt: number }>(
      req.db!,
      `SELECT 'homework' as label, COUNT(*) as cnt FROM homework_submissions WHERE student_id = ? AND status = 'pending'
       UNION ALL
       SELECT 'assignments', COUNT(*) FROM assignment_submissions WHERE student_id = ? AND status = 'pending'
       UNION ALL
       SELECT 'projects', COUNT(*) FROM project_submissions WHERE student_id = ? AND status = 'pending'`,
      [studentId, studentId, studentId],
    );
    const attTotal = attendance?.total || 0;
    const attPresent = Number(attendance?.present || 0);

    return ok(res, {
      attendancePercentage: attTotal > 0 ? Math.round((attPresent / attTotal) * 10000) / 100 : 0,
      averageExamPercentage: Math.round((examAvg?.avg_pct || 0) * 100) / 100,
      pending: pendingCounts,
    });
  }),
);

export default router;
