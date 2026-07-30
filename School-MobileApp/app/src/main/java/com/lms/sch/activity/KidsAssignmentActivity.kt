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
import com.lms.sch.adapter.StudentAssignmentAdapter1
import com.lms.sch.adapter.WeekDayAdapter1
import com.lms.sch.adapter.YearAdapter
import com.lms.sch.databinding.ActivityKidsAssignmentBinding
import com.lms.sch.databinding.DialogMonthPickerBinding
import com.lms.sch.databinding.FilterAssignmentBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.GetStudentAssignmentRes
import com.lms.sch.response.GetStudentAssignmentResponse
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

class KidsAssignmentActivity : BaseActivity() {
    lateinit var binding : ActivityKidsAssignmentBinding
    var result = ArrayList<GetStudentAssignmentRes.Result>()
    var assignmentStatus = ""
    var search = ""
    var count = 0
    var assignmentAttach = ArrayList<String>()
    var selectedMonth = 0
    var calendar = Calendar.getInstance()
    var selectedYear = 0
    private var currentMonthDays = ArrayList<Date>()
    val years = arrayListOf("2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030")
    val months = arrayListOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    var filterDate = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityKidsAssignmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backarrow.setOnClickListener {
            finish()
        }
        binding.profile.setOnClickListener {
            BaseUtils.startActivity(this, ProfileActivity(),null,false)
        }
        val currentYear = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date()).toInt()
        currentMonthDays = getCurrentMonthDays()
        loadDates()
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
                    getStudentAssignment()
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
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                search = binding.search.text.toString()
                getStudentAssignment()
            }
            override fun beforeTextChanged(s: CharSequence, start: Int,count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        binding.search.setOnEditorActionListener(TextView.OnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                search = binding.search.text.toString()
                getStudentAssignment()
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
            
            if (assignmentStatus == "ongoing"){
                UiUtils.textviewImgDrawable(bind.ongoing,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.all,null,"start")
                UiUtils.textviewImgDrawable(bind.pending,null,"start")
                UiUtils.textviewImgDrawable(bind.completed,null,"start")
            }
            else if (assignmentStatus == "pending"){
                UiUtils.textviewImgDrawable(bind.pending,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.all,null,"start")
                UiUtils.textviewImgDrawable(bind.ongoing,null,"start")
                UiUtils.textviewImgDrawable(bind.completed,null,"start")
            }
            else if (assignmentStatus == "completed"){
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

            bind.all.setOnClickListener {
                assignmentStatus = ""
                getStudentAssignment()
                popupWindow.dismiss()
            }
            bind.pending.setOnClickListener {
                assignmentStatus = "pending"
                getStudentAssignment()
                popupWindow.dismiss()
            }
            bind.ongoing.setOnClickListener {
                assignmentStatus = "ongoing"
                getStudentAssignment()
                popupWindow.dismiss()
            }
            bind.completed.setOnClickListener {
                assignmentStatus = "completed"
                getStudentAssignment()
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

            popupWindow.showAtLocation(anchorView,Gravity.NO_GRAVITY,xPos,yPos)
        }

        getStudentAssignment()
    }

    private fun getStudentAssignment() {
        if (search.isEmpty()){
            DialogUtils.showLoader(this)
        }
        ApiConnection.getInstance().getStudentAssignment1(this,search,assignmentStatus,"").observe(this) {
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null && it.result!!.isNotEmpty()) {
                            result = it.result!!
                            binding.assignmentRecycler.visibility = View.VISIBLE
                            binding.noData.root.visibility = View.GONE
                            val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
                            val adapter = StudentAssignmentAdapter1(this,false,result,object : OnClickListener {
                                override fun onClickItem(pos: Int) {
//                                    getAssignmentStatus(pos)
                                }
                            })
                            binding.assignmentRecycler.layoutManager = layoutManager
                            binding.assignmentRecycler.adapter = adapter
                        }
                        else{
                            binding.assignmentRecycler.visibility = View.GONE
                            binding.noData.root.visibility = View.VISIBLE
//                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    }
                    else {
                        binding.assignmentRecycler.visibility = View.GONE
                        binding.noData.root.visibility = View.VISIBLE
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
    }
    
    fun getAssignmentStatus(pos: Int){
        binding.dialogAssignment.cancel.setOnClickListener {
            binding.dialogAssignment.root.visibility = View.GONE
        }
        binding.dialogAssignment.doneAssingment.setOnClickListener {
            binding.dialogAssignment.root.visibility = View.GONE
        }
        binding.dialogAssignment.close.setOnClickListener {
            binding.dialogAssignment.root.visibility = View.GONE
        }
        binding.dialogAssignment.attach.setOnClickListener {
            openDocList()
        }
        binding.dialogAssignment.makeasdone.setOnClickListener {
            if (assignmentAttach.isNotEmpty()){
                DialogUtils.showLoader(this)
                val assignmentId = result[pos].assignment!!._id
                if (assignmentId != null){
                    ApiConnection.getInstance().assignmentStsUpdate(this,assignmentId,assignmentAttach).observe(this){
                        it.let {
                            DialogUtils.dismissLoader()
                            it.success.let { success->
                                if (success){
                                    UiUtils.showSnack(it.msg,binding.root,true)
                                    binding.dialogAssignment.root.visibility = View.GONE
                                    getStudentAssignment()
                                } else {
                                    UiUtils.showSnack(it.msg, binding.root, false)
                                }
                            }
                        }
                    }
                }
                else {
                    UiUtils.showSnack("Assignment id is not present",binding.root,false)
                }
            }
            else {
                UiUtils.showSnack("Please upload your Assignment",binding.root,false)
            }
        }

        if (result[pos].assignment != null && result[pos].assignment!!.createdBy != null && result[pos].assignment!!.createdBy!!.firstName != null && result[pos].assignment!!.createdBy!!.lastName != null){
            binding.dialogAssignment.teacher.text = "Teacher : ${result[pos].assignment!!.createdBy!!.firstName} ${result[pos].assignment!!.createdBy!!.lastName}"
        }
        else{
            binding.dialogAssignment.teacher.text = "--/--"
        }
        if (result[pos].subject != null && result[pos].subject!!.name!!.isNotEmpty()){
            binding.dialogAssignment.subject.text = "Subject : ${result[pos].subject!!.name}"
        }
        else{
            binding.dialogAssignment.subject.text = "--/--"
        }
        if (result[pos].assignment != null && result[pos].assignment!!.title != null && result[pos].assignment!!.description != null){
            binding.dialogAssignment.assTitle.text = result[pos].assignment!!.title
            binding.dialogAssignment.tMarks.text = " / ${result[pos].assignment!!.totalMarks}"
            binding.dialogAssignment.assdsc.setContent(result[pos].assignment!!.description)
        }
        else{
            binding.dialogAssignment.assTitle.text = "--/--"
            binding.dialogAssignment.assdsc.text = "--/--"
        }
        if (result[pos].createdAt != null){
            binding.dialogAssignment.givendate.text = BaseUtils.getFormattedDate(result[pos].createdAt!!,Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT)
        }
        else{
            binding.dialogAssignment.givendate.text = "--/--"
        }
        var dueDate = BaseUtils.getFormattedDate(result[pos].dueDate!!,Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT)
        var submitDate = BaseUtils.getFormattedDate(result[pos].submittedOn!!,Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT)

        if (result[pos].status == "completed"){
            binding.dialogAssignment.resultLay.visibility = View.VISIBLE
            binding.dialogAssignment.uploadLay.visibility = View.GONE
            binding.dialogAssignment.noteLay.visibility = View.VISIBLE
            binding.dialogAssignment.attachLay.visibility = View.VISIBLE
            binding.dialogAssignment.status.text = "Submitted"
            UiUtils.textviewCustomDrawable(binding.dialogAssignment.status, R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(binding.dialogAssignment.status,"#e6ffe7", null)
            UiUtils.textViewTextColor(binding.dialogAssignment.status,"#32B138", null)
            UiUtils.textViewTextColor(binding.dialogAssignment.points,"#32B138", null)
            binding.dialogAssignment.submitOn.text = "Submitted On : "
            binding.dialogAssignment.submitdate.text = submitDate
            if (result[pos].submittedOnTime){
                binding.dialogAssignment.note.text = "Good job! Your Assignment has been submitted successfully, You’ve gained points for your submission!"
                UiUtils.linearLayoutBgTint(binding.dialogAssignment.noteLay,"#EFFFF0",null)
            }
            else {
                binding.dialogAssignment.note.text = "Your Assignment was submitted late. Great effort! Aim to submit on time to maximize your points."
                UiUtils.linearLayoutBgTint(binding.dialogAssignment.noteLay,"#fafce3",null)
            }

            if (result[pos].credits != null){
                binding.dialogAssignment.points.text = "+${result[pos].credits} Points"
                UiUtils.textViewTextColor(binding.dialogAssignment.points,"#32B138", null)
            }
            else {
                binding.dialogAssignment.points.text = "0 Point"
                UiUtils.textViewTextColor(binding.dialogAssignment.points,"#EA5455", null)
            }
            if (result[pos].markStatus == "pending"){
                binding.dialogAssignment.remarks.text = "Not yet updated"
                UiUtils.textViewTextColor(binding.dialogAssignment.remarks,"#333333",null) //orange
            }
            else {
                binding.dialogAssignment.sMarks.text = result[pos].scored_marks.toString()
                when(result[pos].remarks){
                    "verygood" -> {
                        binding.dialogAssignment.remarks.text = "Outstanding performance! You’re doing great."
//                        UiUtils.textViewTextColor(binding.dialogAssignment.remarks,"#32B138",null)//green
                        UiUtils.textViewGradient(binding.dialogAssignment.remarks,"#32B138","#138f18")//green
                    }
                    "good" -> {
                        binding.dialogAssignment.remarks.text = "Great job! Keep improving steadily."
                        UiUtils.textViewTextColor(binding.dialogAssignment.remarks,"#3F8BFB",null) //blue
                    }
                    "poor" -> {
                        binding.dialogAssignment.remarks.text = "Keep trying; you’ll get there soon."
                        UiUtils.textViewTextColor(binding.dialogAssignment.remarks,"#F69300",null) //orange
                    }
                    "need_attention" -> {
                        binding.dialogAssignment.remarks.text = "Work harder; success is within reach."
                        UiUtils.textViewTextColor(binding.dialogAssignment.remarks,"#F69300",null) //orange
                    }
                }
            }

            if (result[pos].attachment!! != null && result[pos].attachment!!.isNotEmpty()){
                binding.dialogAssignment.attachRecycler.visibility = View.VISIBLE
                val adapter = AttachAdapter(this,result[pos].attachment!!)
                val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
                binding.dialogAssignment.attachRecycler.layoutManager = layoutManager
                binding.dialogAssignment.attachRecycler.adapter = adapter
            }
            else {
                binding.dialogAssignment.attachRecycler.visibility = View.GONE
            }
        }
        else if (result[pos].status == "overdue"){
            binding.dialogAssignment.resultLay.visibility = View.GONE
            binding.dialogAssignment.uploadLay.visibility = View.VISIBLE
            binding.dialogAssignment.noteLay.visibility = View.VISIBLE
            binding.dialogAssignment.attachLay.visibility = View.GONE
            binding.dialogAssignment.status.text = "Not Completed"
            UiUtils.textviewCustomDrawable(binding.dialogAssignment.status, R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(binding.dialogAssignment.status,"#fce6e6", null)
            UiUtils.textViewTextColor(binding.dialogAssignment.status,"#EA5455", null)
            UiUtils.textViewTextColor(binding.dialogAssignment.points,"#EA5455", null)
            binding.dialogAssignment.submitOn.text = "Last Date : "
            binding.dialogAssignment.submitdate.text = dueDate
            binding.dialogAssignment.note.text = "This Assingment was due on $dueDate, and it looks like you haven’t finished it yet. Please complete it as soon as possible to stay on track!"

        }
        else if (result[pos].status == "pending"){
            binding.dialogAssignment.resultLay.visibility = View.GONE
            binding.dialogAssignment.uploadLay.visibility = View.VISIBLE
            binding.dialogAssignment.noteLay.visibility = View.VISIBLE
            binding.dialogAssignment.attachLay.visibility = View.GONE
            binding.dialogAssignment.points.visibility = View.GONE
            binding.dialogAssignment.status.text = "Ongoing"
            UiUtils.textviewCustomDrawable(binding.dialogAssignment.status, R.drawable.border_curve_24dp)
            UiUtils.textViewBgTint(binding.dialogAssignment.status,"#fff2d9", null)
            UiUtils.textViewTextColor(binding.dialogAssignment.status,"#F69300", null)
            binding.dialogAssignment.submitOn.text = "Last Date : "
            binding.dialogAssignment.submitdate.text = dueDate
            binding.dialogAssignment.note.text = "Don't forget! You need to complete and submit before the Last date to earn points, if you miss the deadline, you won't receive any points."
            UiUtils.linearLayoutBgTint(binding.dialogAssignment.noteLay,"#FFF2F2",null)
        }

        UiUtils.animation(this,binding.dialogAssignment.topLay,R.anim.slide_in_from_bottom,true)
        binding.dialogAssignment.root.visibility = View.VISIBLE
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
                            val fileInputStream = contentResolver.openInputStream(uri)
                            val mimeType = contentResolver.getType(uri)
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
                        val fileInputStream = contentResolver.openInputStream(uri)
                        val mimeType = contentResolver.getType(uri)
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
                                binding.dialogAssignment.attach.text = documentFile?.name
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

    fun upload(filepart:MultipartBody.Part){
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().uploadFile(this, filepart).observe(this) {
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success && it.result.isNotEmpty()) {
                        UiUtils.showSnack(it.msg, binding.root,true)
                        val url = it.result[0].location!!
                        assignmentAttach.add(url)
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
            getStudentAssignment()
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