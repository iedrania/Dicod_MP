package com.dicoding.mentoring.ui.schedule

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mentoring.data.local.GroupedSchedule
import com.dicoding.mentoring.data.local.Schedule
import com.dicoding.mentoring.data.local.ScheduleResponse
import com.dicoding.mentoring.data.remote.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScheduleViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _listGroupedSchedule = MutableLiveData<List<GroupedSchedule>>()
    val listGroupedSchedule: LiveData<List<GroupedSchedule>> = _listGroupedSchedule

    fun getSchedule(token: String?, fromDate: String?) {
        _isError.value = false
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSchedule("Bearer $token", fromDate ?: "")
        client.enqueue(object : Callback<ScheduleResponse> {
            override fun onResponse(
                call: Call<ScheduleResponse>, response: Response<ScheduleResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null) {
                        _listGroupedSchedule.value = groupSchedulesByDate(responseBody.listSchedule)
                    }
                } else {
                    Log.e(TAG, "getSchedule onError: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ScheduleResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e(TAG, "getSchedule onFailure: ${t.message}")
            }
        })
    }

    private fun groupSchedulesByDate(schedules: List<Schedule>): List<GroupedSchedule> {
        val groupedSchedulesMap = mutableMapOf<String, MutableList<Schedule>>()

        for (schedule in schedules) {
            val date = schedule.fromDate?.substringBefore("T")
            val scheduleList = groupedSchedulesMap.getOrPut(date!!) { mutableListOf() }
            scheduleList.add(schedule)
        }

        return groupedSchedulesMap.map { (date, scheduleList) ->
            GroupedSchedule(date, scheduleList)
        }
    }

    companion object {
        private const val TAG = "ScheduleViewModel"
    }
}