package com.dicoding.mentoring.ui.chat

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.mentoring.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val groupId = intent.getStringExtra(EXTRA_GROUP)
        val pageTitle = intent.getStringExtra(EXTRA_TITLE)
        supportActionBar?.title = pageTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // TODO if groupId null

        val db = Firebase.firestore
        // TODO if not exist add messages document with groupId as id
        // TODO if not exist add texts collection to messages

        db.collection("messages").document(groupId!!).collection("texts").orderBy("sentAt").get()
//            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
//            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    // TODO display chat
                }
            }.addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
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