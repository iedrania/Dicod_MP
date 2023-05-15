package com.dicoding.mentoring.data.remote.network

import com.dicoding.mentoring.data.local.RegisterResponse
import com.dicoding.mentoring.data.local.FeedbackResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("auth/register")
    fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("post-feedback")
    fun postFeedback(
        @Header("Authorization") token: String,
        @Field("id_mentor") idMentor: String,
        @Field("rating") rating: Float,
        @Field("feedback") feedback: String
    ): Call<FeedbackResponse>
}