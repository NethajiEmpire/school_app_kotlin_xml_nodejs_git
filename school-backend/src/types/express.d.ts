import { Pool } from 'mysql2/promise';
import { AuthTokenPayload } from '../utils/jwt';
import { SchoolRow } from '../core/tenantManager';

declare global {
  namespace Express {
    interface Request {
      /** Resolved from X-School-Code header / :schoolCode param by tenant.middleware */
      school?: SchoolRow;
      /** Ready-to-use MySQL pool for the current school's database */
      db?: Pool;
      /** Decoded JWT payload after auth.middleware runs */
      user?: AuthTokenPayload;
    }
  }
}

export {};
