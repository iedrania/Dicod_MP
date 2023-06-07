package com.dicoding.mentoring.data.remote.network

import com.dicoding.mentoring.data.local.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
import java.util.*
import kotlin.collections.ArrayList

interface ApiService {
    @FormUrlEncoded
    @POST("auth/register")
    fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("mentee/feedback")
    fun postFeedback(
        @Header("Authorization") token: String,
        @Field("mentoring_id") mentoringId: Long,
        @Field("feedback") feedback: String,
        @Field("rating") rating: Float,
    ): Call<FeedbackResponse>

    @GET("mentee/mentors")
    fun getMentors(
        @Header("Authorization") token: String,
    ): Call<MentorsResponse>

    @GET("mentee/schedule")
    fun getScheduleMentee(
        @Header("Authorization") token: String,
        @Query("from_date") fromDate: String,
    ): Call<ScheduleResponse>

    @GET("mentor/schedule")
    fun getScheduleMentor(
        @Header("Authorization") token: String,
        @Query("from_date") fromDate: String,
    ): Call<ScheduleResponse>

    @GET("user")
    fun getUserProfile(
        @Header("Authorization") token: String,
    ): Call<GetUserProfileResponse>

    @FormUrlEncoded
    @PUT("user")
    fun updateUserProfile(
        @Header("Authorization") token: String?,
        @Field("name") name: String?,
        @Field("gender_id") gender_id: Int?,
        @Field("phone") phone: String?,
        @Field("bio") bio: String?,
        @Field("email") email: String?,
    ): Call<PostUserProfileResponse>


    @GET("user/interest")
    fun getUserInterest(
        @Header("Authorization") token: String,
    ): Call<InterestResponse>

    @FormUrlEncoded
    @PUT("user/interest")
    fun updateUserInterest(
        @Header("Authorization") token: String,
        @Field("is_path_android") is_path_android: Boolean?,
        @Field("is_path_ios") is_path_ios: Boolean?,
        @Field("is_path_flutter") is_path_flutter: Boolean?,
        @Field("is_path_ml") is_path_ml: Boolean?,
        @Field("is_path_fe") is_path_fe: Boolean?,
        @Field("is_path_be") is_path_be: Boolean?,
        @Field("is_path_react") is_path_react: Boolean?,
        @Field("is_path_devops") is_path_devops: Boolean?,
        @Field("is_path_gcp") is_path_gcp: Boolean?,
    ): Call<PostUserProfileResponse>

    @GET("/user/days")
    fun getAvailableDays(
        @Header("Authorization") token: String,
    ): Call<AvailableDaysResponse>

    @FormUrlEncoded
    @PUT("/user/days")
    fun postAvailableDays(
        @Header("Authorization") token: String,
        @Field("is_monday_available") is_monday_available: Boolean,
        @Field("is_tuesday_available") is_tuesday_available: Boolean,
        @Field("is_wednesday_available") is_wednesday_available: Boolean,
        @Field("is_thursday_available") is_thursday_available: Boolean,
        @Field("is_friday_available") is_friday_available: Boolean,
        @Field("is_saturday_available") is_saturday_available: Boolean,
        @Field("is_sunday_available") is_sunday_available: Boolean,
    ): Call<PostUserProfileResponse>

    @Multipart
    @PUT("user/avatar")
    fun updateUserProfilePicture(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
    ): Call<PostUserProfileResponse>

    @FormUrlEncoded
    @POST("/mentor/mentoring")
    fun uploadMentoringTime(
        @Header("Authorization") token: String,
        @Field("mentees_id") mentees_id: ArrayList<String>,
        @Field("start_time") start_time: String,
    ): Call<MentoringResponse>

    @GET("/mentor/dashboard")
    fun getDashboardMentor(
        @Header("Authorization") token: String,
    ): Call<HomeMentorResponse>
}