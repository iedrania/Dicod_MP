package com.dicoding.mentoring.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.dicoding.mentoring.MainActivity
import com.dicoding.mentoring.R
import com.dicoding.mentoring.data.local.Chat
import com.dicoding.mentoring.ui.chat.ChatActivity
import com.dicoding.mentoring.ui.feedback.FeedbackActivity
import java.text.SimpleDateFormat
import java.util.Locale

class ChatAdapter(
    private val chats: List<Chat>, private val currentUser: String, private val userRole: String
) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return when (viewType) {
            VIEW_TYPE_MY_MESSAGE -> MyChatViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
            )

            VIEW_TYPE_MEETING_MESSAGE -> MeetingChatViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_meeting, parent, false)
            )

            VIEW_TYPE_FEEDBACK_MESSAGE -> FeedbackChatViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_feedback, parent, false)
            )

            else -> OtherChatViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_chat_them, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(chats[position])
    }

    override fun getItemViewType(position: Int): Int {
        return if (chats[position].specialChat == true) {
            if (userRole == "mentor") VIEW_TYPE_MEETING_MESSAGE else VIEW_TYPE_FEEDBACK_MESSAGE
        } else if (currentUser == chats[position].sentBy) {
            VIEW_TYPE_MY_MESSAGE
        } else {
            VIEW_TYPE_OTHER_MESSAGE
        }
    }

    override fun getItemCount() = chats.size

    inner class MyChatViewHolder(view: View) : ChatViewHolder(view) {
        private var chatText: TextView = view.findViewById(R.id.tv_item_chat_text)
        private var timeText: TextView = view.findViewById(R.id.tv_item_chat_time)

        override fun bind(chat: Chat) {
            chatText.text = chat.messageText

            val locale = Locale("id", "ID")
            val outputFormat = SimpleDateFormat("HH:mm", locale)
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
            val outputFormat = SimpleDateFormat("HH:mm", locale)
            val formattedDate: String = outputFormat.format(chat.sentAt?.toDate()!!)
            timeText.text = formattedDate
        }
    }

    inner class MeetingChatViewHolder(view: View) : ChatViewHolder(view) {
        private var timeText: TextView = view.findViewById(R.id.tv_item_chat_time)

        override fun bind(chat: Chat) {
            val locale = Locale("id", "ID")
            val outputFormat = SimpleDateFormat("HH:mm", locale)
            val formattedDate: String = outputFormat.format(chat.sentAt?.toDate()!!)
            timeText.text = formattedDate

            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("fragment", "schedule")
                context.startActivity(intent)
                (context as ChatActivity).finish()
            }
        }
    }

    inner class FeedbackChatViewHolder(view: View) : ChatViewHolder(view) {
        private var timeText: TextView = view.findViewById(R.id.tv_item_chat_time)

        override fun bind(chat: Chat) {
            val locale = Locale("id", "ID")
            val outputFormat = SimpleDateFormat("HH:mm", locale)
            val formattedDate: String = outputFormat.format(chat.sentAt?.toDate()!!)
            timeText.text = formattedDate

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, FeedbackActivity::class.java)
                intent.putExtra("extra_mentoring", chat.mentoringId)
                itemView.context.startActivity(intent)
            }
        }
    }

    open class ChatViewHolder(view: View) : ViewHolder(view) {
        open fun bind(chat: Chat) {}
    }

    companion object {
        private const val VIEW_TYPE_MY_MESSAGE = 1
        private const val VIEW_TYPE_MEETING_MESSAGE = 2
        private const val VIEW_TYPE_FEEDBACK_MESSAGE = 3
        private const val VIEW_TYPE_OTHER_MESSAGE = 4
    }
}