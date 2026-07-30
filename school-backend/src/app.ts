import compression from 'compression';
import cors from 'cors';
import express, { Express } from 'express';
import helmet from 'helmet';
import morgan from 'morgan';
import path from 'path';
import rateLimit from 'express-rate-limit';

import { env } from './config/env';
import { getMasterPool } from './core/masterDb';
import { tenantMiddleware } from './middlewares/tenant.middleware';
import { errorHandler, notFoundHandler } from './middlewares/error.middleware';

import authRoutes from './modules/auth/auth.routes';
import academicRoutes from './modules/academic/academic.routes';
import usersRoutes from './modules/users/users.routes';
import attendanceRoutes from './modules/attendance/attendance.routes';
import homeworkRoutes from './modules/homework/homework.routes';
import assignmentRoutes from './modules/assignment/assignment.routes';
import classtestRoutes from './modules/classtest/classtest.routes';
import projectRoutes from './modules/project/project.routes';
import timetableRoutes from './modules/timetable/timetable.routes';
import examRoutes from './modules/exam/exam.routes';
import feesRoutes from './modules/fees/fees.routes';
import noticeRoutes from './modules/notice/notice.routes';
import leaveRoutes from './modules/leave/leave.routes';
import complaintRoutes from './modules/complaint/complaint.routes';
import documentsRoutes from './modules/documents/documents.routes';
import chatRoutes from './modules/chat/chat.routes';
import pointsRoutes from './modules/points/points.routes';
import dashboardRoutes from './modules/dashboard/dashboard.routes';
import superAdminRoutes from './modules/superadmin/superadmin.routes';

export function createApp(): Express {
  const app = express();

  app.use(helmet());
  app.use(cors());
  app.use(compression());
  app.use(express.json({ limit: '5mb' }));
  app.use(express.urlencoded({ extended: true }));
  if (env.nodeEnv !== 'test') app.use(morgan(env.nodeEnv === 'production' ? 'combined' : 'dev'));

  // Basic rate limiting to slow down brute-force attempts on auth endpoints
  const authLimiter = rateLimit({ windowMs: 15 * 60 * 1000, limit: 100 });

  // Serve uploaded files statically: /uploads/<schoolCode>/<file>
  app.use('/uploads', express.static(path.resolve(process.cwd(), env.upload.dir)));

  app.get('/health', async (_req, res) => {
    let dbStatus = 'disconnected';
    try {
      await getMasterPool().query('SELECT 1');
      dbStatus = 'connected';
    } catch (err) {}
    return res.json({
      status: true,
      message: 'School LMS backend is running',
      database: dbStatus,
      dbName: env.masterDbName,
    });
  });

  // ---- Platform-level routes (operate on the MASTER database, no school context) ----
  app.use('/api/super-admin', superAdminRoutes);

  // ---- School-scoped routes ----
  // Every request here must include the school code, either as a URL param
  // (/api/:schoolCode/...) or via the "X-School-Code" header - tenantMiddleware
  // accepts either. Using the URL param form makes the routes below self-documenting.
  const schoolRouter = express.Router({ mergeParams: true });
  schoolRouter.use(tenantMiddleware);

  schoolRouter.use('/auth', authLimiter, authRoutes);
  schoolRouter.use('/academic', academicRoutes);
  schoolRouter.use('/users', usersRoutes);
  schoolRouter.use('/attendance', attendanceRoutes);
  schoolRouter.use('/homework', homeworkRoutes);
  schoolRouter.use('/assignments', assignmentRoutes);
  schoolRouter.use('/classtests', classtestRoutes);
  schoolRouter.use('/projects', projectRoutes);
  schoolRouter.use('/timetable', timetableRoutes);
  schoolRouter.use('/exams', examRoutes);
  schoolRouter.use('/fees', feesRoutes);
  schoolRouter.use('/notices', noticeRoutes);
  schoolRouter.use('/leave', leaveRoutes);
  schoolRouter.use('/complaints', complaintRoutes);
  schoolRouter.use('/files', documentsRoutes);
  schoolRouter.use('/chat', chatRoutes);
  schoolRouter.use('/points', pointsRoutes);
  schoolRouter.use('/dashboard', dashboardRoutes);

  app.use('/api/:schoolCode', schoolRouter);

  app.use(notFoundHandler);
  app.use(errorHandler);

  return app;
}
