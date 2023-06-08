package com.dicoding.mentoring.ui.timepicker

import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mentoring.R
import com.dicoding.mentoring.databinding.ActivitySetTimeDateBinding
import com.dicoding.mentoring.utils.convertDateToISOString
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class TimePickerActivity : AppCompatActivity() {

    private lateinit var timePickerViewModel: TimePickerViewModel
    private lateinit var binding: ActivitySetTimeDateBinding
    private val calendar: Calendar = Calendar.getInstance()
    private var isTimeFilled: Boolean = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetTimeDateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val groupId = intent.getStringExtra(EXTRA_GROUP)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        timePickerViewModel = ViewModelProvider(this)[TimePickerViewModel::class.java]

        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }

        binding.chipSetTime.setOnClickListener {
            showTimePickerDialog()
        }

        binding.btnSave.setOnClickListener {
            if (isTimeFilled) {
                val selectedDate = calendar.time
                val isoDate = convertDateToISOString(selectedDate)
                if (groupId != null) {
                    postMentoringTime(isoDate, groupId)
                } else {
                    finish()
                }
                finish()
            } else {
                Toast.makeText(
                    this, getString(R.string.mentoring_time_unchosen), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showTimePickerDialog() {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this, { _, selectedHour, selectedMinute ->
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                calendar.set(Calendar.MINUTE, selectedMinute)

                isTimeFilled = true

                binding.tvMentoringTimeChoose.text = calendar.time.toString()

            }, hour, minute, false
        )
        timePickerDialog.show()
    }

    private fun postMentoringTime(isoDate: String, groupId: String) {
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser

        db.collection("groups").document(groupId).get().addOnSuccessListener { document ->
            if (document != null) {
                Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                val members = document.data?.get("members") as ArrayList<String>
                user?.getIdToken(false)?.addOnSuccessListener {
                    timePickerViewModel.postMentoringTime(
                        it.token, arrayListOf(members[0]), isoDate, members, groupId
                    )
                }
            } else {
                Log.d(TAG, "No such document")
            }
        }.addOnFailureListener { exception ->
            Log.d(TAG, "get failed with ", exception)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    companion object {
        private const val EXTRA_GROUP = "extra_group"
        private const val TAG = "TimePickerActivity"
    }
}