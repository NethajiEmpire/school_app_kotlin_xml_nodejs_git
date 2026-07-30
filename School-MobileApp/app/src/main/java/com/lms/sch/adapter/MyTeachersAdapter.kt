package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardMyTeachersBinding
import com.lms.sch.response.GetMyTeachersResponse
import com.lms.sch.utils.UiUtils

class MyTeachersAdapter(
    val mActivity: BaseActivity,
    val list: ArrayList<GetMyTeachersResponse.Result.SubjectTeachers>
) : RecyclerView.Adapter<MyTeachersAdapter.ViewHolder>() {
    inner class  ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding : CardMyTeachersBinding = CardMyTeachersBinding.bind(view)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_my_teachers,parent,false)
        )
    }
    override fun getItemCount(): Int {
        return list.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.name.text = list[position].name
        holder.binding.mobile.text = list[position].mobile
        holder.binding.email.text = list[position].email
        holder.binding.sub.text = "${list[position].subject} Teacher"
        /*if (list[position].img_url != null){
            UiUtils.loadImage(holder.binding.profile1,list[position].img_url)
        }*/
    }
}