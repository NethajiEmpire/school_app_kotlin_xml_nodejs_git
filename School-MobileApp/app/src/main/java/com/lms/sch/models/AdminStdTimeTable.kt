package com.lms.sch.models

data class AdminStdTimeTable(
    var name: String,
    var subject: String,
    val startTime: String,
    val type: String
)