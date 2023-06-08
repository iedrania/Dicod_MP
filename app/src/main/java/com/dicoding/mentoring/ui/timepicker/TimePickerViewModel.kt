package com.dicoding.mentoring.ui.timepicker

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mentoring.data.local.MentoringResponse
import com.dicoding.mentoring.data.remote.network.ApiConfig
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class TimePickerViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun postMentoringTime(
        token: String?,
        menteesId: ArrayList<String>,
        date: String,
        members: ArrayList<String>,
        groupId: String
    ) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().uploadMentoringTime("Bearer $token", menteesId, date)
        client.enqueue(object : Callback<MentoringResponse> {
            override fun onResponse(
                call: Call<MentoringResponse>, response: Response<MentoringResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    Log.d(TAG, "onSuccess: ${response.message()}")
                    postSpecialChat(members[1], groupId, response.body()?.data!!.id)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MentoringResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "OnFailure : ${t.message}")
            }
        })
    }

    private fun postSpecialChat(mentorId: String, groupId: String, mentoringId: Int) {
        val data = hashMapOf(
            "messageText" to "Mentoring created",
            "sentBy" to mentorId,
            "sentAt" to Timestamp.now(),
            "specialChat" to true,
            "mentoringId" to mentoringId,
            "feedbackGiven" to false,
        )

        val db = Firebase.firestore
        db.collection("messages/$groupId/texts").add(data)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")

                val recent = hashMapOf(
                    "messageText" to "Sesi terjadwal!",
                    "sentAt" to Timestamp.now(),
                    "sentBy" to mentorId,
                )

                db.collection("groups").document(groupId).update("recentMessage", recent)
                    .addOnSuccessListener {
                        Log.d(TAG, "DocumentSnapshot successfully updated!")
                    }.addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
            }.addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    companion object {
        const val TAG = "TimePickerViewModel"
    }
}