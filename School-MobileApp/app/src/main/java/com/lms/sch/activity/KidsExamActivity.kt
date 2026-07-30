package com.lms.sch.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.PopupWindow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.adapter.AttachAdapter
import com.lms.sch.adapter.MonthsAdapter
import com.lms.sch.adapter.StudentClassTestAdapter
import com.lms.sch.adapter.StudentExaminationAdapter
import com.lms.sch.adapter.WeekDayAdapter1
import com.lms.sch.adapter.YearAdapter
import com.lms.sch.databinding.ActivityKidsExamBinding
import com.lms.sch.databinding.DialogMonthPickerBinding
import com.lms.sch.databinding.FilterAssignmentBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.GetExamResponse
import com.lms.sch.response.StudentClassTestResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt
import kotlin.text.toInt

class KidsExamActivity : BaseActivity() {
    lateinit var binding : ActivityKidsExamBinding
    var examTabClicked = ""
    var classTestSts = ""
    var examStatus = ""
    var search = ""
    var page = ""
    var clsTestRes = ArrayList<StudentClassTestResponse.Result>()
    var examRes = ArrayList<GetExamResponse.Row>()
    var selectedMonth = 0
    var calendar = Calendar.getInstance()
    var selectedYear = 0
    var count = 0
    var classTestAttach = ArrayList<String>()
    private var currentMonthDays = ArrayList<Date>()
    val years = arrayListOf("2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030")
    val months = arrayListOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    var filterDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityKidsExamBinding.inflate(layoutInflater)
        setContentView(binding.root)
        page = ""+intent.getStringExtra(Constants.IntentKeys.KEY)

        val currentYear = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date()).toInt()
        currentMonthDays = getCurrentMonthDays()
        loadDates()
        binding.backarrow.setOnClickListener{
            onBackPressed()
        }
        binding.date.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.setContentView(R.layout.dialog_month_picker)
            val bind: DialogMonthPickerBinding = DialogMonthPickerBinding.inflate(LayoutInflater.from(this))
            dialog.setContentView(bind.root)
            dialog.window?.setBackgroundDrawable(
                ColorDrawable(ContextCompat.getColor(this, R.color.transparent))
            )
            var width: Int = (resources.displayMetrics.widthPixels * 0.9).roundToInt()
//        var height: Int = (resources.displayMetrics.widthPixels * 0.9).roundToInt()
//            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)
            dialog.window?.setGravity(Gravity.CENTER)
            var sMonth = selectedMonth
            var sYear = selectedYear
            val yearAdapter = YearAdapter(this, years,selectedYear, object : OnClickListener {
                override fun onClickItem(pos: Int) {
                    sYear = pos
                    bind.yText.text = "${months[sMonth]} ${years[sYear]}"
                }
            })
            bind.yearRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            bind.yearRecycler.adapter = yearAdapter

            val currentYearIndex = years.indexOf(currentYear.toString())
            bind.yearRecycler.post {
                if (currentYearIndex != RecyclerView.NO_POSITION) {
                    val layoutManager = bind.yearRecycler.layoutManager as LinearLayoutManager
                    val recyclerWidth = bind.yearRecycler.width - bind.yearRecycler.paddingLeft - bind.yearRecycler.paddingRight
                    val itemWidth = resources.getDimensionPixelSize(R.dimen._70dp)
                    val offset = (recyclerWidth / 2) - (itemWidth / 2)
                    layoutManager.scrollToPositionWithOffset(currentYearIndex, offset)
                }
            }
            val monthAdapter = MonthsAdapter(this, months,years[selectedYear].toInt(),selectedMonth, object : OnClickListener {
                override fun onClickItem(pos: Int) {
                    sMonth = pos
                    bind.yText.text = "${months[sMonth]} ${years[sYear]}"
                }
            })
            bind.monthRecycler.layoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
            bind.monthRecycler.adapter = monthAdapter

            bind.select.setOnClickListener {
                calendar = Calendar.getInstance()
                val currentDate = calendar.time
                val date = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
                val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val sdfApi = SimpleDateFormat("EEEE", Locale.getDefault())
                selectedYear = sYear
                selectedMonth = sMonth
                currentMonthDays = getCurrentMonthDays(years[sYear].toInt(),selectedMonth)
                binding.date.text = "${months[selectedMonth]} ${years[selectedYear]}"
                val adapter1 = WeekDayAdapter1(currentMonthDays, currentDate) { selectedDate ->
                    UiUtils.log("gfhjk",""+selectedDate)
                    val dt = sdfDate.format(selectedDate)
                    filterDate = dt
                    if (examTabClicked == "exam"){
                        getStudentExamination()
                    }
                    else {
                        getClassTest()
                    }
                }
                val linearLayoutManager1 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                binding.dateRecycler1.layoutManager = linearLayoutManager1
                binding.dateRecycler1.adapter = adapter1

                val currentDayIndex1 = currentMonthDays.indexOfFirst { sdfDate.format(it) == sdfDate.format(currentDate) }
                if (currentDayIndex1 != -1) {
                    val itemWidth = resources.getDimensionPixelSize(R.dimen._55dp)
                    binding.dateRecycler1.centerItem(currentDayIndex1, itemWidth)
                }
//                getEventsPager()
                dialog.dismiss()
            }
            bind.cancel.setOnClickListener {
                dialog.dismiss()
            }

            dialog.setOnDismissListener {

            }
            dialog.show()
        }

        binding.classTest.setOnClickListener {
            UiUtils.textviewCustomDrawable(binding.classTest,R.drawable.border_curve_4dp )
            UiUtils.textviewCustomDrawable(binding.classExam,R.drawable.border_curve_6dp )
            UiUtils.textViewBgTint(binding.classTest, null,R.color.white)
            UiUtils.textViewTextColor(binding.classTest, null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.classExam, null, R.color.black_varient3)
            examTabClicked = "classTest"
            classTestSts = ""
            getClassTest()
        }
        binding.classExam.setOnClickListener {
            UiUtils.textviewCustomDrawable(binding.classExam,R.drawable.border_curve_4dp )
            UiUtils.textviewCustomDrawable(binding.classTest,R.drawable.border_curve_6dp )
            UiUtils.textViewBgTint(binding.classExam, null,R.color.white)
            UiUtils.textViewTextColor(binding.classExam, null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.classTest, null, R.color.black_varient3)
            examTabClicked = "exam"
            examStatus = ""
            getStudentExamination()
        }

        if (page == "exam"){
            binding.classExam.performClick()
            binding.topHeader.text = "Exam"
        }
        else {
            binding.classTest.performClick()
            binding.topHeader.text = "Class Test"
        }

        binding.search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                search = binding.search.text.toString()
                if (examTabClicked == "exam"){
                    getStudentExamination()
                }
                else {
                    getClassTest()
                }
            }
            override fun beforeTextChanged(s: CharSequence, start: Int,count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        binding.search.setOnEditorActionListener(TextView.OnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                search = binding.search.text.toString()
                if (examTabClicked == "exam"){
                    getStudentExamination()
                }
                else {
                    getClassTest()
                }
            }
            false
        })

        binding.filter.setOnClickListener {
            val inflater = LayoutInflater.from(this)
            val bind : FilterAssignmentBinding = FilterAssignmentBinding.inflate(inflater)
            val popupView : View = bind.root

            val widthInDp = 120
            val density = resources.displayMetrics.density
            val widthInPx = (widthInDp * density).toInt()

            val popupWindow = PopupWindow(popupView,widthInPx,ViewGroup.LayoutParams.WRAP_CONTENT,true)
            popupWindow.isOutsideTouchable = true
            popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            popupWindow.elevation = 8f
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                popupWindow.setElevation(8f)
            }
            if (examTabClicked == "classTest") {
                bind.ongoing.text = "Today"
                bind.pending.text = "Upcoming"
                if (classTestSts == "today"){
                    UiUtils.textviewImgDrawable(bind.ongoing,R.drawable.hugeicons_tick,"start")
                    UiUtils.textviewImgDrawable(bind.all,null,"start")
                    UiUtils.textviewImgDrawable(bind.pending,null,"start")
                    UiUtils.textviewImgDrawable(bind.completed,null,"start")
                }
                else if (classTestSts == "upcoming"){
                    UiUtils.textviewImgDrawable(bind.pending,R.drawable.hugeicons_tick,"start")
                    UiUtils.textviewImgDrawable(bind.all,null,"start")
                    UiUtils.textviewImgDrawable(bind.ongoing,null,"start")
                    UiUtils.textviewImgDrawable(bind.completed,null,"start")
                }
                else if (classTestSts == "completed"){
                    UiUtils.textviewImgDrawable(bind.completed,R.drawable.hugeicons_tick,"start")
                    UiUtils.textviewImgDrawable(bind.all,null,"start")
                    UiUtils.textviewImgDrawable(bind.ongoing,null,"start")
                    UiUtils.textviewImgDrawable(bind.pending,null,"start")
                }
                else {
                    UiUtils.textviewImgDrawable(bind.all,R.drawable.hugeicons_tick,"start")
                    UiUtils.textviewImgDrawable(bind.pending,null,"start")
                    UiUtils.textviewImgDrawable(bind.ongoing,null,"start")
                    UiUtils.textviewImgDrawable(bind.completed,null,"start")
                }
            }
            else if (examTabClicked == "exam") {
                bind.ongoing.text = "Ongoing"
                bind.pending.text = "Upcoming"
                if (examStatus == "ongoing"){
                    UiUtils.textviewImgDrawable(bind.ongoing,R.drawable.hugeicons_tick,"start")
                    UiUtils.textviewImgDrawable(bind.all,null,"start")
                    UiUtils.textviewImgDrawable(bind.pending,null,"start")
                    UiUtils.textviewImgDrawable(bind.completed,null,"start")
                }
                else if (examStatus == "upcoming"){
                    UiUtils.textviewImgDrawable(bind.pending,R.drawable.hugeicons_tick,"start")
                    UiUtils.textviewImgDrawable(bind.all,null,"start")
                    UiUtils.textviewImgDrawable(bind.ongoing,null,"start")
                    UiUtils.textviewImgDrawable(bind.completed,null,"start")
                }
                else if (examStatus == "completed"){
                    UiUtils.textviewImgDrawable(bind.completed,R.drawable.hugeicons_tick,"start")
                    UiUtils.textviewImgDrawable(bind.all,null,"start")
                    UiUtils.textviewImgDrawable(bind.ongoing,null,"start")
                    UiUtils.textviewImgDrawable(bind.pending,null,"start")
                }
                else {
                    UiUtils.textviewImgDrawable(bind.all,R.drawable.hugeicons_tick,"start")
                    UiUtils.textviewImgDrawable(bind.pending,null,"start")
                    UiUtils.textviewImgDrawable(bind.ongoing,null,"start")
                    UiUtils.textviewImgDrawable(bind.completed,null,"start")
                }
            }

            bind.all.setOnClickListener {
                if (examTabClicked == "classTest"){
                    classTestSts = ""
                    getClassTest()
                }
                else if (examTabClicked == "exam"){
                    examStatus = ""
                    getStudentExamination()
                }
                popupWindow.dismiss()
            }
            bind.pending.setOnClickListener {
                if (examTabClicked == "classTest"){
                    classTestSts = "today"
                    getClassTest()
                }
                else if (examTabClicked == "exam"){
                    examStatus = "ongoing"
                    getStudentExamination()
                }
                popupWindow.dismiss()
            }
            bind.ongoing.setOnClickListener {
                if (examTabClicked == "classTest"){
                    classTestSts = "upcoming"
                    getClassTest()
                }
                else if (examTabClicked == "exam"){
                    examStatus = "upcoming"
                    getStudentExamination()
                }
                popupWindow.dismiss()
            }
            bind.completed.setOnClickListener {
                if (examTabClicked == "classTest"){
                    classTestSts = "completed"
                    getClassTest()
                }
                else if (examTabClicked == "exam"){
                    examStatus = "completed"
                    getStudentExamination()
                }
                popupWindow.dismiss()
            }
            val anchorView = binding.filter
            val location = IntArray(2)
            anchorView.getLocationOnScreen(location)

            val endGapDp = 8
            val topGapDp = 8
            val endGapPx = (endGapDp * density).toInt()
            val topGapPx = (topGapDp * density).toInt()
            val xPos = location[0] + anchorView.width - widthInPx - endGapPx
            val yPos = location[1] + anchorView.height + topGapPx

            popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY,xPos,yPos)
        }
    }

    private fun getClassTest(){
        if (search.isEmpty()){
            DialogUtils.showLoader(this)
        }
        ApiConnection.getInstance().getStudentClassTest(this,search,classTestSts, classTestAttach).observe(this){
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if(success){
                        if (it.result != null && it.result!!.isNotEmpty()){
                            binding.noData.root.visibility = View.GONE
                            binding.examRecycler.visibility = View.VISIBLE
                            clsTestRes = it.result!!
                            val layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
                            val adapter = StudentClassTestAdapter(this,it.result!!,object : OnClickListener{
                                override fun onClickItem(pos: Int) {
                                    getClsTestDialog(pos)
                                }
                            })
                            binding.examRecycler.layoutManager = layoutManager
                            binding.examRecycler.adapter = adapter
                        }else{
                            binding.noData.root.visibility = View.VISIBLE
                            binding.examRecycler.visibility = View.GONE
                        }
                    }
                    else{
                        binding.noData.root.visibility = View.VISIBLE
                        binding.examRecycler.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun getStudentExamination(){
        if (search.isEmpty()){
            DialogUtils.showLoader(this)
        }
        ApiConnection.getInstance().getExam(this,search, examStatus).observe(this){
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if(success){
                        if (it.result != null && it.result!!.rows!!.isNotEmpty()){
                            binding.noData.root.visibility = View.GONE
                            binding.examRecycler.visibility = View.VISIBLE
                            examRes = it.result!!.rows!!
                            val layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
                            val adapter = StudentExaminationAdapter(this,false,it.result!!.rows!!,object : OnClickListener{
                                override fun onClickItem(pos: Int) {
//                                    getExam(pos)
                                }
                            })
                            binding.examRecycler.layoutManager = layoutManager
                            binding.examRecycler.adapter = adapter
                        }  else{
                            UiUtils.showSnack(it.msg, binding.root, false)
                            binding.noData.root.visibility = View.VISIBLE
                            binding.examRecycler.visibility = View.GONE
                        }
                    }
                    else{
                        UiUtils.showSnack(it.msg, binding.root, false)
                        binding.noData.root.visibility = View.VISIBLE
                        binding.examRecycler.visibility = View.GONE
                    }
                }
            }
        }
    }

    fun getClsTestDialog(pos: Int){
        binding.examDialog.root.visibility = View.VISIBLE
        binding.examDialog.close1.setOnClickListener {
            binding.examDialog.root.visibility = View.GONE
            classTestAttach.clear()
            binding.examDialog.attach.text = ""
        }
        binding.examDialog.cancel.setOnClickListener {
            binding.examDialog.root.visibility = View.GONE
            classTestAttach.clear()
            binding.examDialog.attach.text = ""
        }
        binding.examDialog.makeasdone.setOnClickListener {
            binding.examDialog.root.visibility = View.GONE
            classTestAttach.clear()
            binding.examDialog.attach.text = ""
        }
        binding.examDialog.attach.setOnClickListener {
            openDocList()
        }
        binding.examDialog.next.setOnClickListener {
            onBackPressed()
        }
        binding.examDialog.makeasdone.setOnClickListener {
            if (classTestAttach.isNotEmpty()){
                DialogUtils.showLoader(this)
                val classTestId = clsTestRes[pos].classTest!!._id
                if (classTestId != null){
                    ApiConnection.getInstance().classTestStsUpdate(this,classTestId,classTestAttach).observe(this){
                        it.let {
                            DialogUtils.dismissLoader()
                            it.success.let { success->
                                if (success){
                                    UiUtils.showSnack(it.msg,binding.root,true)
                                    binding.examDialog.root.visibility = View.GONE
                                    getClassTest()
                                    classTestAttach.clear()
                                    binding.examDialog.attach.text = ""
                                }
                                else {
                                    UiUtils.showSnack(it.msg,binding.root,false)
                                }
                            }
                        }
                    }
                }
                else {
                    UiUtils.showSnack("Class Test id is not present",binding.root,false)
                }
            }
            else {
                UiUtils.showSnack("Please upload your Class Test",binding.root,false)
            }
        }
        if(clsTestRes[pos].subject != null && clsTestRes[pos].subject!!.name != null){
            binding.examDialog.subName.text = clsTestRes[pos].subject!!.name
        }
        else {
            binding.examDialog.subName.text = "--/--"
        }
        if (clsTestRes[pos].classTest != null && clsTestRes[pos].classTest!!.title != null){
            binding.examDialog.testTitle.text = clsTestRes[pos].classTest!!.title
            binding.examDialog.chapter.text = clsTestRes[pos].classTest!!.title
        }
        else{
            binding.examDialog.testTitle.text = "--/--"
        }
        if (clsTestRes[pos].classTest != null && clsTestRes[pos].classTest!!.description != null){
            binding.examDialog.description.text = " * ${clsTestRes[pos].classTest!!.description}"
        }
        else{
            binding.examDialog.description.text = "--/--"
        }
        if (clsTestRes[pos].classTest != null && clsTestRes[pos].classTest!!.totalMarks != null){
            binding.examDialog.totalMarks.text = clsTestRes[pos].classTest!!.totalMarks!!.toString()
            if (clsTestRes[pos].classTest!!.createdBy != null){
                binding.examDialog.studentName.text = "${clsTestRes[pos].classTest!!.createdBy!!.firstName} ${clsTestRes[pos].classTest!!.createdBy!!.lastName}"
            }
            else{
                binding.examDialog.studentName.text = "--/--"
            }
        }
        else{
            binding.examDialog.totalMarks.text = "--/--"
        }

       /* if (clsTestRes[pos].status == "completed"){
            if (clsTestRes[pos].scored_marks != null){
                binding.examDialog.yourMarks.visibility = View.VISIBLE
                binding.examDialog.scoredMarks.text = clsTestRes[pos].scored_marks!!.toString()
            }
            else{
                binding.examDialog.scoredMarks.text = "--/--"
            }
        }
        else{
            binding.examDialog.yourMarks.visibility = View.GONE
        }
        if (clsTestRes[pos].scheduledOn != null){
            binding.examDialog.date.text = BaseUtils.getFormattedDate(clsTestRes[pos].scheduledOn!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
        }
        else{
            binding.examDialog.date.text = "--/--"
        }
        if (clsTestRes[pos].remarks != null){
            binding.examDialog.remarks.text = clsTestRes[pos].remarks
        }
        else{
            binding.examDialog.remarks.text = "--/--"
        }
        if (clsTestRes[pos].attachment!! != null && clsTestRes[pos].attachment!!.isNotEmpty()){
            binding.examDialog.attachRecycler.visibility = View.VISIBLE
            val adapter = AttachAdapter(this,clsTestRes[pos].attachment!!)
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
            binding.examDialog.attachRecycler.layoutManager = layoutManager
            binding.examDialog.attachRecycler.adapter = adapter
        }
        else {
            binding.examDialog.attachRecycler.visibility = View.GONE
        }*/
        if (clsTestRes[pos].status == "completed" && clsTestRes[pos].markStatus == "completed"){
            binding.examDialog.uploadLay.visibility = View.GONE
            binding.examDialog.attachLay.visibility = View.VISIBLE
            binding.examDialog.makeasdone.visibility = View.VISIBLE
            binding.examDialog.remarkLay.visibility = View.VISIBLE
            binding.examDialog.examStatus.text = "Submitted"
            binding.examDialog.next.text = "Okay"
            binding.examDialog.next.visibility = View.VISIBLE
            //  UiUtils.textviewCustomDrawable(binding.examDialog.examStatus, R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(binding.examDialog.examStatus,"#e6ffe7", null)
            UiUtils.textViewTextColor(binding.examDialog.examStatus,"#32B138", null)
            UiUtils.textViewTextColor(binding.examDialog.remarks,"#32B138", null)
            val submittedOn = BaseUtils.getFormattedDate(clsTestRes[pos].submittedOn!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
            if (clsTestRes[pos].credits != null && clsTestRes[pos].credits!!.toInt() > 0){
                binding.examDialog.credits.text = "+${clsTestRes[pos].credits} Points"
                UiUtils.textViewTextColor(binding.examDialog.credits,"#32B138", null)
            }
            else {
                binding.examDialog.credits.text = "0 Point"
                UiUtils.textViewTextColor(binding.examDialog.credits,"#32B138", null)
            }
            when(clsTestRes[pos].remarks){
                "verygood" -> {
                    binding.examDialog.remarks.text = "Outstanding performance! You’re doing great."
                    UiUtils.textViewGradient(binding.examDialog.remarks,"#32B138","#138f18")//green
                }
                "good" -> {
                    binding.examDialog.remarks.text = "Great job! Keep improving steadily."
                    UiUtils.textViewTextColor(binding.examDialog.remarks,"#3F8BFB",null) //blue
                }
                "poor" -> {
                    binding.examDialog.remarks.text = "Keep trying; you’ll get there soon."
                    UiUtils.textViewTextColor(binding.examDialog.remarks,"#F69300",null) //orange
                }
                "need_attention" -> {
                    binding.examDialog.remarks.text = "Work harder; success is within reach."
                    UiUtils.textViewTextColor(binding.examDialog.remarks,"#F69300",null) //orange
                }
            }
            val attachments = clsTestRes[pos].attachment
            if (!attachments.isNullOrEmpty()){
                binding.examDialog.attachRecycler.visibility = View.VISIBLE
                val adapter = AttachAdapter(this, attachments)
                val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                binding.examDialog.attachRecycler.layoutManager = layoutManager
                binding.examDialog.attachRecycler.adapter = adapter
            }
            else {
                binding.examDialog.attachRecycler.visibility = View.GONE
            }
        }
        else if (clsTestRes[pos].status == "overdue" && clsTestRes[pos].status == "overdue"){
            binding.examDialog.uploadLay.visibility = View.VISIBLE
            binding.examDialog.attachLay.visibility = View.GONE
            binding.examDialog.makeasdone.visibility = View.GONE
            binding.examDialog.remarkLay.visibility = View.GONE
            binding.examDialog.examStatus.text = "Not Completed"
            UiUtils.textviewCustomDrawable(binding.examDialog.examStatus, R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(binding.examDialog.examStatus,"#fce6e6", null)
            UiUtils.textViewTextColor(binding.examDialog.examStatus,"#EA5455", null)
            UiUtils.textViewTextColor(binding.examDialog.remarks,"#EA5455", null)
        }
        else if (clsTestRes[pos].status == "pending" && clsTestRes[pos].status == "pending"){
            binding.examDialog.uploadLay.visibility = View.VISIBLE
            binding.examDialog.attachLay.visibility = View.GONE
            binding.examDialog.makeasdone.visibility = View.GONE
            binding.examDialog.remarkLay.visibility = View.GONE
            binding.examDialog.examStatus.text = "Ongoing"
            UiUtils.textviewCustomDrawable(binding.examDialog.examStatus, R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(binding.examDialog.examStatus,"#fff2d9", null)
            UiUtils.textViewTextColor(binding.examDialog.examStatus,"#F69300", null)
        }
        else if (clsTestRes[pos].status == "completed" && clsTestRes[pos].markStatus == "pending"){
            binding.examDialog.uploadLay.visibility = View.GONE
            binding.examDialog.attachLay.visibility = View.GONE
            binding.examDialog.makeasdone.visibility = View.VISIBLE
            binding.examDialog.remarkLay.visibility = View.VISIBLE
            binding.examDialog.remarks.visibility = View.GONE
            binding.examDialog.examStatus.text = "Submitted"
            binding.examDialog.remarks.text = "Your Points update later"
            UiUtils.textviewCustomDrawable(binding.examDialog.examStatus, R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(binding.examDialog.examStatus,"#e6ffe7", null)
            UiUtils.textViewTextColor(binding.examDialog.examStatus,"#32B138", null)
            UiUtils.textViewTextColor(binding.examDialog.remarks,"#32B138", null)
            val submittedOn = BaseUtils.getFormattedDate(clsTestRes[0].submittedOn!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)

            val attachments = clsTestRes[pos].attachment
            if (!attachments.isNullOrEmpty()){
                binding.examDialog.attachRecycler.visibility = View.VISIBLE
                val adapter = AttachAdapter(this, attachments)
                val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                binding.examDialog.attachRecycler.layoutManager = layoutManager
                binding.examDialog.attachRecycler.adapter = adapter
            }
            else {
                binding.examDialog.attachRecycler.visibility = View.GONE
            }
        }
    }


    fun openDocList() {
        if (BaseUtils.isPermissionsEnabled(this, Constants.IntentKeys.STORAGE)) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "*/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.putExtra(
                Intent.EXTRA_MIME_TYPES, arrayOf(
                    "image/png",
                    "image/jpg",
                    "image/jpeg",
                    "application/pdf",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .docx
                    "application/msword"
                )
            )
            startActivityForResult(intent, 12)
        } else {
            BaseUtils.permissionsEnableRequest(this, Constants.IntentKeys.STORAGE)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 12 && resultCode == RESULT_OK) {
            if (data != null){
                if (data.clipData != null){
                    var docs = data.clipData
                    for (items in 0 until  docs!!.itemCount){
                        count++
                        val uri = docs.getItemAt(items).uri
                        var filePart: MultipartBody.Part? = null
                        if (uri != null) {
                            val documentFile = DocumentFile.fromSingleUri(this, uri)
                            val fileInputStream = this.contentResolver.openInputStream(uri)
                            val mimeType = this.contentResolver.getType(uri)
                            val buffer = fileInputStream?.readBytes()
                            fileInputStream?.close()
                            if (buffer != null && mimeType != null) {
                                val fileSize = buffer.size
                                val fileSizeInMB = fileSize / (1024.0 * 1024.0)
                                val fileBody = RequestBody.create(mimeType.toMediaTypeOrNull(), buffer)
                                filePart = MultipartBody.Part.createFormData("file", documentFile?.name, fileBody)
                                val size = BaseUtils.convertBytes(fileSize.toLong())
                                Log.d("dv1", "" + documentFile?.name)
                                Log.d("dv0", "" + uri.path)
                                Log.d("dv3", "" + size)
                                Log.d("dv4", "" + mimeType)
                                if (fileSizeInMB <= 5){
                                    val json = JSONObject()
                                    json.put("name",documentFile?.name)
                                    json.put("size",size)
                                    json.put("type",mimeType)
//                                    urlName.add(json)
                                    upload(filePart)
                                } else {
                                    UiUtils.showSnack("File size exceeds 50 MB", binding.root,false)
                                }
                            }
                        }
                    }
                } else {
                    val uri = data?.data
                    var filePart: MultipartBody.Part? = null
                    count++
                    if (uri != null) {
                        val documentFile = DocumentFile.fromSingleUri(this, uri)
                        val fileInputStream = this.contentResolver.openInputStream(uri)
                        val mimeType = this.contentResolver.getType(uri)
                        val buffer = fileInputStream?.readBytes()
                        fileInputStream?.close()
                        if (buffer != null && mimeType != null) {
                            val fileSize = buffer.size
                            val fileSizeInMB = fileSize / (1024.0 * 1024.0)
                            val fileBody = RequestBody.create(mimeType.toMediaTypeOrNull(), buffer)
                            filePart = MultipartBody.Part.createFormData("file", documentFile?.name, fileBody)
                            val size = BaseUtils.convertBytes(fileSize.toLong())
                            Log.d("dv1", "" + documentFile?.name)
                            Log.d("dv0", "" + uri.path)
                            Log.d("dv3", "" + size)
                            Log.d("dv4", "" + mimeType)
                            if (fileSizeInMB <= 5){
                                val json = JSONObject()
                                json.put("name",documentFile?.name)
                                json.put("size",size)
                                json.put("type",mimeType)
//                                urlName.add(json)
                                binding.examDialog.attach.text = documentFile?.name
                                upload(filePart)
                            } else {
                                UiUtils.showSnack("File size exceeds 5 MB", binding.root,false)
                            }
                        }
                    }
                }
            }
        }
    }
    fun upload(filepart: MultipartBody.Part){
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().uploadFile(this, filepart).observe(this) {
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success && it.result.isNotEmpty()) {
                        UiUtils.showSnack(it.msg, binding.root,true)
                        val url = it.result[0].location!!
                        classTestAttach.add(url)
                    }
                    else {
                        UiUtils.showSnack(it.msg, binding.root,false)
                    }
                }
            }
        }
    }

    private fun getCurrentMonthDays(year: Int? = null, month: Int? = null): ArrayList<Date> {
        val calendar = Calendar.getInstance()

        // Use provided year and month, or default to the current year and month
        calendar.set(Calendar.YEAR, year ?: calendar.get(Calendar.YEAR))
        calendar.set(Calendar.MONTH, month ?: calendar.get(Calendar.MONTH))
        calendar.set(Calendar.DAY_OF_MONTH, 1) // Start from the 1st of the month

        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val daysList = ArrayList<Date>()

        for (i in 1..daysInMonth) {
            calendar.set(Calendar.DAY_OF_MONTH, i) // Update day in the loop
            daysList.add(calendar.time)
        }

        return daysList
    }

    fun RecyclerView.centerItem(position: Int, itemWidth: Int) {
        post {
            val layoutManager = layoutManager as? LinearLayoutManager ?: return@post
            val visibleWidth = width - paddingLeft - paddingRight
            val exactCenter = (visibleWidth / 2) - (itemWidth / 2)
            layoutManager.scrollToPositionWithOffset(position, exactCenter)
        }
    }

    fun loadDates(){
        calendar = Calendar.getInstance()
        val currentDate = calendar.time
        val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(Date())
        binding.date.text = date
        val sdfApi = SimpleDateFormat("EEEE", Locale.getDefault())
        val adapter1 = WeekDayAdapter1(currentMonthDays, currentDate) { selectedDate ->
            UiUtils.log("gfhjk",""+selectedDate)
            val dt = sdfApi.format(selectedDate).toLowerCase(Locale.getDefault())
            filterDate = dt
            if (examTabClicked == "exam"){
                getStudentExamination()
            }
            else {
                getClassTest()
            }
        }
        val linearLayoutManager1 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.dateRecycler1.layoutManager = linearLayoutManager1
        binding.dateRecycler1.adapter = adapter1

        val currentDayIndex1 = currentMonthDays.indexOfFirst { sdfDate.format(it) == sdfDate.format(currentDate) }
        if (currentDayIndex1 != -1) {
            val itemWidth = resources.getDimensionPixelSize(R.dimen._55dp)
            binding.dateRecycler1.centerItem(currentDayIndex1, itemWidth)
        }
    }

}