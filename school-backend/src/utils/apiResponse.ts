import { Response } from 'express';

/**
 * Every endpoint responds with this same envelope shape so the Android app's
 * BaseModel-style parsing (status/message/data) works consistently.
 */
export function ok(res: Response, data: unknown = null, message = 'Success', statusCode = 200) {
  return res.status(statusCode).json({ status: true, message, data });
}

export function created(res: Response, data: unknown = null, message = 'Created successfully') {
  return ok(res, data, message, 201);
}

export function fail(res: Response, message = 'Something went wrong', statusCode = 400, data: unknown = null) {
  return res.status(statusCode).json({ status: false, message, data });
}
