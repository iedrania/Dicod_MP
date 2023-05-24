package com.dicoding.mentoring.data.local


data class InterestResponse(
    val error: Boolean,
    val message: String,
    val user: Interest
)

data class Interest (
    val is_path_android: Boolean,
    val is_path_web: Boolean,
    val is_path_ios: Boolean,
    val is_path_ml: Boolean,
    val is_path_flutter: Boolean,
    val is_path_fe: Boolean,
    val is_path_be: Boolean,
    val is_path_react: Boolean,
    val is_path_devops: Boolean,
    val is_path_gcp: Boolean
)

data class InterestItem (
    val name : String,
    var isChecked : Boolean = false,
)
