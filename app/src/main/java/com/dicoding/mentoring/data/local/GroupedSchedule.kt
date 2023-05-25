package com.dicoding.mentoring.data.local

data class GroupedSchedule(
    val fromDate: String,
    val schedules: List<Schedule>,
)