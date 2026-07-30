import { Router, Request, Response } from 'express';
import { authMiddleware, requireRole } from '../../middlewares/auth.middleware';
import { upload, fileUrl } from '../../middlewares/upload.middleware';
import { asyncHandler } from '../../utils/asyncHandler';
import { created, ok } from '../../utils/apiResponse';
import { AppError } from '../../utils/AppError';
import { exec, insertRow, query, queryOne } from '../../core/dbHelpers';

const router = Router();

/** Generic single-file upload used by any screen (chat attachments, misc files) */
router.post(
  '/upload',
  authMiddleware,
  upload.single('file'),
  asyncHandler(async (req: Request, res: Response) => {
    if (!req.file) throw new AppError(400, 'No file uploaded');
    return created(res, { url: fileUrl(req, req.file.filename), originalName: req.file.originalname }, 'File uploaded');
  }),
);

/** Student uploads a required document (birth certificate, transfer certificate, etc.) */
router.post(
  '/documents/:documentMasterId',
  authMiddleware,
  upload.single('file'),
  asyncHandler(async (req: Request, res: Response) => {
    if (!req.file) throw new AppError(400, 'No file uploaded');
    const id = await insertRow(req.db!, 'documents_upload', {
      user_id: req.user!.userId,
      document_master_id: req.params.documentMasterId,
      file_path: fileUrl(req, req.file.filename),
    });
    const row = await queryOne(req.db!, 'SELECT * FROM documents_upload WHERE id = ?', [id]);
    return created(res, row, 'Document uploaded, pending verification');
  }),
);

/** All documents uploaded by the current user, with their master doc names */
router.get(
  '/documents/mine',
  authMiddleware,
  asyncHandler(async (req: Request, res: Response) => {
    const rows = await query(
      req.db!,
      `SELECT du.*, dm.doc_name, dm.is_mandatory FROM documents_upload du
       JOIN documents_master dm ON dm.id = du.document_master_id WHERE du.user_id = ?`,
      [req.user!.userId],
    );
    return ok(res, rows);
  }),
);

/** Admin verifies/rejects an uploaded document */
router.put(
  '/documents/:id/status',
  authMiddleware,
  requireRole('admin'),
  asyncHandler(async (req: Request, res: Response) => {
    const { status } = req.body;
    if (!['verified', 'rejected'].includes(status)) throw new AppError(400, 'status must be verified or rejected');
    await exec(req.db!, 'UPDATE documents_upload SET status = ? WHERE id = ?', [status, req.params.id]);
    return ok(res, null, 'Document status updated');
  }),
);

export default router;
