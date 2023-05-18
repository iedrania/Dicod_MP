package com.dicoding.mentoring.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.mentoring.databinding.ItemMessagesBinding
import com.dicoding.mentoring.ui.chat.ChatActivity

class MessagesAdapter(private val listMessage: MutableList<Pair<String, Map<String, Any>>>) :
    RecyclerView.Adapter<MessagesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemMessagesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = listMessage[position]
        holder.binding.tvItemMessagesName.text = message.second["senderName"] as CharSequence?
        holder.binding.tvItemMessagesPreview.text = message.second["messageText"] as CharSequence?
        Glide.with(holder.itemView.context).load(message.second["senderPhotoUrl"])
            .into(holder.binding.ivItemMessagesPhoto)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ChatActivity::class.java)
            intent.putExtra("extra_group", message.first)
            intent.putExtra(
                "extra_title", message.second["senderName"].toString()
            ) // TODO based on role and members
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = listMessage.size

    class ViewHolder(var binding: ItemMessagesBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val TAG = "MessagesAdapter"
    }
}