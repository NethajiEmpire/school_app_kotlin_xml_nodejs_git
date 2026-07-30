package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardClassTestReportBinding
import com.lms.sch.databinding.ClassTestResultprogressBinding
import com.lms.sch.response.GetClassTestResponse
import com.lms.sch.utils.UiUtils

class ClassTestProgressAdapter(
    val mActivity: BaseActivity,
    val isReportLayout: Boolean,
    val list: ArrayList<GetClassTestResponse.Result>
) : RecyclerView.Adapter<ClassTestProgressAdapter.ViewHolder>() {
    inner class ViewHolder(view: View,type: Int):RecyclerView.ViewHolder(view){
        var binding : ClassTestResultprogressBinding? = null
        var  binding1 : CardClassTestReportBinding? = null
        init {
            if (type == 0) {
                binding = ClassTestResultprogressBinding.bind(view)
            } else {
                binding1 = CardClassTestReportBinding.bind(view)
            }
        }
    }
    override fun onCreateViewHolder( parent: ViewGroup,viewType: Int): ViewHolder {
        return if (isReportLayout){ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.class_test_resultprogress,parent,false),0
        )}else{
            ViewHolder(
                LayoutInflater.from(mActivity).inflate(R.layout.card_class_test_report, parent, false),
                1
            )
        }
    }
    override fun getItemCount(): Int {
        return  list.size
    }
    override fun onBindViewHolder(  holder: ViewHolder, position: Int) {
        if (isReportLayout){
            if (list[position] != null){
                holder.binding!!.marksgaind.text = "${list[position].scored_marks!!}/"
                UiUtils.textViewTextColor(holder.binding!!.marksgaind, "#28C76F", null)
                if (list[position].classTest != null){
                    holder.binding!!.txtIncharge.text = list[position].classTest!!.title!!
                    holder.binding!!.marks.text = list[position].classTest!!.totalMarks!!
                    UiUtils.textViewTextColor(holder.binding!!.marks, "#28C76F", null)
                    holder.binding!!.stdMarks.text = list[position].classTest!!.totalMarks!!
                }else{
                    holder.binding!!.txtIncharge.text = "--/--"
                    holder.binding!!.marks.text = "--/--"
                }
                if (list[position].subject != null){
                    holder.binding!!.txtTitle.text = list[position].subject!!.name!!
                }else{
                    holder.binding!!.txtTitle.text = "--/--"
                }
            }
            else{
                holder.binding!!.txtIncharge.text = "--/--"
                holder.binding!!.marks.text = "--/--"
                holder.binding!!.txtTitle.text = "--/--"
            }
        }
        else{
            if (position == 0) {
                holder.binding1!!.head.visibility = View.VISIBLE
            } else {
                holder.binding1!!.head.visibility = View.GONE
            }
            holder.binding1!!.subName.text = list[position].subject!!.name!!
            holder.binding1!!.marks.text = "${list[position].scored_marks!!}/${list[position].classTest!!.totalMarks!!}"
            if (position == list.size - 1) {
                holder.binding1!!.view.visibility = View.GONE
            } else {
                holder.binding1!!.view.visibility = View.VISIBLE
            }

//        holder.binding.answerSheet.setOnClickListener {
//            val url = row.answerSheet
//            if (!url.isNullOrEmpty()) {
//                val intent = Intent(Intent.ACTION_VIEW).apply {
//                    data = Uri.parse(url)
//                }
//                try {
//                    mActivity.startActivity(intent)
//                } catch (e: ActivityNotFoundException) {
//                    Toast.makeText(mActivity, "No application can open this link.", Toast.LENGTH_SHORT).show()
//                }
//            } else {
//                Toast.makeText(mActivity, "Invalid URL.", Toast.LENGTH_SHORT).show()
//            }
//        }
        }
    }

}