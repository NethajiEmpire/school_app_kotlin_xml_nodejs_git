package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.GuestLandingActivity
import com.lms.sch.databinding.CardGuestLandingBinding
import com.lms.sch.models.GuestLanding
import com.lms.sch.utils.UiUtils

class GuestLandingAdapter  (
    val context: GuestLandingActivity,
    private val list: ArrayList<GuestLanding>
): RecyclerView.Adapter<GuestLandingAdapter.ViewHolder>(){
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding: CardGuestLandingBinding = CardGuestLandingBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.card_guest_landing, parent, false)

        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.binding.classtext.text = item.classroom
        holder.binding.classdesc.text = item.destext

        if (item.groupimg == "GroupProfile")
        {
            UiUtils.imageviewDrawable(holder.binding.groupProfile, R.drawable.group_profile)
        } else if (item.groupimg == "Library")
        {
            UiUtils.imageviewDrawable(holder.binding.groupProfile, R.drawable.lib_book)
        } else if (item.groupimg == "ScienceLab")
        {
            UiUtils.imageviewDrawable(holder.binding.groupProfile, R.drawable.lab_tube)
        } else if (item.groupimg == "ComputerLab")
        {
            UiUtils.imageviewDrawable(holder.binding.groupProfile, R.drawable.computer_lab)
        } else if (item.groupimg == "Sports")
        {
            UiUtils.imageviewDrawable(holder.binding.groupProfile, R.drawable.sports_specilities)
        } else if (item.groupimg == "Transport")
        {
            UiUtils.imageviewDrawable(holder.binding.groupProfile, R.drawable.transports)
        }
    }

}


