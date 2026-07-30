package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardAdminTimeTableBinding
import com.lms.sch.databinding.CardPendingLeavesBinding
import com.lms.sch.response.AvailableLeavesRes
import com.lms.sch.response.ProgramResponse

class AvailableLeaveAdapter(
    var mActivity: BaseActivity,
    var list: ArrayList<AvailableLeavesRes.Result>
) : RecyclerView.Adapter<AvailableLeaveAdapter.ViewHolder>(){
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: CardPendingLeavesBinding = CardPendingLeavesBinding.bind(view)
    }
    override fun onCreateViewHolder(  parent: ViewGroup,viewType: Int ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_pending_leaves, parent, false)
        )
    }
    override fun onBindViewHolder( holder: ViewHolder, position: Int ) {
        if (list[position] != null){
            if (list[position].numberOfDays == "1"){
                holder.binding.daysleft.text = "${list[position].numberOfDays!!} Day"
            }else{
                holder.binding.daysleft.text = "${list[position].numberOfDays!!} Days"
            }
            holder.binding.leaveName.text = list[position].leaveType!!.name!!
            holder.binding.count.text = list[position].taken!!
            holder.binding.count2.text = list[position].remaining!!
        }else{
            holder.binding.leaveName.text = "--/--"
            holder.binding.daysleft.text = "--/--"
            holder.binding.count.text = "--/--"
            holder.binding.count2.text = "--/--"
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }
}