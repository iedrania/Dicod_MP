package com.dicoding.mentoring.ui.timepicker

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.mentoring.databinding.ActivitySetTimeDateBinding
import java.util.*


class TimePickerActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySetTimeDateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySetTimeDateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.chipSetTime.setOnClickListener {
            openTimePicker()
        }

        binding.btnSave.setOnClickListener {

        }
    }

    private fun openTimePicker(){
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _: TimePicker, hourOfDay: Int, minute: Int ->
                // Handle the selected time
                val selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)
                Log.d("TimePickerActivity","Format time adalah $selectedTime")
            },
            hour,
            minute,
            false
        )
        timePickerDialog.show()
    }

}