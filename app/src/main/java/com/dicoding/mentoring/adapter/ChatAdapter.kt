package com.dicoding.mentoring.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.dicoding.mentoring.R
import com.dicoding.mentoring.data.local.Chat

class ChatAdapter(private val chats: List<Chat>, private val currentUser: String) :
    RecyclerView.Adapter<ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return if (viewType == VIEW_TYPE_MY_MESSAGE) {
            MyChatViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
            )
        } else {
            OtherChatViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_chat_them, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(chats[position])
    }

    override fun getItemViewType(position: Int): Int {
        return if (currentUser == chats[position].sentBy) VIEW_TYPE_MY_MESSAGE else VIEW_TYPE_OTHER_MESSAGE
    }

    override fun getItemCount() = chats.size

    inner class MyChatViewHolder(view: View) : ChatViewHolder(view) {
        private var chatText: TextView = view.findViewById(R.id.tv_item_chat_text_me)

        override fun bind(chat: Chat) {
            chatText.text = chat.messageText
        }
    }

    inner class OtherChatViewHolder(view: View) : ChatViewHolder(view) {
        private var chatText: TextView = view.findViewById(R.id.tv_item_chat_text_them)

        override fun bind(chat: Chat) {
            chatText.text = chat.messageText
        }
    }

    companion object {
        private const val VIEW_TYPE_MY_MESSAGE = 1
        private const val VIEW_TYPE_OTHER_MESSAGE = 2
    }
}

open class ChatViewHolder(view: View) : ViewHolder(view) {
    open fun bind(chat: Chat) {}
}
