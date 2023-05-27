package com.dicoding.mentoring.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mentoring.data.local.RegisterResponse
import com.dicoding.mentoring.data.remote.network.ApiConfig
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel(private val auth: FirebaseAuth) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    fun postRegister(name: String, email: String, password: String) {
        _isError.value = false
        _isLoading.value = true
        val client = ApiConfig.getApiService().postRegister(name, email, password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>, response: Response<RegisterResponse>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "signInFromRegister:success")
                            } else {
                                Log.w(TAG, "signInFromRegister:failure", task.exception)
                            }
                        }
                } else {
                    _isError.value = true
                    Log.e(TAG, "postRegister ERROR: ${response.message()}")
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody?.let { JSONObject(it).getString("message") }
                    Log.e(TAG, "postRegister onError: $errorMessage")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e(TAG, "postRegister onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "UserViewModel"
    }
}