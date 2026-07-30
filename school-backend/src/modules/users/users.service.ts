import { Pool } from 'mysql2/promise';
import { AppError } from '../../utils/AppError';
import { exec, insertRow, paginate, query, queryOne, updateRow } from '../../core/dbHelpers';
import { hashPassword, generateCode } from '../../utils/hash';

interface RoleRow { id: number }

async function createUserAccount(
  db: Pool,
  roleName: string,
  data: { name: string; mobile: string; email?: string; password: string },
): Promise<number> {
  const role = await queryOne<RoleRow>(db, 'SELECT id FROM roles WHERE name = ?', [roleName]);
  if (!role) throw new AppError(400, `Role "${roleName}" not found`);

  const existing = await queryOne(db, 'SELECT id FROM users WHERE mobile = ?', [data.mobile]);
  if (existing) throw new AppError(409, 'A user with this mobile number already exists');

  const passwordHash = await hashPassword(data.password);
  return insertRow(db, 'users', {
    role_id: role.id,
    name: data.name,
    mobile: data.mobile,
    email: data.email || null,
    password_hash: passwordHash,
    status: 'active',
  });
}

// ---------------- Students ----------------
export async function createStudent(
  db: Pool,
  input: {
    name: string; mobile: string; email?: string; password: string;
    standardId: number; sectionId: number; batchId?: number; dob?: string;
    gender?: string; address?: string; rollNo?: string;
  },
) {
  const userId = await createUserAccount(db, 'student', input);
  const admissionNo = generateCode('ADM');
  const studentId = await insertRow(db, 'students', {
    user_id: userId,
    admission_no: admissionNo,
    standard_id: input.standardId,
    section_id: input.sectionId,
    batch_id: input.batchId || null,
    dob: input.dob || null,
    gender: input.gender || null,
    address: input.address || null,
    roll_no: input.rollNo || null,
    admission_date: new Date().toISOString().slice(0, 10),
  });
  return getStudentById(db, studentId);
}

export async function listStudents(db: Pool, filters: { standardId?: number; sectionId?: number; page?: number; limit?: number }) {
  const conditions: string[] = [];
  const params: unknown[] = [];
  if (filters.standardId) { conditions.push('s.standard_id = ?'); params.push(filters.standardId); }
  if (filters.sectionId) { conditions.push('s.section_id = ?'); params.push(filters.sectionId); }
  const where = conditions.length ? `WHERE ${conditions.join(' AND ')}` : '';

  const baseSql = `
    SELECT s.id, s.admission_no, s.roll_no, s.dob, s.gender, s.admission_date,
           u.id as user_id, u.name, u.mobile, u.email, u.profile_photo, u.status,
           st.name as standard_name, sec.name as section_name
    FROM students s
    JOIN users u ON u.id = s.user_id
    JOIN standards st ON st.id = s.standard_id
    JOIN sections sec ON sec.id = s.section_id
    ${where}
    ORDER BY s.id DESC`;
  const countSql = `SELECT COUNT(*) as total FROM students s ${where}`;

  return paginate(db, baseSql, countSql, params, filters.page, filters.limit);
}

export async function getStudentById(db: Pool, studentId: number) {
  const row = await queryOne(
    db,
    `SELECT s.*, u.name, u.mobile, u.email, u.profile_photo, u.status,
            st.name as standard_name, sec.name as section_name
     FROM students s
     JOIN users u ON u.id = s.user_id
     JOIN standards st ON st.id = s.standard_id
     JOIN sections sec ON sec.id = s.section_id
     WHERE s.id = ?`,
    [studentId],
  );
  if (!row) throw new AppError(404, 'Student not found');
  return row;
}

export async function updateStudent(db: Pool, studentId: number, input: Record<string, unknown>) {
  const allowed = ['standard_id', 'section_id', 'batch_id', 'dob', 'gender', 'address', 'roll_no'];
  const values: Record<string, unknown> = {};
  for (const key of allowed) if (input[key] !== undefined) values[key] = input[key];
  await updateRow(db, 'students', studentId, values);
  return getStudentById(db, studentId);
}

// ---------------- Teachers ----------------
export async function createTeacher(
  db: Pool,
  input: { name: string; mobile: string; email?: string; password: string; designation?: string; qualification?: string },
) {
  const userId = await createUserAccount(db, 'teacher', input);
  const employeeCode = generateCode('TCH');
  const teacherId = await insertRow(db, 'teachers', {
    user_id: userId,
    employee_code: employeeCode,
    designation: input.designation || null,
    qualification: input.qualification || null,
    joining_date: new Date().toISOString().slice(0, 10),
  });
  return getTeacherById(db, teacherId);
}

export async function listTeachers(db: Pool, filters: { page?: number; limit?: number }) {
  const baseSql = `
    SELECT t.id, t.employee_code, t.designation, t.qualification, t.joining_date,
           u.id as user_id, u.name, u.mobile, u.email, u.profile_photo, u.status
    FROM teachers t JOIN users u ON u.id = t.user_id
    ORDER BY t.id DESC`;
  const countSql = `SELECT COUNT(*) as total FROM teachers`;
  return paginate(db, baseSql, countSql, [], filters.page, filters.limit);
}

export async function getTeacherById(db: Pool, teacherId: number) {
  const row = await queryOne(
    db,
    `SELECT t.*, u.name, u.mobile, u.email, u.profile_photo, u.status
     FROM teachers t JOIN users u ON u.id = t.user_id WHERE t.id = ?`,
    [teacherId],
  );
  if (!row) throw new AppError(404, 'Teacher not found');
  return row;
}

export async function assignTeacherSubject(
  db: Pool,
  input: { teacherId: number; subjectId: number; standardId: number; sectionId: number },
) {
  const id = await insertRow(db, 'teacher_subjects', {
    teacher_id: input.teacherId,
    subject_id: input.subjectId,
    standard_id: input.standardId,
    section_id: input.sectionId,
  });
  return { id, ...input };
}

export async function getMyTeachers(db: Pool, studentId: number) {
  const student = await queryOne<{ standard_id: number; section_id: number }>(
    db,
    'SELECT standard_id, section_id FROM students WHERE id = ?',
    [studentId],
  );
  if (!student) throw new AppError(404, 'Student not found');

  return query(
    db,
    `SELECT DISTINCT t.id, u.name, u.mobile, u.email, u.profile_photo, t.designation, sub.name as subject_name
     FROM teacher_subjects ts
     JOIN teachers t ON t.id = ts.teacher_id
     JOIN users u ON u.id = t.user_id
     JOIN subjects sub ON sub.id = ts.subject_id
     WHERE ts.standard_id = ? AND ts.section_id = ?`,
    [student.standard_id, student.section_id],
  );
}

// ---------------- Staff ----------------
export async function createStaff(
  db: Pool,
  input: { name: string; mobile: string; email?: string; password: string; designation?: string; department?: string },
) {
  const userId = await createUserAccount(db, 'staff', input);
  const employeeCode = generateCode('STF');
  const staffId = await insertRow(db, 'staff', {
    user_id: userId,
    employee_code: employeeCode,
    designation: input.designation || null,
    department: input.department || null,
    joining_date: new Date().toISOString().slice(0, 10),
  });
  return queryOne(db, `SELECT s.*, u.name, u.mobile, u.email FROM staff s JOIN users u ON u.id = s.user_id WHERE s.id = ?`, [staffId]);
}

export async function listStaff(db: Pool, filters: { page?: number; limit?: number }) {
  const baseSql = `SELECT s.*, u.name, u.mobile, u.email, u.status FROM staff s JOIN users u ON u.id = s.user_id ORDER BY s.id DESC`;
  const countSql = `SELECT COUNT(*) as total FROM staff`;
  return paginate(db, baseSql, countSql, [], filters.page, filters.limit);
}

// ---------------- Guest admissions ----------------
export async function createGuestAdmission(db: Pool, input: { name: string; mobile: string; email?: string; standardId?: number }) {
  const id = await insertRow(db, 'guest_admissions', {
    name: input.name,
    mobile: input.mobile,
    email: input.email || null,
    standard_id: input.standardId || null,
  });
  return queryOne(db, 'SELECT * FROM guest_admissions WHERE id = ?', [id]);
}

export async function listGuestAdmissions(db: Pool, filters: { page?: number; limit?: number }) {
  const baseSql = `SELECT * FROM guest_admissions ORDER BY id DESC`;
  const countSql = `SELECT COUNT(*) as total FROM guest_admissions`;
  return paginate(db, baseSql, countSql, [], filters.page, filters.limit);
}

export async function updateGuestAdmissionStatus(db: Pool, id: number, status: 'approved' | 'rejected') {
  const affected = await updateRow(db, 'guest_admissions', id, { status });
  if (!affected) throw new AppError(404, 'Guest admission enquiry not found');
  return queryOne(db, 'SELECT * FROM guest_admissions WHERE id = ?', [id]);
}

// ---------------- Generic "my profile" ----------------
export async function getMyProfile(db: Pool, userId: number, roleName: string) {
  const user = await queryOne(
    db,
    `SELECT u.id, u.name, u.mobile, u.email, u.profile_photo, u.status, u.last_login_at, r.name as role
     FROM users u JOIN roles r ON r.id = u.role_id WHERE u.id = ?`,
    [userId],
  );
  if (!user) throw new AppError(404, 'User not found');

  if (roleName === 'student') {
    const student = await queryOne(
      db,
      `SELECT s.*, st.name as standard_name, sec.name as section_name
       FROM students s JOIN standards st ON st.id = s.standard_id JOIN sections sec ON sec.id = s.section_id
       WHERE s.user_id = ?`,
      [userId],
    );
    return { ...user, studentDetails: student };
  }
  if (roleName === 'teacher') {
    const teacher = await queryOne(db, 'SELECT * FROM teachers WHERE user_id = ?', [userId]);
    return { ...user, teacherDetails: teacher };
  }
  if (roleName === 'staff') {
    const staff = await queryOne(db, 'SELECT * FROM staff WHERE user_id = ?', [userId]);
    return { ...user, staffDetails: staff };
  }
  if (roleName === 'parent') {
    const parent = await queryOne(db, 'SELECT * FROM parents WHERE user_id = ?', [userId]);
    const children = parent
      ? await query(
          db,
          `SELECT s.id, u.name, s.admission_no, st.name as standard_name, sec.name as section_name
           FROM students s JOIN users u ON u.id = s.user_id
           JOIN standards st ON st.id = s.standard_id JOIN sections sec ON sec.id = s.section_id
           WHERE s.parent_id = ?`,
          [(parent as { id: number }).id],
        )
      : [];
    return { ...user, parentDetails: parent, children };
  }
  return user;
}

export async function updateProfilePhoto(db: Pool, userId: number, photoUrl: string) {
  await exec(db, 'UPDATE users SET profile_photo = ? WHERE id = ?', [photoUrl, userId]);
  return { profilePhoto: photoUrl };
}
