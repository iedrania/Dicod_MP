package com.dicoding.mentoring.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mentoring.MainActivity
import com.dicoding.mentoring.R
import com.dicoding.mentoring.databinding.ActivityLoginBinding
import com.dicoding.mentoring.helper.LoginPreferences
import com.dicoding.mentoring.helper.ViewModelFactory
import com.dicoding.mentoring.ui.UserViewModel
import com.dicoding.mentoring.ui.register.RegisterActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val pref = LoginPreferences.getInstance(dataStore)
        userViewModel = ViewModelProvider(
            this, ViewModelFactory(pref)
        )[UserViewModel::class.java]
        userViewModel.getLoginInfo().observe(this) { token ->
            if (token.isNotEmpty()) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        userViewModel.isLoading.observe(this) { showLoading(it) }
        userViewModel.isError.observe(this) { showError(it) }

        setLoginButtonEnable()
        binding.edLoginEmail.doOnTextChanged { _, _, _, _ ->
            setLoginButtonEnable()
        }
        binding.edLoginPassword.doOnTextChanged { _, _, _, _ ->
            setLoginButtonEnable()
        }
        binding.btnLoginSubmit.setOnClickListener {
            userViewModel.postLogin(
                binding.edLoginEmail.text.toString(), binding.edLoginPassword.text.toString()
            )
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
        binding.btnLoginRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showError(isError: Boolean) {
        if (isError) {
            Toast.makeText(
                this@LoginActivity, getString(R.string.login_failed), Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setLoginButtonEnable() {
        val emailResult = binding.edLoginEmail.text
        val passwordResult = binding.edLoginPassword.text
        binding.btnLoginSubmit.isEnabled = emailResult != null && emailResult.toString()
            .isNotBlank() && passwordResult != null && passwordResult.toString().isNotBlank()
    }
}
