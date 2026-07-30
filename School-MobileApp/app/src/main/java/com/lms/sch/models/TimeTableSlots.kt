package com.lms.sch.models

data class TimeTableSlots(
    val time: String,
    val periodNumber: String,
    val subject: String,
    val teacher: String,
    val bg_card: String,
    val bg_lay: String,
    val isBreak: Boolean = false,
    val isWeekDay: Boolean = false

)