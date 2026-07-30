package com.lms.sch.adapter

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
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
import android.widget.EditText
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.ApplicationActivity
import com.lms.sch.activity.GuestFeeRegistrationActivity
import com.lms.sch.databinding.CardGuestBinding
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.DropdownResponse
import com.lms.sch.response.StudentBoardResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.util.Calendar
import java.util.regex.Matcher
import java.util.regex.Pattern

class GuestAdapter(var activity: ApplicationActivity, var list: JSONArray) : RecyclerView.Adapter<GuestAdapter.ViewHolder>() {
    var selectedPosition = 0
    var genderlist: ArrayList<String> = ArrayList()
    var bloodGroupList: ArrayList<String> = ArrayList()
    var nationalityList: ArrayList<String> = ArrayList()
    var religoiusList: ArrayList<String> = ArrayList()
    var categoryList: ArrayList<String> = ArrayList()
    var board: ArrayList<StudentBoardResponse.Result> = ArrayList()
    var boardStr: ArrayList<String> = ArrayList()
    var studentCls: ArrayList<DropdownResponse.Result> = ArrayList()
    var studentClsStr: ArrayList<String> = ArrayList()

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
            UiUtils.log("kujhyg","title --- $title"+ "value----$showValue")
        }
        else{
//            holder.binding.edt.setText("")
            holder.binding.edt.hint = hintTxt
        }

        if(type == "editText"){
            var isUpdating = false
            if(hint == "Mobile Number" ){
                holder.binding.edt.isFocusable = false
            }
            else if (hint == "Pincode"){
                holder.binding.edt.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun afterTextChanged(p0: Editable?) {
                        if (isUpdating) return
                        if (p0.toString().length == 6) {
                            BaseUtils.hideKeyboard(activity)
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

                                            activity.runOnUiThread {
                                                activity.binding.recycler.adapter?.notifyItemChanged(position+1)
                                                activity.binding.recycler.adapter?.notifyItemChanged(position+2)
                                                activity.binding.recycler.adapter?.notifyItemChanged(position+3)
                                            }
                                            /*val recyclerView = activity.binding.recycler
                                            val viewHolder1 = recyclerView.findViewHolderForAdapterPosition(position + 1) as? ViewHolder
                                            val viewHolder2 = recyclerView.findViewHolderForAdapterPosition(position + 2) as? ViewHolder
                                            val viewHolder3 = recyclerView.findViewHolderForAdapterPosition(position + 3) as? ViewHolder

                                            viewHolder1?.binding?.edt?.apply {
                                                isFocusable = false
                                                isFocusableInTouchMode = false
                                            }
                                            viewHolder2?.binding?.edt?.apply {
                                                isFocusable = false
                                                isFocusableInTouchMode = false
                                            }
                                            viewHolder3?.binding?.edt?.apply {
                                                isFocusable = false
                                                isFocusableInTouchMode = false
                                            }*/
                                            isUpdating = false
                                        }
                                        else {
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
            else if(hint == "Enter Previous School Name"){
                holder.binding.edt.isFocusable = false
            }
            else if(hint == "Board of Education"){
                holder.binding.edt.isFocusable = false
            }
//            else if (hint == "yyyy/MM/DD") {
//                holder.binding.edt.isFocusable = false
//                holder.binding.edt.isClickable = true
//
//                holder.binding.edt.setOnClickListener {
//                    val calendar = Calendar.getInstance()
//                    val year = calendar.get(Calendar.YEAR)
//                    val month = calendar.get(Calendar.MONTH)
//                    val day = calendar.get(Calendar.DAY_OF_MONTH)
//
//                    val datePickerDialog = DatePickerDialog(
//                        holder.binding.edt.context,
//                        { _, selectedYear, selectedMonth, selectedDay ->
//                            val formattedDate = String.format("%04d/%02d/%02d", selectedYear, selectedMonth + 1, selectedDay)
//                            holder.binding.edt.setText(formattedDate)
//                            list.getJSONObject(position).put("showValue", formattedDate)
//                        },
//                        year,
//                        month,
//                        day
//                    )
//
//                    val minDob = Calendar.getInstance()
//                    minDob.add(Calendar.YEAR, -100)
//
//                    val maxDob = Calendar.getInstance()
//                    maxDob.add(Calendar.YEAR, -3)
//                    maxDob.set(Calendar.HOUR_OF_DAY, 23)
//                    maxDob.set(Calendar.MINUTE, 59)
//                    maxDob.set(Calendar.SECOND, 59)
//                    maxDob.set(Calendar.MILLISECOND, 999)
//
//                    datePickerDialog.datePicker.minDate = minDob.timeInMillis
//                    datePickerDialog.datePicker.maxDate = maxDob.timeInMillis
//
//                    datePickerDialog.show()
//                }
//            }

            else if(hint == "Class Applying For"){
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
                holder.binding.edt.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
                holder.binding.edt.filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
                    if (source != null && source.matches(Regex("[a-zA-Z ]+"))) source else ""
                })
            }
            else if (inputType == "number") {
//                UiUtils.log("Actual Hint", hint)
                holder.binding.edt.inputType = InputType.TYPE_CLASS_NUMBER
//                UiUtils.log("Actual Hint", hint)
                when (title) {
                    "Aadhaar Number *" -> {
                      holder.binding.edt.hint = "Please Enter 12 Digit Aadhaar Number"
                      holder.binding.edt.filters = arrayOf(InputFilter.LengthFilter(12))
                        holder.binding.edt.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                                val str = s.toString()

                                when {
                                    str.isEmpty() -> {
                                        holder.binding.edt.hint = "*Please Enter Aadhaar Number"
                                        holder.binding.emailError.visibility = View.GONE
                                    }
                                    !str.matches(Regex("\\d+")) -> {
                                        holder.binding.emailError.text = "*Aadhaar must contain digits only"
                                        holder.binding.emailError.visibility = View.VISIBLE
                                    }
                                    str.length > 0 && (str[0] == '0' || str[0] == '1') -> {
                                        holder.binding.emailError.text = "*Aadhaar can not start with 0 or 1"
                                        holder.binding.emailError.visibility = View.VISIBLE
                                    }
                                    str.length < 12 -> {
                                        holder.binding.emailError.text = "*Please enter a 12-digit Aadhaar Number"
                                        holder.binding.emailError.visibility = View.VISIBLE
                                    }
                                    str.length == 12 -> {
                                        holder.binding.emailError.visibility = View.GONE
                                    }
                                }
                            }

                            override fun afterTextChanged(s: Editable?) {}
                        })

                    }
                    "Student Phone Number *" -> {
                        holder.binding.edt.hint = "Please Enter 10 Digit Mobile Number"
                        holder.binding.edt.inputType = InputType.TYPE_CLASS_PHONE
                        holder.binding.edt.filters = arrayOf(InputFilter.LengthFilter(10))
                        holder.binding.edt.isFocusable = false
                    }
                    "Parent’s Contact Number*" -> {
                        holder.binding.edt.hint = "Please Enter 10 Digit Mobile Number"
                        holder.binding.edt.filters = arrayOf(InputFilter.LengthFilter(10))
                        holder.binding.edt.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                                val str = s.toString()

                                when {
                                    str.isEmpty() -> {
                                        holder.binding.edt.hint = "*Please Enter Mobile Number"
                                        holder.binding.emailError.visibility = View.GONE
                                    }
                                    str.length != 10 -> {
                                        holder.binding.emailError.text = "*Please Enter a 10-digit Mobile Number"
                                        holder.binding.emailError.visibility = View.VISIBLE
                                    }
                                    !str.matches(Regex("^[6-9][0-9]{9}$")) -> {
                                        holder.binding.emailError.text = "*Mobile Number must start with 6, 7, 8, or 9"
                                        holder.binding.emailError.visibility = View.VISIBLE
                                    }
                                    else -> {
                                        holder.binding.emailError.visibility = View.GONE
                                    }
                                }
                            }

                            override fun afterTextChanged(s: Editable?) {}
                        })


                    }
                    "Emergency Contact Number*" -> {

                            holder.binding.edt.hint = "Please Enter 10 Digit Mobile Number"
                            holder.binding.edt.filters = arrayOf(InputFilter.LengthFilter(10))
                        holder.binding.edt.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                                val str = s.toString()

                                when {
                                    str.isEmpty() -> {
                                        holder.binding.edt.hint = "*Please Enter Mobile Number"
                                        holder.binding.emailError.visibility = View.GONE
                                    }
                                    str.length != 10 -> {
                                        holder.binding.emailError.text = "*Please Enter a 10-digit Mobile Number"
                                        holder.binding.emailError.visibility = View.VISIBLE
                                    }
                                    !str.matches(Regex("^[6-9][0-9]{9}$")) -> {
                                        holder.binding.emailError.text = "*Mobile Number must start with 6, 7, 8, or 9"
                                        holder.binding.emailError.visibility = View.VISIBLE
                                    }
                                    else -> {
                                        holder.binding.emailError.visibility = View.GONE
                                    }
                                }
                            }

                            override fun afterTextChanged(s: Editable?) {}
                        })
                    }
                    else -> {
                        holder.binding.edt.filters = arrayOf(InputFilter.LengthFilter(20)) // Default limit
                    }
                }
            }
            else if(title == "Parent’s Email Address *" && inputType == "email"){
                holder.binding.edt.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                holder.binding.edt.isFocusable = true
                holder.binding.edt.isFocusableInTouchMode = true
                holder.binding.edt.isCursorVisible = true
                holder.binding.edt.isEnabled = true
                holder.binding.edt.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        val email = s.toString()
                        when {
                            email.isEmpty() -> {
                                holder.binding.emailError.text = "*Please Enter Email Id"
                                holder.binding.emailError.visibility = View.VISIBLE
                            }
                            !BaseUtils.isValidEmail(email) -> {
                                holder.binding.emailError.text = "*Please Enter Valid Email Id"
                                holder.binding.emailError.visibility = View.VISIBLE
                            }
                            else -> {
                                holder.binding.emailError.visibility = View.GONE
                            }
                        }
                    }

                    override fun afterTextChanged(s: Editable?) {}
                })
            }
            else if(inputType == "email"){
                holder.binding.edt.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                holder.binding.edt.isFocusable = false
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
                activity.binding.spinnerList.searchLay.visibility = View.VISIBLE
                openDialog(hint,position)
            }

            activity.binding.spinnerList.search.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {
//                    activity.binding.spinnerList.searchLay.visibility = View.VISIBLE
                    val searchText = p0.toString().lowercase()
                    if (hint == "Gender") {
                        activity.binding.spinnerList.searchLay.visibility = View.GONE
                        val filteredList = ArrayList(genderlist.filter { it.lowercase().contains(searchText) })
                        activity.loadSpinnerRecycler(filteredList, position)
                    }
                    else if (hint == "Blood Group") {
                        activity.binding.spinnerList.searchLay.visibility = View.VISIBLE
                        val filteredList = ArrayList(bloodGroupList.filter { it.lowercase().contains(searchText) })
                        activity.loadSpinnerRecycler(filteredList, position)
                    }
                     else if (hint == "Nationality") {
                        activity.binding.spinnerList.searchLay.visibility = View.VISIBLE
                         val filteredList = ArrayList(nationalityList.filter { it.lowercase().contains(searchText) })
                        activity.loadSpinnerRecycler(filteredList, position)
                     }
                     else if (hint == "Religion") {
                        activity.binding.spinnerList.searchLay.visibility = View.VISIBLE
                         val filteredList = ArrayList(religoiusList.filter { it.lowercase().contains(searchText) })
                        activity.loadSpinnerRecycler(filteredList, position)
                     }
                     else if (hint == "Category") {
                        activity.binding.spinnerList.searchLay.visibility = View.VISIBLE
                         val filteredList = ArrayList(categoryList.filter { it.lowercase().contains(searchText) })
                        activity.loadSpinnerRecycler(filteredList, position)
                     }
                     else if (hint == "Board of Education") {
                        activity.binding.spinnerList.searchLay.visibility = View.VISIBLE
                         val filteredList = ArrayList(boardStr.filter { it.lowercase().contains(searchText) })
                        activity.loadSpinnerRecycler(filteredList, position)
                     }

                    /*var slist: ArrayList<String> = ArrayList()
                    if (hint == "Gender") {
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
                    }*/
                }
            })
            if (hint == "Board of Education") {
                DialogUtils.showLoader(activity)
                ApiConnection.getInstance().studentBoard(activity).observe(activity){
                    it.let {
                        DialogUtils.dismissLoader()
                        it.success.let { success->
                            if (success){
                                board = it.result!!
                                boardStr.clear()
                                for (items in board){
                                    boardStr.add(items.name!!)
                                }
                            }
                            else {
                                UiUtils.showToast(activity,it.msg)
                            }
                        }
                    }
                }
            }
            else if (hint == "Class Applying For"){
                DialogUtils.showLoader(activity)
                ApiConnection.getInstance().studentClsDropdown(activity).observe(activity){
                    it.let {
                        DialogUtils.dismissLoader()
                        it.success.let { success ->
                            if (success) {
                                if (it.result != null && it.result!!.isNotEmpty()){
                                    studentClsStr.clear()
                                    studentCls = it.result!!
                                    for (items in studentCls) {
                                        val label = items.label!!.toInt()
                                        val suff = when {
                                            label % 100 in 11..13 -> "th"
                                            label % 10 == 1 -> "st"
                                            label % 10 == 2 -> "nd"
                                            label % 10 == 3 -> "rd"
                                            else -> "th"
                                        }
                                        studentClsStr.add("$label$suff Std")
                                    }
                                }
                                else {
                                    UiUtils.showSnack(it.msg,holder.binding.root,false)
                                }
                            }
                            else {
                                UiUtils.showSnack(it.msg,holder.binding.root,false)
                            }
                        }
                    }
                }

            }

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
            if (showValue.isNotEmpty()){
                holder.binding.txt.text = showValue
            }
            else {
                holder.binding.txt.text = hintTxt
            }
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
                holder.binding.status.visibility = View.GONE
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
                holder.binding.status.visibility = View.GONE
                holder.binding.txtTxt.visibility = View.GONE
                holder.binding.status.text = "Approved"
                UiUtils.textViewTextColor(holder.binding.status,"#28C76F",null)
                UiUtils.textViewBgTint(holder.binding.status,"#1A28C76F",null)
                UiUtils.textviewImgDrawable(holder.binding.txt,R.drawable.prime_upload,Constants.IntentKeys.END)
            }
            else if(status == "pending"){
                holder.binding.status.visibility = View.GONE
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
            genderlist.add("Female")
            genderlist.add("Male")
            genderlist.add("Others")
        }
        else if (hint == "Blood Group") {
            bloodGroupList.clear()
            bloodGroupList.add("O-")
            bloodGroupList.add("O+")
            bloodGroupList.add("A-")
            bloodGroupList.add("A+")
            bloodGroupList.add("B-")
            bloodGroupList.add("B+")
            bloodGroupList.add("AB-")
            bloodGroupList.add("AB+")
        }
        else if (hint == "Nationality") {
            nationalityList.add("American")
            nationalityList.add("Australian")
            nationalityList.add("British")
            nationalityList.add("Canadian")
            nationalityList.add("Indian")
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
        else if(hint == "Board of Education"){
            activity.loadSpinnerRecycler(boardStr,position)
        }
        else if(hint == "Class Applying For"){
            activity.loadSpinnerRecycler(studentClsStr,position)
        }
    }

    private fun getColoredSpanned(text: String, color: String): String {
        val input = "<font color=$color>$text</font>"
        return input
    }

    fun showCalender(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            editText.context,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format("%04d/%02d/%02d", selectedYear, selectedMonth + 1, selectedDay)
                editText.setText(formattedDate)
            },
            year,
            month,
            day
        )

        val minDob = Calendar.getInstance()
        minDob.add(Calendar.YEAR, -100)

        val maxDob = Calendar.getInstance()
        maxDob.set(Calendar.HOUR_OF_DAY, 23)
        maxDob.set(Calendar.MINUTE, 59)
        maxDob.set(Calendar.SECOND, 59)
        maxDob.set(Calendar.MILLISECOND, 999)

        datePickerDialog.datePicker.minDate = minDob.timeInMillis
        datePickerDialog.datePicker.maxDate = maxDob.timeInMillis

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