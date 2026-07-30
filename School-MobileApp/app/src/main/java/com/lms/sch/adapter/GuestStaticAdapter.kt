package com.lms.sch.adapter

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.text.Editable
import android.text.Html
import android.text.InputFilter
import android.text.InputType
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.widget.SearchView
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.ApplicationActivity
import com.lms.sch.activity.ApplicationStaticActivity
import com.lms.sch.databinding.CardGuestBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.network.ApiConnection
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
//import org.jetbrains.anko.sdk27.coroutines.onCheckedChange
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.util.Calendar
import java.util.regex.Matcher
import java.util.regex.Pattern


class GuestStaticAdapter(var activity: ApplicationStaticActivity, var list: JSONArray) : RecyclerView.Adapter<GuestStaticAdapter.ViewHolder>() {
    var selectedPosition = 0
    var casteListStr: ArrayList<String> = ArrayList()
    var countryListStr: ArrayList<String> = ArrayList()
    var stateListStr: ArrayList<String> = ArrayList()
//    var cityList:ArrayList<GetCityResponse.City> = ArrayList()
    var cityListStr: ArrayList<String> = ArrayList()
    var countryId = ""
    var stateId = ""
    var cityId = ""
    var govtIdentifier : ArrayList<String> = ArrayList()
    var modeofEducation : ArrayList<String> = ArrayList()
//    var graduationList:ArrayList<GetGraduationResponse.Graduation> = ArrayList()
    var graduationListStr: ArrayList<String> = ArrayList()
    var graduationId = ""
//    var degreeList:ArrayList<GetGraduationResponse.Graduation> = ArrayList()
    var degreeListStr: ArrayList<String> = ArrayList()
    var degreeId = ""
//    var courseList:ArrayList<GetCourseListResponse.Course> = ArrayList()
    var courseListStr: ArrayList<String> = ArrayList()
    var courseId = ""

    var genderlist: ArrayList<String> = ArrayList()
    var bloodGroupList: ArrayList<String> = ArrayList()
    var nationalityList: ArrayList<String> = ArrayList()
    var religoiusList: ArrayList<String> = ArrayList()
    var categoryList: ArrayList<String> = ArrayList()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: CardGuestBinding = CardGuestBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(activity).inflate(
                R.layout.card_guest,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.length()
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val type:String = list.getJSONObject(position).optString("type")
        val title:String = list.getJSONObject(position).optString("title")
        val hint:String = list.getJSONObject(position).optString("hint")
        val hintTxt:String = list.getJSONObject(position).optString("hintText")
        val showValue:String = list.getJSONObject(position).optString("showValue")
        val value:String = list.getJSONObject(position).optString("value")
        val isForiegn:Boolean = list.getJSONObject(position).optBoolean("visible",false)
//        holder.itemView.visibility = if (isForiegn && title == "Passport Number") View.VISIBLE else View.GONE
        UiUtils.log("iuyg","title----"+title)

        if(title.isNotEmpty()){
            holder.binding.title.visibility = View.VISIBLE
            if(title.contains("*")){
                val name = getColoredSpanned(title.replace(" *",""), "#FF000000")
                val surName = getColoredSpanned("*", "#B32124")
                holder.binding.title.text = Html.fromHtml(name+" "+surName,FROM_HTML_MODE_LEGACY)
            }
            else{
                holder.binding.title.text = title
            }
        }
        else{
            holder.binding.title.visibility = View.GONE
        }
        if(showValue.isNotEmpty()){
            holder.binding.edt.setText(""+showValue)
        }
        else{
//            holder.binding.edt.setText("")
            holder.binding.edt.hint = hintTxt
        }

        if(title == "ABC ID"){
            holder.binding.edtTxt.visibility = View.VISIBLE
            holder.binding.edtTxt.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.abc.gov.in/"))
                activity.startActivity(browserIntent)
            }
        }
        else{
            holder.binding.edtTxt.visibility = View.GONE
        }

        if(type == "editText"){
            var isUpdating = false
            if(hint == "Mobile Number"){
                holder.binding.edt.isFocusable = false
            }
            else if (hint == "Pincode"){
                holder.binding.edt.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun afterTextChanged(p0: Editable?) {
                        if (isUpdating) return
                        if (p0.toString().length == 6) {
                            DialogUtils.showLoader(activity)
                            ApiConnection.getInstance().pincode(activity,p0.toString()).observe(activity) {
                                DialogUtils.dismissLoader()
                                it.success.let { success ->
                                    if (success) {
                                        if (it.result != null && it.result.isNotEmpty()) {
                                            isUpdating = true
                                            list.getJSONObject(position+1).put("showValue","India")
                                            list.getJSONObject(position+1).put("value","India")
                                            list.getJSONObject(position+2).put("showValue",it.result[0].stateName!!)
                                            list.getJSONObject(position+2).put("value",it.result[0].stateName!!)
                                            list.getJSONObject(position+3).put("showValue",it.result[0].district!!)
                                            list.getJSONObject(position+3).put("value",it.result[0].district!!)
                                            val viewHolder1 = activity.binding.recycler.findViewHolderForAdapterPosition(position+1) as ViewHolder
                                            val viewHolder2 = activity.binding.recycler.findViewHolderForAdapterPosition(position+2) as ViewHolder
                                            val viewHolder3 = activity.binding.recycler.findViewHolderForAdapterPosition(position+3) as ViewHolder
                                            viewHolder1.binding.edt.setText("India")
                                            viewHolder2.binding.edt.setText(it.result[0].stateName!!)
                                            viewHolder3.binding.edt.setText(it.result[0].district!!)
                                            isUpdating = false
                                        } else {
                                            UiUtils.showSnack(it.msg, holder.binding.root,false)
                                        }
                                    } else {
                                        UiUtils.showSnack(it.msg, holder.binding.root,false)
                                    }
                                }
                            }
                        }
                    }
                })
            }
            else if(hint == "Lead ID"){
                holder.binding.edt.isFocusable = false
            }
            else{
                holder.binding.edt.isFocusable = true
            }

            holder.binding.checkbox.visibility = View.GONE
            holder.binding.txt.visibility = View.GONE
            holder.binding.edtBig.visibility = View.GONE
            holder.binding.edt.visibility = View.VISIBLE
            UiUtils.editTextImgDrawable(holder.binding.edt,null,Constants.IntentKeys.END)
            val inputType:String = list.getJSONObject(position).optString("inputType")
            if(inputType == "text"){
                holder.binding.edt.inputType = InputType.TYPE_CLASS_TEXT
            }
            else if(inputType == "name"){
                holder.binding.edt.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
                holder.binding.edt.setFilters(arrayOf(getEditTextFilter()))
            }
            else if(inputType == "number"){
                holder.binding.edt.inputType = InputType.TYPE_CLASS_NUMBER
            }
            else if(inputType == "email"){
                holder.binding.edt.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            }
            else{
                holder.binding.edt.inputType = InputType.TYPE_CLASS_TEXT
            }
        }
        else if (type == "address"){
            holder.binding.checkbox.visibility = View.GONE
            holder.binding.txt.visibility = View.GONE
            holder.binding.edt.visibility = View.GONE
            holder.binding.edtBig.visibility = View.VISIBLE
            if(showValue.isNotEmpty()){
                holder.binding.edtBig.setText(""+showValue)
            }
            else{
//            holder.binding.edt.setText("")
                holder.binding.edtBig.hint = hintTxt
            }
        }
        else if(type == "spinner"){
            holder.binding.checkbox.visibility = View.GONE
            holder.binding.edtBig.visibility = View.GONE
            holder.binding.txt.visibility = View.GONE
            holder.binding.edt.visibility = View.VISIBLE
            UiUtils.editTextImgDrawable(holder.binding.edt,R.drawable.dropdown,Constants.IntentKeys.END)
            holder.binding.edt.isFocusable = false

            holder.binding.edt.setOnClickListener {
                openDialog(hint,position)
            }

            activity.binding.spinnerList.search.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val searchText = p0.toString().lowercase()
                    if (hint == "Gender") {
                        val slist: ArrayList<String> = ArrayList()
                        for (item in genderlist) {
                            if (item.contains(searchText, true)) {
                                slist.add(item)
                            }
                        }
                        if (slist.isNotEmpty()) {
                            activity.loadSpinnerRecycler(slist, position)
                        }
                        else {
                            activity.loadSpinnerRecycler(genderlist, position)
                        }
                    }
                    else if (hint == "Blood Group") {
                        val slist: ArrayList<String> = ArrayList()
                        for(items in bloodGroupList){
                            if(items.contains(searchText,true)){
                                slist.add(items)
                            }
                        }
                        if (slist.isNotEmpty()){
                            activity.loadSpinnerRecycler(slist, position)
                        }
                        else {
                            activity.loadSpinnerRecycler(bloodGroupList, position)
                        }
                    }
                    else if (hint == "Nationality") {
                        val slist: ArrayList<String> = ArrayList()
                        for(items in nationalityList){
                            if(items.contains(searchText,true)){
                                slist.add(items)
                            }
                        }
                        if (slist.isNotEmpty()){
                            activity.loadSpinnerRecycler(slist, position)
                        }
                        else {
                            activity.loadSpinnerRecycler(nationalityList, position)
                        }
                    }
                    else if (hint == "Religion") {
                        val slist: ArrayList<String> = ArrayList()
                        for(items in religoiusList){
                            if(items.contains(searchText,true)){
                                slist.add(items)
                            }
                        }
                        if (slist.isNotEmpty()){
                            activity.loadSpinnerRecycler(slist, position)
                        }
                        else {
                            activity.loadSpinnerRecycler(religoiusList, position)
                        }
                    }
                    else if (hint == "Category") {
                        val slist: ArrayList<String> = ArrayList()
                        for(items in categoryList){
                            if(items.contains(searchText,true)){
                                slist.add(items)
                            }
                        }
                        if (slist.isNotEmpty()){
                            activity.loadSpinnerRecycler(slist, position)
                        }
                        else {
                            activity.loadSpinnerRecycler(categoryList, position)
                        }
                    }
                }
                override fun afterTextChanged(p0: Editable?) {}
            })

        }
        else if(type == "calender"){
            holder.binding.checkbox.visibility = View.GONE
            holder.binding.txt.visibility = View.GONE
            holder.binding.edtBig.visibility = View.GONE
            holder.binding.edt.visibility = View.VISIBLE
            UiUtils.editTextImgDrawable(holder.binding.edt,R.drawable.uil_calender,Constants.IntentKeys.END)
            holder.binding.edt.isFocusable = false
            holder.binding.edt.setOnClickListener {
                showCalender(holder.binding.edt)
            }
        }
        else if(type == "checkBox"){
            holder.binding.checkbox.text = hint
            holder.binding.checkbox.visibility = View.VISIBLE
            holder.binding.edt.visibility = View.GONE
            holder.binding.edtBig.visibility = View.GONE
            holder.binding.txt.visibility = View.GONE
            if(value == "checked"){
                holder.binding.checkbox.isChecked = true
            }
            else{
                holder.binding.checkbox.isChecked = false
            }

            holder.binding.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    list.getJSONObject(position).put("value","checked")
                    if(hint == "Same As Permanent Address"){
                        list.getJSONObject(7).put("showValue",list.getJSONObject(0).getString("showValue"))
                        list.getJSONObject(8).put("showValue",list.getJSONObject(1).getString("showValue"))
                        list.getJSONObject(9).put("showValue",list.getJSONObject(2).getString("showValue"))
                        list.getJSONObject(10).put("showValue",list.getJSONObject(3).getString("showValue"))
                        list.getJSONObject(11).put("showValue",list.getJSONObject(4).getString("showValue"))
                        list.getJSONObject(12).put("showValue",list.getJSONObject(5).getString("showValue"))
                        list.getJSONObject(7).put("value",list.getJSONObject(0).getString("value"))
                        list.getJSONObject(8).put("value",list.getJSONObject(1).getString("value"))
                        list.getJSONObject(9).put("value",list.getJSONObject(2).getString("value"))
                        list.getJSONObject(10).put("value",list.getJSONObject(3).getString("value"))
                        list.getJSONObject(11).put("value",list.getJSONObject(4).getString("value"))
                        list.getJSONObject(12).put("value",list.getJSONObject(5).getString("value"))
                       /* notifyItemChanged(7)
                        notifyItemChanged(8)
                        notifyItemChanged(9)
                        notifyItemChanged(10)
                        notifyItemChanged(11)
                        notifyItemChanged(12)*/
                        /*val viewHolder1 = activity.binding.recycler2.findViewHolderForAdapterPosition(7) as GuestAdapter.ViewHolder
                        val viewHolder2 = activity.binding.recycler2.findViewHolderForAdapterPosition(8) as GuestAdapter.ViewHolder
                        val viewHolder3 = activity.binding.recycler2.findViewHolderForAdapterPosition(9) as GuestAdapter.ViewHolder
                        val viewHolder4 = activity.binding.recycler2.findViewHolderForAdapterPosition(10) as GuestAdapter.ViewHolder
                        val viewHolder5 = activity.binding.recycler2.findViewHolderForAdapterPosition(11) as GuestAdapter.ViewHolder
                        val viewHolder6 = activity.binding.recycler2.findViewHolderForAdapterPosition(12) as GuestAdapter.ViewHolder
                        viewHolder1.binding.edt.setText(list.getJSONObject(7).optString("showValue"))
                        viewHolder2.binding.edt.setText(list.getJSONObject(8).optString("showValue"))
                        viewHolder3.binding.edt.setText(list.getJSONObject(9).optString("showValue"))
                        viewHolder4.binding.edt.setText(list.getJSONObject(10).optString("showValue"))
                        viewHolder5.binding.edt.setText(list.getJSONObject(11).optString("showValue"))
                        viewHolder6.binding.edt.setText(list.getJSONObject(12).optString("showValue"))*/
                    }
                }
                else{
                    list.getJSONObject(position).put("value","")
                    if(hint == "Same As Permanent Address"){
                        list.getJSONObject(7).put("showValue","")
                        list.getJSONObject(8).put("showValue","")
                        list.getJSONObject(9).put("showValue","")
                        list.getJSONObject(10).put("showValue","")
                        list.getJSONObject(11).put("showValue","")
                        list.getJSONObject(12).put("showValue","")
                        list.getJSONObject(7).put("value","")
                        list.getJSONObject(8).put("value","")
                        list.getJSONObject(9).put("value","")
                        list.getJSONObject(10).put("value","")
                        list.getJSONObject(11).put("value","")
                        list.getJSONObject(12).put("value","")
                      /*  notifyItemChanged(7)
                        notifyItemChanged(8)
                        notifyItemChanged(9)
                        notifyItemChanged(10)
                        notifyItemChanged(11)
                        notifyItemChanged(12)*/
                        /*val viewHolder1 = activity.binding.recycler2.findViewHolderForAdapterPosition(7) as GuestAdapter.ViewHolder
                        val viewHolder2 = activity.binding.recycler2.findViewHolderForAdapterPosition(8) as GuestAdapter.ViewHolder
                        val viewHolder3 = activity.binding.recycler2.findViewHolderForAdapterPosition(9) as GuestAdapter.ViewHolder
                        val viewHolder4 = activity.binding.recycler2.findViewHolderForAdapterPosition(10) as GuestAdapter.ViewHolder
                        val viewHolder5 = activity.binding.recycler2.findViewHolderForAdapterPosition(11) as GuestAdapter.ViewHolder
                        val viewHolder6 = activity.binding.recycler2.findViewHolderForAdapterPosition(12) as GuestAdapter.ViewHolder
                        viewHolder1.binding.edt.setText(list.getJSONObject(7).optString("showValue"))
                        viewHolder2.binding.edt.setText(list.getJSONObject(8).optString("showValue"))
                        viewHolder3.binding.edt.setText(list.getJSONObject(9).optString("showValue"))
                        viewHolder4.binding.edt.setText(list.getJSONObject(10).optString("showValue"))
                        viewHolder5.binding.edt.setText(list.getJSONObject(11).optString("showValue"))
                        viewHolder6.binding.edt.setText(list.getJSONObject(12).optString("showValue"))*/
                    }
                }
            }

        }
        else if(type == "textView"){
            UiUtils.log("iuyg",showValue)
            holder.binding.checkbox.visibility = View.GONE
            holder.binding.edt.visibility = View.GONE
            holder.binding.edtBig.visibility = View.GONE
            holder.binding.txt.visibility = View.VISIBLE
            holder.binding.txt.text = showValue
            if(value.isNotEmpty()){
                UiUtils.textviewImgDrawable(holder.binding.txt,R.drawable.ic_delete,Constants.IntentKeys.END)
            }
            else{
                UiUtils.textviewImgDrawable(holder.binding.txt,R.drawable.prime_upload,Constants.IntentKeys.END)
            }

            val status:String = list.getJSONObject(position).optString("status","")
            val statusValue:String = list.getJSONObject(position).optString("statusValue","")
            Log.e("cdc ",""+status)
            if(status == "rejected"){
                holder.binding.status.visibility = View.VISIBLE
                holder.binding.status.text = "Rejected"
//                holder.binding.txt.setText("")
                UiUtils.textViewTextColor(holder.binding.status,"#B32124",null)
                UiUtils.textViewBgTint(holder.binding.status,"#1AB32124",null)
                UiUtils.textviewImgDrawable(holder.binding.txt,R.drawable.prime_upload,Constants.IntentKeys.END)
                if(statusValue.isNotEmpty()){
                    holder.binding.txtTxt.visibility = View.VISIBLE
                    holder.binding.txtTxt.text = statusValue
                }
                else{
                    holder.binding.txtTxt.visibility = View.GONE
                }
            }
            else if(status == "approved"){
                holder.binding.status.visibility = View.VISIBLE
                holder.binding.txtTxt.visibility = View.GONE
                holder.binding.status.text = "Approved"
                UiUtils.textViewTextColor(holder.binding.status,"#28C76F",null)
                UiUtils.textViewBgTint(holder.binding.status,"#1A28C76F",null)
                UiUtils.textviewImgDrawable(holder.binding.txt,R.drawable.prime_upload,Constants.IntentKeys.END)
            }
            else if(status == "pending"){
                holder.binding.status.visibility = View.VISIBLE
                holder.binding.txtTxt.visibility = View.GONE
                holder.binding.status.text = "Pending"
                UiUtils.textViewTextColor(holder.binding.status,"#FF9F43",null)
                UiUtils.textViewBgTint(holder.binding.status,"#1AFF9F43",null)
                UiUtils.textviewImgDrawable(holder.binding.txt,R.drawable.ic_delete,Constants.IntentKeys.END)
            }
            else{
                holder.binding.status.visibility = View.GONE
                holder.binding.txtTxt.visibility = View.GONE
                UiUtils.log("cthyn",status)
            }


            holder.binding.txt.setOnClickListener{
                if(value.isNotEmpty()){
                    if(status == "approved"){
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.setData(Uri.parse(value))
                        activity.startActivity(intent)
                    }
                    else{
                        list.getJSONObject(position).put("showValue","")
                        list.getJSONObject(position).put("value","")
                        list.getJSONObject(position).put("status","")
                        list.getJSONObject(position).put("statusValue","")
                        holder.binding.txt.text = showValue
                        UiUtils.textviewImgDrawable(holder.binding.txt,R.drawable.prime_upload,Constants.IntentKeys.END)
                        notifyItemChanged(position)
                    }
                }
                else{
                    selectedPosition = position
                    activity.openDocList()
                }
            }
        }

        holder.binding.edt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                list.getJSONObject(position).put("showValue",s.toString())
               // list.getJSONObject(position).put("value",s.toString())
                Log.e("sds"+position,""+s.toString())
            }
            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
        })

        holder.binding.edtBig.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                list.getJSONObject(position).put("showValue",s.toString())
               // list.getJSONObject(position).put("value",s.toString())
                Log.e("sds"+position,""+s.toString())
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        /*holder.binding.edt.setText(list.getJSONObject(position).optString("showValue",""))
        holder.binding.edt.addTextChangedListener(null)
        holder.binding.edt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                list.getJSONObject(position).put("showValue", s.toString())
                Log.e("sds$position", s.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        holder.binding.edt.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                holder.binding.edt.clearFocus()
            }
        }*/

    }
    fun openDialog(hint:String,position:Int){

        if (hint == "Gender") {
            genderlist.clear()
            genderlist.add("Male")
            genderlist.add("Female")
            genderlist.add("Others")
        }
        else if (hint == "Blood Group") {
            bloodGroupList.clear()
            bloodGroupList.add("A+")
            bloodGroupList.add("A-")
            bloodGroupList.add("B+")
            bloodGroupList.add("B-")
            bloodGroupList.add("AB+")
            bloodGroupList.add("AB-")
            bloodGroupList.add("O+")
            bloodGroupList.add("O-")
        }
        else if (hint == "Nationality") {
            nationalityList.clear()
            nationalityList.add("Indian")
            nationalityList.add("American")
            nationalityList.add("British")
            nationalityList.add("Canadian")
            nationalityList.add("Australian")
            nationalityList.add("Others")
        }
        else if (hint == "Religion") {
            religoiusList.clear()
            religoiusList.add("Hindu")
            religoiusList.add("Muslim")
            religoiusList.add("Christian")
            religoiusList.add("Sikh")
            religoiusList.add("Buddhist")
            religoiusList.add("Jain")
            religoiusList.add("Others")
        }
        else if (hint == "Category") {
            categoryList.clear()
            categoryList.add("General")
            categoryList.add("OBC")
            categoryList.add("SC")
            categoryList.add("ST")
            categoryList.add("EWS")
            categoryList.add("Others")
        }

        if(hint == "Gender"){
            activity.loadSpinnerRecycler(genderlist,position)
        }
        else if(hint == "Blood Group"){
            activity.loadSpinnerRecycler(bloodGroupList,position)
        }
        else if(hint == "Nationality"){
            activity.loadSpinnerRecycler(nationalityList,position)
        }
        else if(hint == "Religion"){
            activity.loadSpinnerRecycler(religoiusList,position)
        }
        else if(hint == "Category"){
            activity.loadSpinnerRecycler(categoryList,position)
        }
    }

    private fun getColoredSpanned(text: String, color: String): String {
        val input = "<font color=$color>$text</font>"
        return input
    }

    private fun showCalender(edt: EditText) {
        var datePickerDialog: DatePickerDialog? = null
        // calender class's instance and get current date , month and year from calender
        val c: Calendar = Calendar.getInstance()
        val mYear: Int = c.get(Calendar.YEAR) // current year
        val mMonth: Int = c.get(Calendar.MONTH) // current month
        val mDay: Int = c.get(Calendar.DAY_OF_MONTH) // current day

        // date picker dialog
        datePickerDialog = DatePickerDialog(activity,
            { view, year, monthOfYear, dayOfMonth -> // set day of month , month and year value in the edit text
                var sDate = dayOfMonth.toString()+"/"+(monthOfYear + 1)+"/"+year
                sDate = BaseUtils.getFormattedDate(sDate,"dd/MM/yyyy","yyyy-MM-dd")
                edt.setText(sDate)
            }, mYear, mMonth, mDay
        )
        //datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    fun loadJSONFromAsset(): JSONObject? {
        var json: String? = null
        json = try {
            val `is`: InputStream = activity.getAssets().open("countriesToCities.json")
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, charset("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return JSONObject(json)
    }

    fun getEditTextFilter(): InputFilter {
        return object : InputFilter {
            override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence? {
                var keepOriginal = true
                val sb = StringBuilder(end - start)
                for (i in start until end) {
                    val c = source[i]
                    if (isCharAllowed(c)) // put your condition here
                        sb.append(c)
                    else keepOriginal = false
                }
                if (keepOriginal) return null
                else {
                    if (source is Spanned) {
                        val sp = SpannableString(sb)
                        TextUtils.copySpansFrom(source as Spanned, start, sb.length, null, sp, 0)
                        return sp
                    } else {
                        return sb
                    }
                }
            }

            private fun isCharAllowed(c: Char): Boolean {
                val ps: Pattern = Pattern.compile("^[a-zA-Z ]+$")
                val ms: Matcher = ps.matcher(c.toString())
                return ms.matches()
            }
        }
    }

}