package com.dicoding.mentoring.ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.mentoring.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setTitle("Profile")

        binding.editInterest.setOnClickListener{
            val intent = Intent(this@ProfileActivity,ListInterestActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getEdTextData(){
        val getEmail = binding.editTextEmail.text.toString()
        val getUsername = binding.editTextUsername.text.toString()
        val getFullname = binding.editTextFullname.text.toString()
    }
}