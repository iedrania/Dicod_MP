package com.dicoding.mentoring

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.mentoring.ui.login.LoginActivity
import com.dicoding.mentoring.ui.rating.RatingActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivityOld : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.old_activity_main)

        auth = Firebase.auth

        val btnMainMain = findViewById<Button>(R.id.btn_main_main)
        btnMainMain.setOnClickListener {
            startActivity(Intent(this@MainActivityOld, MainActivity::class.java))
        }

        val btnMainLogout = findViewById<Button>(R.id.btn_main_logout)
        btnMainLogout.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(this@MainActivityOld, LoginActivity::class.java))
            finish()
        }

        val btnMainRating = findViewById<Button>(R.id.btn_main_rating)
        btnMainRating.setOnClickListener {
            startActivity(Intent(this@MainActivityOld, RatingActivity::class.java))
        }
    }
}