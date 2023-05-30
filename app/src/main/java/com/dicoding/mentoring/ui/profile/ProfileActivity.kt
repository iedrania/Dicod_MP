package com.dicoding.mentoring.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mentoring.databinding.ActivityProfileBinding

import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity(){

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Obtain Firebase user token
        val user = FirebaseAuth.getInstance().currentUser
        user?.getIdToken(false)
        // get list of all mentors for adapter
        val token = user?.getIdToken(false)?.result?.token

        //Obtain ViewModel
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        if (token != null) {
            getUserDataProfile(token)
        }

        //Update profile when save button clicked
        binding.btnSave.setOnClickListener {
            if (token != null) {
                updateUserDataProfile(token)
            }
            finish()
        }

    }


    private fun getUserDataProfile(token: String) {
        Log.d(
            "ProfileFragment",
            "getUserDataProfile : token user pada profile fragment adalah : $token"
        )
        profileViewModel.getProfile(token)
        profileViewModel.userProfile.observe(this) {
            binding.editTextFullname.setText(it.name)
            binding.editTextPhone.setText(it.phone)
            binding.editTextEmail.setText(it.email)
            binding.editTextBiography.setText(it.bio)
            if (it.roleID == 2) {
                binding.radioMentor.isChecked = true
            } else {
                binding.radioMentee.isChecked = true
            }
            if (it.genderID == 1) {
                binding.radioMale.isChecked = true
            } else {
                binding.radioFemale.isChecked = true
            }

        }
    }


    private fun updateUserDataProfile(token: String) {
        var gender_id: Int? = null
        profileViewModel.userProfile.observe(this) {

            if (binding.radioMale.isChecked) {
                it.genderID = 1
            } else if (binding.radioFemale.isChecked) {
                it.genderID = 2
            }
            gender_id = it.genderID
            println(gender_id)

        }
        profileViewModel.updateProfile(
            token,
            binding.editTextFullname.text.toString(),
            gender_id,
            binding.editTextPhone.text.toString(),
            binding.editTextBiography.text.toString(),
            binding.editTextEmail.text.toString()
        )
    }


}