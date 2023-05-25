package com.dicoding.mentoring.ui.rating

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mentoring.R
import com.dicoding.mentoring.databinding.ActivityRatingBinding
import com.dicoding.mentoring.ui.login.LoginActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RatingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRatingBinding
    private lateinit var ratingViewModel: RatingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRatingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        getCurrentUser()

        ratingViewModel = ViewModelProvider(this)[RatingViewModel::class.java]
        ratingViewModel.isLoading.observe(this) { showLoading(it) }
        ratingViewModel.isError.observe(this) { showError(it) }
        ratingViewModel.isSuccess.observe(this) {
            finish()
        }
    }

    private fun getCurrentUser() {
        val user = Firebase.auth.currentUser
        if (user !== null) {
            user.getIdToken(false).addOnSuccessListener {
                binding.btnRatingSubmit.setOnClickListener { it1 ->
                    ratingViewModel.postFeedback(
                        it.token, "9999", // TODO get mentoring id
                        binding.rbRateStars.rating, binding.edRateFeedback.text.toString()
                    )
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(it1.windowToken, 0)
                }
            }
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
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
        finish()
    }
}