package com.lms.sch.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.adapter.GetAcademicBatchAdapter
import com.lms.sch.databinding.CardAcademicBinding
import com.lms.sch.response.GetAcademicBatchResponse
import com.lms.sch.response.GetAcademicStandardResponse

class GetAcademicStandardAdapter (
    val mActivity: BaseActivity,
    val list: ArrayList<GetAcademicStandardResponse.Result>

) : RecyclerView.Adapter<GetAcademicStandardAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: CardAcademicBinding = CardAcademicBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_academic, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }
}