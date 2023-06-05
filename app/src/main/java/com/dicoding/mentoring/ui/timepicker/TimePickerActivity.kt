package com.dicoding.mentoring.ui.timepicker

import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mentoring.databinding.ActivitySetTimeDateBinding
import com.dicoding.mentoring.utils.convertDateToISOString
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList


class TimePickerActivity : AppCompatActivity() {

    private lateinit var timePickerViewModel: TimePickerViewModel
    private lateinit var binding: ActivitySetTimeDateBinding
    private val calendar: Calendar = Calendar.getInstance()

    private var menteeId: String? = null
    private var userIds = ArrayList<String>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetTimeDateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Obtain Firebase user token
        val user = FirebaseAuth.getInstance().currentUser
        user?.getIdToken(false)
        val token = user?.getIdToken(false)?.result?.token

        timePickerViewModel = ViewModelProvider(this)[TimePickerViewModel::class.java]


        //update selected calendar date
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Update the calendar with the selected date
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }


        binding.chipSetTime.setOnClickListener {
            showTimePickerDialog()
        }

        binding.btnSave.setOnClickListener {
            val selectedDate = calendar.time

            // format the combined date and time
            val isoDate = convertDateToISOString(selectedDate)

            binding.tvMentoringTime.text = isoDate

            if (token != null) {
                postMentoringTime(token, isoDate)
                finish()
            }
        }
    }

    private fun showTimePickerDialog() {

        //variable for get time data
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this, { _, selectedHour, selectedMinute ->
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                calendar.set(Calendar.MINUTE, selectedMinute)
            }, hour, minute, false
        )
        timePickerDialog.show()
    }

    private fun postMentoringTime(token: String, isoDate: String): Task<QuerySnapshot> {
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser

        val dbRef = db.collection("groups").whereArrayContains("members", user?.uid!!)
        return dbRef.get().addOnSuccessListener { result ->
            for (document in result) {
                val membersArray = document.data["members"] as List<String>
                if (membersArray.isNotEmpty()) {
                    menteeId = membersArray[1]
                    println("nilai menteeId adalah : $menteeId")
                    userIds.add(menteeId!!)
                    println("Array userIds: $userIds")
                    //upload data
                    timePickerViewModel.postMentoringTime(token, userIds, isoDate)
                    println("Token: $token")
                    println("isoDate: $isoDate")
                    println("PostMentoring sudah tereksekusi. Array userIds: $userIds")
                    break
                }
            }
        }
    }
}