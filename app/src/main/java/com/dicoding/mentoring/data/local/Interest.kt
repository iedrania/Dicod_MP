package com.dicoding.mentoring.data.local


data class InterestResponse(
    val error: Boolean,
    val message: String,
    val user: Interest
)

data class Interest (
    var is_path_android: Boolean?,
    val is_path_web: Boolean?,
    var is_path_ios: Boolean?,
    var is_path_ml: Boolean?,
    var is_path_flutter: Boolean?,
    var is_path_fe: Boolean?,
    var is_path_be: Boolean?,
    var is_path_react: Boolean?,
    var is_path_devops: Boolean?,
    var is_path_gcp: Boolean?
)

data class InterestItem (
    val id : Int,
    val name : String,
    var isChecked : Boolean,
)
