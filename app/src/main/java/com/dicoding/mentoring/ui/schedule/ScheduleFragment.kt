package com.dicoding.mentoring.ui.schedule

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
import com.dicoding.mentoring.adapter.ScheduleAdapter
import com.dicoding.mentoring.databinding.FragmentScheduleBinding
import com.dicoding.mentoring.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.Date

class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private lateinit var scheduleViewModel: ScheduleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkCurrentUser()

        scheduleViewModel = ViewModelProvider(this)[ScheduleViewModel::class.java]
        scheduleViewModel.isLoading.observe(viewLifecycleOwner) { showLoading(it) }
        scheduleViewModel.isError.observe(viewLifecycleOwner) { showError(it) }
        scheduleViewModel.listGroupedSchedule.observe(viewLifecycleOwner) {
            binding.rvSchedule.layoutManager = LinearLayoutManager(requireContext())
            binding.rvSchedule.adapter = ScheduleAdapter(it)
        }
    }

    private fun checkCurrentUser() {
        val user = Firebase.auth.currentUser
        if (user != null) {
            renderSchedulePage(user)
        } else {
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            activity?.finish()
        }
    }

    private fun renderSchedulePage(user: FirebaseUser) {
        val currentDate = Date()
        user.getIdToken(false).addOnSuccessListener {
            val claims = it.claims
            val role = if (claims["role"] == "mentor") "mentor" else "mentee"

            scheduleViewModel.getSchedule(
                it.token, currentDate.toString(), role
            )
        }.addOnFailureListener { e ->
            Log.d(TAG, "get token failed with ", e)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(isError: Boolean) {
        if (isError) {
            Toast.makeText(
                requireContext(), getString(R.string.get_schedule_failed), Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "ScheduleFragment"
    }
}