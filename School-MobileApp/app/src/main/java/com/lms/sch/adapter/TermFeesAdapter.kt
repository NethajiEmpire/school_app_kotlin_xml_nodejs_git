package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardTermFeesBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.response.AdminSingleViewResponse
import com.lms.sch.utils.UiUtils

class TermFeesAdapter (
    var mActivity: BaseActivity,
    var list: ArrayList<AdminSingleViewResponse.Result.Terms>,
    var onClickListener: OnClickListener
): RecyclerView.Adapter<TermFeesAdapter.ViewHolder>() {
    private val expandedPositions = HashSet<Int>()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var binding : CardTermFeesBinding = CardTermFeesBinding.bind(view)
    }
    override fun onCreateViewHolder( parent: ViewGroup,viewType: Int ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_term_fees, parent, false))
    }
    override fun onBindViewHolder( holder: ViewHolder, position: Int) {
        if (list[position] != null){
            holder.binding.term1.text = "${list[position].name} : ₹${list[position].totalAmount}"
            val subTermAdapter = SubTermAdapter( mActivity, list[position].types!!,onClickListener )
            holder.binding.subTerms.layoutManager = LinearLayoutManager(mActivity)
            holder.binding.subTerms.adapter = subTermAdapter
            val isExpanded = expandedPositions.contains(position)
            holder.binding.subTerms.visibility = if (isExpanded) View.VISIBLE else View.GONE
            holder.binding.down1.rotation = if (isExpanded) 180f else 0f
            holder.binding.personalInformation.setOnClickListener {
                if (isExpanded) {
                    expandedPositions.remove(position)
                    UiUtils.relativeLayoutBgDrawable(holder.binding.personalInformation,R.drawable.border_top)
                    UiUtils.relativeLayoutBgTint(holder.binding.personalInformation,"#DAEFFF",null)
                } else {
                    expandedPositions.add(position)
                    UiUtils.relativeLayoutBgDrawable(holder.binding.personalInformation,R.drawable.border_line_curve_8dp_red)
                    UiUtils.relativeLayoutBgTint(holder.binding.personalInformation,"#DAEFFF",null)
                }
                notifyItemChanged(position)
            }
        }
        else{
            holder.binding.term1.text = "0"
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }
}