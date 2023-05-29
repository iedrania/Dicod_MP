package com.dicoding.mentoring.ui.feedback

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mentoring.R
import com.dicoding.mentoring.databinding.ActivityFeedbackBinding
import com.dicoding.mentoring.ui.login.LoginActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FeedbackActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedbackBinding
    private lateinit var feedbackViewModel: FeedbackViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        getCurrentUser()

        feedbackViewModel = ViewModelProvider(this)[FeedbackViewModel::class.java]
        feedbackViewModel.isLoading.observe(this) { showLoading(it) }
        feedbackViewModel.isError.observe(this) { showError(it) }
        feedbackViewModel.isSuccess.observe(this) {
            finish()
        }
    }

    private fun getCurrentUser() {
        val user = Firebase.auth.currentUser
        if (user !== null) {
            user.getIdToken(false).addOnSuccessListener {
                binding.btnFeedbackSubmit.setOnClickListener { it1 ->
                    feedbackViewModel.postFeedback(
                        it.token, "9999", // TODO get mentoring id
                        binding.rbFeedbackStars.rating, binding.edFeedbackFeedback.text.toString()
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
                this@FeedbackActivity, getString(R.string.feedback_failed), Toast.LENGTH_SHORT
            ).show()
        }
        finish()
    }
}