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
import com.dicoding.mentoring.adapter.MentorsAdapter
import com.dicoding.mentoring.databinding.FragmentHomeBinding
import com.dicoding.mentoring.ui.login.LoginActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getCurrentUser()

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        homeViewModel.isLoading.observe(viewLifecycleOwner) { showLoading(it) }
        homeViewModel.isError.observe(viewLifecycleOwner) { showError(it) }
    }

    private fun getCurrentUser() {
        val user = Firebase.auth.currentUser
        if (user !== null) {
            "Halo, ${user.displayName}".also { binding.tvMainWelcome.text = it }
            if (user.getIdToken(false).result.claims["role"] == "mentor") {
                binding.tvMainIntro.text = getString(R.string.home_intro_mentor)
            } else {
                binding.tvMainIntro.text = getString(R.string.home_intro_mentee)
            }

            user.getIdToken(false).addOnSuccessListener { homeViewModel.findMentors(it.token) }

            val db = Firebase.firestore
            db.collection("users").document(user.uid).get().addOnSuccessListener { document ->
                if (document != null) {
                    homeViewModel.listMentor.observe(viewLifecycleOwner) {
                        if (document.data?.get("groups") !== null) {
                            binding.rvMentors.layoutManager = LinearLayoutManager(requireContext())
                            binding.rvMentors.adapter = MentorsAdapter(user, db, it)
                        } else {
                            Log.d(TAG, "User's groups field does not exist")
                        }
                    }
                } else {
                    Log.d(TAG, "User's document does not exist")
                }
            }.addOnFailureListener { exception ->
                Log.d(TAG, "get user's document failed with ", exception)
            }
        } else {
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            activity?.finish()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(isError: Boolean) {
        binding.tvMainError.visibility = if (isError) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "HomeFragment"
    }
}