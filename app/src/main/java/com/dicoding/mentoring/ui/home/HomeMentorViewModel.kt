package com.dicoding.mentoring.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mentoring.data.local.Dashboard
import com.dicoding.mentoring.data.local.HomeMentorResponse
import com.dicoding.mentoring.data.remote.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeMentorViewModel: ViewModel() {

    private val _dashboardMentorData = MutableLiveData<Dashboard>()
    val dashboardMentorData: LiveData<Dashboard> = _dashboardMentorData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        const val TAG = "HomeMentorViewModel"
    }

    fun getProfile(token: String?){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDashboardMentor("Bearer $token")
        client.enqueue(object : Callback<HomeMentorResponse> {
            override fun onResponse(
                call: Call<HomeMentorResponse>,
                response: Response<HomeMentorResponse>
            ) {
                _isLoading.value = false
                if(response.isSuccessful){
                    _dashboardMentorData.value = response.body()?.dashboard
                    Log.d(TAG, "Response sukses, response body : ${response.body()}")
                }else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<HomeMentorResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "OnFailure : ${t.message}")
            }
        })
    }




}