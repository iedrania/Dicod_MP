package com.dicoding.mentoring.data.local

import com.google.firebase.Timestamp

data class Chat(
    val messageText: String? = null,
    val imageUrl: String? = null,
    val sentAt: Timestamp? = null,
    val sentBy: String? = null,
    var specialChat: Boolean? = null,
    val mentoringId: Long? = null,
)
