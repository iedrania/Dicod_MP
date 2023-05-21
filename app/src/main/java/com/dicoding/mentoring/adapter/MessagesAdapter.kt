package com.dicoding.mentoring.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.mentoring.databinding.ItemMessagesBinding
import com.dicoding.mentoring.ui.chat.ChatActivity
import com.google.firebase.auth.FirebaseUser

class MessagesAdapter(
    private val user: FirebaseUser,
    private val listMessage: MutableList<Pair<String, Map<String, Any>>>
) : RecyclerView.Adapter<MessagesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemMessagesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = listMessage[position]

//      TODO val mentee = user.getIdToken(false).result.claims["mentee"] as Boolean
        val mentee = true
        val displayName = message.second["displayName"] as HashMap<String, String>
        val chatTitle: String?
        val photoUrl = message.second["photoUrl"] as HashMap<String, String>
        if (mentee) {
            chatTitle = displayName["mentor"]
            holder.binding.tvItemMessagesName.text = chatTitle
            Glide.with(holder.itemView.context).load(photoUrl["mentor"])
                .into(holder.binding.ivItemMessagesPhoto)
        } else {
            chatTitle = displayName["mentee"]
            holder.binding.tvItemMessagesName.text = chatTitle
            Glide.with(holder.itemView.context).load(photoUrl["mentee"])
                .into(holder.binding.ivItemMessagesPhoto)
        }

        val recentMessage = message.second["recentMessage"] as HashMap<String, Any>
        holder.binding.tvItemMessagesPreview.text = recentMessage["messageText"] as CharSequence?

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ChatActivity::class.java)
            intent.putExtra("extra_group", message.first)
            intent.putExtra("extra_title", chatTitle)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = listMessage.size

    class ViewHolder(var binding: ItemMessagesBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val TAG = "MessagesAdapter"
    }
}