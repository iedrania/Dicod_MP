package com.dicoding.mentoring

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dicoding.mentoring.databinding.ActivityMainBinding
import com.dicoding.mentoring.ui.onboard.OnboardActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        auth = Firebase.auth
        authStateListener = FirebaseAuth.AuthStateListener { auth ->
            val user = auth.currentUser
            if (user == null) {
                startActivity(Intent(this, OnboardActivity::class.java))
                finish()
            } else {
                user.getIdToken(false).addOnSuccessListener {
                    val claims = it.claims

                    val navGraphResId = if (claims["role"] == "mentor") {
                        R.navigation.navigation_mentor
                    } else {
                        R.navigation.navigation_mentee
                    }

                    val navView: BottomNavigationView = binding.navView

                    val navHostFragment =
                        supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_bottom_navigation) as NavHostFragment
                    val navController = navHostFragment.navController
                    val inflater = navController.navInflater
                    val graph = inflater.inflate(navGraphResId)

                    navController.graph = graph

                    // Passing each menu ID as a set of Ids because each
                    // menu should be considered as top level destinations.
                    val appBarConfiguration = AppBarConfiguration(
                        setOf(
                            R.id.navigation_home,
                            R.id.navigation_message,
                            R.id.navigation_schedule,
                            R.id.navigation_menu
                        )
                    )
                    setupActionBarWithNavController(navController, appBarConfiguration)
                    navView.setupWithNavController(navController)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()
        auth.removeAuthStateListener(authStateListener)
    }
}
