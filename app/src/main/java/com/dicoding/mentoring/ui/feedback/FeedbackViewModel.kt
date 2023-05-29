package com.dicoding.mentoring.ui.feedback

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mentoring.data.local.FeedbackResponse
import com.dicoding.mentoring.data.remote.network.ApiConfig
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedbackViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    fun postFeedback(token: String?, mentoringId: String, rating: Float, feedback: String) {
        _isSuccess.value = false
        _isError.value = false
        _isLoading.value = true
        val client =
            ApiConfig.getApiService().postFeedback("Bearer $token", mentoringId, rating, feedback)
        client.enqueue(object : Callback<FeedbackResponse> {
            override fun onResponse(
                call: Call<FeedbackResponse>, response: Response<FeedbackResponse>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _isSuccess.value = true
                } else {
                    _isError.value = true
                    Log.e(TAG, "postFeedback ERROR: ${response.message()}")
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody?.let { JSONObject(it).getString("message") }
                    Log.e(TAG, "postFeedback onError: $errorMessage")
                }
            }

            override fun onFailure(call: Call<FeedbackResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e(TAG, "postFeedback onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "FeedbackViewModel"
    }
}