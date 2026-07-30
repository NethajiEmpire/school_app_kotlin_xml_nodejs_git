package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.AssignmentActivity
import com.lms.sch.activity.BaseActivity
import com.lms.sch.activity.ProjectActivity
import com.lms.sch.databinding.CardProjectworkBinding
import com.lms.sch.models.ProjectModelClass
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils

class ProjectCardsAdapter(private val mActivity: BaseActivity, private val list: ArrayList<ProjectModelClass>) :
    RecyclerView.Adapter<ProjectCardsAdapter.ViewHolder>() {
        inner class  ViewHolder(view: View) : RecyclerView.ViewHolder(view){
            val binding : CardProjectworkBinding = CardProjectworkBinding.bind(view)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder( LayoutInflater.from(parent.context).inflate(R.layout.card_projectwork,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.subjectName.text = list[position].subname
        holder.binding.tname.text = list[position].tnmae
        holder.binding.projectStatus.text = list[position].status
            //  UiUtils.imageviewDrawable(holder.binding.img, R.drawable.book_image)
        holder.binding.root.setOnClickListener{
            BaseUtils.startActivity(mActivity, AssignmentActivity(), null, false)
        }
    }
}