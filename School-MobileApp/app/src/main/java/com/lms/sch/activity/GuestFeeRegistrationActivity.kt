package com.lms.sch.activity

import android.R
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import com.lms.sch.databinding.ActivityGuestFeeRegistrationBinding
import com.lms.sch.network.ApiConnection
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.lms.sch.adapter.CardsAdapter
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.response.StudentBoardResponse
import com.lms.sch.response.DropdownResponse
import com.lms.sch.response.SchoolDwRes
import com.lms.sch.session.TempSingleton
import com.lms.sch.utils.BaseUtils
import java.util.Collections.list

class GuestFeeRegistrationActivity : BaseActivity() {
    lateinit var binding: ActivityGuestFeeRegistrationBinding
    var studentBoard = ArrayList<StudentBoardResponse.Result>()
    var studentClass = ArrayList<DropdownResponse.Result>()
    var studentschoolres = ArrayList<SchoolDwRes.Result>()
    var value = ""
    var data = ArrayList<String>()
    var studentCls = ArrayList<String>()
    var studentSchool = ArrayList<String>()
    var schoolId = ""
    var boardId = ""
    var clicked = ""
    var classId = ""
    var boardid = ""
    private var isPageOne = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGuestFeeRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.page1.visibility = View.VISIBLE
        binding.page2.visibility = View.GONE
        binding.gradeLay.visibility = View.GONE
//        binding.next1.visibility = View.VISIBLE
//        binding.next.visibility = View.GONE
        setupInputType("name", "Enter first name", binding.fName)
        setupInputType("name", "Enter Last name", binding.lName)
        setupInputType("number", "Enter Phone Number", binding.mobile)
        setupInputType("textEmailAddress", "Enter your email id", binding.email)

        binding.back.setOnClickListener {
            onBackPressed()
        }
        isPageOne = true
        getProfile()
        getStudentSchool()
        if (sharedHelper.mobileNumber.isNotEmpty()){
            binding.mobile.setText(sharedHelper.mobileNumber)
            binding.mobile.isEnabled = false
        }

        binding.board.setOnClickListener {
            clicked = "board"
            loadBoardRecycler(data)
        }
        binding.std.setOnClickListener {
            clicked = "std"
            loadClassRecycler(studentCls)
        }
        binding.schooltxt.setOnClickListener {
            clicked = "school"
            loadSchoolRecycler(studentSchool)
        }
        binding.includeSearch.close.setOnClickListener {
            binding.includeSearch.root.visibility = View.GONE
        }

        binding.includeSearch.search.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?,p1: Int,p2: Int,p3: Int) {}
            override fun onTextChanged(p0: CharSequence?,p1: Int,p2: Int,p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                if (clicked == "board"){
                    val filteredList = ArrayList<String>()
                    for (item in data) {
                        if (item.lowercase().contains(p0.toString().lowercase())) {
                            filteredList.add(item)
                        }
                    }
                    loadBoardRecycler(filteredList)
                }
                else if (clicked == "std"){
                    val filteredList = ArrayList<String>()
                    for (item in studentCls) {
                        if (item.lowercase().contains(p0.toString().lowercase())) {
                            filteredList.add(item)
                        }
                    }
                    loadClassRecycler(filteredList)
                }
                else if (clicked == "school"){
                    val filteredList = ArrayList<String>()
                    for (item in studentSchool) {
                        if (item.lowercase().contains(p0.toString().lowercase())) {
                            filteredList.add(item)
                        }
                    }
                    loadSchoolRecycler(filteredList)
                }
            }
        })

        binding.next.setOnClickListener {
            if (binding.page1.isVisible){
                if (isValidFields()){
                    DialogUtils.showLoader(this)
                    ApiConnection.getInstance().register(this,binding.fName.text.toString(),binding.lName.text.toString(),
                        binding.mobile.text.toString(),binding.email.text.toString(),binding.preSch.text.toString(),boardId,classId,value
                    ).observe(this){
                        it.let {
                            DialogUtils.dismissLoader()
                            it.success.let { success->
                                if (success){
                                    Log.d("hzgdfjhf",value)
                                    binding.page1.visibility = View.GONE
                                    binding.page2.visibility = View.VISIBLE
                                    getProfile()
                                }
                                else {

                                }
                            }
                        }
                    }
                }
            }
            else {
                DialogUtils.showLoader(this)
                ApiConnection.getInstance().payRegFee(this).observe(this){
                    it.let {
                        DialogUtils.dismissLoader()
                        it.success.let { success->
                            if (success){
                                if (it.result != null){
                                    TempSingleton.getInstance().webUrl = it.result!!.paymentLink!!
                                    BaseUtils.startActivity(this,WebviewActivity(),null,false)
                                }
                                else {
                                    UiUtils.showSnack(it.msg,binding.root,false)
                                }
                            }
                            else {
                                UiUtils.showSnack(it.msg,binding.root,false)
                            }
                        }
                    }
                }
            }
        }

        val name1 = getColoredSpanned("Email", "#FF000000")
        val surName1 = getColoredSpanned("*", "#B32124")
        binding.emailTxt.text = Html.fromHtml(name1+" "+surName1, FROM_HTML_MODE_LEGACY)

        val name2 = getColoredSpanned("Where You Studied Before", "#FF000000")
        val surName2 = getColoredSpanned("", "#B32124")
        binding.preSchTxt.text = Html.fromHtml(name2+" "+surName2, FROM_HTML_MODE_LEGACY)

        val name3 = getColoredSpanned("Grade Applying For " + "", "#FF000000")
        val surName3 = getColoredSpanned("*", "#B32124")
        binding.gradetxt.text = Html.fromHtml(name3+" "+surName3, FROM_HTML_MODE_LEGACY)

        val name4 = getColoredSpanned("First Name", "#FF000000")
        val surName4 = getColoredSpanned("*", "#B32124")
        binding.fNameTxt.text = Html.fromHtml(name4+" "+surName4, FROM_HTML_MODE_LEGACY)

        val name5 = getColoredSpanned("Last Name", "#FF000000")
        val surName5 = getColoredSpanned("*", "#B32124")
        binding.lNameTxt.text = Html.fromHtml(name5+" "+surName5, FROM_HTML_MODE_LEGACY)

        val name6 = getColoredSpanned("Mobile", "#FF000000")
        val surName6 = getColoredSpanned("*", "#B32124")
        binding.mobTxt.text = Html.fromHtml(name6+" "+surName6, FROM_HTML_MODE_LEGACY)

        val name7 = getColoredSpanned("Board Applying For", "#FF000000")
        val surName7 = getColoredSpanned("*", "#B32124")
        binding.boardtxt.text = Html.fromHtml(name7+" "+surName7, FROM_HTML_MODE_LEGACY)

        val name8 = getColoredSpanned("School Applying For " + "",  "#FF000000")
        val surName8 = getColoredSpanned("*", "#B32124")
        binding.school.text = Html.fromHtml(name8+" "+surName8, FROM_HTML_MODE_LEGACY)

    }
    private fun loadSchoolRecycler(data: ArrayList<String>) {
        binding.includeSearch.root.visibility = View.VISIBLE
        if (data.isNotEmpty()) {
            val filtered = studentschoolres.filter { it.label!! in data }
            val adapter = CardsAdapter(this, data, object : OnClickListener {
                override fun onClickItem(pos: Int) {
                    val selectedSchool = filtered[pos]
                    value = selectedSchool.value!!
//                    value = studentschoolres[pos].value!!
                    binding.schooltxt.text = filtered[pos].label!!
                    binding.includeSearch.root.visibility = View.GONE
                    if (value.isNotEmpty()){
                        getStudentBoard()
                        binding.gradeLay.visibility = View.VISIBLE
                    }
                }
            })
            val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.includeSearch.recycler.layoutManager = linearLayoutManager
            binding.includeSearch.recycler.adapter = adapter
        }
    }
    private fun loadBoardRecycler(data: ArrayList<String>) {
        binding.includeSearch.root.visibility = View.VISIBLE
        if (data.isNotEmpty()) {
            val filtered = studentBoard.filter { it.name!! in data }
            val adapter = CardsAdapter(this, data, object : OnClickListener {
                override fun onClickItem(pos: Int) {
                    val selectedBoard = filtered[pos]
                    boardId = selectedBoard._id!!
//                    boardId = studentBoard[pos]._id!!
                    binding.board.text = filtered[pos].name
                    binding.includeSearch.root.visibility = View.GONE
                    if (boardId.isNotEmpty()){
                        getStudentCls()
                        binding.gradeLay.visibility = View.VISIBLE
                    }
                }
            })
            val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.includeSearch.recycler.layoutManager = linearLayoutManager
            binding.includeSearch.recycler.adapter = adapter
        }
    }
    private fun loadClassRecycler(data: ArrayList<String>) {
        binding.includeSearch.root.visibility = View.VISIBLE
        if (studentClass.isNotEmpty()) {
            val filtered = studentClass.filter { cls ->
                val label = cls.label!!.toInt()
                val suff = when {
                    label % 100 in 11..13 -> "th"
                    label % 10 == 1 -> "st"
                    label % 10 == 2 -> "nd"
                    label % 10 == 3 -> "rd"
                    else -> "th"
                }
                val formatted = "$label$suff Std"
                formatted in data
            }
            val filteredLabels = filtered.map { cls ->
                val label = cls.label!!.toInt()
                val suff = when {
                    label % 100 in 11..13 -> "th"
                    label % 10 == 1 -> "st"
                    label % 10 == 2 -> "nd"
                    label % 10 == 3 -> "rd"
                    else -> "th"
                }
                "$label$suff Std"
            }
//            val filtered = studentClass.filter { it.label!! in data }
            val adapter = CardsAdapter(this, filteredLabels as ArrayList<String>, object : OnClickListener {
                override fun onClickItem(pos: Int) {
                    val selectedClass = filtered[pos]
                    classId = selectedClass.value!!
//                    classId = studentClass[pos].value!!
                    binding.std.text = filteredLabels[pos]
                    binding.includeSearch.root.visibility = View.GONE
                }
            })
            val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.includeSearch.recycler.layoutManager = linearLayoutManager
            binding.includeSearch.recycler.adapter = adapter
        }
    }
    private fun getStudentSchool(){
        ApiConnection.getInstance().schoolDropdown(this@GuestFeeRegistrationActivity).observe(this@GuestFeeRegistrationActivity){
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null) {
                            studentSchool.clear()
                            studentschoolres = it.result!!
                            for (items in studentschoolres) {
                                val label = items.label!!.toString()
                                schoolId = items.value!!.toString()
                                Log.d("skjdhfjdsgh",schoolId)
                                studentSchool.add(label)

                            }

                        }
                        else {
                            UiUtils.showSnack(it.msg,binding.root,false)
                        }
                    }
                    else {
                        UiUtils.showSnack(it.msg,binding.root,false)
                    }
                }
            }
        }
    }
    private fun getStudentBoard(){
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().studentBoard(this,value).observe(this){
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        studentBoard = it.result!!
                        data.clear()
                        for (items in studentBoard){
//                            boardid = items._id!!.toString()
                            Log.d("sjgdhs",boardid)
                            data.add(items.name!!)
//                            getStudentCls()
                        }
                    }
                    else {
                        UiUtils.showSnack(it.msg,binding.root,false)
                    }
                }
            }
        }
    }
    private fun getStudentCls(){
        ApiConnection.getInstance().studentClsDropdown(this@GuestFeeRegistrationActivity,boardId).observe(this@GuestFeeRegistrationActivity){
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null && it.result!!.isNotEmpty()){
                            studentCls.clear()
                            studentClass = it.result!!
                            for (items in studentClass) {
                                val label = items.label!!.toInt()
                                val suff = when {
                                    label % 100 in 11..13 -> "th"
                                    label % 10 == 1 -> "st"
                                    label % 10 == 2 -> "nd"
                                    label % 10 == 3 -> "rd"
                                    else -> "th"
                                }
                                studentCls.add("$label$suff Std")
                            }
                        }
                        else {
                            UiUtils.showSnack(it.msg,binding.root,false)
                        }
                    }
                    else {
                        UiUtils.showSnack(it.msg,binding.root,false)
                    }
                }
            }
        }
    }

    private fun isValidFields(): Boolean {
        if (binding.fName.text.toString().isEmpty()){
            UiUtils.showSnack("Please enter first name",binding.root,false)
            return false
        }
        if (binding.lName.text.toString().isEmpty()){
            UiUtils.showSnack("Please enter last name",binding.root,false)
            return false
        }
        if (binding.mobile.text.toString().isEmpty()){
            UiUtils.showSnack("Please enter mobile number",binding.root,false)
            return false
        }
        if (binding.email.text.toString().isEmpty()){
            UiUtils.showSnack("Please enter email id",binding.root,false)
            return false
        }
        if (!BaseUtils.isValidEmailId(binding.email.text.toString())){
            UiUtils.showSnack("Please enter valid email id",binding.root,false)
            return false
        }
      /*  if (binding.preSch.text.toString().isEmpty()){
            UiUtils.showSnack("Please enter previous school",binding.root,false)
            return false
        }*/
        if (boardId.isEmpty()){
            UiUtils.showSnack("Please select board",binding.root,false)
            return false
        }
        if (classId.isEmpty()){
            UiUtils.showSnack("Please grade level",binding.root,false)
            return false
        }
        return true
    }

    private fun getColoredSpanned(text: String, color: String): String {
        val input = "<font color=$color>$text</font>"
        return input
    }

    private fun getProfile() {
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().profile(this).observe(this) {
            DialogUtils.dismissLoader()
            it.success.let { success->
                if (success){
                    if (it.result != null && it.result?.userprofile != null) {
                        val profile = it.result!!.userprofile!!

                        binding.name.text = "${profile.firstName ?: ""} ${profile.lastName ?: ""}"
                        Log.d("hsgdfjsdf","${profile.firstName ?: ""} ${profile.lastName ?: ""}")
                        sharedHelper.firstname = profile.firstName ?: ""

                        sharedHelper.lastname = profile.lastName ?: ""

                        binding.emailId.text = profile.email ?: ""
                        Log.d("hsgdfjsdf","${profile.email}")
                        sharedHelper.email = profile.email ?: ""
                        binding.mobileNo.text = profile.mobile ?: ""
                        Log.d("hsgdfjsdf","${profile.mobile}")

                        val grade = profile.grade_level?.name?.toIntOrNull() ?: 0
                        val displayGrade = grade
                        val suffix = when {
                            grade % 100 in 11..13 -> "th"
                            grade % 10 == 1 -> "st"
                            grade % 10 == 2 -> "nd"
                            grade % 10 == 3 -> "rd"
                            else -> "th"
                        }
//                        val suffix1 = when {
//                            displayGrade % 100 in 11..13 -> "th"
//                            displayGrade % 10 == 1 -> "st"
//                            displayGrade % 10 == 2 -> "nd"
//                            displayGrade % 10 == 3 -> "rd"
//                            else -> "th"
//                        }

                        binding.feeAmt.text = "₹500"
                        binding.applyfor.text = "$displayGrade$suffix Std"
                        Log.d("shfjdsgf","$displayGrade$suffix Std")
                        if (isPageOne){
                            isPageOne = false
                            binding.next.text = "Next"
                        }else if(!isPageOne){
                            binding.next.text = "Pay Now ₹500"
                        }
                        val isPaymentDone = profile.registrationFee == true
                        val isPaymentSuccess = TempSingleton.getInstance().isPaymentSuccess

                        if (isPaymentDone) {
                            if (isPaymentSuccess) {
                                TempSingleton.getInstance().isPaymentSuccess = false
                                BaseUtils.startActivity(this, GuestActivity(), null, true)
                            }
                        } else {
                            if (isPaymentSuccess){
                                TempSingleton.getInstance().isPaymentSuccess = false
                                UiUtils.showSnack("Please complete your payment to continue your application.", binding.root, false)
                            }
                        }
                    }
                    else {
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
                else {
                    UiUtils.showSnack(it.msg, binding.root, false)
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        if (TempSingleton.getInstance().isPaymentSuccess) {
            getProfile()
        }
    }

    override fun onBackPressed() {
        if (binding.includeSearch.root.visibility == View.VISIBLE){
            binding.includeSearch.root.visibility = View.GONE
        }
        else {
            super.onBackPressed()
        }
    }

    fun setupInputType(inputType: String, hint: String, editText: EditText) {
        when (inputType) {
            "name" -> {
                editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
                editText.filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
                    if (source != null && source.matches(Regex("[a-zA-Z *]+"))) source else ""
                })
            }

            "number" -> {
                editText.inputType = InputType.TYPE_CLASS_NUMBER
                when (hint) {
                    "Enter Aadhaar Number" -> {
                        editText.hint = "Please enter 12 digit Aadhaar number"
                        editText.filters = arrayOf(InputFilter.LengthFilter(12))
                    }
                    in listOf("Enter Phone Number", "Enter Contact Number", "Enter Emergency Contact Number") -> {
                        editText.hint = "Please enter 10 digit mobile number"
                        editText.inputType = InputType.TYPE_CLASS_PHONE
                        editText.filters = arrayOf(InputFilter.LengthFilter(10))
                    }
                    else -> {
                        editText.filters = arrayOf(InputFilter.LengthFilter(20)) // Default max length
                    }
                }
            }
            "textEmailAddress" -> {
                editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                val emailRegex = Regex("^[a-z0-9+_.-]+@[a-z0-9.-]+\\.[a-z]{2,}$")

                editText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                    override fun afterTextChanged(s: Editable?) {
                        val input = s.toString()
                        if (input.isNotEmpty() && !emailRegex.matches(input)) {
                            Log.d("ghgfg", "gdfffffgghhjj")
                            binding.emailError.visibility = View.GONE
                            editText.error = "Please Enter valid email."
                            Log.d("ghgfg","${editText.error}")
                            binding.emailError.text = "* Please Enter valid email."
                        } else {
                            editText.error = null
                        }
                    }
                })
            }
            else -> {
                editText.inputType = InputType.TYPE_CLASS_TEXT
                editText.filters = arrayOf() // No filter by default
            }
        }
    }
}