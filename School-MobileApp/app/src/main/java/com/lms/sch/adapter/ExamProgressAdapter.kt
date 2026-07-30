package com.lms.sch.adapter

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardClassTestReportBinding
import com.lms.sch.response.ExamResultResponse
import com.lms.sch.response.GetExamResponse
import kotlin.math.log

class ExamProgressAdapter (
    val mActivity: BaseActivity,
    val list: ArrayList<ExamResultResponse.Rows>
): RecyclerView.Adapter<ExamProgressAdapter.ViewHolder>()  {
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        var binding: CardClassTestReportBinding = CardClassTestReportBinding.bind(view)
    }
    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_class_test_report,parent,false)
        )
    }
    override fun onBindViewHolder(  holder: ViewHolder, position: Int) {
        val row = list[position]
        if (position == 0) {
            holder.binding.head.visibility = View.VISIBLE
        } else {
            holder.binding.head.visibility = View.GONE
        }
        holder.binding.subName.text = row.subject?.name ?: "--"
        holder.binding.marks.text = "${row.scoredMark}/${row.totalMark}"
        if (position == list.size - 1) {
            holder.binding.view.visibility = View.GONE
        } else {
            holder.binding.view.visibility = View.VISIBLE
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
    override fun getItemCount(): Int {
        return list.size
    }
}