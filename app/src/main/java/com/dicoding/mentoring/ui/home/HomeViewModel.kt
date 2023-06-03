package com.dicoding.mentoring.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mentoring.data.local.Mentor
import com.dicoding.mentoring.data.local.Mentors
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

    private val _listMentor = MutableLiveData<List<Mentors>>()
    val listMentor: MutableLiveData<List<Mentors>> = _listMentor

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
                        _listMentor.value = responseBody.mentors
                        // Uncomment this line to use dummy mentor list
//                        useDummyMentors()
                    }
                } else {
                    _isError.value = true
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

    private fun useDummyMentors() {
        _listMentor.value = listOf(
            Mentors(
                4.6.toFloat(), Mentor(
                    "1",
                    "Anton",
                    1,
                    "Mobile Enthusiast",
                    "https://duckduckgo.com/i/7072e50d.png",
                    isPathAndroid = true,
                    isPathWeb = false,
                    isPathIos = true,
                    isPathMl = false,
                    isPathFlutter = true,
                    isPathFe = false,
                    isPathBe = false,
                    isPathReact = false,
                    isPathDevops = false,
                    isPathGcp = false,
                    isMondayAvailable = true,
                    isTuesdayAvailable = true,
                    isWednesdayAvailable = true,
                    isThursdayAvailable = false,
                    isFridayAvailable = false,
                    isSaturdayAvailable = false,
                    isSundayAvailable = false
                )
            ),
            Mentors(
                4.6.toFloat(), Mentor(
                    "2",
                    "Lady",
                    2,
                    "HMU! :D",
                    "https://duckduckgo.com/i/7072e50d.png",
                    isPathAndroid = false,
                    isPathWeb = true,
                    isPathIos = false,
                    isPathMl = true,
                    isPathFlutter = false,
                    isPathFe = false,
                    isPathBe = false,
                    isPathReact = false,
                    isPathDevops = false,
                    isPathGcp = false,
                    isMondayAvailable = false,
                    isTuesdayAvailable = false,
                    isWednesdayAvailable = false,
                    isThursdayAvailable = false,
                    isFridayAvailable = false,
                    isSaturdayAvailable = true,
                    isSundayAvailable = true
                )
            ),
        )
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}