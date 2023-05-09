package com.dicoding.mentoring.data.local

data class User (
    val id : Int,
    val name : String,
    val photo : Int,
)
data class UserResponse(
    val loginResult: Login
)

data class Login(
    val token: String
) // TODO sesuaikan dengan API