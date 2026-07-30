package com.lms.sch.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.ApplicationActivity
import com.lms.sch.databinding.CardGuestFeesSelectBinding
import com.lms.sch.response.GuestFeesResponse
import com.lms.sch.utils.UiUtils
import org.json.JSONArray
import org.json.JSONObject

class GuestTermFeesAdapter (
    private val mActivity: ApplicationActivity,
    private val list: ArrayList<GuestFeesResponse.Result.Terms>
) : RecyclerView.Adapter<GuestTermFeesAdapter.ViewHolder>() {
    inner class  ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding : CardGuestFeesSelectBinding = CardGuestFeesSelectBinding.bind(view)
    }

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_guest_fees_select,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.title.text = list[position].name
        if (mActivity.selectedPos.contains(position)){
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
        }

        val adapter = GuestTermFeesChildAdapter(mActivity,list[position].types!!)
        val layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false)
        holder.binding.recycler.layoutManager = layoutManager
        holder.binding.recycler.adapter = adapter

        /*holder.binding.lin.setOnClickListener {
            if (mActivity.selectedPos.contains(position)) {
                val iterator = mActivity.selectedPos.iterator()
                while (iterator.hasNext()) {
                    val selected = iterator.next()
                    if (selected >= position) {
                        iterator.remove()
                    }
                }

                val updatedArr = JSONArray()
                for (i in 0 until mActivity.termsArr.length()) {
                    val obj = mActivity.termsArr.getJSONObject(i)
                    if (obj.getString("_id") != list[position]._id) {
                        updatedArr.put(obj)
                    }
                }
                mActivity.termsArr = updatedArr

                Log.e("mnde", "Removed: ${mActivity.termsArr.toString()}")
            } else {
                for (i in 0..position) {
                    if (!mActivity.selectedPos.contains(i)) {
                        mActivity.selectedPos.add(i)
                    }
                }

                if (mActivity.selectedPos.contains(position)) {
                    val newTerm = JSONObject()
                    newTerm.put("_id", list[position]._id)
                    if (!mActivity.termsArr.toString().contains(list[position]._id!!)) {
                        mActivity.termsArr.put(newTerm)
                    }
                }


                Log.e("mnde", "Added: ${mActivity.termsArr.toString()}")
            }

            if (position == 2) {
                mActivity.binding.includePayment1.lin.performClick()
            }

            mActivity.selectedValue = position + 1
            notifyDataSetChanged()
        }*/

        holder.binding.lin.setOnClickListener {

            if (mActivity.selectedPos.contains(position)) {
                mActivity.selectedPos.removeAll { it >= position }

                val updatedArr = JSONArray()
                val selectedIds = mActivity.selectedPos.map { list[it]._id }
                for (i in 0 until mActivity.termsArr.length()) {
                    val obj = mActivity.termsArr.getJSONObject(i)
                    if (selectedIds.contains(obj.getString("_id"))) {
                        updatedArr.put(obj)
                    }
                }
                mActivity.termsArr = updatedArr
                mActivity.selectedValue = mActivity.selectedPos.size

                Log.e("mnde", "Removed: ${mActivity.termsArr.toString()}")
            } else {
                // Handle selection
                for (i in 0..position) {
                    if (!mActivity.selectedPos.contains(i)) {
                        mActivity.selectedPos.add(i)
                        val newTerm = JSONObject()
                        newTerm.put("_id", list[i]._id)
                        if (!mActivity.termsArr.toString().contains(list[i]._id!!)) {
                            mActivity.termsArr.put(newTerm)
                        }
                    }
                }

                Log.e("mnde", "Added: ${mActivity.termsArr.toString()}")
            }
            if (position == 2) {
                mActivity.binding.includePayment1.lin.performClick()
            }
            mActivity.selectedValue = mActivity.selectedPos.size
            notifyDataSetChanged()
        }


    }
}