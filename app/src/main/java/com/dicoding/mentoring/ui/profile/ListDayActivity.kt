package com.dicoding.mentoring.ui.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mentoring.R
import com.dicoding.mentoring.data.local.Days
import com.dicoding.mentoring.databinding.ActivityListDayBinding
import com.google.firebase.auth.FirebaseAuth

class ListDayActivity : AppCompatActivity() {

    private lateinit var dayViewModel: DayViewModel
    private lateinit var binding: ActivityListDayBinding

    private lateinit var days: MutableList<Days>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListDayBinding.inflate(layoutInflater)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.choose_days)

        days = mutableListOf(
            Days(0, getString(R.string.monday), false),
            Days(1, getString(R.string.tuesday), false),
            Days(2, getString(R.string.wednesday), false),
            Days(3, getString(R.string.thursday), false),
            Days(4, getString(R.string.friday), false),
            Days(5, getString(R.string.saturday), false),
            Days(6, getString(R.string.sunday), false),
        )

        dayViewModel = ViewModelProvider(this)[DayViewModel::class.java]

        val user = FirebaseAuth.getInstance().currentUser
        user?.getIdToken(false)
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
        setContentView(binding.root)
    }

    private fun updateItemValue(idDays: Int, newValue: Boolean) {
        val itemIndex = days.indexOfFirst { it.id == idDays }
        if (idDays > -1) {
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
        if (binding.checkboxMonday.isChecked) updateItemValue(0, true)
        else updateItemValue(0, false)
        if (binding.checkboxTuesday.isChecked) updateItemValue(1, true)
        else updateItemValue(1, false)
        if (binding.checkboxWednesday.isChecked) updateItemValue(2, true)
        else updateItemValue(2, false)
        if (binding.checkboxThursday.isChecked) updateItemValue(3, true)
        else updateItemValue(3, false)
        if (binding.checkboxFriday.isChecked) updateItemValue(4, true)
        else updateItemValue(4, false)
        if (binding.checkboxSaturday.isChecked) updateItemValue(5, true)
        else updateItemValue(5, false)
        if (binding.checkboxSunday.isChecked) updateItemValue(6, true)
        else updateItemValue(6, false)

        val statMonday = days[0].isSelected
        val statTuesday = days[1].isSelected
        val statWednesday = days[2].isSelected
        val statThursday = days[3].isSelected
        val statFriday = days[4].isSelected
        val statSaturday = days[5].isSelected
        val statSunday = days[6].isSelected

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

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}