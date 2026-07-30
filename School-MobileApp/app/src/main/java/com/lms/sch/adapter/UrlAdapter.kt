package com.lms.sch.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.ImageViewActivity
import com.lms.sch.databinding.CardUploadDocsBinding
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils
import com.lms.sch.interfaces.UrlActionListener
import org.json.JSONObject
import java.util.ArrayList

class UrlAdapter(
    var context: Activity,
    var list: ArrayList<JSONObject>,
    var urlActionListener: UrlActionListener
) : RecyclerView.Adapter<UrlAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: CardUploadDocsBinding = CardUploadDocsBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.card_upload_docs,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val name = list[position].optString("name")
        val size = list[position].optString("size")
        val type = list[position].optString("type")
        holder.binding.txtTitle.text = name
        holder.binding.size.text = size
        holder.binding.type.text = when(type){
            "application/msword" -> "doc"
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> "docx"
            "application/vnd.ms-excel" -> "xls"
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" -> "xlsx"
            "image/jpeg" -> "jpeg"
            "application/pdf" -> "pdf"
            else -> "N/A"
        }
        if (position == list.size -1){
            holder.binding.v1.visibility = View.GONE
        }else{
            holder.binding.v1.visibility = View.VISIBLE
        }
        when (type){
            "image/jpg" -> {
                UiUtils.imageviewDrawable(holder.binding.img, R.drawable.ic_jpg)
            }
            "image/png" -> {
                UiUtils.imageviewDrawable(holder.binding.img, R.drawable.ic_png)
            }
            "image/jpeg" -> {
                UiUtils.imageviewDrawable(holder.binding.img, R.drawable.ic_jpeg)
            }
            "application/pdf" -> {
                UiUtils.imageviewDrawable(holder.binding.img, R.drawable.pdf)
            }
        }
        holder.binding.close.setOnClickListener {
            urlActionListener.onRemoveFile(position)
            if (list.isEmpty()) {
                urlActionListener.onFileEmpty()
            }
            notifyDataSetChanged()
        }
        holder.binding.root.setOnClickListener {
            urlActionListener.onClickFile(position)
        }

    }
}