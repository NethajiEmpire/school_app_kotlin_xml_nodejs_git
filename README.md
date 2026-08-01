# 🏫 Multi-Tenant School LMS (Kotlin + XML + Node.js + MySQL)

Welcome to the **Multi-Tenant School Learning Management System (LMS)** repository! This repository contains a production-grade multi-tenant mobile and backend platform built with **Node.js, Express, TypeScript, MySQL, and Native Android (Kotlin + XML)**.

---

## 📚 Complete Project Documentation

To make this codebase easy to understand for freshers, developers, and system administrators, complete documentation is organized into three dedicated guides:

### 1. 🏗️ [System Architecture Document](file:///d:/GitHub/school_app_kotlin_xml_nodejs_git/ARCHITECTURE.md)
* High-Level System Architecture & Flow Charts
* Multi-Tenancy Design (Database-per-Tenant model)
* Dynamic Database Connection Pool Caching
* Security & Request Lifecycle Sequence Diagrams
* Database Schema Blueprint (Master DB vs Tenant DBs)

### 2. 🛠️ [Technical Developer Guide](file:///d:/GitHub/school_app_kotlin_xml_nodejs_git/TECHNICAL_DOCS.md)
* Step-by-Step Environment Setup & Prerequisites
* Backend Installation & Database Migration commands (`npm run provision:master`, `npm run provision:school`)
* Android Mobile App Configuration & Android Studio Setup
* Source Code Directory Tree Breakdown
* Complete API Endpoint Specifications & Middlewares
* Developer Guide: How to add new features step-by-step

### 3. 📖 [User Manual & Operational Guide](file:///d:/GitHub/school_app_kotlin_xml_nodejs_git/USER_MANUAL.md)
* System User Roles & Access Control Hierarchy
* Super Admin Platform Onboarding Guide
* School Administrator Setup Workflow (Academic Years, Classes, Teachers, Students)
* Teacher Daily Guide (Attendance, Homework, Exams, Notices)
* Student & Parent Mobile App User Guide
* Troubleshooting Matrix & Frequently Asked Questions

---

## 🚀 Quick Start Commands

### Backend API (`/school-backend`)
```bash
cd school-backend
npm install
npm run provision:master
npm run provision:school -- --code STMARY001 --name "St Marys High School"
npm run dev
```

### Android App (`/School-MobileApp`)
1. Open `School-MobileApp` in Android Studio.
2. Update base URL in `app/src/main/java/com/lms/sch/session/Constants.kt`.
3. Build & Run on Emulator or Device.
