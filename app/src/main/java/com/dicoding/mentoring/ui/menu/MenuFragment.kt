package com.dicoding.mentoring.ui.menu

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dicoding.mentoring.databinding.FragmentMenuBinding
import com.dicoding.mentoring.ui.login.LoginActivity
import com.dicoding.mentoring.ui.profile.EditProfileActivity
import com.dicoding.mentoring.ui.profile.ListDayActivity
import com.dicoding.mentoring.ui.profile.ListInterestActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MenuFragment : Fragment() {

    private lateinit var binding: FragmentMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBinding.inflate(inflater, container, false)


        binding.cardEditDays.setOnClickListener {
            val intent = Intent(activity, ListDayActivity::class.java)
            activity?.startActivity(intent)
        }

        binding.cardProfile.setOnClickListener {
            val intent = Intent(activity, EditProfileActivity::class.java)
            activity?.startActivity(intent)
        }

        binding.cardInterest.setOnClickListener {
            val intent = Intent(activity, ListInterestActivity::class.java)
            activity?.startActivity(intent)
        }

        binding.cardLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Apakah kamu yakin ingin logout dari akunmu?")
                .setPositiveButton("Ya") { _, _ ->
                    Firebase.auth.signOut()
                    startActivity(Intent(requireActivity(), LoginActivity::class.java))
                    requireActivity().finish()
                }
                .setNegativeButton("Tidak", null)
                .show()
        }
        return binding.root
    }
}
