package com.dicoding.mentoring.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.mentoring.R
import com.dicoding.mentoring.adapter.InterestAdapter
import com.dicoding.mentoring.data.local.Interest
import com.dicoding.mentoring.data.local.InterestItem
import com.dicoding.mentoring.databinding.ActivityListInterestBinding
import com.google.firebase.auth.FirebaseAuth

class ListInterestActivity : AppCompatActivity() {

    private lateinit var interestViewModel: InterestViewModel
    private lateinit var binding: ActivityListInterestBinding

    private val interests : MutableList<InterestItem> = mutableListOf(
        InterestItem(1,"Android Developer",false),
        InterestItem(2,"iOS Developer",false),
        InterestItem(3,"Multi-Platform App Developer",false),
        InterestItem(4,"Machine Learning Developer",false),
        InterestItem(5,"Front-End Web Developer",false),
        InterestItem(6,"Back-End Web Developer",false),
        InterestItem(7,"React Developer",false),
        InterestItem(8,"DevOps Engineer Developer",false),
        InterestItem(9,"Google Cloud Professional",false),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListInterestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setTitle("Choose Interest")

        val btnSave = findViewById<Button>(R.id.btn_save)

        //obtain viewmodel
        interestViewModel = ViewModelProvider(this).get(InterestViewModel::class.java)

        //Obtain Firebase user token
        val user = FirebaseAuth.getInstance().currentUser
        user?.getIdToken(false)
        // get list of all mentors for adapter
        val token = user?.getIdToken(false)?.result?.token

        getInterest(token)

        btnSave.setOnClickListener {
            finish()
        }
    }

    private fun getInterest(token: String?) {
        interestViewModel.getInterest(token)
        interestViewModel.userInterest.observe(this) {
            if (it.is_path_android == true) updateItemValue(1,true)
            if (it.is_path_ios == true) updateItemValue(2,true)
            if (it.is_path_flutter == true) updateItemValue(3,true)
            if (it.is_path_ml == true) updateItemValue(4,true)
            if (it.is_path_fe == true) updateItemValue(5,true)
            if (it.is_path_be == true) updateItemValue(6,true)
            if (it.is_path_react == true) updateItemValue(7,true)
            if (it.is_path_devops == true) updateItemValue(8,true)
            if (it.is_path_gcp == true) updateItemValue(9,true)

            setRecyclerView(interests)
        }

    }

    private fun setRecyclerView(interests: List<InterestItem>) {
        binding.rvInterest.layoutManager = LinearLayoutManager(this)
        val adapter = InterestAdapter(interests)
        binding.rvInterest.adapter = adapter
    }

    private fun updateItemValue(idInterest: Int, newValue:Boolean){
        val itemIndex = interests.indexOfFirst { it.id == idInterest }
        if (idInterest > 0) {
            interests[itemIndex].isChecked = newValue
        }
    }
}