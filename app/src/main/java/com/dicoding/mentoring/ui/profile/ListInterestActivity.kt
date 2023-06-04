package com.dicoding.mentoring.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mentoring.R
import com.dicoding.mentoring.adapter.InterestAdapter
import com.dicoding.mentoring.data.local.InterestItem
import com.dicoding.mentoring.databinding.ActivityListInterestBinding
import com.google.firebase.auth.FirebaseAuth

class ListInterestActivity : AppCompatActivity() {

    private lateinit var interestViewModel: InterestViewModel
    private lateinit var binding: ActivityListInterestBinding

    private val interests: MutableList<InterestItem> = mutableListOf(
        InterestItem(0, "Android Developer", false),
        InterestItem(1, "iOS Developer", false),
        InterestItem(2, "Multi-Platform App Developer", false),
        InterestItem(3, "Machine Learning Developer", false),
        InterestItem(4, "Front-End Web Developer", false),
        InterestItem(5, "Back-End Web Developer", false),
        InterestItem(6, "React Developer", false),
        InterestItem(7, "DevOps Engineer Developer", false),
        InterestItem(8, "Google Cloud Professional", false),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListInterestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setTitle("Choose Interest")

        val btnSave = findViewById<Button>(R.id.btn_save)

        //obtain viewmodel
        interestViewModel = ViewModelProvider(this).get(InterestViewModel::class.java)
        interestViewModel.isLoading.observe(this){
            showLoading(it)
        }

        //Obtain Firebase user token
        val user = FirebaseAuth.getInstance().currentUser
        user?.getIdToken(false)
        // get list of all mentors for adapter
        val token = user?.getIdToken(false)?.result?.token

        getInterest(token)

        btnSave.setOnClickListener {
            updateInterest(token)
            finish()
        }
    }

    private fun getInterest(token: String?) {
        interestViewModel.getInterest(token)
        interestViewModel.userInterest.observe(this) {
            if (it.is_path_android == true) updateItemValue(0, true)
            if (it.is_path_ios == true) updateItemValue(1, true)
            if (it.is_path_flutter == true) updateItemValue(2, true)
            if (it.is_path_ml == true) updateItemValue(3, true)
            if (it.is_path_fe == true) updateItemValue(4, true)
            if (it.is_path_be == true) updateItemValue(5, true)
            if (it.is_path_react == true) updateItemValue(6, true)
            if (it.is_path_devops == true) updateItemValue(7, true)
            if (it.is_path_gcp == true) updateItemValue(8, true)

            setRecyclerView(interests)
        }
    }

    private fun updateInterest(token: String?) {
        //Get checkbox value of each interest
        var isPathAndro = interests[0].isChecked
        var isPathIos = interests[1].isChecked
        var isPathFlutter = interests[2].isChecked
        var isPathML = interests[3].isChecked
        var isPathFE = interests[4].isChecked
        var isPathBE = interests[5].isChecked
        var isPathReact = interests[6].isChecked
        var isPathDevops = interests[7].isChecked
        var isPathGCP = interests[8].isChecked

        //Put the data to API
        interestViewModel.updateInterest(
            token,
            isPathAndro,
            isPathIos,
            isPathFlutter,
            isPathML,
            isPathFE,
            isPathBE,
            isPathReact,
            isPathDevops,
            isPathGCP,
        )
    }


    private fun setRecyclerView(interests: List<InterestItem>) {
        binding.rvInterest.layoutManager = LinearLayoutManager(this)
        val adapter = InterestAdapter(interests)
        binding.rvInterest.adapter = adapter
    }

    private fun updateItemValue(idInterest: Int, newValue: Boolean) {
        val itemIndex = interests.indexOfFirst { it.id == idInterest }
        if (idInterest > 0) {
            interests[itemIndex].isChecked = newValue
        }
    }

    private fun showLoading(isLoading : Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}