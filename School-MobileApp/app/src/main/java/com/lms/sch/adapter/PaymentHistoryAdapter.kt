package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.activity.SubjectWiseProgressActivity
import com.lms.sch.databinding.CardPaymentHistoryBinding
import com.lms.sch.databinding.ItemTextTabBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.response.DropdownResponse
import com.lms.sch.response.GetTransactionResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils

class PaymentHistoryAdapter(
    val mActivity: BaseActivity,
    val isHome : Boolean,
    val list: ArrayList<GetTransactionResponse.Result.Row>,
    val onClickListener: OnClickListener
): RecyclerView.Adapter<PaymentHistoryAdapter.ViewHolder>()  {
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        var binding: CardPaymentHistoryBinding = CardPaymentHistoryBinding.bind(view)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_payment_history,parent,false)
        )
    }
    override fun getItemCount(): Int{
        if (isHome && list.size > 3){
            return 3
        }
        else {
            return list.size
        }
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (list[position].feesMaster != null){
            holder.binding.fees.text = list[position].feesMaster!!.title
        }
        else {
            holder.binding.fees.text = "--/--"
        }
        val date = BaseUtils.getFormattedDate(list[position].paidOn!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
        val time = BaseUtils.getFormattedDate(list[position].paidOn!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.HH_MM_AAA)

        if (date.isNotEmpty() && time.isNotEmpty()){
            holder.binding.date.text = "$date at $time"
        }
        else {
            holder.binding.date.text = "--/--"
        }
        holder.binding.amount.text = "₹ ${list[position].amount}"
        if (list[position].status == "paid"){
            holder.binding.status.text = "Paid"
            UiUtils.textviewCustomDrawable(holder.binding.status,R.drawable.border_curve_24dp)
            UiUtils.textViewTextColor(holder.binding.status,"#348F23",null)
            UiUtils.textViewBgTint(holder.binding.status,"#e5fce1",null)
        }
        else {
            holder.binding.status.text = "Failed"
            UiUtils.textviewCustomDrawable(holder.binding.status,R.drawable.border_curve_24dp)
            UiUtils.textViewTextColor(holder.binding.status,"#E85A5B",null)
            UiUtils.textViewBgTint(holder.binding.status,"#FFEBEB",null)
        }
        holder.binding.root.setOnClickListener {
            onClickListener.onClickItem(position)
        }
    }

}