-- =====================================================================
-- TENANT (PER-SCHOOL) DATABASE SCHEMA
-- One copy of this schema is created for every school by
-- core/tenantManager.ts -> provisionSchoolDatabase(). Completely isolated
-- from every other school's data.
-- =====================================================================

-- ---------- Roles & accounts ----------
CREATE TABLE IF NOT EXISTS roles (
  id   INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(40) NOT NULL UNIQUE   -- admin, teacher, student, parent, staff, guest
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS users (
  id             INT AUTO_INCREMENT PRIMARY KEY,
  role_id        INT NOT NULL,
  name           VARCHAR(150) NOT NULL,
  mobile         VARCHAR(20) NOT NULL,
  email          VARCHAR(150),
  password_hash  VARCHAR(255) NOT NULL,
  profile_photo  VARCHAR(255),
  status         ENUM('active','inactive') NOT NULL DEFAULT 'active',
  last_login_at  DATETIME NULL,
  created_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY uq_users_mobile (mobile),
  CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES roles(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS otp_verifications (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  mobile      VARCHAR(20) NOT NULL,
  otp         VARCHAR(10) NOT NULL,
  purpose     ENUM('register','login','forgot_password') NOT NULL DEFAULT 'register',
  is_verified TINYINT(1) NOT NULL DEFAULT 0,
  expires_at  DATETIME NOT NULL,
  created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------- Academic structure ----------
CREATE TABLE IF NOT EXISTS academic_years (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  name       VARCHAR(20) NOT NULL,        -- "2026-2027"
  start_date DATE NOT NULL,
  end_date   DATE NOT NULL,
  is_current TINYINT(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS boards (
  id   INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL              -- CBSE, ICSE, State Board...
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS standards (
  id       INT AUTO_INCREMENT PRIMARY KEY,
  board_id INT NULL,
  name     VARCHAR(50) NOT NULL,          -- "10th", "Grade 5"...
  CONSTRAINT fk_standards_board FOREIGN KEY (board_id) REFERENCES boards(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sections (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  standard_id INT NOT NULL,
  name        VARCHAR(20) NOT NULL,        -- "A", "B"
  CONSTRAINT fk_sections_standard FOREIGN KEY (standard_id) REFERENCES standards(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS subjects (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  standard_id INT NULL,
  name        VARCHAR(100) NOT NULL,
  code        VARCHAR(20),
  CONSTRAINT fk_subjects_standard FOREIGN KEY (standard_id) REFERENCES standards(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS batches (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  name       VARCHAR(50) NOT NULL,
  start_date DATE,
  end_date   DATE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS programs (
  id   INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------- People ----------
CREATE TABLE IF NOT EXISTS parents (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  user_id    INT NOT NULL UNIQUE,
  occupation VARCHAR(100),
  address    VARCHAR(255),
  CONSTRAINT fk_parents_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS students (
  id              INT AUTO_INCREMENT PRIMARY KEY,
  user_id         INT NOT NULL UNIQUE,
  admission_no    VARCHAR(40) NOT NULL UNIQUE,
  standard_id     INT NOT NULL,
  section_id      INT NOT NULL,
  batch_id        INT NULL,
  parent_id       INT NULL,
  dob             DATE,
  gender          ENUM('male','female','other'),
  address         VARCHAR(255),
  admission_date  DATE,
  roll_no         VARCHAR(20),
  CONSTRAINT fk_students_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT fk_students_standard FOREIGN KEY (standard_id) REFERENCES standards(id),
  CONSTRAINT fk_students_section FOREIGN KEY (section_id) REFERENCES sections(id),
  CONSTRAINT fk_students_batch FOREIGN KEY (batch_id) REFERENCES batches(id),
  CONSTRAINT fk_students_parent FOREIGN KEY (parent_id) REFERENCES parents(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS teachers (
  id             INT AUTO_INCREMENT PRIMARY KEY,
  user_id        INT NOT NULL UNIQUE,
  employee_code  VARCHAR(40) NOT NULL UNIQUE,
  designation    VARCHAR(100),
  qualification  VARCHAR(150),
  joining_date   DATE,
  CONSTRAINT fk_teachers_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS staff (
  id             INT AUTO_INCREMENT PRIMARY KEY,
  user_id        INT NOT NULL UNIQUE,
  employee_code  VARCHAR(40) NOT NULL UNIQUE,
  designation    VARCHAR(100),
  department     VARCHAR(100),
  joining_date   DATE,
  CONSTRAINT fk_staff_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS teacher_subjects (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  teacher_id  INT NOT NULL,
  subject_id  INT NOT NULL,
  standard_id INT NOT NULL,
  section_id  INT NOT NULL,
  CONSTRAINT fk_ts_teacher FOREIGN KEY (teacher_id) REFERENCES teachers(id) ON DELETE CASCADE,
  CONSTRAINT fk_ts_subject FOREIGN KEY (subject_id) REFERENCES subjects(id),
  CONSTRAINT fk_ts_standard FOREIGN KEY (standard_id) REFERENCES standards(id),
  CONSTRAINT fk_ts_section FOREIGN KEY (section_id) REFERENCES sections(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS guest_admissions (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  name        VARCHAR(150) NOT NULL,
  mobile      VARCHAR(20) NOT NULL,
  email       VARCHAR(150),
  standard_id INT NULL,
  status      ENUM('pending','approved','rejected') NOT NULL DEFAULT 'pending',
  applied_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_guest_standard FOREIGN KEY (standard_id) REFERENCES standards(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------- Timetable ----------
CREATE TABLE IF NOT EXISTS timetable (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  standard_id INT NOT NULL,
  section_id  INT NOT NULL,
  subject_id  INT NOT NULL,
  teacher_id  INT NOT NULL,
  day_of_week TINYINT NOT NULL,     -- 1=Sunday ... 7=Saturday
  period_no   TINYINT NOT NULL,
  start_time  TIME NOT NULL,
  end_time    TIME NOT NULL,
  CONSTRAINT fk_tt_standard FOREIGN KEY (standard_id) REFERENCES standards(id),
  CONSTRAINT fk_tt_section FOREIGN KEY (section_id) REFERENCES sections(id),
  CONSTRAINT fk_tt_subject FOREIGN KEY (subject_id) REFERENCES subjects(id),
  CONSTRAINT fk_tt_teacher FOREIGN KEY (teacher_id) REFERENCES teachers(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------- Attendance ----------
CREATE TABLE IF NOT EXISTS attendance (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  student_id  INT NOT NULL,
  standard_id INT NOT NULL,
  section_id  INT NOT NULL,
  date        DATE NOT NULL,
  status      ENUM('present','absent','leave','half_day') NOT NULL,
  marked_by   INT NULL,             -- users.id of teacher/admin
  remarks     VARCHAR(255),
  created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uq_attendance_student_date (student_id, date),
  CONSTRAINT fk_att_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
  CONSTRAINT fk_att_standard FOREIGN KEY (standard_id) REFERENCES standards(id),
  CONSTRAINT fk_att_section FOREIGN KEY (section_id) REFERENCES sections(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------- Homework / Assignment / Class Test / Project ----------
-- These four modules share an identical workflow (teacher posts work with a
-- due date -> students submit -> teacher grades), so they use a matching
-- table shape. Kept as separate tables (not one polymorphic table) because
-- each has slightly different fields (marks for tests, submissions for the
-- rest) and separate tables map 1:1 to the app's four distinct screens.

CREATE TABLE IF NOT EXISTS homework (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  subject_id  INT NOT NULL,
  standard_id INT NOT NULL,
  section_id  INT NOT NULL,
  teacher_id  INT NOT NULL,
  title       VARCHAR(200) NOT NULL,
  description TEXT,
  attachment  VARCHAR(255),
  due_date    DATE NOT NULL,
  created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_hw_subject FOREIGN KEY (subject_id) REFERENCES subjects(id),
  CONSTRAINT fk_hw_standard FOREIGN KEY (standard_id) REFERENCES standards(id),
  CONSTRAINT fk_hw_section FOREIGN KEY (section_id) REFERENCES sections(id),
  CONSTRAINT fk_hw_teacher FOREIGN KEY (teacher_id) REFERENCES teachers(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS homework_submissions (
  id             INT AUTO_INCREMENT PRIMARY KEY,
  homework_id    INT NOT NULL,
  student_id     INT NOT NULL,
  status         ENUM('pending','submitted','late','completed') NOT NULL DEFAULT 'pending',
  submitted_file VARCHAR(255),
  submitted_at   DATETIME NULL,
  remarks        VARCHAR(255),
  points         INT NOT NULL DEFAULT 0,
  UNIQUE KEY uq_hw_submission (homework_id, student_id),
  CONSTRAINT fk_hws_homework FOREIGN KEY (homework_id) REFERENCES homework(id) ON DELETE CASCADE,
  CONSTRAINT fk_hws_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS assignments (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  subject_id  INT NOT NULL,
  standard_id INT NOT NULL,
  section_id  INT NOT NULL,
  teacher_id  INT NOT NULL,
  title       VARCHAR(200) NOT NULL,
  description TEXT,
  attachment  VARCHAR(255),
  total_marks INT NOT NULL DEFAULT 0,
  due_date    DATE NOT NULL,
  created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_as_subject FOREIGN KEY (subject_id) REFERENCES subjects(id),
  CONSTRAINT fk_as_standard FOREIGN KEY (standard_id) REFERENCES standards(id),
  CONSTRAINT fk_as_section FOREIGN KEY (section_id) REFERENCES sections(id),
  CONSTRAINT fk_as_teacher FOREIGN KEY (teacher_id) REFERENCES teachers(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS assignment_submissions (
  id             INT AUTO_INCREMENT PRIMARY KEY,
  assignment_id  INT NOT NULL,
  student_id     INT NOT NULL,
  status         ENUM('pending','submitted','late','completed') NOT NULL DEFAULT 'pending',
  submitted_file VARCHAR(255),
  marks_obtained INT NULL,
  submitted_at   DATETIME NULL,
  remarks        VARCHAR(255),
  UNIQUE KEY uq_as_submission (assignment_id, student_id),
  CONSTRAINT fk_ass_assignment FOREIGN KEY (assignment_id) REFERENCES assignments(id) ON DELETE CASCADE,
  CONSTRAINT fk_ass_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS class_tests (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  subject_id  INT NOT NULL,
  standard_id INT NOT NULL,
  section_id  INT NOT NULL,
  teacher_id  INT NOT NULL,
  title       VARCHAR(200) NOT NULL,
  description TEXT,
  attachment  VARCHAR(255),
  total_marks INT NOT NULL DEFAULT 0,
  test_date   DATE NOT NULL,
  created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_ct_subject FOREIGN KEY (subject_id) REFERENCES subjects(id),
  CONSTRAINT fk_ct_standard FOREIGN KEY (standard_id) REFERENCES standards(id),
  CONSTRAINT fk_ct_section FOREIGN KEY (section_id) REFERENCES sections(id),
  CONSTRAINT fk_ct_teacher FOREIGN KEY (teacher_id) REFERENCES teachers(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS class_test_results (
  id              INT AUTO_INCREMENT PRIMARY KEY,
  class_test_id   INT NOT NULL,
  student_id      INT NOT NULL,
  marks_obtained  INT NULL,
  status          ENUM('pending','completed') NOT NULL DEFAULT 'pending',
  remarks         VARCHAR(255),
  UNIQUE KEY uq_ct_result (class_test_id, student_id),
  CONSTRAINT fk_ctr_test FOREIGN KEY (class_test_id) REFERENCES class_tests(id) ON DELETE CASCADE,
  CONSTRAINT fk_ctr_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS projects (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  subject_id  INT NOT NULL,
  standard_id INT NOT NULL,
  section_id  INT NOT NULL,
  teacher_id  INT NOT NULL,
  title       VARCHAR(200) NOT NULL,
  description TEXT,
  attachment  VARCHAR(255),
  total_marks INT NOT NULL DEFAULT 0,
  due_date    DATE NOT NULL,
  created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_pr_subject FOREIGN KEY (subject_id) REFERENCES subjects(id),
  CONSTRAINT fk_pr_standard FOREIGN KEY (standard_id) REFERENCES standards(id),
  CONSTRAINT fk_pr_section FOREIGN KEY (section_id) REFERENCES sections(id),
  CONSTRAINT fk_pr_teacher FOREIGN KEY (teacher_id) REFERENCES teachers(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS project_submissions (
  id             INT AUTO_INCREMENT PRIMARY KEY,
  project_id     INT NOT NULL,
  student_id     INT NOT NULL,
  status         ENUM('pending','submitted','late','completed') NOT NULL DEFAULT 'pending',
  submitted_file VARCHAR(255),
  marks_obtained INT NULL,
  submitted_at   DATETIME NULL,
  remarks        VARCHAR(255),
  UNIQUE KEY uq_pr_submission (project_id, student_id),
  CONSTRAINT fk_prs_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
  CONSTRAINT fk_prs_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------- Exams ----------
CREATE TABLE IF NOT EXISTS exams (
  id                INT AUTO_INCREMENT PRIMARY KEY,
  name              VARCHAR(150) NOT NULL,   -- "Mid Term 2026"
  standard_id       INT NOT NULL,
  academic_year_id  INT NULL,
  start_date        DATE NOT NULL,
  end_date          DATE NOT NULL,
  status            ENUM('scheduled','ongoing','completed') NOT NULL DEFAULT 'scheduled',
  CONSTRAINT fk_exam_standard FOREIGN KEY (standard_id) REFERENCES standards(id),
  CONSTRAINT fk_exam_year FOREIGN KEY (academic_year_id) REFERENCES academic_years(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS exam_subjects (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  exam_id     INT NOT NULL,
  subject_id  INT NOT NULL,
  exam_date   DATE NOT NULL,
  start_time  TIME,
  end_time    TIME,
  total_marks INT NOT NULL DEFAULT 100,
  CONSTRAINT fk_es_exam FOREIGN KEY (exam_id) REFERENCES exams(id) ON DELETE CASCADE,
  CONSTRAINT fk_es_subject FOREIGN KEY (subject_id) REFERENCES subjects(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS exam_results (
  id              INT AUTO_INCREMENT PRIMARY KEY,
  exam_subject_id INT NOT NULL,
  student_id      INT NOT NULL,
  marks_obtained  DECIMAL(6,2) NULL,
  grade           VARCHAR(5),
  remarks         VARCHAR(255),
  UNIQUE KEY uq_exam_result (exam_subject_id, student_id),
  CONSTRAINT fk_er_examsubject FOREIGN KEY (exam_subject_id) REFERENCES exam_subjects(id) ON DELETE CASCADE,
  CONSTRAINT fk_er_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------- Fees ----------
CREATE TABLE IF NOT EXISTS fee_structures (
  id                INT AUTO_INCREMENT PRIMARY KEY,
  standard_id       INT NOT NULL,
  academic_year_id  INT NULL,
  fee_type          VARCHAR(100) NOT NULL,   -- "Tuition Fee", "Transport Fee"
  amount             DECIMAL(10,2) NOT NULL,
  due_date          DATE,
  CONSTRAINT fk_fs_standard FOREIGN KEY (standard_id) REFERENCES standards(id),
  CONSTRAINT fk_fs_year FOREIGN KEY (academic_year_id) REFERENCES academic_years(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS fee_transactions (
  id                INT AUTO_INCREMENT PRIMARY KEY,
  student_id        INT NOT NULL,
  fee_structure_id  INT NOT NULL,
  amount_paid       DECIMAL(10,2) NOT NULL,
  payment_mode      VARCHAR(40),             -- online, cash, cheque, UPI
  transaction_ref   VARCHAR(100),
  payment_date      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  status             ENUM('pending','success','failed') NOT NULL DEFAULT 'pending',
  CONSTRAINT fk_ft_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
  CONSTRAINT fk_ft_feestructure FOREIGN KEY (fee_structure_id) REFERENCES fee_structures(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------- Notices / Leave / Complaints ----------
CREATE TABLE IF NOT EXISTS notices (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  title       VARCHAR(200) NOT NULL,
  description TEXT,
  target_role VARCHAR(40) DEFAULT 'all',     -- all / student / teacher / parent
  standard_id INT NULL,
  section_id  INT NULL,
  attachment  VARCHAR(255),
  created_by  INT NOT NULL,                  -- users.id
  created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_notice_standard FOREIGN KEY (standard_id) REFERENCES standards(id),
  CONSTRAINT fk_notice_section FOREIGN KEY (section_id) REFERENCES sections(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS leave_types (
  id   INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(60) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS leave_requests (
  id            INT AUTO_INCREMENT PRIMARY KEY,
  user_id       INT NOT NULL,
  leave_type_id INT NOT NULL,
  from_date     DATE NOT NULL,
  to_date       DATE NOT NULL,
  reason        VARCHAR(255),
  status        ENUM('pending','approved','rejected') NOT NULL DEFAULT 'pending',
  approved_by   INT NULL,
  applied_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_lr_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT fk_lr_type FOREIGN KEY (leave_type_id) REFERENCES leave_types(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS complaint_types (
  id   INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(60) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS complaints (
  id                 INT AUTO_INCREMENT PRIMARY KEY,
  raised_by          INT NOT NULL,
  complaint_type_id  INT NOT NULL,
  subject            VARCHAR(200) NOT NULL,
  description        TEXT,
  status             ENUM('open','in_progress','resolved') NOT NULL DEFAULT 'open',
  resolved_by        INT NULL,
  resolved_remarks   VARCHAR(255),
  created_at         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  resolved_at        DATETIME NULL,
  CONSTRAINT fk_cmp_user FOREIGN KEY (raised_by) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT fk_cmp_type FOREIGN KEY (complaint_type_id) REFERENCES complaint_types(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------- Documents ----------
CREATE TABLE IF NOT EXISTS documents_master (
  id            INT AUTO_INCREMENT PRIMARY KEY,
  doc_name      VARCHAR(150) NOT NULL,
  doc_code      VARCHAR(40) NOT NULL UNIQUE,
  is_mandatory  TINYINT(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS documents_upload (
  id                   INT AUTO_INCREMENT PRIMARY KEY,
  user_id              INT NOT NULL,
  document_master_id   INT NOT NULL,
  file_path            VARCHAR(255) NOT NULL,
  status               ENUM('pending','verified','rejected') NOT NULL DEFAULT 'pending',
  uploaded_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_du_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT fk_du_doc FOREIGN KEY (document_master_id) REFERENCES documents_master(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------- Chat ----------
CREATE TABLE IF NOT EXISTS chat_messages (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  sender_id   INT NOT NULL,
  receiver_id INT NOT NULL,
  message     TEXT,
  attachment  VARCHAR(255),
  is_read     TINYINT(1) NOT NULL DEFAULT 0,
  sent_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_chat_sender FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT fk_chat_receiver FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------- Points / Leaderboard ----------
CREATE TABLE IF NOT EXISTS points_history (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  student_id  INT NOT NULL,
  points      INT NOT NULL,
  reason      VARCHAR(200),
  awarded_by  INT NULL,
  awarded_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_ph_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------- Notifications ----------
CREATE TABLE IF NOT EXISTS notifications (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  user_id    INT NOT NULL,
  title      VARCHAR(200) NOT NULL,
  message    VARCHAR(255),
  is_read    TINYINT(1) NOT NULL DEFAULT 0,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_notif_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------- Seed default lookup data ----------
INSERT IGNORE INTO roles (id, name) VALUES
  (1, 'admin'), (2, 'teacher'), (3, 'student'), (4, 'parent'), (5, 'staff'), (6, 'guest');

INSERT IGNORE INTO leave_types (id, name) VALUES
  (1, 'Sick Leave'), (2, 'Casual Leave'), (3, 'Emergency Leave'), (4, 'Other');

INSERT IGNORE INTO complaint_types (id, name) VALUES
  (1, 'Academic'), (2, 'Infrastructure'), (3, 'Transport'), (4, 'Behavioral'), (5, 'Other');
