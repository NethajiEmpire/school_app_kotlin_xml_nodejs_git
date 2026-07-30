package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.FeeStructureActivity
import com.lms.sch.activity.GuestLandingActivity
import com.lms.sch.databinding.CardAdmissionBinding
import com.lms.sch.models.AdmissionProcess
import com.lms.sch.utils.UiUtils


class AdmissionProcessAdapter (
    val context: GuestLandingActivity,
    private val list: ArrayList<AdmissionProcess>
): RecyclerView.Adapter<AdmissionProcessAdapter.ViewHolder>(){
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding: CardAdmissionBinding = CardAdmissionBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.card_admission, parent, false)

        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = list[position]
        holder.binding.one.text = item.admNo
        holder.binding.name.text = item.regAcc

        if (item.regimg == "Register Your Account")
        {
            UiUtils.imageviewDrawable(holder.binding.regImage, R.drawable.onlinebill)
        } else if (item.regimg == "Fee")
        {
            UiUtils.imageviewDrawable(holder.binding.regImage, R.drawable.registerfee)
        } else if (item.regimg == "Doc")
        {
            UiUtils.imageviewDrawable(holder.binding.regImage, R.drawable.docverrify)
        } else if (item.regimg == "Form")
        {
            UiUtils.imageviewDrawable(holder.binding.regImage, R.drawable.applyform)
        } else if (item.regimg == "Payment")
        {
            UiUtils.imageviewDrawable(holder.binding.regImage, R.drawable.fepay)
        } else if (item.regimg == "Approval")
        {
            UiUtils.imageviewDrawable(holder.binding.regImage, R.drawable.schapp)
        }
    }

}


