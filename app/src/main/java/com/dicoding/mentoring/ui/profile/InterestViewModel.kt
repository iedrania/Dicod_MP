package com.dicoding.mentoring.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.dicoding.mentoring.data.local.Interest
import com.dicoding.mentoring.data.local.InterestResponse
import com.dicoding.mentoring.data.local.PostUserProfileResponse
import com.dicoding.mentoring.data.local.UserResponse
import com.dicoding.mentoring.data.remote.network.ApiConfig
import com.google.firebase.firestore.auth.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InterestViewModel : ViewModel() {

    private val _userInterest = MutableLiveData<Interest>()
    val userInterest : LiveData<Interest> = _userInterest

    private val _postUserInterest = MutableLiveData<UserResponse>()
    val postUserInterest : LiveData<UserResponse> = _postUserInterest

    companion object{
        const val TAG = "InterestViewModel"
    }

    fun getInterest(token:String?){
        val client = ApiConfig.getApiService().getUserInterest("Bearer $token")
        client.enqueue(object : Callback<InterestResponse>{
            override fun onResponse(
                call: Call<InterestResponse>,
                response: Response<InterestResponse>
            ) {
                if(response.isSuccessful){
                    _userInterest.value = response.body()?.user
                    Log.d(TAG,"Rersponse sukses, response body: ${response.body()}")
                }else{
                    Log.e(ProfileViewModel.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<InterestResponse>, t: Throwable) {
                Log.e(ProfileViewModel.TAG, "OnFailure : ${t.message}")
            }
        })
    }

    fun updateInterest(token: String?, isPathAndroid : Boolean, isPathWeb : Boolean, isPathIos : Boolean, isPathMl : Boolean, isPathFlutter: Boolean, isPathFe: Boolean, isPathBe: Boolean, isPathReact: Boolean, isPathDevops: Boolean){
        val client = ApiConfig.getApiService().updateUserInterest("Bearer $token", isPathAndroid, isPathWeb, isPathIos, isPathMl,isPathFlutter, isPathFe, isPathBe, isPathReact, isPathDevops)
        client.enqueue(object : Callback<PostUserProfileResponse>{
            override fun onResponse(
                call: Call<PostUserProfileResponse>,
                response: Response<PostUserProfileResponse>
            ) {
                Log.e(TAG, "Response sukses! response body : $response")
                if(response.isSuccessful){
                    _postUserInterest.value = response.body()?.updatedUser
                }else {
                    Log.e(ProfileViewModel.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<PostUserProfileResponse>, t: Throwable) {
                Log.e(TAG, "OnFailure : ${t.message}")
            }

        })
    }
}