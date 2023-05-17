package com.dicoding.mentoring.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mentoring.data.local.Mentor
import com.dicoding.mentoring.data.local.MentorsResponse
import com.dicoding.mentoring.data.remote.network.ApiConfig
import com.google.firebase.auth.GetTokenResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _listMentor = MutableLiveData<List<Mentor>>()
    val listMentor: LiveData<List<Mentor>> = _listMentor

    fun findMentors(
        token: GetTokenResult, id: String, listDay: List<String>, listInterest: List<String>
    ) {
//        _listMentor.value = listOf(
//            Mentor(
//                "1", "Andi", 0, 4.4.toFloat(), "Ini Andi", listOf("UI/UX", "Android")
//            ), Mentor(
//                "2", "Budi", 0, 4.6.toFloat(), "Ini Budi", listOf("Web", "Android")
//            ), Mentor(
//                "3", "Cici", 0, 4.8.toFloat(), "Ini Cici", listOf("Web", "UI/UX")
//            )
//        )
        _isError.value = false
        _isLoading.value = true
        val client =
            ApiConfig.getApiService().getMentors("Bearer $token", id, listDay, listInterest)
        client.enqueue(object : Callback<MentorsResponse> {
            override fun onResponse(
                call: Call<MentorsResponse>, response: Response<MentorsResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null) {
                        _listMentor.value = responseBody.listMentor
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