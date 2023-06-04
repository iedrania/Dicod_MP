package com.dicoding.mentoring.ui.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mentoring.R
import com.dicoding.mentoring.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.bumptech.glide.Glide
import com.dicoding.mentoring.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var profileViewModel: ProfileViewModel

    companion object {
        const val REQUEST_CODE_EDIT_PROFILE = 222
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        //Obtain Firebase user token
        val user = FirebaseAuth.getInstance().currentUser
        user?.getIdToken(false)
        val token = user?.getIdToken(false)?.result?.token

        //Obtain ViewModel
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        profileViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        //Get User Profile Data
        if (token != null) {
            getUserDataProfile(token)
        }

        //action when click Edit Profile Button
        binding.btnEditProfile.setOnClickListener {
            val intent = Intent(activity, ProfileActivity::class.java)
            activity?.startActivityForResult(intent, 256)
        }

        //action when click Edit Interest Button
        binding.btnEditInterest.setOnClickListener {
            val intent = Intent(activity, ListInterestActivity::class.java)
            activity?.startActivity(intent)
        }

        binding.btnEditDays.setOnClickListener {
            val intent = Intent(activity, ListDayActivity::class.java)
            activity?.startActivity(intent)
        }

        binding.btnProfileLogout.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            activity?.finish()
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            if (token != null) {
                getUserDataProfile(token)
            }
            binding.swipeRefreshLayout.isRefreshing = false
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_profile, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                Toast.makeText(context, "Action logo berhasil", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onContextItemSelected(item)
    }

    @Suppress
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_EDIT_PROFILE && resultCode == Activity.RESULT_OK && data != null) {
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.detach(this)
            fragmentTransaction?.attach(this)
            fragmentTransaction?.commit()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getUserDataProfile(token: String) {
        Log.d(
            "ProfileFragment",
            "getUserDataProfile : token user pada profile fragment adalah : $token"
        )
        profileViewModel.getProfile(token)
        profileViewModel.userProfile.observe(viewLifecycleOwner) {

            if (it.profile_picture_url.isNullOrEmpty()) {
                Glide.with(this)
                    .load("https://static.vecteezy.com/system/resources/thumbnails/003/337/584/small/default-avatar-photo-placeholder-profile-icon-vector.jpg")
                    .into(binding.imgProfilePic)
            } else {
                Glide.with(this)
                    .load(it.profile_picture_url)
                    .into(binding.imgProfilePic)
            }
            binding.editTextFullname.text = it.name
            binding.editTextPhone.text = it.phone
            binding.editTextEmail.text = it.email
            binding.editTextBiography.text = it.bio

            if (it.is_mentor) {
                binding.radioRole.text = "Mentor"
            } else {
                binding.radioRole.text = "Mentee"
            }
            if (it.gender_id == 1) {
                binding.radioGender.text = "Male"
            } else {
                binding.radioGender.text = "Female"
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}

