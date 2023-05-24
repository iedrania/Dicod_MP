package com.dicoding.mentoring.data.remote.network

import com.dicoding.mentoring.data.local.*
import retrofit2.Call
import retrofit2.Callback
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
        @Field("mentoring_id") mentoringId: String,
        @Field("rating") rating: Float,
        @Field("feedback") feedback: String
    ): Call<FeedbackResponse>

    @GET("mentor/all")
    fun getMentors(
        @Header("Authorization") token: String,
    ): Call<MentorsResponse>

    @GET("mentors-by-interest")
    fun getMentorsByInterest(
        @Header("Authorization") token: String, @Query("list_interest") listInterest: List<String>
    ): Call<MentorsResponse>

    @GET("schedules")
    fun getSchedules(
        @Header("Authorization") token: String,
        @Query("from_date") fromDate: String,
    ): Call<ScheduleResponse>

    @GET("user")
    fun getUserProfile(
        @Header("Authorization") token: String,
    ): Call<UserProfileResponse>

    @FormUrlEncoded
    @POST("user")
    fun updateUserProfile(
        @Header("Authorization") token: String?,
        @Field("name") name : String?,
        @Field("gender_id") gender_id : Int?,
        @Field("phone") phone : String?,
        @Field("bio") bio : String?,
        @Field("email") email : String?
    ) : Call<UserProfileResponse>

    @GET("user/interest")
    fun getUserInterest(
        @Header("Authorization") token: String
    ) : Call<InterestResponse>

    @FormUrlEncoded
    @POST("user/interest")
    fun updateUserInterest(
        @Header("Authorization") token: String,
    ) : Call<UserProfileResponse>

    @FormUrlEncoded
    @POST("user/avatar")
    fun updateUserProfilePicture(
        @Header("Authorization") token:String
    ) : Callback<UserProfileResponse>
}