package com.lms.sch.network.local

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.BuildConfig
import com.lms.sch.R
import com.lms.sch.databinding.CardApiBinding
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils
import java.io.File
import java.io.FileWriter
import java.io.IOException

class ApiDataAdapter(
    var activity: Activity,
    var context: Context,
    var list: ArrayList<ApiDataStore>,
) :
    RecyclerView.Adapter<ApiDataAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: CardApiBinding = CardApiBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.card_api,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val url = "${context.getString(R.string.url)} ${":"} ${list[position].url}"
        val method = "${context.getString(R.string.method)} ${":"} ${list[position].method}"
        val code = "${context.getString(R.string.response_code)} ${":"} ${list[position].responseCode}"
        val body = "${context.getString(R.string.request_body)} ${":"} ${list[position].body}"
        val header = "${context.getString(R.string.request_header)} ${":"} ${list[position].header}"
        val response = "${context.getString(R.string.response)} ${":"} ${list[position].response}"

        holder.binding.url.text = url
        holder.binding.responseCode.text = code
        holder.binding.method.text = method
        holder.binding.requestBody.text = body
        holder.binding.requestHeader.text = header

        if((response.length / 1024) <= 250){
            holder.binding.share.visibility = View.VISIBLE
            holder.binding.response.text = response
        }
        else{
            holder.binding.response.text = ""
            holder.binding.share.visibility = View.GONE
        }
        holder.binding.share.setOnClickListener {
            val shareContent = holder.binding.url.text.toString()+ "\n"+holder.binding.method.text.toString()+ "\n"+holder.binding.responseCode.text.toString()+ "\n"+holder.binding.requestBody.text.toString()+ "\n"+holder.binding.requestHeader.text.toString()+ "\n"+holder.binding.response.text.toString()
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type="text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareContent)
            activity.startActivity(Intent.createChooser(shareIntent,context.getString(R.string.share_with)))
        }

        holder.binding.response.setOnClickListener {
            holder.binding.response.isSingleLine = holder.binding.response.lineCount != 1
        }
        holder.binding.url.setOnClickListener {
            holder.binding.url.isSingleLine = holder.binding.url.lineCount != 1
        }
        holder.binding.responseCode.setOnClickListener {
            holder.binding.responseCode.isSingleLine = holder.binding.responseCode.lineCount != 1
        }
        holder.binding.method.setOnClickListener {
            holder.binding.method.isSingleLine = holder.binding.method.lineCount != 1
        }
        holder.binding.requestHeader.setOnClickListener {
            holder.binding.requestHeader.isSingleLine = holder.binding.requestHeader.lineCount != 1
        }
        holder.binding.requestBody.setOnClickListener {
            holder.binding.requestBody.isSingleLine = holder.binding.requestBody.lineCount != 1
        }

        if(list[position].isNew == 2){
            UiUtils.cardViewBgColor(holder.binding.card,null, R.color.grey)
        }
        else{
            UiUtils.cardViewBgColor(holder.binding.card,null, R.color.white)
        }
    }

    fun shareAll(){
        if(BaseUtils.isPermissionsEnabled(context, Constants.IntentKeys.STORAGE)){
            var sBody = ""
            for(items in list.reversed()){
                val url = "${context.getString(R.string.url)} ${":"} ${items.url}"
                val method = "${context.getString(R.string.method)} ${":"} ${items.method}"
                val code = "${context.getString(R.string.response_code)} ${":"} ${items.responseCode}"
                val body = "${context.getString(R.string.request_body)} ${":"} ${items.body}"
                val header = "${context.getString(R.string.request_header)} ${":"} ${items.header}"
                val response = "${context.getString(R.string.response)} ${":"} ${items.response}"
                val shareContent = "${"\n"}${url}${"\n"}${method}${"\n"}${code}${"\n"}${body}${"\n"}${header}${"\n"}${response}${"\n"}"
                sBody += UiUtils.convertHtml("<b>$shareContent</b>")
            }
            generateNoteOnSD(sBody)
        }
        else{
            if(BaseUtils.isDeniedPermission(activity,Constants.IntentKeys.STORAGE)){
                BaseUtils.permissionsEnableRequest(activity, Constants.IntentKeys.STORAGE)
            }
            else {
                BaseUtils.displayManuallyEnablePermissionsDialog(activity,Constants.IntentKeys.STORAGE,null)
            }
        }
    }

    private fun generateNoteOnSD(sBody: String) {
        try {
            // val root = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),"Notes")
            // val root = File(Environment.getExternalStorageDirectory(), "Notes")
            val root = File(context.getExternalFilesDir(null), "API")
            if (!root.exists()) {
                root.mkdirs()
            }
            val file = File(root, "Api_Requests.doc")
            val writer = FileWriter(file)
            writer.append(sBody)
            writer.flush()
            writer.close()
            val uri: Uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", file)
            val share = Intent()
            share.action = Intent.ACTION_SEND
            share.type = "application/pdf"
            share.putExtra(Intent.EXTRA_STREAM, uri)
            activity.startActivity(share)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

