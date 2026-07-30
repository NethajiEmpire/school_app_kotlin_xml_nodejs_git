package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.ApplicationActivity
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardGuestFeesSelectChildBinding
import com.lms.sch.response.GuestFeesResponse

class GuestTermFeesChildAdapter (
    private val mActivity: ApplicationActivity,
    private val list: ArrayList<GuestFeesResponse.Result.Terms.Types>
) : RecyclerView.Adapter<GuestTermFeesChildAdapter.ViewHolder>() {
    inner class  ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding : CardGuestFeesSelectChildBinding = CardGuestFeesSelectChildBinding.bind(view)
    }

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_guest_fees_select_child,parent,false)
        )
    }
    override fun getItemCount(): Int {
        return list.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == list.size - 1){
            holder.binding.line.visibility = View.GONE
        }else{
            holder.binding.line.visibility = View.VISIBLE
        }
        if (list[position].name != null){
            holder.binding.title.text = list[position].name!!.name
        }
        holder.binding.amt.text = "₹${list[position].amount}"
       /* for (items in list){
            mActivity.payableAmt += items.amount!!.toInt()
        }*/
    }
}