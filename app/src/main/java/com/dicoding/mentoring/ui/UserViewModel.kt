package com.dicoding.mentoring.ui

import android.util.Log
import androidx.lifecycle.*
import com.dicoding.mentoring.data.local.UserResponse
import com.dicoding.mentoring.data.remote.network.ApiConfig
import com.dicoding.mentoring.helper.LoginPreferences
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel(private val pref: LoginPreferences) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    fun postRegister(name: String, email: String, password: String) {
        _isError.value = false
        _isLoading.value = true
        val client = ApiConfig.getApiService().postRegister(name, email, password)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>, response: Response<UserResponse>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    postLogin(email, password)
                } else {
                    _isError.value = true
                    Log.e(TAG, "postRegister ERROR: ${response.message()}")
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody?.let { JSONObject(it).getString("message") }
                    Log.e(TAG, "postRegister onError: $errorMessage")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e(TAG, "postRegister onFailure: ${t.message}")
            }
        })
    }

    fun postLogin(email: String, password: String) {
        _isError.value = false
        _isLoading.value = true
        val client = ApiConfig.getApiService().postLogin(email, password)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>, response: Response<UserResponse>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    saveLoginInfo(responseBody.loginResult.token)
                } else {
                    _isError.value = true
                    Log.e(TAG, "postLogin ERROR: ${response.message()}")
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody?.let { JSONObject(it).getString("message") }
                    Log.e(TAG, "postLogin onError: $errorMessage")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e(TAG, "postLogin onFailure: ${t.message}")
            }
        })
    }

    fun getLoginInfo(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    fun saveLoginInfo(token: String) {
        viewModelScope.launch {
            pref.setToken(token)
        }
    }

    companion object {
        private const val TAG = "UserViewModel"
    }
}