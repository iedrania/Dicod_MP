package com.dicoding.mentoring.ui.timepicker

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mentoring.data.local.MentoringResponse
import com.dicoding.mentoring.data.remote.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class TimePickerViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        const val TAG = "TimePickerViewModel"
    }

    fun postMentoringTime(token: String, menteeId: ArrayList<String>, date: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().uploadMentoringTime("Bearer $token", menteeId, date)
        client.enqueue(object : Callback<MentoringResponse>{
            override fun onResponse(
                call: Call<MentoringResponse>,
                response: Response<MentoringResponse>
            ) {
                _isLoading.value = false
                Log.d(TAG,"response : $response")
                if(response.isSuccessful){
                    Log.d(TAG,"Rersponse sukses, response body: ${response.body()}")
                }else{
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MentoringResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "OnFailure : ${t.message}")
            }

        })
    }



}