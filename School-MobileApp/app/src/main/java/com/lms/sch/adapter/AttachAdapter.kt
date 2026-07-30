package com.lms.sch.adapter

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.activity.ImageViewActivity
import com.lms.sch.activity.PdfViewerActivity
import com.lms.sch.databinding.CardAttachmentBinding
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils
import java.util.ArrayList

class AttachAdapter(
    var mActivity: BaseActivity,
    var list: ArrayList<String>
) : RecyclerView.Adapter<AttachAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: CardAttachmentBinding = CardAttachmentBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(
                R.layout.card_attachment,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.txt.text = "Attachment ${position + 1}"

        if (list[position].endsWith(".png")) {
            UiUtils.imageviewDrawable(holder.binding.img, R.drawable.ic_png)
        }
        else if (list[position].endsWith(".pdf")) {
            UiUtils.imageviewDrawable(holder.binding.img, R.drawable.pdf)
        }
        else if (list[position].endsWith(".jpg")) {
            UiUtils.imageviewDrawable(holder.binding.img, R.drawable.ic_jpg)
        }
        else if (list[position].endsWith(".jpeg")) {
            UiUtils.imageviewDrawable(holder.binding.img, R.drawable.ic_jpeg)
        }
        holder.binding.root.setOnClickListener {
            if (list[position] != null && list[position].isNotEmpty()){
                if (list[position].endsWith(".pdf")){
                    val bundle = Bundle()
                    bundle.putString(Constants.IntentKeys.KEY, list[position])
                    BaseUtils.startActivity(mActivity, PdfViewerActivity(),bundle,false)
                }
                else if (list[position].endsWith(".png") || list[position].endsWith(".jpeg") || list[position].endsWith(".jpg")){
                    val doc = ArrayList<String>()
                    doc.add(list[position])
                    val bundle = Bundle()
                    bundle.putSerializable(Constants.IntentKeys.KEY,doc)
                    BaseUtils.startActivity(mActivity, ImageViewActivity(),bundle,false)
                }
                else {
                    val url = list[position]
                    Log.e("jhg",url)
                    if (!url.isNullOrBlank()) {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse(url)
                        }
                        try {
                            mActivity.startActivity(intent)
                        } catch (e: ActivityNotFoundException) {
                            Toast.makeText(mActivity, "No application can open this link.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(mActivity, "Invalid URL.", Toast.LENGTH_SHORT).show()
                    }

                }
            }
            else {
                UiUtils.showSnack("We are unable to fetch the Image",holder.binding.root,false)
            }
        }
    }
}