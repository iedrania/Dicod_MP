package com.dicoding.mentoring.data.local

import com.google.gson.annotations.SerializedName

data class MentoringResponse(
    val message: String,
    val data: MentoringData,
)

data class MentoringData(
    val id: Int,
    @SerializedName("mentor_id") val mentorId: String,
    @SerializedName("start_time") val startTime: String,
    @SerializedName("end_time") val endTime: String,
    @SerializedName("event_id") val eventId: String,
    @SerializedName("meeting_id") val meetingId: String,
    @SerializedName("is_finished") val isFinished: Boolean,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)
