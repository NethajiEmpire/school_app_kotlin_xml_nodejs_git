import fs from 'fs';
import multer from 'multer';
import path from 'path';
import { v4 as uuid } from 'uuid';
import { env } from '../config/env';

const uploadRoot = path.resolve(process.cwd(), env.upload.dir);
if (!fs.existsSync(uploadRoot)) fs.mkdirSync(uploadRoot, { recursive: true });

const storage = multer.diskStorage({
  destination: (req, _file, cb) => {
    // Files are grouped per-school so two schools' uploads never collide
    const schoolFolder = req.school?.school_code || 'common';
    const dir = path.join(uploadRoot, schoolFolder);
    if (!fs.existsSync(dir)) fs.mkdirSync(dir, { recursive: true });
    cb(null, dir);
  },
  filename: (_req, file, cb) => {
    const ext = path.extname(file.originalname);
    cb(null, `${Date.now()}-${uuid()}${ext}`);
  },
});

export const upload = multer({
  storage,
  limits: { fileSize: env.upload.maxMb * 1024 * 1024 },
});

/** Builds a public URL for a stored file, given req + the relative storage path. */
export function fileUrl(req: { school?: { school_code: string } }, filename: string): string {
  const schoolFolder = req.school?.school_code || 'common';
  return `${env.baseUrl}/uploads/${schoolFolder}/${filename}`;
}
