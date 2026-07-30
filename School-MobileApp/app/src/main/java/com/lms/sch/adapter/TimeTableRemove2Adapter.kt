package com.lms.sch.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.AdminTimeTableActivity
import com.lms.sch.databinding.CardTextCloseBinding
import com.lms.sch.fragment.UserManagementFragment
import com.lms.sch.interfaces.OnClickListener

class TimeTableRemove2Adapter (
    var context: Context,
    var fragment: UserManagementFragment,
    var list: ArrayList<String>,
    var onClickListener: OnClickListener
): RecyclerView.Adapter<TimeTableRemove2Adapter.ViewHolder>(){

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: CardTextCloseBinding = CardTextCloseBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.card_text_close, parent, false)
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
                if (fragment.filterArr.isNotEmpty() && fragment.filterArr.contains(currentItem)) {
                    onClickListener.onClickItem(position)
                    val removedIndex = fragment.filterArr.indexOf(currentItem)
                    if (removedIndex >= 0) {
                        fragment.filterArr.removeAt(removedIndex)
                        notifyDataSetChanged()
                    }
                }
            }
        }
    }
}