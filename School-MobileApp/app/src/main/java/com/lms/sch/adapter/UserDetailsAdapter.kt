package com.lms.sch.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.ImageViewActivity
import com.lms.sch.databinding.CardUserDetailsBinding
import com.lms.sch.session.Constants
import org.json.JSONArray

class UserDetailsAdapter(var context: Context,var list: JSONArray) : RecyclerView.Adapter<UserDetailsAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: CardUserDetailsBinding = CardUserDetailsBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate( R.layout.card_user_details, parent,   false )
        )
    }

    override fun getItemCount(): Int {
        return list.length()
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list.getJSONObject(position)
        val type = list.getJSONObject(position).getString("type")
        if (type == "text"){
            holder.binding.textLay.visibility = View.VISIBLE
            holder.binding.docLay.visibility = View.GONE
            holder.binding.label.text = list.getJSONObject(position).getString("label")
            holder.binding.value.text = list.getJSONObject(position).getString("value")
            Log.e("nfkd",list.getJSONObject(position).getString("value"))
        }
        else {
            holder.binding.textLay.visibility = View.GONE
            holder.binding.docLay.visibility = View.VISIBLE
            holder.binding.txtTitle.text = list.getJSONObject(position).getString("label")
            val docUrl = list.getJSONObject(position).getString("value")
//            holder.binding.type.text = list.getJSONObject(position).getString("type")
//            holder.binding.size.text = list.getJSONObject(position).getString("size")
//            if (position == list.length() - 1){
//                holder.binding.view.visibility = View.GONE
//            }
//            else {
//                holder.binding.view.visibility = View.VISIBLE
//            }
            if (position == list.length() - 1) {
                holder.binding.v1.visibility = View.GONE
            } else {
                holder.binding.v1.visibility = View.VISIBLE
            }
            holder.binding.docLay.setOnClickListener {
                if (!docUrl.isNullOrEmpty() && docUrl != "--/--") {
                    val intent = android.content.Intent(context, ImageViewActivity::class.java)
                    intent.putStringArrayListExtra(Constants.IntentKeys.KEY, arrayListOf(docUrl))
                    context.startActivity(intent)
                }else{
                }
            }
        }
    }

}