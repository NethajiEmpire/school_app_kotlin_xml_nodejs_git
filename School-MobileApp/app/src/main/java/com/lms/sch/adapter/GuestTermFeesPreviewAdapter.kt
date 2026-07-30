package com.lms.sch.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.ApplicationActivity
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardFeesShowBinding
import com.lms.sch.databinding.CardGuestFeesSelectBinding
import com.lms.sch.databinding.CardGuestFeesSelectChildBinding
import com.lms.sch.response.GuestFeesResponse
import com.lms.sch.utils.UiUtils

class GuestTermFeesPreviewAdapter (
    private val mActivity: ApplicationActivity,
    private val selectedValue : Int,
    private val list: ArrayList<GuestFeesResponse.Result.Terms>
) : RecyclerView.Adapter<GuestTermFeesPreviewAdapter.ViewHolder>() {
    inner class  ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding : CardFeesShowBinding = CardFeesShowBinding.bind(view)
    }

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_fees_show,parent,false)
        )
    }
    override fun getItemCount(): Int {
        return selectedValue
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.e("mnde", "selectedValue.. ${selectedValue}")
        /*if (position == selectedValue){

//            holder.binding.pAmt.text = ""+mActivity.payableAmt
            holder.binding.payableLay.visibility = View.VISIBLE
        }
        else {
            holder.binding.payableLay.visibility = View.GONE
        }*/
        holder.binding.title.text = list[position].name
        /*if (selectedPos.contains(position)){
            UiUtils.imageviewDrawable(holder.binding.box,R.drawable.checkbox_tick)
            UiUtils.imageviewDrawable(holder.binding.down,R.drawable.guest_up)
            UiUtils.linearLayoutBgDrawable(holder.binding.lin,R.drawable.border_curve_top_10dp)
            UiUtils.linearLayoutBgTint(holder.binding.lin,"#005DA7",null)
            UiUtils.textViewTextColor(holder.binding.title,"#FFFFFF",null)
            UiUtils.linearLayoutBgDrawable(holder.binding.top,R.drawable.border_line_curve_8dp_light_blue)
            holder.binding.recycler.visibility = View.VISIBLE
        }
        else {
            UiUtils.imageviewDrawable(holder.binding.box,R.drawable.checkbox_untick)
            UiUtils.imageviewDrawable(holder.binding.down,R.drawable.guest_down)
            UiUtils.linearLayoutBgDrawable(holder.binding.lin,R.drawable.border_curve_6dp)
            UiUtils.linearLayoutBgTint(holder.binding.lin,"#FFFFFF",null)
            UiUtils.textViewTextColor(holder.binding.title,"#333333",null)
            UiUtils.linearLayoutBgDrawable(holder.binding.top,R.drawable.border_line_curve_8dp_gb_grey)
            holder.binding.recycler.visibility = View.GONE
        }*/

        val adapter = GuestTermFeesChildAdapter(mActivity,list[position].types!!)
        val layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false)
        holder.binding.recycler.layoutManager = layoutManager
        holder.binding.recycler.adapter = adapter

        /*holder.binding.lin.setOnClickListener {
            if (selectedPos == position) {
                val previousPos = selectedPos
                selectedPos = RecyclerView.NO_POSITION
                notifyItemChanged(previousPos)
            } else {
                val previousPos = selectedPos
                selectedPos = position
                notifyItemChanged(previousPos)
                notifyItemChanged(selectedPos)
            }
        }*/
        /*holder.binding.lin.setOnClickListener {
            if (selectedPos.contains(position)) {
                val i = selectedPos.iterator()
                while (i.hasNext()) {
                    val selected = i.next()
                    if (selected >= position) {
                        i.remove()
                    }
                }
            } else {
                for (i in 0..position) {
                    selectedPos.add(i)
                }
            }
            mActivity.selectedValue = position
            notifyDataSetChanged()
        }*/
    }
}