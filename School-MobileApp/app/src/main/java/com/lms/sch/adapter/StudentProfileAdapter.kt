    package com.lms.sch.adapter

    import android.os.Bundle
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import androidx.recyclerview.widget.RecyclerView
    import com.lms.sch.R
    import com.lms.sch.activity.AssignmentActivity
    import com.lms.sch.activity.BaseActivity
    import com.lms.sch.activity.StudentInfoActivity
    import com.lms.sch.activity.StudentProfileActivity
    import com.lms.sch.databinding.CardStudentListBinding
    import com.lms.sch.interfaces.OnClickListener
    import com.lms.sch.response.GetStudentResponse
    import com.lms.sch.session.Constants
    import com.lms.sch.utils.BaseUtils
    import com.lms.sch.utils.UiUtils

    class StudentProfileAdapter
        ( val mActivity: BaseActivity,
          val list: ArrayList<GetStudentResponse.Result.Row>,
          val  onClickListener: OnClickListener) : RecyclerView.Adapter<StudentProfileAdapter.ViewHolder>() {
              inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
                  var binding : CardStudentListBinding = CardStudentListBinding.bind(view)
              }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.card_student_list,parent,false))
        }

        override fun getItemCount(): Int {
           return list.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if (list[position].student != null){
                if (list[position].student!!.firstName != null && list[position].student!!.firstName != null ) {
                    holder.binding.stdName.text = "${list[position].student!!.firstName ?: ""} ${list[position].student!!.lastName ?: ""}"
                }else{
                    holder.binding.stdName.text = "--/--"
                }
                UiUtils.loadImage(holder.binding.profile,list[position].student!!.img_url)
                if (list[position].student!!.activeStatus == "active"){
                    UiUtils.textViewBgTint(holder.binding.status,"#EAFFEA",null)
                    UiUtils.textViewTextColor(holder.binding.status,"#32B138",null)
                    holder.binding.status.text = "Present"
                }else{
                    UiUtils.textViewBgTint(holder.binding.status,"#FFE6E6",null)
                    UiUtils.textViewTextColor(holder.binding.status,"#E85A5B",null)
                    UiUtils.textViewBgTint(holder.binding.status,"#EAFFEA",null)
                    holder.binding.status.text = "Absent"

                }
            }
            if (list[position].studentClass != null && list[position].studentClass!!.name != null){
                if(list[position].section != null && list[position].section!!.name != null){
                    holder.binding.stdclass.text = "${list[position].studentClass!!.name ?: ""} ${list[position].section!!.name ?: ""}"
                }
                else{
                    holder.binding.stdclass.text = "--/--"
                }
            }
            holder.binding.root.setOnClickListener {
                onClickListener.onClickItem(position)
            }
        }
    }