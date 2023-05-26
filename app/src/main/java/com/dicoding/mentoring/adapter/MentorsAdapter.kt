package com.dicoding.mentoring.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.mentoring.data.local.Mentors
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
    private val mentorsResponse: List<Mentors>
) : RecyclerView.Adapter<MentorsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMentorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mentor = mentorsResponse[position]

        holder.binding.tvItemName.text = mentor.User.name
        holder.binding.tvItemBio.text = mentor.User.bio
        holder.binding.rbItemRating.rating = mentor.averageRating ?: 0.toFloat()

        val listInterest = ArrayList<String>()
        if (mentor.User.isPathAndroid == true) listInterest.add("Android")
        if (mentor.User.isPathWeb == true) listInterest.add("Web")
        if (mentor.User.isPathIos == true) listInterest.add("iOS")
        if (mentor.User.isPathMl == true) listInterest.add("ML")
        if (mentor.User.isPathFlutter == true) listInterest.add("Flutter")
        if (mentor.User.isPathFe == true) listInterest.add("FE")
        if (mentor.User.isPathBe == true) listInterest.add("BE")
        if (mentor.User.isPathReact == true) listInterest.add("React")
        if (mentor.User.isPathDevops == true) listInterest.add("DevOps")
        if (mentor.User.isPathGcp == true) listInterest.add("GCP")
        listInterest.forEach { holder.binding.cgItemInterests.addChip(it) }

        val listDays = ArrayList<String>()
        if (mentor.User.is_monday_available == true) listDays.add("Senin")
        if (mentor.User.is_tuesday_available == true) listDays.add("Selasa")
        if (mentor.User.is_wednesday_available == true) listDays.add("Rabu")
        if (mentor.User.is_thursday_available == true) listDays.add("Kamis")
        if (mentor.User.is_friday_available == true) listDays.add("Jumat")
        if (mentor.User.is_saturday_available == true) listDays.add("Sabtu")
        if (mentor.User.is_sunday_available == true) listDays.add("Minggu")
        listDays.forEach { holder.binding.cgItemDays.addChip(it) }

        Glide.with(holder.itemView.context).load(mentor.User.profile_picture_url)
            .into(holder.binding.ivItemPhoto)

        // onclick: redirect to messages
        holder.itemView.setOnClickListener {
            var groupId: String? = null
            var groupData: Map<String, Any>? = null
            val listGroup: MutableList<Pair<String, Map<String, Any>>> = mutableListOf()

            db.collection("groups").whereArrayContains("members", user.uid).get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        document.let {
                            val (id, data) = document.id to document.data
                            val tuple = id to data
                            listGroup.add(tuple)
                        }
                    }
                    Log.d(TAG, "groups for this user $listGroup")

                    // get groups for this user that contains this mentor
                    for ((id, data) in listGroup) {
                        val members = data["members"] as ArrayList<String>
                        if (members.contains(mentor.User.id.toString())) {
                            groupId = id
                            groupData = data
                            break
                        }
                    }
                    Log.d(TAG, "groups for this user and mentor ${groupData.toString()}")

                    if (groupId == null && groupData == null) {
                        // create new group if not exist
                        val group = hashMapOf(
                            "createdAt" to Timestamp.now(),
                            "createdBy" to user.uid,
                            "displayName" to hashMapOf(
                                "group" to "",
                                "mentor" to mentor.User.name,
                                "mentee" to user.displayName,
                            ),
                            "isPrivate" to true,
                            "members" to hashMapOf(
                                user.uid to true,
                                mentor.User.id.toString() to true,
                            ),
                            "modifiedAt" to Timestamp.now(),
                            "photoUrl" to hashMapOf(
                                "mentee" to user.photoUrl.toString(),
                                "mentor" to mentor.User.profile_picture_url.toString(),
                            ),
                            "recentMessage" to hashMapOf(
                                "messageText" to null,
                                "senderName" to null,
                                "senderPhotoUrl" to null,
                                "sentAt" to null,
                                "sentBy" to null,
                            )
                        )

                        db.collection("groups").add(group)
                            .addOnSuccessListener { documentReference ->
                                groupId = documentReference.id
                                Log.d(
                                    TAG, "DocumentSnapshot written with ID: ${documentReference.id}"
                                )

                                // update user
                                db.collection("users").document(user.uid)
                                    .update("groups", FieldValue.arrayUnion(groupId))
                                    .addOnSuccessListener {
                                        Log.d(TAG, "group added to user")

                                        // update mentor
                                        db.collection("users").document(mentor.User.id.toString())
                                            .update("groups", FieldValue.arrayUnion(groupId))
                                            .addOnSuccessListener {
                                                Log.d(TAG, "group added to mentor")
                                            }.addOnFailureListener { e ->
                                                Log.w(TAG, "Error updating mentor groups", e)
                                            }

                                        // open chat
                                        openChatActivity(
                                            holder.itemView.context,
                                            documentReference.id,
                                            group["displayName"] as Map<String, String>
                                        )
                                    }.addOnFailureListener { e ->
                                        Log.w(TAG, "Error updating user groups", e)
                                    }
                            }.addOnFailureListener { e ->
                                Log.w(TAG, "Error adding group document", e)
                            }
                    } else {
                        // open old group if exist
                        openChatActivity(
                            holder.itemView.context,
                            groupId!!,
                            groupData!!["displayName"] as Map<String, String>
                        )
                    }
                }.addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting groups documents: ", exception)
                }
        }
    }

    private fun openChatActivity(
        context: Context, groupId: String, mapDisplayName: Map<String, String>
    ) {
        if (groupId.isNotEmpty()) {
            // TODO move activity fragment to messages
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("extra_group", groupId)
            if (user.getIdToken(false).result.claims["mentor"] as Boolean) {
                intent.putExtra("extra_title", mapDisplayName["mentee"])
            } else {
                intent.putExtra("extra_title", mapDisplayName["mentor"])
            }
            context.startActivity(intent)
        } else {
            Log.d(TAG, "groupId empty")
        }
    }

    private fun ChipGroup.addChip(label: String) {
        Chip(context).apply {
            text = label
            addView(this)
        }
    }

    override fun getItemCount() = mentorsResponse.size

    class ViewHolder(var binding: ItemMentorBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val TAG = "MentorsAdapter"
    }
}