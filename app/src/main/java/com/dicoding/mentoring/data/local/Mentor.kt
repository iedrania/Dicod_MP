package com.dicoding.mentoring.data.local

data class Mentor(
    val id: String,
    val name: String,
    val photoUrl: Int,
    val avgRating: Float,
    val bio: String,
    val listInterest: List<String>
)