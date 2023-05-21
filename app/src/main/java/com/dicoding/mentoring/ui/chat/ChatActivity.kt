package com.dicoding.mentoring.ui.chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mentoring.adapter.ChatAdapter
import com.dicoding.mentoring.data.local.Chat
import com.dicoding.mentoring.databinding.ActivityChatBinding
import com.dicoding.mentoring.ui.login.LoginActivity
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var registration: ListenerRegistration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val groupId = intent.getStringExtra(EXTRA_GROUP)
        val pageTitle = intent.getStringExtra(EXTRA_TITLE)
        supportActionBar?.title = pageTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (groupId != null) getCurrentUser(groupId) else finish()
    }

    private fun getCurrentUser(groupId: String) {
        val user = Firebase.auth.currentUser
        if (user !== null) {
            val db = Firebase.firestore

            binding.btnChatSend.setOnClickListener {
                if (binding.edChatInput.text.isNotBlank()) {
                    val data = hashMapOf(
                        "messageText" to binding.edChatInput.text.toString(),
                        "sentBy" to user.uid,
                        "sentAt" to Timestamp.now(),
                        "imageUrl" to null, // TODO image chat
                    )

                    db.collection("messages/${groupId}/texts").add(data)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                        }.addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }

                    val recent = hashMapOf(
                        "messageText" to binding.edChatInput.text.toString(),
                        "sentAt" to Timestamp.now(),
                        "sentBy" to user.uid,
                    )

                    db.collection("groups").document(groupId).update("recentMessage", recent)
                        .addOnSuccessListener {
                            Log.d(
                                TAG, "DocumentSnapshot successfully updated!"
                            )
                        }.addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }

                    binding.edChatInput.text.clear()
                }
            }

            val chats = ArrayList<Chat>()
            registration = db.collection("messages/${groupId}/texts").orderBy("sentAt")
                .addSnapshotListener { value, e ->
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e)
                        return@addSnapshotListener
                    }

                    chats.clear()

                    for (doc in value!!) {
                        doc.toObject<Chat>().let {
                            chats.add(it)
                        }
                    }
                    Log.d(TAG, "Current chats for user: $chats")
                    binding.rvChats.layoutManager = LinearLayoutManager(this)
                    binding.rvChats.adapter = ChatAdapter(chats, user.uid)
                }
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun finish() {
        registration.remove()
        super.finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    companion object {
        private const val EXTRA_GROUP = "extra_group"
        private const val EXTRA_TITLE = "extra_title"
        private const val TAG = "ChatActivity"
    }
}