package com.dicoding.mentoring.ui.rating

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.mentoring.R

class RatingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)

        supportActionBar?.hide()
    }
}