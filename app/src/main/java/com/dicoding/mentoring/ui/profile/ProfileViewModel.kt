package com.dicoding.mentoring.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mentoring.data.local.Interest
import com.dicoding.mentoring.data.local.InterestResponse
import com.dicoding.mentoring.data.local.UserProfileResponse
import com.dicoding.mentoring.data.local.UserResponse
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

    //TODO responsenya masih belum succesful
    fun updateProfile(token: String?, name : String?, gender_id : Int?, phone: String?, bio: String?, email: String?){
        val client = ApiConfig.getApiService().updateUserProfile("Bearer $token", name, gender_id, phone, bio, email)
        client.enqueue(object : Callback<UserProfileResponse>{
            override fun onResponse(
                call: Call<UserProfileResponse>,
                response: Response<UserProfileResponse>
            ) {
                if (response.isSuccessful){
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

    fun getInterest(token: String?){
        val client = ApiConfig.getApiService().getUserInterest("Bearer $token")
        client.enqueue(object : Callback<InterestResponse>{
            override fun onResponse(
                call: Call<InterestResponse>,
                response: Response<InterestResponse>
            ) {
                if (response.isSuccessful){
                _interestProfile.value = response.body()?.user
                Log.e(TAG, "Response sukses, response body : ${response.body()}")
                }
            }

            override fun onFailure(call: Call<InterestResponse>, t: Throwable) {
                Log.e(TAG, "OnFailure : ${t.message}")
            }
        })
    }

}