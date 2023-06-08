package com.dicoding.mentoring.ui.message

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mentoring.adapter.MessagesAdapter
import com.dicoding.mentoring.databinding.FragmentMessageBinding
import com.dicoding.mentoring.ui.onboard.OnboardActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MessageFragment : Fragment() {

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!

    private lateinit var registration: ListenerRegistration

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        checkCurrentUser()
    }

    override fun onStop() {
        super.onStop()
        registration.remove() // Unregister the snapshot listener
    }

    private fun checkCurrentUser() {
        val user = Firebase.auth.currentUser
        if (user != null) {
            renderMessagesPage(user)
        } else {
            startActivity(Intent(requireActivity(), OnboardActivity::class.java))
            activity?.finish()
        }
    }

    private fun renderMessagesPage(user: FirebaseUser) {
        user.getIdToken(false).addOnSuccessListener {
            val claims = it.claims
            val role = if (claims["role"] == "mentor") "mentor" else "mentee"

            val db = Firebase.firestore
            registration = db.collection("groups").whereArrayContains("members", user.uid)
                .orderBy("recentMessage.sentAt", Query.Direction.DESCENDING)
                .addSnapshotListener { value, e ->
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e)
                        return@addSnapshotListener
                    }

                    val messages = ArrayList<Pair<String, Map<String, Any>>>()

                    for (doc in value!!) {
                        val (id, message) = doc.id to doc.data
                        val tuple = id to message
                        messages.add(tuple)
                    }
                    binding.rvMessages.layoutManager = LinearLayoutManager(requireContext())
                    binding.rvMessages.adapter = MessagesAdapter(role, messages)
                }
        }.addOnFailureListener { e ->
            Log.d(TAG, "get token failed with ", e)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "MessageFragment"
    }
}