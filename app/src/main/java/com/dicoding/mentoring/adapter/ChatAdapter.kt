package com.dicoding.mentoring.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.dicoding.mentoring.R
import com.dicoding.mentoring.data.local.Chat
import java.text.SimpleDateFormat
import java.util.Locale

class ChatAdapter(private val chats: List<Chat>, private val currentUser: String) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

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
        private var chatText: TextView = view.findViewById(R.id.tv_item_chat_text)
        private var timeText: TextView = view.findViewById(R.id.tv_item_chat_time)

        override fun bind(chat: Chat) {
            chatText.text = chat.messageText

            val locale = Locale("id", "ID")
            val outputFormat = SimpleDateFormat("dd/MM HH:mm", locale)
            val formattedDate: String = outputFormat.format(chat.sentAt?.toDate()!!)
            timeText.text = formattedDate
        }
    }

    inner class OtherChatViewHolder(view: View) : ChatViewHolder(view) {
        private var chatText: TextView = view.findViewById(R.id.tv_item_chat_text)
        private var timeText: TextView = view.findViewById(R.id.tv_item_chat_time)

        override fun bind(chat: Chat) {
            chatText.text = chat.messageText

            val locale = Locale("id", "ID")
            val outputFormat = SimpleDateFormat("HH:mm dd/MM ", locale)
            val formattedDate: String = outputFormat.format(chat.sentAt?.toDate()!!)
            timeText.text = formattedDate
        }
    }

    open class ChatViewHolder(view: View) : ViewHolder(view) {
        open fun bind(chat: Chat) {}
    }

    companion object {
        private const val VIEW_TYPE_MY_MESSAGE = 1
        private const val VIEW_TYPE_OTHER_MESSAGE = 2
    }
}