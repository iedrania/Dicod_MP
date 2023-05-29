package com.dicoding.mentoring.data.local

import com.google.gson.annotations.SerializedName

data class MentorsResponse(
    @field:SerializedName("mentors") val mentors: List<Mentors>
)

data class Mentors(
    @field:SerializedName("average_rating") val averageRating: Float?,
    @field:SerializedName("User") val user: Mentor,
)

data class Mentor(
    @field:SerializedName("id") val id: Long?,
    @field:SerializedName("name") val name: String?,
    @field:SerializedName("role_id") val roleId: Int?,
    @field:SerializedName("gender_id") val genderId: Int?,
    @field:SerializedName("bio") val bio: String?,
    @field:SerializedName("profile_picture_url") val profilePictureUrl: String?,
    @field:SerializedName("is_path_android") val isPathAndroid: Boolean?,
    @field:SerializedName("is_path_web") val isPathWeb: Boolean?,
    @field:SerializedName("is_path_ios") val isPathIos: Boolean?,
    @field:SerializedName("is_path_ml") val isPathMl: Boolean?,
    @field:SerializedName("is_path_flutter") val isPathFlutter: Boolean?,
    @field:SerializedName("is_path_fe") val isPathFe: Boolean?,
    @field:SerializedName("is_path_be") val isPathBe: Boolean?,
    @field:SerializedName("is_path_react") val isPathReact: Boolean?,
    @field:SerializedName("is_path_devops") val isPathDevops: Boolean?,
    @field:SerializedName("is_path_gcp") val isPathGcp: Boolean?,
    @field:SerializedName("is_monday_available") val isMondayAvailable: Boolean?,
    @field:SerializedName("is_tuesday_available") val isTuesdayAvailable: Boolean?,
    @field:SerializedName("is_wednesday_available") val isWednesdayAvailable: Boolean?,
    @field:SerializedName("is_thursday_available") val isThursdayAvailable: Boolean?,
    @field:SerializedName("is_friday_available") val isFridayAvailable: Boolean?,
    @field:SerializedName("is_saturday_available") val isSaturdayAvailable: Boolean?,
    @field:SerializedName("is_sunday_available") val isSundayAvailable: Boolean?,
)