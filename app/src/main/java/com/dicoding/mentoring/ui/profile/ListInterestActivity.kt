package com.dicoding.mentoring.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.mentoring.R
import com.dicoding.mentoring.adapter.InterestAdapter
import com.dicoding.mentoring.data.local.Interest
import com.dicoding.mentoring.databinding.ActivityListInterestBinding

class ListInterestActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_interest)
        supportActionBar?.setTitle("Choose Interest")

        val interests = listOf(
            Interest("Multiplatform"),
            Interest("Machine Learning"),
            Interest("Mobile Development"),
            Interest("React Development")
        )

        //setRecyclerView
        recyclerView = findViewById(R.id.rvInterest)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = InterestAdapter(interests)
        recyclerView.adapter = adapter
    }
}