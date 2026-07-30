package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardMySubjectsBinding
import com.lms.sch.databinding.ItemTextTabBinding
import com.lms.sch.response.DropdownResponse
import com.lms.sch.response.GetSectionResponse

class SectionAdapter(
    val mActivity: BaseActivity,
    val list : ArrayList<GetSectionResponse.Result>
): RecyclerView.Adapter<SectionAdapter.ViewHolder>()  {

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        var binding: ItemTextTabBinding = ItemTextTabBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_my_subjects,parent,false)
        )
    }

    override fun getItemCount(): Int{
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tab1.text = list[position].name
    }

}