package com.dicoding.mentoring.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mentoring.data.local.UserProfileResponse
import com.dicoding.mentoring.data.local.UserResponse
import com.dicoding.mentoring.data.remote.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel : ViewModel() {

    private val _userProfile = MutableLiveData<UserResponse>()
    val userProfile : LiveData<UserResponse> = _userProfile

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        const val TAG = "ProfileViewModel"
    }

    fun getProfile(token: String?){
        val client = ApiConfig.getApiService().getUserProfile("Bearer $token")
        client.enqueue(object: Callback<UserProfileResponse>{
            override fun onResponse(
                call: Call<UserProfileResponse>,
                response: Response<UserProfileResponse>
            ) {
                if(response.isSuccessful){
                    _userProfile.value = response.body()?.user
                    Log.e(TAG, "Response sukses, response body : ${response.body()}")
                }else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                Log.e(TAG, "OnFailure : ${t.message}")
            }
        })
    }

    fun postProfile(token: String?){

    }

}