package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardSubTermsBinding
import com.lms.sch.databinding.CardTermFeesBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.response.AdminFeesResponse
import com.lms.sch.response.AdminSingleViewResponse

class SubTermAdapter (
    var mActivity: BaseActivity,
    var list: ArrayList<AdminSingleViewResponse.Result.Terms.Types>,
    var onClickListener: OnClickListener
): RecyclerView.Adapter<SubTermAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var binding : CardSubTermsBinding = CardSubTermsBinding.bind(view)
    }
    override fun onCreateViewHolder( parent: ViewGroup,viewType: Int ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_sub_terms, parent, false))
    }
    override fun onBindViewHolder( holder: ViewHolder, position: Int) {
        if (list[position] != null){
            holder.binding.freeName.text = list[position].name!!.name.toString()
            holder.binding.amount.text = "₹${list[position].amount.toString()}"
        }
        else{
            holder.binding.freeName.text = "--/--"
            holder.binding.amount.text = "0"
        }
        if (position == list.size - 1) {
            holder.binding.view.visibility = View.GONE
        } else {
            holder.binding.view.visibility = View.VISIBLE
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }
}