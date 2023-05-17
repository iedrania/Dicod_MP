package com.dicoding.mentoring.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.mentoring.data.local.Mentor
import com.dicoding.mentoring.databinding.ItemMentorBinding
import com.dicoding.mentoring.ui.chat.ChatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class MentorsAdapter(
    private val user: FirebaseUser,
    private val db: FirebaseFirestore,
    private val listMentor: List<Mentor>
) : RecyclerView.Adapter<MentorsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMentorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mentor = listMentor[position]
        holder.binding.tvItemName.text = mentor.name
        holder.binding.tvItemBio.text = mentor.bio
        holder.binding.rbItemRating.rating = mentor.avgRating
        mentor.listInterest.forEach { holder.binding.cgItemInterests.addChip(it) }
        Glide.with(holder.itemView.context).load(mentor.photoUrl).into(holder.binding.ivItemPhoto)

        holder.itemView.setOnClickListener {
            val group = hashMapOf(
                "createdAt" to Timestamp.now(),
                "createdBy" to user.uid,
                "isPrivate" to true,
                "members" to arrayListOf(user.uid, mentor.id),
                "modifiedAt" to Timestamp.now(),
                "name" to mentor.name,
                "recentMessage" to hashMapOf(
                    "messageText" to null,
                    "sentAt" to null,
                    "sentBy" to null,
                )
            )

            var groupId = ""

            // TODO check if group already exist
            db.collection("groups").add(group).addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                groupId = documentReference.id
                // TODO if groupId == ""
            }.addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }

            // FIXME empty string added to groups even when groupId is not
            db.collection("users").document(user.uid)
                .update("groups", FieldValue.arrayUnion(groupId))
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
            db.collection("users").document(mentor.id)
                .update("groups", FieldValue.arrayUnion(groupId))
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }

            // TODO move fragment to chats
            val intent = Intent(holder.itemView.context, ChatActivity::class.java)
            intent.putExtra("extra_group", groupId)
            intent.putExtra("extra_title", mentor.id) // TODO based on role
            holder.itemView.context.startActivity(intent)
        }
    }

    private fun ChipGroup.addChip(label: String) {
        Chip(context).apply {
            text = label
            addView(this)
        }
    }

    override fun getItemCount() = listMentor.size

    class ViewHolder(var binding: ItemMentorBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val TAG = "MentorsAdapter"
    }
}