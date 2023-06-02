package com.dicoding.mentoring.ui.authmentor

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.dicoding.mentoring.MainActivity
import com.dicoding.mentoring.R
import com.dicoding.mentoring.databinding.ActivityLoginMentorBinding
import com.dicoding.mentoring.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginMentorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginMentorBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginMentorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        auth = Firebase.auth

        setLoginButtonEnable()
        binding.edLoginEmail.doOnTextChanged { _, _, _, _ ->
            setLoginButtonEnable()
        }
        binding.edLoginPassword.doOnTextChanged { _, _, _, _ ->
            setLoginButtonEnable()
        }

        binding.btnLoginSubmit.setOnClickListener {
            signIn(binding.edLoginEmail.text.toString(), binding.edLoginPassword.text.toString())
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }

        binding.btnLoginRegister.setOnClickListener {
            startActivity(Intent(this, RegisterMentorActivity::class.java))
            finish()
        }

        binding.btnLoginAsMentee.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "signInWithEmail:success")
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Log.w(TAG, "signInWithEmail:failure", task.exception)
                Toast.makeText(
                    baseContext, R.string.login_failed, Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }

    private fun setLoginButtonEnable() {
        val emailResult = binding.edLoginEmail.text
        val passwordResult = binding.edLoginPassword.text
        binding.btnLoginSubmit.isEnabled = emailResult != null && emailResult.toString()
            .isNotBlank() && passwordResult != null && passwordResult.toString().isNotBlank()
    }

    companion object {
        private const val TAG = "LoginMentorActivity"
    }
}
