package com.lms.sch.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.activity.StudentStatsActivity
import com.lms.sch.activity.TermFeesActivity
import com.lms.sch.databinding.CardStudentFeesBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.response.AdminFeesResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils

class AdminFeesAdapter(
    val mActivity: BaseActivity,
    val list: ArrayList<AdminFeesResponse.Result.Rows>,
    var onClickListener: OnClickListener
)  : RecyclerView.Adapter<AdminFeesAdapter.ViewHolder>() {
     inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
         var binding : CardStudentFeesBinding = CardStudentFeesBinding.bind(view)
     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.card_student_fees,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (list[position].studentClass != null && list[position].studentClass!!.name != null){
            holder.binding.std.text = "${UiUtils.getOrdinalSuffix(list[position].studentClass!!.name!!.toInt())}"
        }
        else{
            holder.binding.std.text = "--/--"
        }
        if (list[position].batch != null && list[position].batch!!.name != null){
            holder.binding.batch.text = list[position].batch!!.name
        }
        else{
            holder.binding.batch.text = "--/--"
        }
        if (list[position].status != null){
            holder.binding.status.text = list[position].status
        }
        else{
            holder.binding.status.text = "Unavailable"
        }
        if (list[position].studentCount != null){
            holder.binding.stdNum.text = "${list!![position].studentCount.toString()} Students"
        }
        else{
            holder.binding.stdNum.text = "0"
        }
        if (list[position].admissionFee != null){
            holder.binding.admissionAmountt.text = "₹ ${list!![position].admissionFee.toString()}"
        }
        else{
            holder.binding.admissionAmountt.text = "0"
        }
        if (list[position].classTotals!!.collected != null){
            holder.binding.collectAmt.text = "₹ ${list!![position].classTotals!!.collected.toString()}"
        }
        else{
            holder.binding.collectAmt.text = "0"
        }
        if (list[position].classTotals!!.pending != null){
            holder.binding.pendingAmt.text = "₹ ${list!![position].classTotals!!.pending.toString()}"
        }
        else{
            holder.binding.pendingAmt.text = "0"
        }
        if (list[position].classTotals!!.overdue != null){
            holder.binding.overdueAmt.text = "₹ ${list!![position].classTotals!!.overdue.toString()}"
        }
        else{
            holder.binding.overdueAmt.text = "0"
        }
        if (list[position].classTotals!!.total != null){
            holder.binding.totalAmt.text = "₹ ${list!![position].classTotals!!.total.toString()}"
        }
        else{
            holder.binding.totalAmt.text = "0"
        }
        holder.binding!!.root.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(Constants.IntentKeys.KEY,list[position]!!._id?:"")
            BaseUtils.startActivity(mActivity, TermFeesActivity(),bundle,false)
        }
    }
}