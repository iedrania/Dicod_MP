package com.dicoding.mentoring.ui.timepicker

import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mentoring.databinding.ActivitySetTimeDateBinding
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class TimePickerActivity : AppCompatActivity() {

    private lateinit var timePickerViewModel: TimePickerViewModel
    private lateinit var binding: ActivitySetTimeDateBinding
    private val calendar: Calendar = Calendar.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetTimeDateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Obtain Firebase user token
        val user = FirebaseAuth.getInstance().currentUser
        user?.getIdToken(false)
        val token = user?.getIdToken(false)?.result?.token

        //Obtaiun Firebase user UID
        val userId = user?.uid
        val userIds = arrayListOf<String>()
        if (userId != null) {
            userIds.add("5")
        }

        println(userIds)
        for(userId in userIds){
            println(userId)
        }

        timePickerViewModel = ViewModelProvider(this).get(TimePickerViewModel::class.java)


        //update selected calendar date
        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
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

            val selectedTime = getTimeFromTimePicker()

            // combine the date and time
//            val combinedDateTime = combineDateAndTime(selectedDate, selectedTime)

            // format the combined date and time
            val isoDate = convertDateToISOString(selectedDate)

            binding.tvMentoringTime.text = isoDate

            if (token != null) {
                timePickerViewModel.postMentoringTime(token, userIds, isoDate)
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

    private fun getTimeFromTimePicker(): Date {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val time = "$hour:$minute"
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return timeFormat.parse(time) ?: Date()
    }
//
//    private fun combineDateAndTime(date: Date, time: Date): Date{
//        val calendar = Calendar.getInstance()
//        calendar.time = date
//
//        val timeCalendar = Calendar.getInstance()
//        timeCalendar.time = time
//        calendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY))
//        calendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE))
//
//        return calendar.time
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertDateToISOString(date: Date): String {
        val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.getDefault())
        // Define the input pattern
        val inputPattern = "EEE MMM dd HH:mm:ss 'GMT'XXX yyyy"

        // Parse the input string with the defined pattern
        val dateTime = OffsetDateTime.parse(date.toString(), DateTimeFormatter.ofPattern(inputPattern))

        // Format the parsed date/time to ISO 8601 string
        val isoDate = dateTime.format(DateTimeFormatter.ISO_DATE_TIME)

        return isoDate
    }

}