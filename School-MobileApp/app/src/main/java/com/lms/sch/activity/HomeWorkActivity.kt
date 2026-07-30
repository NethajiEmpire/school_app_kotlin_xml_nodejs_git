package com.lms.sch.activity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import com.lms.sch.R
import com.lms.sch.adapter.AttachAdapter
import com.lms.sch.adapter.SpinnerAdapter
import com.lms.sch.adapter.StudentHwListAdapter
import com.lms.sch.databinding.ActivityCreateHomeWorkBinding
import com.lms.sch.databinding.FilterHomeworkBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.GetHomeworkResponse
import com.lms.sch.response.GetTeacherHomeWorkResponse
import com.lms.sch.response.TeacherHwSingleResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils

class HomeWorkActivity : BaseActivity() {
    lateinit var binding : ActivityCreateHomeWorkBinding
    var result = ArrayList<GetTeacherHomeWorkResponse.Result.Rows>()
    var studentHwRes = ArrayList<GetHomeworkResponse.Result>()
    private var hwRes: TeacherHwSingleResponse.Result? = null
    var homeworkId = ""
    var status = ""
    var remark = ""
    var search = ""
    var student = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateHomeWorkBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        homeworkId = intent.getStringExtra("id")!!
        status = ""
        getStudentDetails()
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().getHwSingle(this, homeworkId).observe(this) {
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null) {
                            hwRes = it.result
                            if (hwRes!!.subject != null && hwRes!!.subject!!.name!!.isNotEmpty()) {
                                binding.subject.text = hwRes!!.subject!!.name
                            } else {
                                binding.subject.text = "--/--"
                            }
                            binding.title.text = " Title : ${it.result!!.title!!}"
                            binding.desc.text = it.result!!.description!!

//                            if (hwRes!!.teacher != null && hwRes!!.teacher!!.firstName != null && hwRes!!.teacher!!.lastName != null) {
//                                binding.txtIncharge.text = "${hwRes!!.teacher!!.firstName} ${hwRes!!.teacher!!.lastName}"
//                            } else {
//                                binding.txtIncharge.text = "--/--"
//                            }
                            if (hwRes!!.createdAt != null) {
                                binding.date.text = "Given On : ${BaseUtils.getFormattedDate(hwRes!!.createdAt!!,
                                    Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT)}-${BaseUtils.getFormattedDate(hwRes!!.updatedAt!!,
                                    Constants.ApiKeys.TIME_INPUT_FORMAT,Constants.ApiKeys.DATE_FORMAT)}"
                            } else {
                                binding.date.text = "--/--"
                            }
//                            when(hwRes!!.status){
//                                "pending" ->{
//                                    binding.status.text = "Pending"
//                                    UiUtils.textviewCustomDrawable(binding.status,R.drawable.border_curve_24dp)
//                                    UiUtils.textViewBgTint(binding.status,"#fff2d9" ,null)
//                                    UiUtils.textViewTextColor(binding.status,"#F69300",null)
//                                }
//                                "completed" ->{
//                                    binding.status.text = "Completed"
//                                    UiUtils.textviewCustomDrawable(binding.status,R.drawable.border_curve_24dp)
//                                    UiUtils.textViewBgTint(binding.status,"#e6ffe7" ,null)
//                                    UiUtils.textViewTextColor(binding.status,"#32B138",null)
//                                }
//                                else -> {
//                                    binding.status.text = hwRes!!.status
//                                    UiUtils.textviewCustomDrawable(binding.status,R.drawable.border_curve_24dp)
//                                    UiUtils.textViewBgTint(binding.status,"#fff2d9" ,null)
//                                    UiUtils.textViewTextColor(binding.status,"#F69300",null)
//                                }
//                            }
                        }
                        else{
                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    }
                    else {
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }

        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int,p2: Int,p3: Int) {}
            override fun onTextChanged(p0: CharSequence?,p1: Int,p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                search = binding.search.text.toString()
                getStudentDetails()
            }
        })
        binding.search.setOnEditorActionListener(TextView.OnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                search = binding.search.text.toString()
                getStudentDetails()
            }
            false
        })
        binding.backarrow.setOnClickListener{
            onBackPressed()
        }
        binding.filter.setOnClickListener {
            val inflater = LayoutInflater.from(this)
            val bind : FilterHomeworkBinding = FilterHomeworkBinding.inflate(inflater)
            bind.today.visibility = View.GONE
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
            if (status == "today"){
                UiUtils.textviewImgDrawable(bind.today,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.all,null,"start")
                UiUtils.textviewImgDrawable(bind.pending,null,"start")
                UiUtils.textviewImgDrawable(bind.completed,null,"start")
            }
            else if (status == "pending"){
                UiUtils.textviewImgDrawable(bind.pending,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.all,null,"start")
                UiUtils.textviewImgDrawable(bind.today,null,"start")
                UiUtils.textviewImgDrawable(bind.completed,null,"start")
            }
            else if (status == "completed"){
                UiUtils.textviewImgDrawable(bind.completed,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.all,null,"start")
                UiUtils.textviewImgDrawable(bind.today,null,"start")
                UiUtils.textviewImgDrawable(bind.pending,null,"start")
            }
            else {
                UiUtils.textviewImgDrawable(bind.all,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.pending,null,"start")
                UiUtils.textviewImgDrawable(bind.today,null,"start")
                UiUtils.textviewImgDrawable(bind.completed,null,"start")
            }
            bind.all.setOnClickListener {
                status = ""
                getStudentDetails()
                popupWindow.dismiss()
            }
            bind.pending.setOnClickListener {
                status = "pending"
                getStudentDetails()
                popupWindow.dismiss()
            }

            bind.completed.setOnClickListener {
                status = "completed"
                getStudentDetails()
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

            popupWindow.showAtLocation(
                anchorView,
                Gravity.NO_GRAVITY,
                xPos,
                yPos
            )
        }
    }

    fun getStudentDetails(){
        if (search.isEmpty()){
            DialogUtils.showLoader(this)
        }
        ApiConnection.getInstance().studentHomework(this,search,"",status,homeworkId).observe(this){
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success){
                        if (it.result != null && it.result!!.isNotEmpty()){
                            binding.noData.root.visibility = View.GONE
                            binding.recycler.visibility = View.VISIBLE
                            studentHwRes = it.result!!
                            val adapter = StudentHwListAdapter(this,status,it.result!!,object: OnClickListener{
                                override fun onClickItem(pos: Int) {
                                    updMark(pos)
                                }
                            })
                            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
                            binding.recycler.layoutManager = layoutManager
                            binding.recycler.adapter = adapter
                        }
                        else {
                            binding.noData.root.visibility = View.VISIBLE
                            binding.recycler.visibility = View.GONE
//                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    }
                    else {
                        binding.noData.root.visibility = View.VISIBLE
                        binding.recycler.visibility = View.GONE
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
    }
    fun updMark(pos : Int){
        if (studentHwRes[pos].status == "pending" ){
            UiUtils.showSnack(" student not submited the Homework yet ", binding.root, false)
            binding.dialogMarkShow.markLay.visibility = View.GONE
            binding.dialogMarkShow.root.visibility = View.GONE
            binding.dialogMarkUpdate.root.visibility = View.GONE
        }
        else if (studentHwRes[pos].markStatus == "pending" && studentHwRes[pos].status == "completed"){
            var spin = ArrayList<String>()
            spin.add("Select..")
            spin.add("Very Good")
            spin.add("Good")
            spin.add("Average")
            spin.add("Need Attention")
            spin.add("Poor")
            val adapter = SpinnerAdapter(this, spin)
            binding.dialogMarkUpdate.spinner.adapter = adapter
            binding.dialogMarkUpdate.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,view: View?,position: Int,id: Long) {
                    val clickedValue: String = parent.getItemAtPosition(position) as String
                    if (position == 0) {
                        remark = ""
                        return
                    }
                    remark = when (parent.getItemAtPosition(position) as String) {
                        "Very Good" -> "verygood"
                        "Good" -> "good"
                        "Average" -> "average"
                        "Need Attention" -> "need_attention"
                        "Poor" -> "poor"
                        else -> ""
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
            var homework = ""
            var marks = ""

//            marks = binding.dialogMarkUpdate.mark.text.toString()
            if (studentHwRes[pos].student != null){
                student = studentHwRes[pos].student!!.id!!
                binding.dialogMarkUpdate.stdName.text = "${studentHwRes[pos].student!!.firstName} ${studentHwRes[pos].student!!.lastName}"
            }
            if (studentHwRes[pos].homework != null){
                homework = studentHwRes[pos].homework!!._id!!
                binding.dialogMarkUpdate.tMark.text = studentHwRes[pos].homework!!.title
            }
            if (studentHwRes[pos].attachment!!.isNotEmpty()){
                binding.dialogMarkUpdate.attachRecycler.visibility = View.VISIBLE
                binding.dialogMarkUpdate.txct.visibility = View.VISIBLE
                val adapter = AttachAdapter(this, studentHwRes[pos].attachment!!)
                val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
                binding.dialogMarkUpdate.attachRecycler.layoutManager = layoutManager
                binding.dialogMarkUpdate.attachRecycler.adapter = adapter
            }
            else {
                binding.dialogMarkUpdate.attachRecycler.visibility = View.GONE
                binding.dialogMarkUpdate.txct.visibility = View.GONE
            }
            binding.dialogMarkUpdate.cancel.setOnClickListener {
                binding.dialogMarkUpdate.root.visibility = View.GONE
            }
            binding.dialogMarkUpdate.close.setOnClickListener {
                binding.dialogMarkUpdate.root.visibility = View.GONE
            }
            binding.dialogMarkUpdate.done.setOnClickListener {
                marks = binding.dialogMarkUpdate.Homemarks.text.toString()
                if (remark.isEmpty()) {
                    UiUtils.showSnack("Please select a valid remark", binding.root, false)
                    return@setOnClickListener
                }
                if (marks.trim().isEmpty()) {
                    UiUtils.showSnack("Please enter marks", binding.root, false)
                    return@setOnClickListener
                }
                if (student.isNotEmpty()){
                    DialogUtils.showLoader(this)
                    ApiConnection.getInstance().homeworkMarkUpd(this,homework,student,remark,marks).observe(this){
                        it.let {
                            DialogUtils.dismissLoader()
                            it.success.let { success ->
                                if (success){
                                    UiUtils.showSnack(it.msg, binding.root, true)
                                    getStudentDetails()
                                    binding.dialogMarkUpdate.root.visibility = View.GONE
                                }
                                else {
                                    UiUtils.showSnack(it.msg, binding.root, false)
                                }
                            }
                        }
                    }
                }
                else {
                    UiUtils.showSnack("Student id is not present", binding.root, false)
                }
            }
            UiUtils.animation(this,binding.dialogMarkUpdate.topLay, R.anim.slide_in_from_bottom,true)
            binding.dialogMarkUpdate.root.visibility = View.VISIBLE
        }
        else if(studentHwRes[pos].markStatus == "completed" && studentHwRes[pos].status == "completed"){
            binding.dialogMarkShow.root.visibility = View.VISIBLE
            binding.dialogMarkShow.markLay.visibility = View.VISIBLE
            binding.dialogMarkShow.makeasdone.setOnClickListener {
                onBackPressed()
            }
            binding.dialogMarkShow.close2.setOnClickListener {
                onBackPressed()
            }
            binding.dialogMarkShow.txt1.text = "Completed HomeWork"
            binding.dialogMarkShow.urMarktxt.text = "Student Mark"
            binding.dialogMarkShow.stdName.text = "${studentHwRes[pos].student!!.firstName} ${studentHwRes[pos].student!!.lastName}"
            binding.dialogMarkShow.stdMarks.text = "Roll No : --/--"
            binding.dialogMarkShow.remarks.text = studentHwRes[pos].remarks
            binding.dialogMarkShow.marks.text = "${studentHwRes[pos].scored_marks}"
            if (studentHwRes[pos].attachment!!.isNotEmpty()){
                binding.dialogMarkShow.attachRecycler.visibility = View.VISIBLE
                binding.dialogMarkShow.txct.visibility = View.VISIBLE
                val adapter = AttachAdapter(this, studentHwRes[pos].attachment!!)
                val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
                binding.dialogMarkShow.attachRecycler.layoutManager = layoutManager
                binding.dialogMarkShow.attachRecycler.adapter = adapter
            }
            else {
                binding.dialogMarkShow.attachRecycler.visibility = View.GONE
                binding.dialogMarkShow.txct.visibility = View.GONE
            }
            binding.dialogMarkShow.remarks.text = studentHwRes[pos].remarks
        }
        else {
            binding.dialogMarkShow.markLay.visibility = View.GONE
            binding.dialogMarkShow.root.visibility = View.VISIBLE
            binding.dialogMarkUpdate.root.visibility = View.GONE
            binding.dialogMarkShow.txt1.text = "Homework Result"
            if (studentHwRes[pos].student != null){
                binding.dialogMarkShow.stdName.text = "${studentHwRes[pos].student!!.firstName} ${studentHwRes[pos].student!!.lastName}"
            }
            else{
                binding.dialogMarkShow.stdName.text = "--/--"
            }
            if (studentHwRes[pos].attachment!!.isNotEmpty()){
                binding.dialogMarkShow.attachRecycler.visibility = View.VISIBLE
                binding.dialogMarkShow.txct.visibility = View.VISIBLE
                val adapter = AttachAdapter(this, studentHwRes[pos].attachment!!)
                val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
                binding.dialogMarkShow.attachRecycler.layoutManager = layoutManager
                binding.dialogMarkShow.attachRecycler.adapter = adapter
            }
            else {
                binding.dialogMarkShow.attachRecycler.visibility = View.GONE
                binding.dialogMarkShow.txct.visibility = View.GONE
            }
            if (studentHwRes[pos].markStatus == "pending"){
                binding.dialogMarkShow.remarks.text = "Not yet updated"
                UiUtils.textViewTextColor(binding.dialogMarkShow.remarks,"#333333",null) //orange
            }
            else {
                when(studentHwRes[pos].remarks){
                    "verygood" -> {
                        binding.dialogMarkShow.remarks.text = "Outstanding performance! You’re doing great."
//                        UiUtils.textViewTextColor(binding.dialogMarkShow.remarks,"#32B138",null)//green
                        UiUtils.textViewGradient(binding.dialogMarkShow.remarks,"#32B138","#138f18")//green
                    }
                    "good" -> {
                        binding.dialogMarkShow.remarks.text = "Great job! Keep improving steadily."
                        UiUtils.textViewTextColor(binding.dialogMarkShow.remarks,"#3F8BFB",null) //blue
                    }
                    "poor" -> {
                        binding.dialogMarkShow.remarks.text = "Keep trying; you’ll get there soon."
                        UiUtils.textViewTextColor(binding.dialogMarkShow.remarks,"#F69300",null) //orange
                    }
                    "need_attention" -> {
                        binding.dialogMarkShow.remarks.text = "Work harder; success is within reach."
                        UiUtils.textViewTextColor(binding.dialogMarkShow.remarks,"#F69300",null) //orange
                    }
                }
            }

            binding.dialogMarkShow.cancel.setOnClickListener {
                binding.dialogMarkShow.root.visibility = View.GONE
            }
            binding.dialogMarkShow.close2.setOnClickListener {
                binding.dialogMarkShow.root.visibility = View.GONE
            }
            binding.dialogMarkShow.makeasdone.setOnClickListener {
                binding.dialogMarkShow.root.visibility = View.GONE
            }
        }
    }
}