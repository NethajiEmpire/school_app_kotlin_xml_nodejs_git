package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.ChatActivity
import com.lms.sch.databinding.CardChatBinding
import com.lms.sch.models.ChatMessage

class ChatAdapter(
    val context: ChatActivity,
    private val messages: ArrayList<ChatMessage>
) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: CardChatBinding = CardChatBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.card_chat, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messages[position]

        if (message.isSentByUser) {
            holder.binding.layoutInput.visibility = View.GONE
            holder.binding.layoutOutput.visibility = View.VISIBLE
            holder.binding.textOutputChat.text = message.message
            holder.binding.txtOutputTime.text = message.timestamp
        } else {

            holder.binding.layoutInput.visibility = View.VISIBLE
            holder.binding.layoutOutput.visibility = View.GONE
            holder.binding.textInputChat.text = message.message
            holder.binding.txtInputTime.text = message.timestamp
        }
    }
}
