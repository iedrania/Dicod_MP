package com.dicoding.mentoring.data.local

import com.google.gson.annotations.SerializedName

data class HomeMentorResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("dashboard")
    val dashboard: Dashboard
)

data class Dashboard(
    @SerializedName("average_rating")
    val averageRating: Double,
    @SerializedName("rating_count")
    val ratingCount: Int,
    @SerializedName("feedback_summary")
    val feedbackSummary: String?,
    @SerializedName("sentiment")
    val sentiment: Sentiment,
    @SerializedName("reviews")
    val reviews: List<Review>
)

data class Sentiment(
    @SerializedName("negative")
    val negative: Float,
    @SerializedName("neutral")
    val neutral: Float,
    @SerializedName("positive")
    val positive: Float
)

data class Review(
    @SerializedName("start_time")
    val startTime: String,
    @SerializedName("end_time")
    val endTime: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("mentoring_id")
    val mentoringId: Int,
    @SerializedName("rating")
    val rating: Float,
    @SerializedName("feedback")
    val feedback: String,
    @SerializedName("sentiment")
    val sentiment: String,
    @SerializedName("mentee_name")
    val menteeName: String
)
