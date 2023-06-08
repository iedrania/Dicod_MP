package com.dicoding.mentoring.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mentoring.R
import com.dicoding.mentoring.adapter.ReviewAdapter
import com.dicoding.mentoring.databinding.FragmentHomeMentorBinding
import com.dicoding.mentoring.ui.onboard.OnboardActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeMentorFragment : Fragment() {

    private lateinit var homeMentorViewModel: HomeMentorViewModel
    private lateinit var binding: FragmentHomeMentorBinding

    companion object {
        const val TAG = "HomeMentorFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeMentorBinding.inflate(inflater, container, false)

        binding.cardIndividualFeedback.setOnClickListener {
            val intent = Intent(activity, IndividualReviewActivity::class.java)
            activity?.startActivity(intent)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkCurrentUser()

        homeMentorViewModel = ViewModelProvider(this)[HomeMentorViewModel::class.java]
        homeMentorViewModel.isLoading.observe(viewLifecycleOwner){showLoading(it)}
        homeMentorViewModel.dashboardMentorData.observe(viewLifecycleOwner) {

            binding.ratingBar.rating = it.averageRating.toFloat()
            binding.tvAverageRating.text =
                getString(R.string.rating_value, it.averageRating.toString())
            binding.tvPercentagePositive.text =
                getString(R.string.sentiment_value, it.sentiment.positive.toString())
            binding.tvPercentageNegative.text =
                getString(R.string.sentiment_value, it.sentiment.negative.toString())
            binding.tvMenteeFeedbackSummarizer.text = it.feedbackSummary
        }
    }

    private fun checkCurrentUser() {
        val user = Firebase.auth.currentUser
        if (user != null) {
            renderHomePage(user)
        } else {
            startActivity(Intent(requireActivity(), OnboardActivity::class.java))
            activity?.finish()
        }
    }

    private fun renderHomePage(user: FirebaseUser) {
        user.let {
            "Halo, ${user.displayName}".also { binding.tvMainWelcome.text = it }

            user.getIdToken(false).addOnSuccessListener {
                binding.tvMainIntro.text = getString(R.string.home_intro_mentor)
                homeMentorViewModel.getProfile(it.token)
            }

        }.addOnFailureListener { e ->
            Log.d(TAG, "get token failed with ", e)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}

