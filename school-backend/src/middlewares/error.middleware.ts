import { NextFunction, Request, Response } from 'express';
import { AppError } from '../utils/AppError';
import { logger } from '../utils/logger';

export function notFoundHandler(req: Request, res: Response) {
  res.status(404).json({ status: false, message: `Route not found: ${req.method} ${req.originalUrl}`, data: null });
}

// eslint-disable-next-line @typescript-eslint/no-unused-vars
export function errorHandler(err: unknown, req: Request, res: Response, _next: NextFunction) {
  if (err instanceof AppError) {
    return res.status(err.statusCode).json({ status: false, message: err.message, data: null });
  }

  // MySQL duplicate entry, etc.
  const anyErr = err as { code?: string; sqlMessage?: string; message?: string };
  if (anyErr?.code === 'ER_DUP_ENTRY') {
    return res.status(409).json({ status: false, message: 'Duplicate entry. This record already exists.', data: null });
  }

  logger.error('Unhandled error:', err);
  const message = anyErr?.sqlMessage || anyErr?.message || 'Internal server error';
  return res.status(500).json({ status: false, message, data: null });
}
