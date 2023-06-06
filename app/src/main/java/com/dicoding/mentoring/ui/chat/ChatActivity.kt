package com.dicoding.mentoring.ui.chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mentoring.adapter.ChatAdapter
import com.dicoding.mentoring.data.local.Chat
import com.dicoding.mentoring.databinding.ActivityChatBinding
import com.dicoding.mentoring.ui.login.LoginActivity
import com.dicoding.mentoring.ui.timepicker.TimePickerActivity
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var registration: ListenerRegistration
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val groupId = intent.getStringExtra(EXTRA_GROUP)
        val pageTitle = intent.getStringExtra(EXTRA_TITLE)
        supportActionBar?.title = pageTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.ivChatCalendar.setOnClickListener {
            val intent = Intent(this@ChatActivity, TimePickerActivity::class.java)
            startActivity(intent)
        }

        if (groupId != null) {
            checkCurrentUser(groupId)
        } else {
            finish()
        }
    }

    private fun checkCurrentUser(groupId: String) {
        val user = Firebase.auth.currentUser
        if (user != null) {
            checkUserRole(user, groupId)
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun renderChatPage(user: FirebaseUser, userRole: String, groupId: String) {
        val db = Firebase.firestore

        binding.btnChatSend.setOnClickListener {
            if (binding.edChatInput.text.isNotBlank()) {
                val data = hashMapOf(
                    "messageText" to binding.edChatInput.text.toString(),
                    "sentBy" to user.uid,
                    "sentAt" to Timestamp.now(),
                    "imageUrl" to null,
                )

                db.collection("messages/$groupId/texts").add(data)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")

                        val recent = hashMapOf(
                            "messageText" to binding.edChatInput.text.toString(),
                            "sentAt" to Timestamp.now(),
                            "sentBy" to user.uid,
                        )

                        db.collection("groups").document(groupId).update("recentMessage", recent)
                            .addOnSuccessListener {
                                Log.d(TAG, "DocumentSnapshot successfully updated!")
                            }.addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }

                        binding.edChatInput.text.clear()
                    }.addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                    }
            }
        }

        registration = db.collection("messages/$groupId/texts").orderBy("sentAt")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                val chats = ArrayList<Chat>()

                for (doc in value!!) {
                    doc.toObject<Chat>().let {
                        chats.add(it)
                    }
                }

                Log.d(TAG, "Current chats for user: $chats")
                binding.rvChats.layoutManager = LinearLayoutManager(this)
                chatAdapter = ChatAdapter(chats, user.uid, userRole)
                binding.rvChats.adapter = chatAdapter
                scrollToBottom()
            }
    }

    private fun checkUserRole(user: FirebaseUser, groupId: String) {
        user.getIdToken(false).addOnSuccessListener {
            val claims = it.claims
            if (claims["role"] == "mentor") {
                renderChatMentorPage(user, groupId)
            } else {
                renderChatMenteePage(user, groupId)
            }
        }.addOnFailureListener { e ->
            Log.d(TAG, "get token failed with ", e)
        }
    }

    private fun renderChatMentorPage(user: FirebaseUser, groupId: String) {
        binding.ivChatCalendar.visibility = View.VISIBLE
        binding.ivChatCalendar.setOnClickListener {
            TODO("Intent to Session Page")
        }
        renderChatPage(user, groupId, "mentor")
    }

    private fun renderChatMenteePage(user: FirebaseUser, groupId: String) {
        binding.ivChatCalendar.visibility = View.GONE
        renderChatPage(user, groupId, "mentee")
    }

    private fun scrollToBottom() {
        val itemCount = chatAdapter.itemCount
        if (itemCount > 0) {
            binding.rvChats.scrollToPosition(itemCount - 1)
        }
    }

    override fun onDestroy() {
        registration.remove()
        super.onDestroy()
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