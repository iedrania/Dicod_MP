package com.dicoding.mentoring.data.local

import com.google.gson.annotations.SerializedName

data class Schedule(
    @field:SerializedName("name") val name: String?,
    @field:SerializedName("start_time") val fromDate: String?,
    @field:SerializedName("end_time") val toDate: String?,
)