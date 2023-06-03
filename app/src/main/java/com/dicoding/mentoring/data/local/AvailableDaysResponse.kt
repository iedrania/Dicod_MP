package com.dicoding.mentoring.data.local

import com.google.gson.annotations.SerializedName

data class AvailableDaysResponse(
    val message: String,
    val user: ListDay

)

data class ListDay(
    @SerializedName("is_monday_available") val isMondayAvailable: Boolean,
    @SerializedName("is_tuesday_available") val isTuesdayAvailable: Boolean,
    @SerializedName("is_wednesday_available") val isWednesdayAvailable: Boolean,
    @SerializedName("is_thursday_available") val isThursdayAvailable: Boolean,
    @SerializedName("is_friday_available") val isFridayAvailable: Boolean,
    @SerializedName("is_saturday_available") val isSaturdayAvailable: Boolean,
    @SerializedName("is_sunday_available") val isSundayAvailable: Boolean
)