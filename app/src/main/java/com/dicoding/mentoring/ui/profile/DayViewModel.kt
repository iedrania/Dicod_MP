package com.dicoding.mentoring.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mentoring.data.local.AvailableDaysResponse
import com.dicoding.mentoring.data.local.Days
import com.dicoding.mentoring.data.local.ListDay
import com.dicoding.mentoring.data.remote.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DayViewModel: ViewModel() {

    private val _daysAvailable = MutableLiveData<ListDay>()
    val daysAvailable : LiveData<ListDay> = _daysAvailable

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        const val TAG = "DayViewModel"
    }


    fun getDaysAvailability(token: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getAvailableDays("Bearer $token")
        client.enqueue(object : Callback<AvailableDaysResponse> {
            override fun onResponse(
                call: Call<AvailableDaysResponse>,
                response: Response<AvailableDaysResponse>
            ) {
                _isLoading.value = false
                Log.d(TAG,"Response : $response")
                if (response.isSuccessful){
                    _daysAvailable.value = response.body()?.user
                    Log.d(TAG,"Response sukses, response body: ${response.body()}")
                }else{
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AvailableDaysResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "OnFailure : ${t.message}")
            }
        })
    }

    private fun postDayAvailibility(
        token: String,
        isMonday: Boolean,
        isTuesday: Boolean,
        isWednesday: Boolean,
        isThursday: Boolean,
        isFriday: Boolean,
        isSaturday: Boolean,
        isSunday: Boolean
    ){
        _isLoading.value = true
        val client = ApiConfig.getApiService()
    }

}