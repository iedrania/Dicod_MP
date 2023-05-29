package com.dicoding.mentoring.data.local

import com.google.gson.annotations.SerializedName

data class ScheduleResponse(
    @field:SerializedName("data") val listSchedule: List<Schedule>
)

data class Schedule(
    @field:SerializedName("name") val name: String?,
    @field:SerializedName("start_time") val fromDate: String?,
    @field:SerializedName("end_time") val toDate: String?,
    @field:SerializedName("meeting_id") val meetingId: String?,
)