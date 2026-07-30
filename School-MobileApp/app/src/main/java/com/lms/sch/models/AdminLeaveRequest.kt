package com.lms.sch.models

data class AdminLeaveRequest(
    val staffNames: String,
    val staffRole: String,
    val typeLeave: String,
    val reason: String,
)