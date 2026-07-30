import { Request, Response } from 'express';
import { asyncHandler } from '../../utils/asyncHandler';
import { created, ok } from '../../utils/apiResponse';
import * as usersService from './users.service';
import { AppError } from '../../utils/AppError';
import { fileUrl } from '../../middlewares/upload.middleware';

// ---- Students ----
export const createStudentHandler = asyncHandler(async (req: Request, res: Response) => {
  const b = req.body;
  if (!b.name || !b.mobile || !b.password || !b.standardId || !b.sectionId) {
    throw new AppError(400, 'name, mobile, password, standardId, sectionId are required');
  }
  const student = await usersService.createStudent(req.db!, {
    name: b.name, mobile: b.mobile, email: b.email, password: b.password,
    standardId: b.standardId, sectionId: b.sectionId, batchId: b.batchId,
    dob: b.dob, gender: b.gender, address: b.address, rollNo: b.rollNo,
  });
  return created(res, student, 'Student created successfully');
});

export const listStudentsHandler = asyncHandler(async (req: Request, res: Response) => {
  const result = await usersService.listStudents(req.db!, {
    standardId: req.query.standardId ? Number(req.query.standardId) : undefined,
    sectionId: req.query.sectionId ? Number(req.query.sectionId) : undefined,
    page: req.query.page ? Number(req.query.page) : undefined,
    limit: req.query.limit ? Number(req.query.limit) : undefined,
  });
  return ok(res, result);
});

export const getStudentHandler = asyncHandler(async (req: Request, res: Response) => {
  const student = await usersService.getStudentById(req.db!, Number(req.params.id));
  return ok(res, student);
});

export const updateStudentHandler = asyncHandler(async (req: Request, res: Response) => {
  const student = await usersService.updateStudent(req.db!, Number(req.params.id), req.body);
  return ok(res, student, 'Student updated successfully');
});

// ---- Teachers ----
export const createTeacherHandler = asyncHandler(async (req: Request, res: Response) => {
  const b = req.body;
  if (!b.name || !b.mobile || !b.password) throw new AppError(400, 'name, mobile, password are required');
  const teacher = await usersService.createTeacher(req.db!, b);
  return created(res, teacher, 'Teacher created successfully');
});

export const listTeachersHandler = asyncHandler(async (req: Request, res: Response) => {
  const result = await usersService.listTeachers(req.db!, {
    page: req.query.page ? Number(req.query.page) : undefined,
    limit: req.query.limit ? Number(req.query.limit) : undefined,
  });
  return ok(res, result);
});

export const getTeacherHandler = asyncHandler(async (req: Request, res: Response) => {
  const teacher = await usersService.getTeacherById(req.db!, Number(req.params.id));
  return ok(res, teacher);
});

export const assignTeacherSubjectHandler = asyncHandler(async (req: Request, res: Response) => {
  const b = req.body;
  if (!b.teacherId || !b.subjectId || !b.standardId || !b.sectionId) {
    throw new AppError(400, 'teacherId, subjectId, standardId, sectionId are required');
  }
  const result = await usersService.assignTeacherSubject(req.db!, b);
  return created(res, result, 'Teacher assigned to subject/class');
});

export const myTeachersHandler = asyncHandler(async (req: Request, res: Response) => {
  const studentId = Number(req.params.studentId);
  const teachers = await usersService.getMyTeachers(req.db!, studentId);
  return ok(res, teachers);
});

// ---- Staff ----
export const createStaffHandler = asyncHandler(async (req: Request, res: Response) => {
  const b = req.body;
  if (!b.name || !b.mobile || !b.password) throw new AppError(400, 'name, mobile, password are required');
  const staff = await usersService.createStaff(req.db!, b);
  return created(res, staff, 'Staff created successfully');
});

export const listStaffHandler = asyncHandler(async (req: Request, res: Response) => {
  const result = await usersService.listStaff(req.db!, {
    page: req.query.page ? Number(req.query.page) : undefined,
    limit: req.query.limit ? Number(req.query.limit) : undefined,
  });
  return ok(res, result);
});

// ---- Guest admissions ----
export const createGuestAdmissionHandler = asyncHandler(async (req: Request, res: Response) => {
  const b = req.body;
  if (!b.name || !b.mobile) throw new AppError(400, 'name and mobile are required');
  const result = await usersService.createGuestAdmission(req.db!, b);
  return created(res, result, 'Admission enquiry submitted');
});

export const listGuestAdmissionsHandler = asyncHandler(async (req: Request, res: Response) => {
  const result = await usersService.listGuestAdmissions(req.db!, {
    page: req.query.page ? Number(req.query.page) : undefined,
    limit: req.query.limit ? Number(req.query.limit) : undefined,
  });
  return ok(res, result);
});

export const updateGuestAdmissionStatusHandler = asyncHandler(async (req: Request, res: Response) => {
  const { status } = req.body;
  if (!['approved', 'rejected'].includes(status)) throw new AppError(400, 'status must be approved or rejected');
  const result = await usersService.updateGuestAdmissionStatus(req.db!, Number(req.params.id), status);
  return ok(res, result, 'Admission status updated');
});

// ---- Profile ----
export const myProfileHandler = asyncHandler(async (req: Request, res: Response) => {
  const profile = await usersService.getMyProfile(req.db!, req.user!.userId, req.user!.roleName);
  return ok(res, profile);
});

export const uploadProfilePhotoHandler = asyncHandler(async (req: Request, res: Response) => {
  if (!req.file) throw new AppError(400, 'No file uploaded');
  const url = fileUrl(req, req.file.filename);
  const result = await usersService.updateProfilePhoto(req.db!, req.user!.userId, url);
  return ok(res, result, 'Profile photo updated');
});
