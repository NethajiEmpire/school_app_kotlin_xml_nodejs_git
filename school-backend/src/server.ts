import { createApp } from './app';
import { env } from './config/env';
import { logger } from './utils/logger';
import { closeAllTenantPools } from './core/tenantManager';

import { getMasterPool } from './core/masterDb';

const app = createApp();

const server = app.listen(env.port, async () => {
  logger.info(`School LMS backend listening on port ${env.port} (${env.nodeEnv})`);
  logger.info(`Health check: ${env.baseUrl}/health`);
  try {
    await getMasterPool().query('SELECT 1');
    logger.info(`Database connected successfully (${env.masterDbName})`);
  } catch (err: any) {
    logger.error(`Database connection failed: ${err.message}`);
  }
});

async function shutdown(signal: string) {
  logger.info(`${signal} received. Shutting down gracefully...`);
  server.close(async () => {
    await closeAllTenantPools();
    process.exit(0);
  });
}

process.on('SIGINT', () => shutdown('SIGINT'));
process.on('SIGTERM', () => shutdown('SIGTERM'));
