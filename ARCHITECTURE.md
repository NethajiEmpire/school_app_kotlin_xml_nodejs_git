# 🏗️ Multi-Tenant School LMS - System Architecture Document

Welcome to the **School LMS Architecture Document**! This document provides a complete technical blueprint of the system, designed to help freshers and senior developers understand the high-level architecture, database isolation model, network request lifecycle, and component interactions.

---

## 📌 1. High-Level Architecture Overview

The system is a **Multi-Tenant School Learning Management System (LMS)** consisting of two primary components:
1. **Node.js + Express + TypeScript Backend API**: Handles business logic, authentication, multi-tenant database routing, file uploads, and platform management.
2. **Native Android Mobile App (Kotlin + XML)**: The client application used by School Admins, Teachers, Students, and Parents to interact with the platform.

```mermaid
graph TD
    subgraph Mobile Client Layer
        AndroidApp["📱 Android Mobile App (Kotlin + XML)\nRetrofit / ApiClient"]
    end

    subgraph Backend API Gateway Layer
        ExpressServer["⚡ Node.js Express Server (TypeScript)\n(Port 5000)"]
        TenantMW["🛡️ Tenant Middleware\n(Extracts X-School-Code)"]
        AuthMW["🔑 JWT Auth Middleware"]
    end

    subgraph Database Layer
        MasterDB[("🗄️ Master Database\n(school_lms_master)\nStores Schools & SuperAdmins")]
        
        subgraph Tenant DB Pools
            TenantDB1[("🏫 Tenant DB: school_stmarys\n(Users, Attendance, Fees, etc.)")]
            TenantDB2[("🏫 Tenant DB: school_dps\n(Users, Attendance, Fees, etc.)")]
            TenantDBN[("🏫 Tenant DB: school_[code]\n(...)")]
        end
    end

    AndroidApp -->|"HTTP/REST + X-School-Code Header"| ExpressServer
    ExpressServer --> TenantMW
    TenantMW -->|"Look up school code"| MasterDB
    TenantMW --> AuthMW
    AuthMW -->|"Execute queries on tenant pool"| TenantDB1
    AuthMW --> TenantDB2
    AuthMW --> TenantDBN
```

---

## 🗄️ 2. Multi-Tenancy Architecture (Database-per-Tenant)

Our application implements a **Database-per-Tenant** isolation model. Every school onboarded to the system gets its own physically separate MySQL database.

### Why Database-per-Tenant?
* **Data Security & Privacy**: Complete isolation ensures one school's data can never bleed into another school's queries.
* **Scalability**: Individual school databases can be backed up, migrated, or hosted on separate database servers as the platform grows.
* **Custom Customization**: Allows schema modifications per school if needed in the future.

### Master Database vs. Tenant Databases

```mermaid
classDiagram
    class MasterDB {
        +schools (id, school_code, db_name, status)
        +super_admins (id, email, password_hash)
        +platform_logs
    }

    class TenantDB {
        +users (id, role, reg_no, password_hash)
        +academic_years, standards, sections, subjects
        +attendance, homework, assignments, classtests
        +timetable, exams, exam_marks, fee_structures
        +notices, leave_requests, complaints, chat_messages
    }

    MasterDB --> TenantDB : "Resolves school_code -> db_name"
```

1. **Master Database (`school_lms_master`)**:
   - Platform-level database.
   - Contains `schools` registry table (`school_code` -> `db_name`).
   - Contains `super_admins` table for platform owners.

2. **Tenant Database (`school_[code]`, e.g., `school_stmarys`)**:
   - Contains all school-specific data: Students, Staff, Attendance, Homework, Grades, Fees, Notices, Timetables, etc.

---

## 🔄 3. School Tenant Resolution & Connection Pooling

When a request arrives at the backend:

```mermaid
sequenceDiagram
    autonumber
    participant App as 📱 Android App
    participant MW as 🛡️ Tenant Middleware
    participant TM as ⚙️ Tenant Manager
    participant Master as 🗄️ Master DB
    participant Pool as 🏊 Tenant Connection Pool
    participant Controller as 🎮 Route Controller

    App->>MW: GET /api/STMARY001/attendance (Header: X-School-Code: STMARY001)
    MW->>TM: resolveTenant("STMARY001")
    alt Connection Pool Cached
        TM-->>MW: Return cached Pool for "school_stmarys"
    else Pool Not Cached
        TM->>Master: SELECT * FROM schools WHERE school_code = 'STMARY001'
        Master-->>TM: Returns db_name = "school_stmarys"
        TM->>Pool: mysql.createPool({ database: 'school_stmarys' })
        TM-->>MW: Return new Pool
    end
    MW->>Controller: Attach req.school & req.db -> Execute query
    Controller-->>App: JSON Response { status: true, data: [...] }
```

### Dynamic Pool Caching (`tenantManager.ts`)
* To avoid creating new database connections on every HTTP request, connection pools are cached in memory using a JavaScript `Map<string, Pool>()`.
* Key: Database Name (e.g. `school_stmarys`).
* Value: MySQL Connection Pool (Limit: 10 connections).

---

## 📱 4. Android Mobile App Architecture (Kotlin + XML)

The Android mobile application follows a modular activity-driven pattern with centralized networking and session helpers.

```
School-MobileApp/
├── app/src/main/java/com/lms/sch/
│   ├── activity/             # Android UI Activities (Login, Splash, Academic, etc.)
│   ├── fragment/             # Reusable UI Fragments
│   ├── adapter/              # RecyclerView Adapters for Lists
│   ├── network/              # Retrofit & OkHttp API Layer
│   │   ├── ApiClient.kt      # Centralized HTTP request engine
│   │   ├── ApiConnection.kt   # Endpoint URLs and payload mapping
│   │   ├── AddCookiesInterceptor.kt
│   │   └── ReceivedCookiesInterceptor.kt
│   ├── session/              # User Preferences & Token Storage
│   │   ├── SharedHelper.kt   # SharedPreferences wrapper
│   │   └── Constants.kt      # App Configuration & Server Base URL
│   └── models/               # Data Models / Data Transfer Objects (DTOs)
```

### Key Highlights:
* **Session Persistence**: User authentication tokens (`jwt_token`) and selected `school_code` are saved locally using Android `SharedPreferences`.
* **Automatic Header Injection**: Every outgoing network request via `ApiClient` automatically appends:
  * `X-School-Code`: Saved school code.
  * `Authorization`: `Bearer <jwt_token>`.

---

## 🔐 5. Security & Authentication Flow

```mermaid
sequenceDiagram
    autonumber
    actor User as 👤 Teacher / Student / Admin
    participant Mobile as 📱 Android App
    participant API as ⚡ Backend Server
    participant DB as 🗄️ Tenant Database

    User->>Mobile: Enters School Code, Reg No / Email, Password
    Mobile->>API: POST /api/:schoolCode/auth/login { username, password }
    API->>DB: SELECT * FROM users WHERE (email=? OR reg_no=?)
    DB-->>API: User Record with bcrypt password_hash
    API->>API: bcrypt.compare(password, password_hash)
    alt Valid Credentials
        API->>API: jwt.sign({ userId, role, schoolCode }, SECRET)
        API-->>Mobile: { status: true, token, user: { id, name, role } }
        Mobile->>Mobile: Save token & schoolCode to SharedHelper
    else Invalid Credentials
        API-->>Mobile: 401 Unauthorized { status: false, message: "Invalid credentials" }
    end
```

---

## 📊 6. Database Schema Quick Reference

### Master DB Tables
* `schools`: `id`, `school_code`, `school_name`, `db_name`, `status` (`active`/`inactive`), `created_at`.
* `super_admins`: `id`, `name`, `email`, `password_hash`, `created_at`.

### Tenant DB Tables (Per School)
* **Users & Roles**: `users` (`role`: `superadmin`, `admin`, `teacher`, `student`, `parent`).
* **Academics**: `academic_years`, `standards` (Classes), `sections`, `subjects`, `teacher_subject_assignments`.
* **Daily Operations**: `attendance`, `homework`, `assignments`, `classtests`, `projects`.
* **Exams & Marks**: `exams`, `exam_schedules`, `exam_marks`.
* **Finance**: `fee_components`, `fee_structures`, `student_fee_payments`.
* **Communication**: `notices`, `leave_requests`, `complaints`, `chat_messages`.

---

## 🎯 Summary for Freshers
1. **Always send `X-School-Code` header** when calling backend APIs.
2. **Never query `school_lms_master` directly** for student/teacher data. Use `req.db` attached by `tenantMiddleware`.
3. **Backend uses pure Promises with `mysql2/promise`**, so use `async/await` and parameterized queries (`?`) to prevent SQL injection.
