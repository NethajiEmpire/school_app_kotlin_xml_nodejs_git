package com.lms.sch.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardTextBinding
import com.lms.sch.interfaces.OnClickListener

class CardsAdapter (
    var mActivity: BaseActivity,
    var list: ArrayList<String>,
    var onClickListener: OnClickListener
): RecyclerView.Adapter<CardsAdapter.ViewHolder>(){

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: CardTextBinding = CardTextBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_text, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == list.size - 1){
            holder.binding.v1.visibility = View.GONE
        }
        else {
            holder.binding.v1.visibility = View.VISIBLE
        }
        if (position >= 0 && position < list.size) {
            holder.binding.name.text = list[position]
        } else {
            holder.binding.name.text = "Invalid Data"
        }

        holder.binding.root.setOnClickListener {
            onClickListener.onClickItem(position)
        }
    }
}