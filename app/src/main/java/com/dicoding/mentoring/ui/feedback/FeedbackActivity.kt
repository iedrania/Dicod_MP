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
import com.dicoding.mentoring.ui.onboard.OnboardActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FeedbackActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedbackBinding
    private lateinit var feedbackViewModel: FeedbackViewModel

    private lateinit var groupId: String
    private lateinit var textId: String
    private var mentoringId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        groupId = intent.getStringExtra(EXTRA_GROUP).toString()
        textId = intent.getStringExtra(EXTRA_TEXT).toString()
        mentoringId = intent.getLongExtra(EXTRA_MENTORING, 0)
        if (mentoringId == 0.toLong()) finish() else getCurrentUser()

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
                        it.token,
                        mentoringId,
                        binding.edFeedbackFeedback.text.toString(),
                        binding.rbFeedbackStars.rating,
                        groupId,
                        textId
                    )
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(it1.windowToken, 0)
                }
            }
        } else {
            startActivity(Intent(this, OnboardActivity::class.java))
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
    }

    companion object {
        private const val EXTRA_MENTORING = "extra_mentoring"
        private const val EXTRA_GROUP = "extra_group"
        private const val EXTRA_TEXT = "extra_text"
    }
}