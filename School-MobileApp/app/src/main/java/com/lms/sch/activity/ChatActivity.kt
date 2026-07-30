package com.lms.sch.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lms.sch.R
import com.lms.sch.adapter.ChatAdapter
import com.lms.sch.databinding.ActivityChatBinding
import com.lms.sch.models.ChatMessage
import com.lms.sch.session.SharedHelper
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatActivity : BaseActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var adapter: ChatAdapter
    var messages = ArrayList<ChatMessage>()
    var key = ""
    var isMic = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        sharedHelper = SharedHelper(this)

        val chatLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        adapter = ChatAdapter(this, messages)
        binding.recycler.layoutManager = chatLayoutManager
        binding.recycler.adapter = adapter

        key = intent.getStringExtra("key") ?: ""
        binding.txt.text = key

        binding.back.setOnClickListener {
            onBackPressed()
        }

        if (messages.isEmpty()){
            binding.noData.txt.text = "No Messages are available."
            binding.noData.root.visibility = View.VISIBLE
            binding.recycler.visibility = View.GONE
        }

       // loadChatMessages()

        binding.cam.setOnClickListener {

        }
        binding.msg.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?,p1: Int,p2: Int,p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int,p2: Int,p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                if(binding.msg.text.toString().isNotEmpty()){
                    UiUtils.imageviewDrawable(binding.send,R.drawable.send)
                    isMic = false
                }
                else{
                    UiUtils.imageviewDrawable(binding.send,R.drawable.ic_mic)
                    isMic = true
                }
            }
        })
        binding.send.setOnClickListener {
            if(!isMic){
                val str = binding.msg.text.toString()
                if(str.isNotEmpty()){
                    BaseUtils.hideForceKeyboard(binding.root)
                    sendMessage(str)
                }
                else{
                    UiUtils.showSnack("Can't send empty message",binding.root,false)
                }
            }
            else{
                if(binding.micLay.visibility == View.GONE){
                    UiUtils.showSnack("Audio message not available at the moment",binding.root,false)
                }
            }
        }
    }

    private fun sendMessage(text: String) {
        binding.msg.text.clear()
        val timestamp = getCurrentTimestamp()
        val newMessage = ChatMessage(text, true,timestamp)
        messages.add(newMessage)
        adapter.notifyItemInserted(messages.size - 1)
        binding.recycler.scrollToPosition(messages.size - 1)
        saveChatMessages()
        if (messages.isNotEmpty()){
            binding.noData.root.visibility = View.GONE
            binding.recycler.visibility = View.VISIBLE
        }
        simulateReceivedMessage()
    }

    private fun simulateReceivedMessage() {
        binding.recycler.postDelayed({
            val responseText = generateAutoResponse()
            val timestamp = getCurrentTimestamp()
            val receivedMessage = ChatMessage(responseText,false,timestamp)
            messages.add(receivedMessage)
            adapter.notifyItemInserted(messages.size - 1)
            binding.recycler.scrollToPosition(messages.size - 1)

            saveChatMessages()
        }, 2000)
    }

    private fun getCurrentTimestamp(): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun generateAutoResponse(): String {
        val responses = listOf(
            "Got it!",
            "That's interesting.",
            "Let’s catch up soon.",
            "Thanks for sharing!",
            "Noted!",
            "Sounds good!",
            "I will get back to you."
        )
        return responses.random()
    }

    private fun saveChatMessages() {
        sharedHelper.chatMessages = listOf(Gson().toJson(messages))
    }

    private fun loadChatMessages() {
        val jsonList = sharedHelper.chatMessages ?: return
        if (jsonList.isEmpty()) return

        val jsonString = jsonList.first()
        if (jsonString.isEmpty()) return


        val type = object : TypeToken<ArrayList<ChatMessage>>() {}.type
       /* val loadedMessages: ArrayList<ChatMessage> = Gson().fromJson(jsonString, type)*/

        messages.clear()
       /* messages.addAll(loadedMessages)*/

        adapter.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        sharedHelper.chatMessages = emptyList()
    }
}

