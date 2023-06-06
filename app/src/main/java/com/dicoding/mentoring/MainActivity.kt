package com.dicoding.mentoring

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dicoding.mentoring.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val navView: BottomNavigationView = binding.navView

        val user = Firebase.auth.currentUser
        user.let {
            user?.getIdToken(false)?.addOnSuccessListener {
                val claims = it.claims

                // Role validation
                val navGraphResId = if (claims["role"] == "mentor") {
                    R.navigation.navigation_mentor
                } else {
                    R.navigation.navigation_mentee
                }

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
                        R.id.navigation_profile
                    )
                )
                setupActionBarWithNavController(navController, appBarConfiguration)
                navView.setupWithNavController(navController)
            }

        }
        setContentView(binding.root)
    }

}
