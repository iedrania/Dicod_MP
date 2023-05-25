package com.dicoding.mentoring.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mentoring.data.local.*
import com.dicoding.mentoring.data.remote.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel: ViewModel() {

    private val _userProfile = MutableLiveData<UserResponse>()
    val userProfile : LiveData<UserResponse> = _userProfile

    private val _interestProfile = MutableLiveData<Interest>()
    val interestProfile : LiveData<Interest> = _interestProfile

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        const val TAG = "ProfileViewModel"
    }

    fun getProfile(token: String?){
        val client = ApiConfig.getApiService().getUserProfile("Bearer $token")
        client.enqueue(object: Callback<GetUserProfileResponse>{
            override fun onResponse(
                call: Call<GetUserProfileResponse>,
                response: Response<GetUserProfileResponse>
            ) {
                if(response.isSuccessful){
                    _userProfile.value = response.body()?.user
                    Log.e(TAG, "Response sukses, response body : ${response.body()}")
                }else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetUserProfileResponse>, t: Throwable) {
                Log.e(TAG, "OnFailure : ${t.message}")
            }
        })
    }

    fun updateProfile(token: String?, name : String?, phone: String?, bio: String?, email: String?){
        val client = ApiConfig.getApiService().updateUserProfile("Bearer $token", name, phone, bio, email)
        client.enqueue(object : Callback<PostUserProfileResponse>{
            override fun onResponse(
                call: Call<PostUserProfileResponse>,
                response: Response<PostUserProfileResponse>
            ) {
                Log.e(TAG, "Response gagal, response body : $response")
                if (response.isSuccessful){
                    _userProfile.value = response.body()?.updatedUser
                    Log.e(TAG, "Response sukses, response body : ${response.body()}")
                }else {
                    Log.e(TAG, "updateProfile onFailure: ${response.body().toString()}")
                }
            }
            override fun onFailure(call: Call<PostUserProfileResponse>, t: Throwable) {
                Log.e(TAG, "OnFailure : ${t.message.toString()}")
            }
        })
    }
}