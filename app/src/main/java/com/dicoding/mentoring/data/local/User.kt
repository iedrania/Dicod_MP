package com.dicoding.mentoring.data.local



data class GetUserProfileResponse(
    val error: Boolean,
    val message: String,
    val user : UserResponse
)

data class PostUserProfileResponse(
    val error: Boolean,
    val message: String,
    val updatedUser : UserResponse
)

data class UserResponse (
    val id: String,
    val name: String,
    var gender_id: Int,
    val email: String,
    val address: String?,
    val phone: String,
    val bio: String,
    val profile_picture_url: String,
    val is_mentor: Boolean,
    val is_mentee: Boolean,
    var is_path_android: Boolean,
    var is_path_web: Boolean,
    var is_path_ios: Boolean,
    var is_path_ml: Boolean,
    var is_path_flutter: Boolean,
    var is_path_fe: Boolean,
    var is_path_be: Boolean,
    var is_path_react: Boolean,
    var is_path_devops: Boolean,
    var is_path_gcp: Boolean,
    var is_monday_available: Boolean?,
    var is_tuesday_available: Boolean?,
    var is_wednesday_available: Boolean?,
    var is_thursday_available: Boolean?,
    var is_friday_available: Boolean?,
    var is_saturday_available: Boolean?,
    var is_sunday_available: Boolean?,
    val createdAt: String?,
    val updatedAt: String?
)

