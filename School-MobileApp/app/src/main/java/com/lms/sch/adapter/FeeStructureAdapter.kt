package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.FeeStructureActivity
import com.lms.sch.databinding.CardFeeStructureBinding
import com.lms.sch.models.FeeStructure

class FeeStructureAdapter (
    val context: FeeStructureActivity,
    private val list: ArrayList<FeeStructure>
    ): RecyclerView.Adapter<FeeStructureAdapter.ViewHolder>(){
inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
    val binding: CardFeeStructureBinding=CardFeeStructureBinding.bind(view)
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.card_fee_structure, parent, false)

        )
    }

    override fun getItemCount(): Int {
    return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.standard.text= list[position].standard

    }
    }


