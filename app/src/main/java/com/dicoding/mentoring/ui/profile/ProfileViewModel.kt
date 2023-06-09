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

class ProfileViewModel : ViewModel() {

    private val _userProfile = MutableLiveData<UserResponse>()
    val userProfile: LiveData<UserResponse> = _userProfile

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    fun getProfile(token: String?) {
        _isError.value = false
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserProfile("Bearer $token")
        client.enqueue(object : Callback<GetUserProfileResponse> {
            override fun onResponse(
                call: Call<GetUserProfileResponse>, response: Response<GetUserProfileResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userProfile.value = response.body()?.user
                    Log.d(TAG, "Response sukses, response body : ${response.body()}")
                } else {
                    _isError.value = true
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetUserProfileResponse>, t: Throwable) {
                _isError.value = true
                _isLoading.value = false
                Log.e(TAG, "OnFailure : ${t.message}")
            }
        })
    }

    fun updateProfile(
        token: String?, name: String?, gender: Int?, phone: String?, bio: String?, email: String?
    ) {
        _isError.value = false
        _isLoading.value = true
        val client = ApiConfig.getApiService()
            .updateUserProfile("Bearer $token", name, gender, phone, bio, email)
        client.enqueue(object : Callback<PostUserProfileResponse> {
            override fun onResponse(
                call: Call<PostUserProfileResponse>, response: Response<PostUserProfileResponse>
            ) {
                _isLoading.value = false
                Log.e(TAG, "response body : $response")
                if (response.isSuccessful) {
                    _userProfile.value = response.body()?.updatedUser
                    Log.d(TAG, "Response sukses, response body : ${response.body()}")
                    _isSuccess.value = true
                } else {
                    _isError.value = true
                    Log.e(TAG, "updateProfile onFailure: ${response.body().toString()}")
                }
            }

            override fun onFailure(call: Call<PostUserProfileResponse>, t: Throwable) {
                _isError.value = true
                _isLoading.value = false
                Log.e(TAG, "OnFailure : ${t.message.toString()}")
            }
        })
    }

    companion object {
        const val TAG = "ProfileViewModel"
    }
}