package com.lms.sch.models

data class ChatMessage(
    val message: String,
    val isSentByUser: Boolean,
    val timestamp: String
)