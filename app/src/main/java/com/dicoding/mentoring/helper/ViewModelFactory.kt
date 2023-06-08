package com.dicoding.mentoring.helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mentoring.ui.registerMentor.RegisterMentorViewModel
import com.dicoding.mentoring.ui.register.RegisterViewModel
import com.google.firebase.auth.FirebaseAuth

class ViewModelFactory(private val auth: FirebaseAuth) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(auth) as T
        } else if (modelClass.isAssignableFrom(RegisterMentorViewModel::class.java)) {
            return RegisterMentorViewModel(auth) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}