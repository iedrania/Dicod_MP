package com.dicoding.mentoring.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.dicoding.mentoring.data.local.Interest
import com.dicoding.mentoring.data.local.InterestResponse
import com.dicoding.mentoring.data.remote.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InterestViewModel : ViewModel() {

    private val _userInterest = MutableLiveData<Interest>()
    val userInterest : LiveData<Interest> = _userInterest

    fun getInterest(token:String?){
        val client = ApiConfig.getApiService().getUserInterest("Bearer $token")
        client.enqueue(object : Callback<InterestResponse>{
            override fun onResponse(
                call: Call<InterestResponse>,
                response: Response<InterestResponse>
            ) {
                if(response.isSuccessful){
                    _userInterest.value = response.body()?.user
                }else{
                    Log.e(ProfileViewModel.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<InterestResponse>, t: Throwable) {
                Log.e(ProfileViewModel.TAG, "OnFailure : ${t.message}")
            }
        })
    }

//    fun updateInterest(token: String?){
//        val client = ApiConfig.getApiService().updateUserInterest("Bearer $token, ")
//        client.enqueue(object : Callback<InterestResponse>{
//            override fun onResponse(
//                call: Call<InterestResponse>,
//                response: Response<InterestResponse>
//            ) {
//                if(response.isSuccessful){
//                    Log.d()
//                }
//            }
//
//            override fun onFailure(call: Call<InterestResponse>, t: Throwable) {
//                TODO("Not yet implemented")
//            }
//
//        })
//    }

}