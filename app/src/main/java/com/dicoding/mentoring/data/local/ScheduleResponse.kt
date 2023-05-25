package com.dicoding.mentoring.data.local

import com.google.gson.annotations.SerializedName

data class ScheduleResponse(
    @field:SerializedName("data") val listSchedule: List<Schedule>
)