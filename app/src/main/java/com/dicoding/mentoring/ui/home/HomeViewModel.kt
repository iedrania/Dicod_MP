package com.dicoding.mentoring.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mentoring.data.local.MentorsResponse
import com.dicoding.mentoring.data.remote.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _listMentor = MutableLiveData<MentorsResponse>()
    val listMentor: MutableLiveData<MentorsResponse> = _listMentor

    fun findMentors(token: String?) {
        _isError.value = false
        _isLoading.value = true
        val client = ApiConfig.getApiService().getMentors("Bearer $token")
        client.enqueue(object : Callback<MentorsResponse> {
            override fun onResponse(
                call: Call<MentorsResponse>, response: Response<MentorsResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null) {
                        _listMentor.value = responseBody!!
                    }
                } else {
                    Log.e(TAG, "findMentors onError: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MentorsResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e(TAG, "findMentors onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}