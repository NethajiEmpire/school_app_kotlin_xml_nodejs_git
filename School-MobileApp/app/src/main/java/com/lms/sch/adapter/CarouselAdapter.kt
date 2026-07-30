package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R

import com.lms.sch.activity.GuestLandingActivity
import com.lms.sch.activity.OtpActivity
import com.lms.sch.databinding.CardCarousilBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.utils.UiUtils

class CarouselAdapter (
    val mActivity: GuestLandingActivity,
    val list: ArrayList<Int>,
    val onClickListener: OnClickListener
) : RecyclerView.Adapter<CarouselAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) :RecyclerView.ViewHolder(view){
        var binding : CardCarousilBinding = CardCarousilBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(
                R.layout.card_carousil,
                parent,
                false
            )
        )
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        UiUtils.imageviewDrawable(holder.binding.img, list[position])

        holder.binding!!.root.setOnClickListener {
            onClickListener.onClickItem(position)
        }
    }
}