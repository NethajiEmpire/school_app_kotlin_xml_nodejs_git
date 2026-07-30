/* Minimal logger. Swap for winston/pino later if needed. */
const ts = () => new Date().toISOString();

export const logger = {
  info: (...args: unknown[]) => console.log(`[INFO ${ts()}]`, ...args),
  warn: (...args: unknown[]) => console.warn(`[WARN ${ts()}]`, ...args),
  error: (...args: unknown[]) => console.error(`[ERROR ${ts()}]`, ...args),
};
