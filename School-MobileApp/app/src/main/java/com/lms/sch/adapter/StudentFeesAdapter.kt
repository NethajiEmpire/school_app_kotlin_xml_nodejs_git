package com.lms.sch.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.activity.PaymentSummaryActivity
import com.lms.sch.databinding.CardFeesCompletedBinding
import com.lms.sch.response.StudentFeeResponse
import com.lms.sch.session.Constants
import com.lms.sch.session.TempSingleton
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils

class StudentFeesAdapter(
    val mActivity: BaseActivity ,
    val list: ArrayList<StudentFeeResponse.Term>
): RecyclerView.Adapter<StudentFeesAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding : CardFeesCompletedBinding = CardFeesCompletedBinding.bind(view)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.card_fees_completed,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (list[position].name != null && list[position].name!!.isNotEmpty()) {
            holder.binding.termOfFee.text = list[position].name
        } else {
            holder.binding.termOfFee.text = "--/--"
        }
        if (list[position].totalAmount != null && list[position].totalAmount!!.isNotEmpty()) {
            holder.binding.totalAmount.text = "₹${list!![position].totalAmount!!}"
        } else {
            holder.binding.totalAmount.text = "--/--"
        }
        if (list[position].status != null && list[position].status == "paid") {
            holder.binding.typeOfAmount.text = "Paid Amount"
            holder.binding.dueDate.text = "Paid Date : "
            holder.binding.payNow.text = "Receipt"
            if (list[position].status != null) {
                holder.binding.status.text = "Completed"
                UiUtils.textviewCustomDrawable(holder.binding.status,R.drawable.border_curve_24dp)
                UiUtils.textViewBgTint(holder.binding.status,"#e6ffe7" ,null)
                UiUtils.textViewTextColor(holder.binding.status,"#32B138",null)
            } else {
                holder.binding.status.text = "--/--"
            }
            if (list[position].dueDate != null){
                holder.binding.date.text = BaseUtils.getFormattedDate(list[position].dueDate!!,Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT)
            }
            else{
                holder.binding.date.text = "--/--"
            }
        }
        else{
            holder.binding.typeOfAmount.text = "Payable Amount"
            holder.binding.dueDate.text = "Due Date : "
            holder.binding.payNow.text = "Pay Now"
            if (list[position].dueDate != null){
                holder.binding.date.text = BaseUtils.getFormattedDate(list[position].dueDate!!,Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT)
            }
            else{
                holder.binding.date.text = "--/--"
            }
            if (list[position].status != null) {
                holder.binding.status.text = "Over Due"
                UiUtils.textviewCustomDrawable(holder.binding.status,R.drawable.border_curve_24dp)
                UiUtils.textViewBgTint(holder.binding.status,"#fff2d9" ,null)
                UiUtils.textViewTextColor(holder.binding.status,"#F69300",null)
            } else {
                holder.binding.status.text = "--/--"
            }
        }

        holder.binding.root.setOnClickListener {
            TempSingleton.getInstance().feesPos = list[position]._id!!
            val bundle = Bundle()
            bundle.putString(Constants.IntentKeys.KEY,list[position].name!!)
            BaseUtils.startActivity(mActivity, PaymentSummaryActivity(),bundle,false)
        }

    }
}