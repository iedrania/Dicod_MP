package com.dicoding.mentoring.ui.onboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.mentoring.R
import com.dicoding.mentoring.databinding.ActivityLoginBinding
import com.dicoding.mentoring.databinding.ActivityOnboardBinding
import com.dicoding.mentoring.ui.registerMentor.RegisterMentorActivity
import com.dicoding.mentoring.ui.login.LoginActivity
import com.dicoding.mentoring.ui.register.RegisterActivity

class OnboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnOnboardLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnOnboardRegisterMentee.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.btnOnboardRegisterMentor.setOnClickListener {
            startActivity(Intent(this, RegisterMentorActivity::class.java))
        }
    }
}