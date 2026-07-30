import { Router, Request, Response } from 'express';
import { authMiddleware } from '../../middlewares/auth.middleware';
import { upload, fileUrl } from '../../middlewares/upload.middleware';
import { asyncHandler } from '../../utils/asyncHandler';
import { created, ok } from '../../utils/apiResponse';
import { AppError } from '../../utils/AppError';
import { exec, insertRow, paginate, query } from '../../core/dbHelpers';

const router = Router();

/** Send a message (optionally with an attachment) */
router.post(
  '/send',
  authMiddleware,
  upload.single('attachment'),
  asyncHandler(async (req: Request, res: Response) => {
    const { receiverId, message } = req.body;
    if (!receiverId || (!message && !req.file)) throw new AppError(400, 'receiverId and a message or attachment are required');
    const attachment = req.file ? fileUrl(req, req.file.filename) : null;
    const id = await insertRow(req.db!, 'chat_messages', {
      sender_id: req.user!.userId, receiver_id: receiverId, message: message || null, attachment,
    });
    return created(res, { id, senderId: req.user!.userId, receiverId, message, attachment }, 'Message sent');
  }),
);

/** Conversation history between the current user and another user */
router.get(
  '/conversation/:otherUserId',
  authMiddleware,
  asyncHandler(async (req: Request, res: Response) => {
    const me = req.user!.userId;
    const other = Number(req.params.otherUserId);
    const baseSql = `SELECT * FROM chat_messages
      WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?)
      ORDER BY sent_at DESC`;
    const countSql = `SELECT COUNT(*) as total FROM chat_messages
      WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?)`;
    const params = [me, other, other, me];
    const result = await paginate(req.db!, baseSql, countSql, params, Number(req.query.page) || 1, Number(req.query.limit) || 30);
    // mark incoming messages as read
    await exec(req.db!, 'UPDATE chat_messages SET is_read = 1 WHERE sender_id = ? AND receiver_id = ?', [other, me]);
    return ok(res, result);
  }),
);

/** List of recent conversations (inbox) for the current user */
router.get(
  '/inbox',
  authMiddleware,
  asyncHandler(async (req: Request, res: Response) => {
    const me = req.user!.userId;
    const rows = await query(
      req.db!,
      `SELECT u.id as user_id, u.name, u.profile_photo,
              (SELECT message FROM chat_messages cm2
               WHERE (cm2.sender_id = u.id AND cm2.receiver_id = ?) OR (cm2.sender_id = ? AND cm2.receiver_id = u.id)
               ORDER BY cm2.sent_at DESC LIMIT 1) as last_message,
              (SELECT COUNT(*) FROM chat_messages cm3 WHERE cm3.sender_id = u.id AND cm3.receiver_id = ? AND cm3.is_read = 0) as unread_count
       FROM users u
       WHERE u.id IN (
         SELECT DISTINCT CASE WHEN sender_id = ? THEN receiver_id ELSE sender_id END
         FROM chat_messages WHERE sender_id = ? OR receiver_id = ?
       )`,
      [me, me, me, me, me, me],
    );
    return ok(res, rows);
  }),
);

export default router;
