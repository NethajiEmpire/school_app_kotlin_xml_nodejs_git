package com.lms.sch.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardAcademicBinding
import com.lms.sch.databinding.CardAcademicBoardBinding
import com.lms.sch.databinding.CardAcademicSectionBinding
import com.lms.sch.databinding.CardAcademicStandardBinding
import com.lms.sch.databinding.CardAcademicSubjectBinding
import com.lms.sch.response.GetAcademicBoardResponse
import com.lms.sch.response.GetAcademicStandardResponse
import com.lms.sch.response.GetAcademicSubjectResponse
import com.lms.sch.response.GetSectionResponse
import com.lms.sch.utils.UiUtils

class GetAcademicBoardAdapter (
    val mActivity: BaseActivity,
    val boardList: ArrayList<GetAcademicBoardResponse.Result>,
    val stdList: ArrayList<GetAcademicStandardResponse.Result>,
    val secList: ArrayList<GetSectionResponse.Result>,
    var type: Int
) : RecyclerView.Adapter<GetAcademicBoardAdapter.ViewHolder>() {
    inner class ViewHolder(view: View, type: Int) : RecyclerView.ViewHolder(view) {
        var binding: CardAcademicStandardBinding ?= null
        var binding1: CardAcademicSectionBinding ?= null
        var binding2: CardAcademicBoardBinding ?= null

        init {
            if (type == 1){
                binding = CardAcademicStandardBinding.bind(view)
            }
            else if (type == 2){
                binding1 = CardAcademicSectionBinding.bind(view)
            }
            else if (type == 0) {
                binding2 = CardAcademicBoardBinding.bind(view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (type == 1) {
            return ViewHolder(
                LayoutInflater.from(mActivity).inflate(R.layout.card_academic_standard, parent, false),
                type
            )
        }
        else if (type == 2){
            return ViewHolder(
                LayoutInflater.from(mActivity).inflate(R.layout.card_academic_section, parent, false),
                type
            )
        }
        else {
            return ViewHolder(
                LayoutInflater.from(mActivity).inflate(R.layout.card_academic_board, parent, false),
                type
            )
        }
    }

    override fun getItemCount(): Int {
        if (type == 0) {
            return boardList.size
        }
        else if (type == 1){
            return stdList.size
        }
        else if (type == 2){
            return secList.size
        }
       /* else if (type == 3)
        {
            return boardList3.size
        }*/
        else{
            return  0
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (type == 0) {
            Log.e("lkjhkj", type.toString())

            if (boardList[position].boardId != null) {
                holder.binding2!!.boardId.text = boardList[position].boardId
            } else {
                holder.binding2!!.boardId.text = "--/--"
            }
            if (boardList[position].name != null) {
                holder.binding2!!.name.text = boardList[position].name
            } else {
                holder.binding2!!.name.text = "--/--"
            }
            if (boardList[position].classCount != null) {
                holder.binding2!!.clss.text = ""+boardList[position].classCount+"+"
            } else {
                holder.binding2!!.clss.text = "--/--"
            }
            if (boardList[position].studentCount != null) {
                holder.binding2!!.studentCounts.text = ""+boardList[position].studentCount+"+"
            } else {
                holder.binding2!!.studentCounts.text = "--/--"
            }
            val colors = arrayListOf("#4CA5D5", "#FF7043", "#66BB6A", "#26C6DA", "#AB47BC")
            val pos = position % colors.size
            UiUtils.viewBgTint(holder.binding2!!.view, colors[pos], null)

            when (boardList[position].status) {
                "active" -> {
                    holder.binding2!!.status.text = "Active"
                    UiUtils.textviewCustomDrawable(holder.binding2!!.status,R.drawable.border_curve_24dp)
                    UiUtils.textViewBgTint(holder.binding2!!.status, "#E5F8ED", null)
                    UiUtils.textViewTextColor(holder.binding2!!.status, "#28C76F", null)
                }

                "inactive" -> {
                    holder.binding2!!.status.text = "Inactive"
                    UiUtils.textviewCustomDrawable(holder.binding2!!.status,R.drawable.border_curve_24dp)
                    UiUtils.textViewBgTint(holder.binding2!!.status, "#FFF2DE", null)
                    UiUtils.textViewTextColor(holder.binding2!!.status, "#F39519", null)
                }
            }
        }
        else if (type == 1){
            if (stdList[position].standardId != null) {
                holder.binding!!.batchId.text = stdList[position].standardId
            } else {
                holder.binding!!.batchId.text = "--/--"
            }
            if (stdList[position].name != null) {
                holder.binding!!.batchYear.text = UiUtils.getOrdinalSuffix(stdList[position].name!!.toInt())
            } else {
                holder.binding!!.batchYear.text = "--/--"
            }
            if (stdList[position].sectionCount != null) {
                holder.binding!!.clsCount.text = ""+stdList[position].sectionCount+"+"
            } else {
                holder.binding!!.clsCount.text = "--/--"
            }
            if (stdList[position].studentCount != null) {
                holder.binding!!.studentCounts.text = ""+stdList[position].studentCount+"+"
            } else {
                holder.binding!!.studentCounts.text = "--/--"
            }
            val colors = arrayListOf(
                "#F5FAFF", // Ultra Light Blue
                "#FFF8F5", // Ultra Light Coral
                "#F5FBF6", // Ultra Light Green
                "#F5FEFF", // Ultra Light Aqua
                "#FAF5FB", // Ultra Light Purple
                "#FFFFF5", // Ultra Light Yellow
                "#FFFBF5", // Ultra Light Amber
            )
            val pos = position % colors.size
//            "#F8F5FF"  // Ultra Light Lavender
            UiUtils.linearLayoutBgTint(holder.binding!!.lin, colors[pos], null)
            if (stdList[position].img_url != null && stdList[position].img_url!!.startsWith("https")) {
                Glide.with(mActivity).load(stdList[position].img_url).into(holder.binding!!.img)
            }

            when (stdList[position].status) {
                "active" -> {
                    holder.binding!!.status.text = "Active"
                    UiUtils.textviewCustomDrawable(holder.binding!!.status, R.drawable.border_curve_16dp)
                    UiUtils.textViewBgTint(holder.binding!!.status, "#E5F8ED", null)
                    UiUtils.textViewTextColor(holder.binding!!.status, "#28C76F", null)
                }
                "inactive" -> {
                    holder.binding!!.status.text = "Inactive"
                    UiUtils.textviewCustomDrawable(holder.binding!!.status, R.drawable.border_curve_16dp)
                    UiUtils.textViewBgTint(holder.binding!!.status, "#FFF2DE", null)
                    UiUtils.textViewTextColor(holder.binding!!.status, "#F39519", null)
                }
            }
        }
        else if (type == 2){
            if (secList[position].sectionNo != null) {
                holder.binding1!!.name.text = "Section - ${secList[position].name}"
            } else {
                holder.binding1!!.name.text = "--/--"
            }
            /*if (secList[position].description != null) {
                holder.binding1!!.desc.setContent(secList[position].description)
            } else {
                holder.binding1!!.desc.setContent("--/--")
            }*/

            if (secList[position].sectionId != null) {
                holder.binding1!!.secId.text = "Sec ID\n${secList[position].sectionId}"
            } else {
                holder.binding1!!.secId.text = "--/--"
            }
            if (secList[position].studentCount != null) {
                holder.binding1!!.studentCount.text = ""+secList[position].studentCount+"+"
            } else {
                holder.binding1!!.studentCount.text = "--/--"
            }
            if (secList[position].classCount != null) {
                holder.binding1!!.clsCount.text = ""+secList[position].classCount+"+"
            } else {
                holder.binding1!!.clsCount.text = "--/--"
            }
            if (secList[position].img_url != null && secList[position].img_url!!.startsWith("https")) {
                Glide.with(mActivity).load(secList[position].img_url).into(holder.binding1!!.img)
            }

            when (secList[position].status) {
                "active" -> {
                    holder.binding1!!.status.text = "Active"
                    UiUtils.textviewCustomDrawable(holder.binding1!!.status, R.drawable.border_curve_24dp)
                    UiUtils.textViewBgTint(holder.binding1!!.status, "#E5F8ED", null)
                    UiUtils.textViewTextColor(holder.binding1!!.status, "#28C76F", null)
                }
                "inactive" -> {
                    holder.binding1!!.status.text = "Inactive"
                    UiUtils.textviewCustomDrawable(holder.binding1!!.status, R.drawable.border_curve_24dp)
                    UiUtils.textViewBgTint(holder.binding1!!.status, "#FFF2DE", null)
                    UiUtils.textViewTextColor(holder.binding1!!.status, "#F39519", null)
                }
            }
        }
    }
}