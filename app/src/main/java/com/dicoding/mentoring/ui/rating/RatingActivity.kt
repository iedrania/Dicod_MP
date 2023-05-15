package com.dicoding.mentoring.ui.rating

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mentoring.R
import com.dicoding.mentoring.databinding.ActivityRatingBinding
import com.dicoding.mentoring.helper.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RatingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRatingBinding
    private lateinit var ratingViewModel: RatingViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRatingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        auth = Firebase.auth

        ratingViewModel = ViewModelProvider(
            this, ViewModelFactory(auth)
        )[RatingViewModel::class.java]
        ratingViewModel.isLoading.observe(this) { showLoading(it) }
        ratingViewModel.isError.observe(this) { showError(it) }
        ratingViewModel.isSuccess.observe(this) {
            finish()
        }

        binding.btnRatingSubmit.setOnClickListener {
            ratingViewModel.postFeedback(
                "idMentor", // TODO get mentor id
                binding.rbRateStars.rating,
                binding.edRateFeedback.text.toString()
            )
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showError(isError: Boolean) {
        if (isError) {
            Toast.makeText(
                this@RatingActivity, getString(R.string.feedback_failed), Toast.LENGTH_SHORT
            ).show()
        }
    }
}