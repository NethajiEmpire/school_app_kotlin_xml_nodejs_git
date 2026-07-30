import { Router, Request, Response } from 'express';
import { authMiddleware, requireRole } from '../../middlewares/auth.middleware';
import { asyncHandler } from '../../utils/asyncHandler';
import { created, ok } from '../../utils/apiResponse';
import { AppError } from '../../utils/AppError';
import { generateCode } from '../../utils/hash';
import { insertRow, query, queryOne } from '../../core/dbHelpers';

const router = Router();

/** Admin defines a fee structure for a standard (e.g. "Tuition Fee - 10th - Term 1") */
router.post(
  '/structures',
  authMiddleware,
  requireRole('admin'),
  asyncHandler(async (req: Request, res: Response) => {
    const b = req.body;
    if (!b.standardId || !b.feeType || !b.amount) throw new AppError(400, 'standardId, feeType, amount are required');
    const id = await insertRow(req.db!, 'fee_structures', {
      standard_id: b.standardId, academic_year_id: b.academicYearId || null,
      fee_type: b.feeType, amount: b.amount, due_date: b.dueDate || null,
    });
    const row = await queryOne(req.db!, 'SELECT * FROM fee_structures WHERE id = ?', [id]);
    return created(res, row, 'Fee structure created');
  }),
);

router.get(
  '/structures',
  authMiddleware,
  asyncHandler(async (req: Request, res: Response) => {
    const { standardId } = req.query;
    const conditions: string[] = [];
    const params: unknown[] = [];
    if (standardId) { conditions.push('standard_id = ?'); params.push(standardId); }
    const where = conditions.length ? `WHERE ${conditions.join(' AND ')}` : '';
    const rows = await query(req.db!, `SELECT * FROM fee_structures ${where} ORDER BY id DESC`, params);
    return ok(res, rows);
  }),
);

/** Student/parent view: fee structures + how much has been paid so far, per structure */
router.get(
  '/student/:studentId',
  authMiddleware,
  asyncHandler(async (req: Request, res: Response) => {
    const student = await queryOne<{ standard_id: number }>(req.db!, 'SELECT standard_id FROM students WHERE id = ?', [req.params.studentId]);
    if (!student) throw new AppError(404, 'Student not found');

    const rows = await query(
      req.db!,
      `SELECT fs.*, COALESCE(SUM(ft.amount_paid),0) as paid_amount
       FROM fee_structures fs
       LEFT JOIN fee_transactions ft ON ft.fee_structure_id = fs.id AND ft.student_id = ? AND ft.status = 'success'
       WHERE fs.standard_id = ?
       GROUP BY fs.id`,
      [req.params.studentId, student.standard_id],
    );
    return ok(res, rows);
  }),
);

/** Record a fee payment (online gateway callback or manual/cash entry by admin) */
router.post(
  '/pay',
  authMiddleware,
  asyncHandler(async (req: Request, res: Response) => {
    const b = req.body;
    if (!b.studentId || !b.feeStructureId || !b.amountPaid) {
      throw new AppError(400, 'studentId, feeStructureId, amountPaid are required');
    }
    const transactionRef = b.transactionRef || generateCode('TXN', 10);
    const id = await insertRow(req.db!, 'fee_transactions', {
      student_id: b.studentId, fee_structure_id: b.feeStructureId, amount_paid: b.amountPaid,
      payment_mode: b.paymentMode || 'online', transaction_ref: transactionRef,
      status: b.status || 'success',
    });
    const row = await queryOne(req.db!, 'SELECT * FROM fee_transactions WHERE id = ?', [id]);
    return created(res, row, 'Payment recorded');
  }),
);

/** Payment history for a student */
router.get(
  '/transactions/:studentId',
  authMiddleware,
  asyncHandler(async (req: Request, res: Response) => {
    const rows = await query(
      req.db!,
      `SELECT ft.*, fs.fee_type FROM fee_transactions ft
       JOIN fee_structures fs ON fs.id = ft.fee_structure_id
       WHERE ft.student_id = ? ORDER BY ft.payment_date DESC`,
      [req.params.studentId],
    );
    return ok(res, rows);
  }),
);

/** Admin: overall fee collection summary for a standard (for bar-chart dashboards) */
router.get(
  '/summary',
  authMiddleware,
  requireRole('admin'),
  asyncHandler(async (req: Request, res: Response) => {
    const { standardId } = req.query;
    const conditions: string[] = [];
    const params: unknown[] = [];
    if (standardId) { conditions.push('fs.standard_id = ?'); params.push(standardId); }
    const where = conditions.length ? `WHERE ${conditions.join(' AND ')}` : '';
    const rows = await query(
      req.db!,
      `SELECT fs.fee_type, SUM(fs.amount) as total_due, COALESCE(SUM(ft.amount_paid),0) as total_collected
       FROM fee_structures fs
       LEFT JOIN fee_transactions ft ON ft.fee_structure_id = fs.id AND ft.status = 'success'
       ${where}
       GROUP BY fs.fee_type`,
      params,
    );
    return ok(res, rows);
  }),
);

export default router;
