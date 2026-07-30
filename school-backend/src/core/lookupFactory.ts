import { Router, Request, Response } from 'express';
import { asyncHandler } from '../utils/asyncHandler';
import { created, ok } from '../utils/apiResponse';
import { exec, insertRow, query, queryOne, updateRow } from './dbHelpers';
import { AppError } from '../utils/AppError';
import { authMiddleware, requireRole } from '../middlewares/auth.middleware';

interface LookupOptions {
  table: string;
  /** Columns accepted on create/update, e.g. ['name','board_id'] */
  fields: string[];
  /** Roles allowed to create/update/delete. Everyone authenticated can read (GET). */
  writeRoles?: string[];
  /** Optional extra WHERE clause column to filter list by (e.g. filter subjects by ?standardId=) */
  filterableBy?: string[];
}

/**
 * Generates a full REST router (GET list, GET one, POST, PUT, DELETE) for a
 * simple reference/lookup table. Used for boards, standards, sections,
 * subjects, batches, programs, academic years, leave types, complaint types,
 * document master, roles - anything that's basically a dropdown source.
 */
export function createLookupRouter(opts: LookupOptions): Router {
  const router = Router();
  const { table, fields, writeRoles = ['admin'], filterableBy = [] } = opts;

  router.get(
    '/',
    authMiddleware,
    asyncHandler(async (req: Request, res: Response) => {
      const conditions: string[] = [];
      const params: unknown[] = [];
      for (const col of filterableBy) {
        const val = req.query[col];
        if (val !== undefined) {
          conditions.push(`\`${col}\` = ?`);
          params.push(val);
        }
      }
      const where = conditions.length ? `WHERE ${conditions.join(' AND ')}` : '';
      const rows = await query(req.db!, `SELECT * FROM \`${table}\` ${where} ORDER BY id`, params);
      return ok(res, rows);
    }),
  );

  router.get(
    '/:id',
    authMiddleware,
    asyncHandler(async (req: Request, res: Response) => {
      const row = await queryOne(req.db!, `SELECT * FROM \`${table}\` WHERE id = ?`, [req.params.id]);
      if (!row) throw new AppError(404, 'Not found');
      return ok(res, row);
    }),
  );

  router.post(
    '/',
    authMiddleware,
    requireRole(...writeRoles),
    asyncHandler(async (req: Request, res: Response) => {
      const values: Record<string, unknown> = {};
      for (const f of fields) if (req.body[f] !== undefined) values[f] = req.body[f];
      if (Object.keys(values).length === 0) throw new AppError(400, `Provide at least one of: ${fields.join(', ')}`);
      const id = await insertRow(req.db!, table, values);
      const row = await queryOne(req.db!, `SELECT * FROM \`${table}\` WHERE id = ?`, [id]);
      return created(res, row);
    }),
  );

  router.put(
    '/:id',
    authMiddleware,
    requireRole(...writeRoles),
    asyncHandler(async (req: Request, res: Response) => {
      const values: Record<string, unknown> = {};
      for (const f of fields) if (req.body[f] !== undefined) values[f] = req.body[f];
      const affected = await updateRow(req.db!, table, Number(req.params.id), values);
      if (!affected) throw new AppError(404, 'Not found');
      const row = await queryOne(req.db!, `SELECT * FROM \`${table}\` WHERE id = ?`, [req.params.id]);
      return ok(res, row, 'Updated successfully');
    }),
  );

  router.delete(
    '/:id',
    authMiddleware,
    requireRole(...writeRoles),
    asyncHandler(async (req: Request, res: Response) => {
      const result = await exec(req.db!, `DELETE FROM \`${table}\` WHERE id = ?`, [req.params.id]);
      if (!result.affectedRows) throw new AppError(404, 'Not found');
      return ok(res, null, 'Deleted successfully');
    }),
  );

  return router;
}
