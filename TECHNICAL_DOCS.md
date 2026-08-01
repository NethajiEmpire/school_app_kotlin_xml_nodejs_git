# 🛠️ Multi-Tenant School LMS - Complete Technical Documentation

Welcome to the **Technical Developer Guide**! This document provides step-by-step setup instructions, environment configuration, code structure explanations, API specifications, and guidelines for adding new features.

---

## 📋 1. System Requirements & Prerequisites

Before setting up the project, make sure you have installed:
* **Node.js**: `v18.x` or `v20.x` LTS ([Download Node.js](https://nodejs.org/))
* **MySQL Server**: `v8.0` or higher ([Download MySQL](https://dev.mysql.com/downloads/installer/))
* **Java Development Kit (JDK)**: `JDK 17`
* **Android Studio**: Android Studio Koala / Ladybug or newer with Android SDK 34+
* **Git**: Command line git tool

---

## 🚀 2. Backend Setup Guide (Step-by-Step)

### Step 1: Navigate to Backend Directory
```bash
cd school-backend
```

### Step 2: Install Node.js Dependencies
```bash
npm install
```

### Step 3: Configure Environment Variables
Create a `.env` file inside the `school-backend/` directory by copying `.env.example`:

```bash
cp .env.example .env
```

Here is a typical `.env` configuration for local development:
```env
PORT=5000
NODE_ENV=development

# MySQL Connection Details
DB_HOST=127.0.0.1
DB_PORT=3306
DB_USER=root
DB_PASSWORD=your_mysql_password
MASTER_DB_NAME=school_lms_master

# JWT Authentication Secret
JWT_SECRET=super_secret_jwt_key_change_in_production
JWT_EXPIRES_IN=7d

# File Upload Directory
UPLOAD_DIR=uploads
MAX_FILE_SIZE_MB=10
```

### Step 4: Provision the Master Database
Run the master database migration script. This creates `school_lms_master` and its required tables (`schools`, `super_admins`):

```bash
npm run provision:master
```

### Step 5: Provision a Test School Tenant Database
Run the tenant provisioning script to create a sample school database (e.g. `STMARY001`):

```bash
npm run provision:school -- --code STMARY001 --name "St Marys High School"
```
* This creates a database named `school_stmarys` and initializes all tables (`users`, `academic_years`, `attendance`, `homework`, `fees`, etc.).

### Step 6: Start the Backend Server
```bash
# For Development (Hot Reload with nodemon)
npm run dev

# For Production Build & Execution
npm run build
npm start
```

### Step 7: Verify Backend Health
Open your browser or Postman and visit:
`http://localhost:5000/health`

Response:
```json
{
  "status": true,
  "message": "School LMS backend is running",
  "database": "connected",
  "dbName": "school_lms_master"
}
```

---

## 📱 3. Android Mobile App Setup Guide

### Step 1: Open Project in Android Studio
1. Launch Android Studio.
2. Select **Open** and choose the `School-MobileApp` directory:
   `d:\GitHub\school_app_kotlin_xml_nodejs_git\School-MobileApp`

### Step 2: Configure Server URL
Open `com.lms.sch.session.Constants.kt` (located at `School-MobileApp/app/src/main/java/com/lms/sch/session/Constants.kt`).

Update the `BASE_URL` to point to your backend server:
```kotlin
package com.lms.sch.session

object Constants {
    // For Android Emulator targeting local machine:
    const val BASE_URL = "http://10.0.2.2:5000/api/"
    
    // For physical device on same Wi-Fi network:
    // const val BASE_URL = "http://192.168.1.100:5000/api/"
}
```

### Step 3: Build & Run
1. Click **Sync Project with Gradle Files** in Android Studio.
2. Select your Android Emulator or connected device.
3. Click **Run 'app'** (`Shift + F10`).

---

## 📁 4. Backend Source Code Directory Layout

```
school-backend/src/
├── app.ts                 # Express Application setup, middleware & route registration
├── server.ts              # Server startup entry point (listens on PORT)
├── config/                # Environment variables parser & Zod validation schema
│   └── env.ts
├── core/                  # Core multi-tenancy & DB helper logic
│   ├── masterDb.ts        # Master DB connection pool getter
│   ├── tenantManager.ts   # Tenant resolution & MySQL connection pool caching
│   ├── dbHelpers.ts       # Query helpers (execute, selectOne, insert)
│   └── lookupFactory.ts   # Reusable lookup table query factories
├── middlewares/           # Custom Express Middlewares
│   ├── tenant.middleware.ts # Extracts X-School-Code & attaches req.db
│   ├── auth.middleware.ts   # Verifies JWT token & user role
│   ├── error.middleware.ts  # Global error handler & 404 handler
│   └── upload.middleware.ts # Multer file upload configuration
├── modules/               # Feature Modules (Domain-Driven Structure)
│   ├── academic/          # Academic Years, Standards, Sections, Subjects
│   ├── assignment/        # Assignments & Submissions
│   ├── attendance/        # Student & Staff Attendance
│   ├── auth/              # User Authentication (Login, Refresh, Profile)
│   ├── chat/              # In-app Messaging & Chat
│   ├── classtest/         # Class Tests & Marks
│   ├── complaint/         # Complaints & Grievance redressal
│   ├── dashboard/         # Role-based Dashboard stats
│   ├── documents/         # Document uploads & Downloads
│   ├── exam/              # Exams, Schedules & Marks Entry
│   ├── fees/              # Fee structures & Payment records
│   ├── homework/          # Daily Homework assignments
│   ├── leave/             # Staff & Student Leave requests
│   ├── notice/            # Circulars & Notice board
│   ├── points/            # Student reward points
│   ├── project/           # Student Projects
│   ├── superadmin/        # Platform Super-Admin APIs
│   ├── timetable/         # Class & Teacher Timetables
│   └── users/             # User Management (CRUD for Staff/Students)
├── types/                 # Custom TypeScript declaration files
└── utils/                 # Utility functions (AppError, asyncHandler, Password hash)
```

---

## 🔌 5. API Endpoint Specifications

All school-scoped APIs require the **`X-School-Code`** header.

### 🔑 Authentication Module (`/api/:schoolCode/auth`)
| Method | Endpoint | Description | Auth Required |
| :--- | :--- | :--- | :--- |
| `POST` | `/login` | Authenticate user & return JWT token | No |
| `GET` | `/me` | Get current logged-in user profile | Yes |
| `POST` | `/change-password` | Update current user's password | Yes |

### 📚 Academic Module (`/api/:schoolCode/academic`)
| Method | Endpoint | Description | Auth Required |
| :--- | :--- | :--- | :--- |
| `GET` | `/academic-years` | List all academic years | Yes |
| `POST` | `/academic-years` | Create a new academic year | Yes (Admin) |
| `GET` | `/standards` | List classes / standards | Yes |
| `GET` | `/sections` | List sections for a standard | Yes |
| `GET` | `/subjects` | List subjects | Yes |

### 📝 Attendance Module (`/api/:schoolCode/attendance`)
| Method | Endpoint | Description | Auth Required |
| :--- | :--- | :--- | :--- |
| `POST` | `/mark` | Mark daily student attendance | Yes (Teacher) |
| `GET` | `/summary` | Get attendance percentage & monthly report | Yes |

### 💳 Fees Module (`/api/:schoolCode/fees`)
| Method | Endpoint | Description | Auth Required |
| :--- | :--- | :--- | :--- |
| `GET` | `/my-fees` | View fee dues & payment receipts | Yes (Student/Parent) |
| `POST` | `/record-payment` | Record a fee payment | Yes (Admin) |

---

## 💡 6. Guide for Freshers: How to Add a New API Feature

When adding a new feature (e.g. `Library Management`):

1. **Create Module Folder**: Create `src/modules/library/`.
2. **Define Routes (`library.routes.ts`)**:
   ```typescript
   import { Router } from 'express';
   import { asyncHandler } from '../../utils/asyncHandler';
   const router = Router();
   
   router.get('/books', asyncHandler(async (req, res) => {
     const [books] = await req.db.query('SELECT * FROM library_books');
     res.json({ status: true, data: books });
   }));
   
   export default router;
   ```
3. **Register Route in `src/app.ts`**:
   ```typescript
   import libraryRoutes from './modules/library/library.routes';
   schoolRouter.use('/library', libraryRoutes);
   ```
4. **Update Tenant Schema SQL**: Add table definition to `src/database/tenant/schema.sql`.
