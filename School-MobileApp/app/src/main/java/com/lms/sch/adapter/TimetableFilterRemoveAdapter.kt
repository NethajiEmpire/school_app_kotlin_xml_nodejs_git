package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.AdminTimeTableActivity
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardTextCloseBinding
import com.lms.sch.interfaces.OnClickListener

class TimetableFilterRemoveAdapter (
    var mActivity: AdminTimeTableActivity,
    var list: ArrayList<String>,
    var onClickListener: OnClickListener
): RecyclerView.Adapter<TimetableFilterRemoveAdapter.ViewHolder>(){

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: CardTextCloseBinding = CardTextCloseBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_text_close, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position >= 0 && position < list.size) {
            holder.binding.name.text = list[position]
        } else {
            holder.binding.name.text = "Invalid Data"
        }

        holder.binding.root.setOnClickListener {
            if (position >= 0 && position < list.size) {
                val currentItem = list[position]
                if (mActivity.filterArr.isNotEmpty() && mActivity.filterArr.contains(currentItem)) {
                    onClickListener.onClickItem(position)
                    val removedIndex = mActivity.filterArr.indexOf(currentItem)
                    if (removedIndex >= 0) {
                        mActivity.filterArr.removeAt(removedIndex)
                        notifyDataSetChanged()
                    }
                }
            }
        }
    }
}