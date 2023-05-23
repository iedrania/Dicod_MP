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
import com.dicoding.mentoring.ui.login.LoginActivity
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getCurrentUser()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getCurrentUser() {
        val user = Firebase.auth.currentUser
        if (user !== null) {
            val userRole = user.getIdToken(false).result.claims["role"] as String

            // get list of all groups for this user
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
                    binding.rvMessages.adapter = MessagesAdapter(userRole, messages)
                }
        } else {
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            activity?.finish()
        }
    }

    companion object {
        private const val TAG = "MessageFragment"
    }
}