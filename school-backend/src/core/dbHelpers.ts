import { Pool, RowDataPacket, ResultSetHeader } from 'mysql2/promise';

/** Run a SELECT and get back plain rows, typed as T[]. */
export async function query<T = RowDataPacket>(pool: Pool, sql: string, params: unknown[] = []): Promise<T[]> {
  const [rows] = await pool.query(sql, params);
  return rows as T[];
}

/** Run a SELECT expecting at most one row. */
export async function queryOne<T = RowDataPacket>(pool: Pool, sql: string, params: unknown[] = []): Promise<T | null> {
  const rows = await query<T>(pool, sql, params);
  return rows.length ? rows[0] : null;
}

/** Run an INSERT/UPDATE/DELETE and get back the ResultSetHeader (insertId, affectedRows...). */
export async function exec(pool: Pool, sql: string, params: unknown[] = []): Promise<ResultSetHeader> {
  const [result] = await pool.query(sql, params);
  return result as ResultSetHeader;
}

/** Build a simple paginated query. Returns { data, page, limit, total, totalPages }. */
export async function paginate<T = RowDataPacket>(
  pool: Pool,
  baseSql: string,
  countSql: string,
  params: unknown[],
  page = 1,
  limit = 20,
) {
  const safePage = Math.max(1, page);
  const safeLimit = Math.min(100, Math.max(1, limit));
  const offset = (safePage - 1) * safeLimit;

  const [data, totalRow] = await Promise.all([
    query<T>(pool, `${baseSql} LIMIT ? OFFSET ?`, [...params, safeLimit, offset]),
    queryOne<{ total: number }>(pool, countSql, params),
  ]);

  const total = totalRow?.total ?? 0;
  return { data, page: safePage, limit: safeLimit, total, totalPages: Math.ceil(total / safeLimit) };
}

/** Simple INSERT builder from a plain object of column:value pairs. Returns insertId. */
export async function insertRow(pool: Pool, table: string, values: Record<string, unknown>): Promise<number> {
  const columns = Object.keys(values);
  const placeholders = columns.map(() => '?').join(', ');
  const sql = `INSERT INTO \`${table}\` (${columns.map((c) => `\`${c}\``).join(', ')}) VALUES (${placeholders})`;
  const result = await exec(pool, sql, Object.values(values));
  return result.insertId;
}

/** Simple UPDATE builder: updates row(s) matching id column. */
export async function updateRow(
  pool: Pool,
  table: string,
  id: number,
  values: Record<string, unknown>,
  idColumn = 'id',
): Promise<number> {
  const columns = Object.keys(values);
  if (columns.length === 0) return 0;
  const setClause = columns.map((c) => `\`${c}\` = ?`).join(', ');
  const sql = `UPDATE \`${table}\` SET ${setClause} WHERE \`${idColumn}\` = ?`;
  const result = await exec(pool, sql, [...Object.values(values), id]);
  return result.affectedRows;
}
