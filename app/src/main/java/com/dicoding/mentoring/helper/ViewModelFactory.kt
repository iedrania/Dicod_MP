package com.dicoding.mentoring.helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mentoring.ui.UserViewModel
import com.dicoding.mentoring.ui.rating.RatingViewModel
import com.google.firebase.auth.FirebaseAuth

class ViewModelFactory(private val auth: FirebaseAuth) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return UserViewModel(auth) as T
        } else if (modelClass.isAssignableFrom(RatingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return RatingViewModel(auth) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}