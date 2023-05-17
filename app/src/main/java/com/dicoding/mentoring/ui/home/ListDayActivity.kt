package com.dicoding.mentoring.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mentoring.R
import com.dicoding.mentoring.adapter.DaysAdapter
import com.dicoding.mentoring.data.local.Days
import com.dicoding.mentoring.databinding.ActivityListDayBinding

class ListDayActivity : AppCompatActivity() {

    private lateinit var adapter: DaysAdapter
    private lateinit var binding: ActivityListDayBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListDayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val days = listOf(
            Days("Monday",false),
            Days("Tuesday",false),
            Days("Wednesday",false),
            Days("Thursday",false),
            Days("Friday",false),
            Days("Saturday",false),
            Days("Sunday",false)
        )

        adapter = DaysAdapter(days)
        binding.rvDays.layoutManager =  LinearLayoutManager(this)
        binding.rvDays.adapter = adapter

        binding.btnSave.setOnClickListener{
            finish()
        }
    }
}