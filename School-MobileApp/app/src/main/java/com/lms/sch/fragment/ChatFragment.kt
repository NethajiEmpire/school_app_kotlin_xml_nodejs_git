package com.lms.sch.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lms.sch.R
import com.lms.sch.activity.ChatActivity
import com.lms.sch.activity.ProfileActivity
import com.lms.sch.databinding.FragmentChatBinding
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils

class ChatFragment : BaseFragment() {
    lateinit var binding: FragmentChatBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        val view = binding.root
        mActivity.selectBottomNav(3)
        binding.profile.setOnClickListener {
            BaseUtils.startActivity(mActivity, ProfileActivity(),null,false)
        }
        binding.scrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            if (scrollY > binding.header.height) {
                binding.tabs.translationY = scrollY.toFloat() - binding.header.height
                binding.tabs.elevation = 8f
            } else {
                binding.tabs.translationY = 0f
                binding.tabs.elevation = 0f
            }
        }
        binding.directMessage.setOnClickListener{
                UiUtils.linearLayoutBgDrawable(binding.directMessage, R.drawable.border_line_curve_24dp_grey)
                UiUtils.linearLayoutBgTint(binding.directMessage,null, R.color.orange_yellow)
                UiUtils.linearLayoutBgDrawable(binding.groups, R.drawable.border_line_curve_24dp_grey)
                UiUtils.linearLayoutBgTint(binding.groups,null, R.color.white)

        }
        binding.groups.setOnClickListener{
            UiUtils.linearLayoutBgDrawable(binding.groups, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgTint(binding.groups,null, R.color.orange_yellow)
            UiUtils.linearLayoutBgDrawable(binding.directMessage, R.drawable.border_line_curve_24dp_grey)
            UiUtils.linearLayoutBgTint(binding.directMessage,null, R.color.white)

        }

        binding.pin.karthickchat.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("key","Karthick")
            BaseUtils.startActivity(mActivity,ChatActivity(),bundle,false)
        }
        binding.pin.suryaChat.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("key","Surya")
            BaseUtils.startActivity(mActivity,ChatActivity(),bundle,false)
        }
        binding.staticChats.supriyaChat.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("key","Supriya")
            BaseUtils.startActivity(mActivity,ChatActivity(),bundle,false)
        }
        binding.staticChats.veenaChat.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("key","Veena")
            BaseUtils.startActivity(mActivity,ChatActivity(),bundle,false)
        }
        binding.staticChats.sushmaChat.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("key","Sushma")
            BaseUtils.startActivity(mActivity,ChatActivity(),bundle,false)
        }
        binding.staticChats.chandruChat.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("key","Chandru")
            BaseUtils.startActivity(mActivity,ChatActivity(),bundle,false)
        }
        binding.staticChats.arunChat.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("key","Arun")
            BaseUtils.startActivity(mActivity,ChatActivity(),bundle,false)
        }
        binding.staticChats.arishChat.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("key","Arish")
            BaseUtils.startActivity(mActivity,ChatActivity(),bundle,false)
        }
        binding.tchat.tkarthick.setOnClickListener{
            val bundle=Bundle()
            bundle.putString("key","Mr Karthick")
            BaseUtils.startActivity(mActivity,ChatActivity(),bundle,false)
        }
        binding.tStaticChat.teachersushma.setOnClickListener{
            val bundle=Bundle()
            bundle.putString("key","Sushma")
            BaseUtils.startActivity(mActivity,ChatActivity(),bundle,false)
        }
        binding.tStaticChat.tSasi.setOnClickListener{
            val bundle=Bundle()
            bundle.putString("key","Mr Sasi Balan")
            BaseUtils.startActivity(mActivity,ChatActivity(),bundle,false)
        }
        binding.tStaticChat.tchandru.setOnClickListener{
            val bundle=Bundle()
            bundle.putString("key","Mr Chandru")
            BaseUtils.startActivity(mActivity,ChatActivity(),bundle,false)
        }
        binding.tStaticChat.tSurya.setOnClickListener{
            val bundle=Bundle()
            bundle.putString("key","Mr Surya")
            BaseUtils.startActivity(mActivity,ChatActivity(),bundle,false)
        }
        binding.tStaticChat.tArish.setOnClickListener{
            val bundle=Bundle()
            bundle.putString("key","Mr Arish")
            BaseUtils.startActivity(mActivity,ChatActivity(),bundle,false)
        }


        return view
    }

}