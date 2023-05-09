package com.dicoding.mentoring

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mentoring.helper.LoginPreferences
import com.dicoding.mentoring.helper.ViewModelFactory
import com.dicoding.mentoring.ui.UserViewModel
import com.dicoding.mentoring.ui.login.LoginActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pref = LoginPreferences.getInstance(dataStore)
        val userViewModel = ViewModelProvider(
            this, ViewModelFactory(pref)
        )[UserViewModel::class.java]
        userViewModel.getLoginInfo().observe(this) { token ->
            if (token.isEmpty()) {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        val btnMainLogout = findViewById<Button>(R.id.btn_main_logout)
        btnMainLogout.setOnClickListener {
            userViewModel.saveLoginInfo("")
        }
    }
}