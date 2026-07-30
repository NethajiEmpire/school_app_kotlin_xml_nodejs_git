import { Router } from 'express';
import { authMiddleware, requireRole } from '../../middlewares/auth.middleware';
import { upload } from '../../middlewares/upload.middleware';
import * as c from './users.controller';

const router = Router();

// Profile (any logged-in user)
router.get('/profile/me', authMiddleware, c.myProfileHandler);
router.post('/profile/photo', authMiddleware, upload.single('photo'), c.uploadProfilePhotoHandler);

// Students
router.post('/students', authMiddleware, requireRole('admin'), c.createStudentHandler);
router.get('/students', authMiddleware, requireRole('admin', 'teacher'), c.listStudentsHandler);
router.get('/students/:id', authMiddleware, c.getStudentHandler);
router.put('/students/:id', authMiddleware, requireRole('admin'), c.updateStudentHandler);
router.get('/students/:studentId/teachers', authMiddleware, c.myTeachersHandler);

// Teachers
router.post('/teachers', authMiddleware, requireRole('admin'), c.createTeacherHandler);
router.get('/teachers', authMiddleware, c.listTeachersHandler);
router.get('/teachers/:id', authMiddleware, c.getTeacherHandler);
router.post('/teacher-subjects', authMiddleware, requireRole('admin'), c.assignTeacherSubjectHandler);

// Staff
router.post('/staff', authMiddleware, requireRole('admin'), c.createStaffHandler);
router.get('/staff', authMiddleware, requireRole('admin'), c.listStaffHandler);

// Guest admissions (enquiry form is public within the tenant - still needs school code header)
router.post('/guest-admissions', c.createGuestAdmissionHandler);
router.get('/guest-admissions', authMiddleware, requireRole('admin'), c.listGuestAdmissionsHandler);
router.put('/guest-admissions/:id/status', authMiddleware, requireRole('admin'), c.updateGuestAdmissionStatusHandler);

export default router;
