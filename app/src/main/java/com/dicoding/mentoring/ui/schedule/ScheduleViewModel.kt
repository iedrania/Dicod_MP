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
                        _listGroupedSchedule.value = groupScheduleByDate(responseBody.listSchedule)
                        // Uncomment this line to use dummy schedule list
//                        useDummySchedule()
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

    private fun groupScheduleByDate(listSchedule: List<Schedule>): List<GroupedSchedule> {
        val groupedScheduleMap = mutableMapOf<String, MutableList<Schedule>>()

        for (schedule in listSchedule) {
            val date = schedule.fromDate?.substringBefore("T")
            val scheduleList = groupedScheduleMap.getOrPut(date!!) { mutableListOf() }
            scheduleList.add(schedule)
        }

        return groupedScheduleMap.map { (date, scheduleList) ->
            GroupedSchedule(date, scheduleList)
        }
    }

    private fun useDummySchedule() {
        _listGroupedSchedule.value = groupScheduleByDate(
            listOf(
                Schedule("Andy John", "2023-12-12T12:00:00.000Z", "2023-12-12T13:00:00.000Z", "id"),
                Schedule("Dodo Bird", "2023-12-12T13:00:00.000Z", "2023-12-12T14:00:00.000Z", "id"),
                Schedule("Bob Dylan", "2023-12-13T13:00:00.000Z", "2023-12-13T14:00:00.000Z", "id"),
            )
        )
    }

    companion object {
        private const val TAG = "ScheduleViewModel"
    }
}