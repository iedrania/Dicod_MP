package com.dicoding.mentoring.ui.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mentoring.data.local.Days
import com.dicoding.mentoring.databinding.ActivityListDayBinding
import com.google.firebase.auth.FirebaseAuth

class ListDayActivity : AppCompatActivity() {

    private lateinit var dayViewModel: DayViewModel
    private lateinit var binding: ActivityListDayBinding

    private val days: MutableList<Days> = mutableListOf(
        Days(0, "Senin", false),
        Days(1, "Selasa", false),
        Days(2, "Rabu", false),
        Days(3, "Kamis", false),
        Days(4, "Jumat", false),
        Days(5, "Sabtu", false),
        Days(6, "Minggu", false),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListDayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setTitle("Pilih Hari")

        //Obtain Viewmodel
        dayViewModel = ViewModelProvider(this).get(DayViewModel::class.java)

        //Obtain Firebase user token
        val user = FirebaseAuth.getInstance().currentUser
        user?.getIdToken(false)
        // get list of all mentors for adapter
        val token = user?.getIdToken(false)?.result?.token

        if (token != null) {
            getAvailableDays(token)
        }

        binding.btnSave.setOnClickListener {
            if (token != null) {
                updateAvailableDays(token)
                finish()
            }
        }
    }

    private fun updateItemValue(idDays: Int, newValue: Boolean) {
        val itemIndex = days.indexOfFirst { it.id == idDays }
        if (idDays > 0) {
            days[itemIndex].isSelected = newValue
        }
    }

    private fun getAvailableDays(token: String) {
        dayViewModel.getDaysAvailability(token)
        dayViewModel.daysAvailable.observe(this) {
            if (it.isMondayAvailable) binding.checkboxMonday.isChecked = true
            if (it.isTuesdayAvailable) binding.checkboxTuesday.isChecked = true
            if (it.isWednesdayAvailable) binding.checkboxWednesday.isChecked = true
            if (it.isThursdayAvailable) binding.checkboxThursday.isChecked = true
            if (it.isFridayAvailable) binding.checkboxFriday.isChecked = true
            if (it.isSaturdayAvailable) binding.checkboxSaturday.isChecked = true
            if (it.isSundayAvailable) binding.checkboxSunday.isChecked = true
        }
    }

    private fun updateAvailableDays(token: String) {
        //update days value
        if(binding.checkboxMonday.isChecked) updateItemValue(0,true) else updateItemValue(0,false)
        if(binding.checkboxTuesday.isChecked)updateItemValue(1,true) else updateItemValue(1,false)
        if(binding.checkboxWednesday.isChecked)updateItemValue(2,true) else updateItemValue(2,false)
        if(binding.checkboxThursday.isChecked)updateItemValue(3,true) else updateItemValue(3,false)
        if(binding.checkboxFriday.isChecked)updateItemValue(4,true) else updateItemValue(4,false)
        if(binding.checkboxSaturday.isChecked)updateItemValue(5,true) else updateItemValue(5,false)
        if(binding.checkboxSunday.isChecked)updateItemValue(6,true) else updateItemValue(6,false)

        //get days value
        var statMonday = days[0].isSelected
        var statTuesday = days[1].isSelected
        var statWednesday = days[2].isSelected
        var statThursday = days[3].isSelected
        var statFriday = days[4].isSelected
        var statSaturday = days[5].isSelected
        var statSunday = days[6].isSelected

        dayViewModel.postDayAvailibility(
            token,
            statMonday,
            statTuesday,
            statWednesday,
            statThursday,
            statFriday,
            statSaturday,
            statSunday
        )
    }

}