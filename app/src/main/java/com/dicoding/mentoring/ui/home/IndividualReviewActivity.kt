package com.dicoding.mentoring.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mentoring.adapter.ReviewAdapter
import com.dicoding.mentoring.databinding.ActivityIndividualReviewBinding
import com.dicoding.mentoring.ui.onboard.OnboardActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class IndividualReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIndividualReviewBinding
    private lateinit var homeMentorViewModel: HomeMentorViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIndividualReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Ulasan Individu"

        checkCurrentUser()

        homeMentorViewModel = ViewModelProvider(this)[HomeMentorViewModel::class.java]
        homeMentorViewModel.isLoading.observe(this) { showLoading(it) }
        homeMentorViewModel.dashboardMentorData.observe(this) {


            val adapter = ReviewAdapter(it.feedbacks)
            binding.rvFeedback.layoutManager = LinearLayoutManager(this)
            binding.rvFeedback.adapter = adapter
        }
    }


    private fun checkCurrentUser() {
        val user = Firebase.auth.currentUser
        if (user != null) {
            renderIndividualReviewPage(user)
        } else {
            startActivity(Intent(this@IndividualReviewActivity, OnboardActivity::class.java))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun renderIndividualReviewPage(user: FirebaseUser) {
        user.let {
            user.getIdToken(false).addOnSuccessListener {
                homeMentorViewModel.getProfile(it.token)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


}
