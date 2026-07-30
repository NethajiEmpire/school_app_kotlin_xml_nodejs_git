package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardGuestFeesSelectChildBinding
import com.lms.sch.response.StudentFeeResponse

class PaymentSummaryAdapter(
    val mActivity: BaseActivity ,
    val list: ArrayList<StudentFeeResponse.TermType>
): RecyclerView.Adapter<PaymentSummaryAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding : CardGuestFeesSelectChildBinding = CardGuestFeesSelectChildBinding.bind(view)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.card_guest_fees_select_child,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (list[position].name != null && list[position].name!!.name != null){
            holder.binding.title.text = list[position].name!!.name
        }
        else{
            holder.binding.title.text = "--/--"
        }
        if (list[position].amount != null){
            holder.binding.amt.text = list[position].amount
        }
        else{
            holder.binding.amt.text = "--/--"
        }
        if (position == list.size - 1){
            holder.binding.line.visibility = View.GONE
        }
        else{
            holder.binding.line.visibility = View.VISIBLE
        }
    }

}