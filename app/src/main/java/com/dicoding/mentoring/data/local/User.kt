package com.dicoding.mentoring.data.local

data class User(
    val loginResult: Login
)

data class Login(
    val token: String
) // TODO sesuaikan dengan API