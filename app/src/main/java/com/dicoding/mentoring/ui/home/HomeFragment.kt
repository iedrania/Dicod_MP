package com.dicoding.mentoring.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.chipChooseDays.setOnClickListener {
            val intent = Intent(activity, ListDayActivity::class.java)
            activity?.startActivity(intent)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getCurrentUser()

        homeViewModel.isLoading.observe(viewLifecycleOwner) { showLoading(it) }
        homeViewModel.isError.observe(viewLifecycleOwner) { showError(it) }
    }

    private fun getCurrentUser() {
        val user = Firebase.auth.currentUser
        if (user !== null) {
            // get list of all mentors for adapter
            user.getIdToken(false).addOnSuccessListener { homeViewModel.findMentors(it.token) }

            // get groups of user: list of mentors already in this user's groups
            // to make a new collection if not exist yet for messages

            val db = Firebase.firestore
            val docRef = db.collection("users").document(user.uid)
            docRef.get().addOnSuccessListener { document ->
                if (document != null) {
                    homeViewModel.listMentor.observe(viewLifecycleOwner) {
                        if (document.data?.get("groups") !== null) {
                            binding.rvMentors.layoutManager = LinearLayoutManager(requireContext())
                            binding.rvMentors.adapter = MentorsAdapter(user, db, it.mentors)
                        } else {
                            Log.d(TAG, "make sure user is in firestore")
                        }
                    }
                } else {
                    Log.d(TAG, "No such document")
                }
            }.addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
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
        if (isError) {
            Toast.makeText(
                requireContext(), getString(R.string.get_mentors_failed), Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "HomeFragment"
    }
}