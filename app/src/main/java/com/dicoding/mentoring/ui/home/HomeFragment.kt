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
import com.google.firebase.auth.FirebaseUser
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

        checkCurrentUser()

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        homeViewModel.isLoading.observe(viewLifecycleOwner) { showLoading(it) }
        homeViewModel.isError.observe(viewLifecycleOwner) { showError(it) }
        homeViewModel.listMentor.observe(viewLifecycleOwner) {
            val db = Firebase.firestore
            val user = Firebase.auth.currentUser

            binding.rvMentors.layoutManager = LinearLayoutManager(requireContext())
            if (user != null) {
                binding.rvMentors.adapter = MentorsAdapter(user, db, it)
            }
        }
    }

    private fun checkCurrentUser() {
        val user = Firebase.auth.currentUser
        if (user != null) {
            renderHomePage(user)
        } else {
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            activity?.finish()
        }
    }

    private fun renderHomePage(user: FirebaseUser) {
        user.let {
            "Halo, ${user.displayName}".also { binding.tvMainWelcome.text = it }

            user.getIdToken(false).addOnSuccessListener {
                val claims = it.claims
                if (claims["role"] == "mentor") {
                    binding.tvMainIntro.text = getString(R.string.home_intro_mentor)
                } else {
                    binding.tvMainIntro.text = getString(R.string.home_intro_mentee)
                }

                homeViewModel.findMentors(it.token)
            }.addOnFailureListener { e ->
                Log.d(TAG, "get token failed with ", e)
            }
        }
    }

    private fun getUserGroups() {
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser
        user?.uid?.let {
            db.collection("users").document(it).get().addOnSuccessListener { document ->
                if (document != null) {
                    if (document.data?.get("groups") != null) {
                        Log.d(TAG, "User's groups field exists, process OK")
                    } else {
                        Log.d(TAG, "User's groups field does not exist")
                    }
                } else {
                    Log.d(TAG, "User's document does not exist")
                }
            }.addOnFailureListener { exception ->
                Log.d(TAG, "get user's document failed with ", exception)
            }
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