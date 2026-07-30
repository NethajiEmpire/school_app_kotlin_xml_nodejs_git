package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.adapter.AdminLeaveRequestAdapter.ViewHolder
import com.lms.sch.databinding.CardAdminFinanceFeesBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.models.AdminFinanceFee
import com.lms.sch.models.AdminLeaveRequest
import com.lms.sch.utils.UiUtils

class AdminFinanceFeesAdapter(
    var mActivity: BaseActivity,
    var list: ArrayList<AdminFinanceFee>,
    val onClickListener: OnClickListener
) : RecyclerView.Adapter<AdminFinanceFeesAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: CardAdminFinanceFeesBinding = CardAdminFinanceFeesBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_admin_finance_fees, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AdminFinanceFeesAdapter.ViewHolder, position: Int) {
        holder.binding.name.text = list[position].name

        if (list[position].statusType == "Pending") {
            UiUtils.textViewBgTint(holder.binding.status, "#FFE0E1", null)
            UiUtils.textViewTextColor(holder.binding.status, "#EA5455", null)
            UiUtils.cardViewBgTint(holder.binding.card, "#EB7172", null)
            UiUtils.textViewTextColor(holder.binding.date, "#EA5455", null)
            holder.binding.status.text = "Pending"
            holder.binding.feeDateType.text = "Due Date"
            holder.binding.amtType.text = "Pending Amount"
            holder.binding.date.text = "₹1,80,000"
        }

        else if (list[position].statusType == "OverDue")
        {
            UiUtils.textViewBgTint(holder.binding.status, "#FFE0E1",null)
            UiUtils.textViewTextColor(holder.binding.status, "#FF7C00",null)
            UiUtils.cardViewBgTint(holder.binding.card, "#FF7C00", null)
            UiUtils.textViewTextColor(holder.binding.date, "#FF7C00", null)
            holder.binding.status.text = "OverDue"
            holder.binding.feeDateType.text = "Last Date"
            holder.binding.amtType.text = "Overdue Amount"
            holder.binding.date.text = "₹1,80,000"

            holder.binding!!.root.setOnClickListener {
                onClickListener.onClickItem(position)
            }
        }

        else if (list[position].statusType == "Paid")
        {
            UiUtils.textViewBgTint(holder.binding.status, "#D2FFD5",null)
            UiUtils.textViewTextColor(holder.binding.status, "#32B138",null)
            UiUtils.cardViewBgTint(holder.binding.card, "#32B138", null)
            UiUtils.textViewTextColor(holder.binding.date, "#32B138", null)
            holder.binding.status.text = "Paid"
            holder.binding.feeDateType.text = "Payment Date"
            holder.binding.amtType.text = "Paid Amountt"
            holder.binding.date.text = "₹1,00,000"

            holder.binding!!.root.setOnClickListener {
                onClickListener.onClickItem(position)
            }
        }
    }
}
