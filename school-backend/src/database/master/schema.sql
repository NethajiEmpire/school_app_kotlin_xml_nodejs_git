-- =====================================================================
-- MASTER DATABASE SCHEMA
-- This database ONLY stores which schools exist and how to reach their
-- individual databases. It never stores students/teachers/attendance.
-- =====================================================================

CREATE TABLE IF NOT EXISTS super_admins (
  id            INT AUTO_INCREMENT PRIMARY KEY,
  name          VARCHAR(120) NOT NULL,
  email         VARCHAR(150) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  created_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS schools (
  id                  INT AUTO_INCREMENT PRIMARY KEY,
  school_code         VARCHAR(40)  NOT NULL UNIQUE,   -- used by the app as X-School-Code, e.g. "STMARY001"
  school_name         VARCHAR(180) NOT NULL,
  db_name             VARCHAR(80)  NOT NULL UNIQUE,    -- e.g. "school_stmary001"
  address             VARCHAR(255),
  city                VARCHAR(100),
  state               VARCHAR(100),
  pincode             VARCHAR(12),
  contact_email       VARCHAR(150),
  contact_phone       VARCHAR(20),
  logo_url            VARCHAR(255),
  status              ENUM('active','inactive','suspended') NOT NULL DEFAULT 'active',
  subscription_expiry DATE NULL,
  created_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
