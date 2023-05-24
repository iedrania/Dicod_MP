package com.dicoding.mentoring.data.local

data class MentorsResponse(
    val mentors: List<Mentors>
)

data class Mentors(
    val averageRating: Float?,
    val User: Mentor,
)