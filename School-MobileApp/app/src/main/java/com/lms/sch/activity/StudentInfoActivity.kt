package com.lms.sch.activity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.lms.sch.R
import com.lms.sch.adapter.StudentProfileAdapter
import com.lms.sch.adapter.UserDetailsAdapter
import com.lms.sch.customviews.CurvedPieChartRenderer
import com.lms.sch.databinding.ActivityStudentInfoBinding
import com.lms.sch.databinding.FilterHomeworkBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.GetStudentAttenDanceResponse
import com.lms.sch.response.ProfileDetailsResponse
import com.lms.sch.response.StudentProfileResponse
import com.lms.sch.response.SubmissionProgressResponse
import com.lms.sch.session.Constants
import com.lms.sch.session.SharedHelper
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
import org.json.JSONArray
import org.json.JSONObject

class StudentInfoActivity : BaseActivity() {
    lateinit var binding : ActivityStudentInfoBinding
    private lateinit var pieChart: PieChart
    private lateinit var pieChart1 : PieChart
    var userId = ""
    var result = StudentProfileResponse.ApplicationForm()
    var resultacademic = StudentProfileResponse.Result()
    var resultAtt = GetStudentAttenDanceResponse.Result()
    var type = ""
    var datefilter = ""
    var isAttendanceFilter = false
    var attendanceFilters = ArrayList<String>()
    var clickedValue = ""
    var studentId = ""
    var gender = ""
    var programId = ""
    var pageName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStudentInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pieChart = binding.pieChart
        pieChart1 = binding.pieChart1
        binding.backarrow.setOnClickListener {
            onBackPressed()
        }
        userId = intent.getStringExtra(Constants.IntentKeys.KEY1)?: "--/--"
        pageName = intent.getStringExtra(Constants.IntentKeys.KEY)?: "--/--"
        if (pageName == "Student Info"){
            binding.page1.visibility = View.GONE
            binding.page2.visibility = View.VISIBLE
            userId = intent.getStringExtra(Constants.IntentKeys.KEY1)?: "--/--"
            getStudent()
        }
        Log.d("ghghgout",userId)
        binding.filter.setOnClickListener {
            val inflater = LayoutInflater.from(this)
            val bind : FilterHomeworkBinding = FilterHomeworkBinding.inflate(inflater)
            val popupView : View = bind.root
            bind.today.visibility = View.GONE
            bind.pending.text = "Female"
            bind.completed.text = "Male"
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
            if (gender == ""){
                UiUtils.textviewImgDrawable(bind.today,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.all,null,"start")
                UiUtils.textviewImgDrawable(bind.pending,null,"start")
                UiUtils.textviewImgDrawable(bind.completed,null,"start")
            }
            else if (gender == "female"){
                UiUtils.textviewImgDrawable(bind.pending,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.all,null,"start")
                UiUtils.textviewImgDrawable(bind.today,null,"start")
                UiUtils.textviewImgDrawable(bind.completed,null,"start")
            }
            else if (gender == "male"){
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
                gender = ""
                getStudentList()
                popupWindow.dismiss()
            }
            bind.pending.setOnClickListener {
                gender = "female"
                getStudentList()
                popupWindow.dismiss()
            }
            bind.completed.setOnClickListener {
                gender = "male"
                getStudentList()
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

        DialogUtils.showLoader(this)
        ApiConnection.getInstance().teacherProfile(this, SharedHelper(this).id).observe(this) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null && it.result!!.teacherPreference != null && it.result!!.teacherPreference!!.myStudentClass != null){
                             programId = it.result?.teacherPreference?.myStudentClass?._id ?: "--/--"
                             getStudentList()
                        } else {
//                            UiUtils.showSnack(it.msg,binding.root,false)
                        }
                    }else {
//                        UiUtils.showSnack(it.msg,binding.root,false)
                    }

                }
            }
        }
        binding.mainn1.setOnClickListener {
            gender = ""
            binding.filterLayCls.visibility = View.VISIBLE
            binding.studentListRecycler.visibility = View.VISIBLE
            getStudentList()
        }
        binding.mainn2.setOnClickListener {
            binding.filterLayCls.visibility = View.VISIBLE
            binding.studentListRecycler.visibility = View.VISIBLE
            gender = "male"
            getStudentList()
        }
        binding.mainn3.setOnClickListener {
            binding.filterLayCls.visibility = View.VISIBLE
            binding.studentListRecycler.visibility = View.VISIBLE
            gender = "female"
            getStudentList()
        }
        getStudentAttendance()
        binding.personalInfoLay.setOnClickListener {
            if(binding.personalInfoRecycler.visibility == View.GONE){
                val list = JSONArray()
                val studentInfo = result.studentInfo
                val json1 = JSONObject()
                json1.put("label", "Date of Birth")
                json1.put("type","text")
                json1.put("value", if (studentInfo != null && studentInfo.dob!!.isNotEmpty()) studentInfo.dob else "--/--")
                list.put(json1)

                val json2 = JSONObject()
                json2.put("label", "Gender")
                json2.put("type","text")
                json2.put("value", if (studentInfo != null && studentInfo.gender!!.isNotEmpty()) studentInfo.gender else "--/--")
                list.put(json2)

                val json3 = JSONObject()
                json3.put("label", "Blood Group")
                json3.put("type","text")
                json3.put("value", if (studentInfo != null && studentInfo.blood_group!!.isNotEmpty()) studentInfo.blood_group else "--/--")
                list.put(json3)

                val json4 = JSONObject()
                json4.put("label", "Address")
                json4.put("type","text")
                json4.put("value", if (studentInfo != null && studentInfo.address!!.isNotEmpty()) studentInfo.address else "--/--")
                list.put(json4)

                val json5 = JSONObject()
                json5.put("label", "State")
                json5.put("type","text")
                json5.put("value", if (studentInfo != null && studentInfo.state!!.isNotEmpty()) studentInfo.state else "--/--")
                list.put(json5)

                val json6 = JSONObject()
                json6.put("label", "City")
                json6.put("type","text")
                json6.put("value", if (studentInfo != null && studentInfo.city!!.isNotEmpty()) studentInfo.city else "--/--")
                list.put(json6)

                val json7 = JSONObject()
                json7.put("label", "Pincode")
                json7.put("type","text")
                json7.put("value", if (studentInfo != null && studentInfo.pincode!!.isNotEmpty()) studentInfo.pincode else "--/--")
                list.put(json7)

                val json8 = JSONObject()
                json8.put("label", "Country")
                json8.put("type","text")
                json8.put("value", if (studentInfo != null && studentInfo.country!!.isNotEmpty()) studentInfo.country else "--/--")
                list.put(json8)

                val json9 = JSONObject()
                json9.put("label", "Aadhar Number")
                json9.put("type","text")
                json9.put("value", if (studentInfo != null && studentInfo.aadhar_number!!.isNotEmpty()) studentInfo.aadhar_number else "--/--")
                list.put(json9)

                val json10 = JSONObject()
                json10.put("label", "Nationality")
                json10.put("type","text")
                json10.put("value", if (studentInfo != null && studentInfo.nationality!!.isNotEmpty()) studentInfo.nationality else "--/--")
                list.put(json10)

                val json11 = JSONObject()
                json11.put("label", "Religion")
                json11.put("type","text")
                json11.put("value", if (studentInfo != null && studentInfo.religion!!.isNotEmpty()) studentInfo.religion else "--/--")
                list.put(json11)

                val json12 = JSONObject()
                json12.put("label", "Category")
                json12.put("type","text")
                json12.put("value", if (studentInfo != null && studentInfo.category!!.isNotEmpty()) studentInfo.category else "--/--")
                list.put(json12)

                loadRecycler("text",list,binding.personalInfoRecycler)
                binding.personalInfoRecycler.visibility = View.VISIBLE
                binding.parentInfoRecycler.visibility = View.GONE
                binding.academicInfoRecycler.visibility = View.GONE
                binding.documentInfoRecycler.visibility = View.GONE
                UiUtils.relativeLayoutBgDrawable(binding.personalInformation,R.drawable.border_curve_top_10dp)
                UiUtils.relativeLayoutBgTint(binding.personalInformation,"#DAEFFF",null)
                binding.down1.rotation = 180F
            }
            else{
                binding.personalInfoRecycler.visibility = View.GONE
                binding.parentInfoRecycler.visibility = View.GONE
                binding.academicInfoRecycler.visibility = View.GONE
                binding.documentInfoRecycler.visibility = View.GONE
                UiUtils.relativeLayoutBgDrawable(binding.personalInformation,R.drawable.border_curve_6dp)
                binding.down1.rotation = 0F
            }
        }
        binding.parentInfoLay.setOnClickListener {
            if(binding.parentInfoRecycler.visibility == View.GONE){
                val list = JSONArray()
                val parentInfo = result.parentInfo
                val json1 = JSONObject()
                json1.put("label", "Father's Name")
                json1.put("type","text")
                json1.put("value", if (parentInfo != null && parentInfo.fatherName!!.isNotEmpty()) parentInfo.fatherName else "--/--")
                list.put(json1)

                val json2 = JSONObject()
                json2.put("label", "Mother's Name")
                json2.put("type","text")
                json2.put("value", if (parentInfo != null && parentInfo.motherName!!.isNotEmpty()) parentInfo.motherName else "--/--")
                list.put(json2)

                val json3 = JSONObject()
                json3.put("label", "Guardian's Name")
                json3.put("type","text")
                json3.put("value", if (parentInfo != null && parentInfo.guardianName!!.isNotEmpty()) parentInfo.guardianName else "--/--")
                list.put(json3)

                val json4 = JSONObject()
                json4.put("label", "Father’s Occupation")
                json4.put("type","text")
                json4.put("value", if (parentInfo != null && parentInfo.fatherOccupation!!.isNotEmpty()) parentInfo.fatherOccupation else "--/--")
                list.put(json4)

                val json5 = JSONObject()
                json5.put("label", "Mother’s Occupation")
                json5.put("type","text")
                json5.put("value", if (parentInfo != null && parentInfo.motherOccupation!!.isNotEmpty()) parentInfo.motherOccupation else "--/--")
                list.put(json5)

                val json6 = JSONObject()
                json6.put("label", "Parent’s Contact Number")
                json6.put("type","text")
                json6.put("value", if (parentInfo != null && parentInfo.parentsMobile!!.isNotEmpty()) parentInfo.parentsMobile else "--/--")
                list.put(json6)

                val json7 = JSONObject()
                json7.put("label", "Emergency Contact Number")
                json7.put("type","text")
                json7.put("value", if (parentInfo != null && parentInfo.emergencyMobile!!.isNotEmpty()) parentInfo.emergencyMobile else "--/--")
                list.put(json7)

                val json8 = JSONObject()
                json8.put("label", "Parent’s Email Address")
                json8.put("type","text")
                json8.put("value", if (parentInfo != null && parentInfo.parentsEmail!!.isNotEmpty()) parentInfo.parentsEmail else "--/--")
                list.put(json8)

                val json9 = JSONObject()
                json9.put("label", "Parent’s Address")
                json9.put("type","text")
                json9.put("value", if (parentInfo != null && parentInfo.parentsAddress!!.isNotEmpty()) parentInfo.parentsAddress else "--/--")
                list.put(json9)

                loadRecycler("text",list,binding.parentInfoRecycler)
                binding.parentInfoRecycler.visibility = View.VISIBLE
                binding.personalInfoRecycler.visibility = View.GONE
                binding.academicInfoRecycler.visibility = View.GONE
                binding.documentInfoRecycler.visibility = View.GONE
                UiUtils.relativeLayoutBgDrawable(binding.parentInformation,R.drawable.border_curve_top_10dp)
                UiUtils.relativeLayoutBgTint(binding.parentInformation,"#DAEFFF",null)
                binding.down2.rotation = 180F
            }
            else{
                binding.parentInfoRecycler.visibility = View.GONE
                binding.personalInfoRecycler.visibility = View.GONE
                binding.academicInfoRecycler.visibility = View.GONE
                binding.documentInfoRecycler.visibility = View.GONE
                UiUtils.relativeLayoutBgDrawable(binding.parentInformation,R.drawable.border_curve_6dp)
                binding.down2.rotation = 0F
            }
        }
        binding.academicInfoLay.setOnClickListener {
            if(binding.academicInfoRecycler.visibility == View.GONE){
                val list = JSONArray()
                val academicInfo = result.academicInfo
                val json1 = JSONObject()
                json1.put("label", "Previous School Name")
                json1.put("type","text")
                json1.put("value", if (academicInfo != null && academicInfo.previousSchoolName!!.isNotEmpty()) academicInfo.previousSchoolName else "--/--")
                list.put(json1)

//                val json2 = JSONObject()
//                json2.put("label", "Class Applying For")
//                json2.put("type","text")
//                json2.put("value", if (academicInfo != null && academicInfo.classApplying != null && academicInfo.classApplying!!.name!!.isNotEmpty()) academicInfo.classApplying!!.name!! else "--/--")
//                list.put(json2)

//                val json3 = JSONObject()
//                json3.put("label", "Board of Education")
//                json3.put("type","text")
//                json3.put("value", if (academicInfo != null && academicInfo.boardOfEducation != null && academicInfo.boardOfEducation!!.name!!.isNotEmpty()) academicInfo.boardOfEducation!!.name else "--/--")
//                list.put(json3)

                loadRecycler("text",list,binding.academicInfoRecycler)
                binding.academicInfoRecycler.visibility = View.VISIBLE
                binding.personalInfoRecycler.visibility = View.GONE
                binding.parentInfoRecycler.visibility = View.GONE
                binding.documentInfoRecycler.visibility = View.GONE
                UiUtils.relativeLayoutBgDrawable(binding.academicInformation,R.drawable.border_curve_top_10dp)
                UiUtils.relativeLayoutBgTint(binding.academicInformation,"#DAEFFF",null)
                binding.down3.rotation = 180F
            }
            else{
                binding.academicInfoRecycler.visibility = View.GONE
                binding.personalInfoRecycler.visibility = View.GONE
                binding.parentInfoRecycler.visibility = View.GONE
                binding.documentInfoRecycler.visibility = View.GONE
                UiUtils.relativeLayoutBgDrawable(binding.academicInformation,R.drawable.border_curve_6dp)
                binding.down3.rotation = 0F
            }
        }
        binding.documentInfoLay.setOnClickListener {
            if(binding.documentInfoRecycler.visibility == View.GONE){
                val list = JSONArray()
                val documentInfo = result.documentInfo
                val json1 = JSONObject()
                json1.put("label", "Birth Certificate")
                json1.put("type","document")
                json1.put("value", if (documentInfo != null && documentInfo.birthCertificate != null && documentInfo.birthCertificate!!.url!!.isNotEmpty()) documentInfo.birthCertificate!!.url!! else "--/--")
                list.put(json1)

                val json2 = JSONObject()
                json2.put("label", "Aadhaar Card")
                json2.put("type","document")
                json2.put("value", if (documentInfo != null && documentInfo.aadharCard != null && documentInfo.aadharCard!!.url!!.isNotEmpty()) documentInfo.aadharCard!!.url!! else "--/--")
                list.put(json2)

                val json3 = JSONObject()
                json3.put("label", "Student Photo")
                json3.put("type","document")
                json3.put("value", if (documentInfo != null && documentInfo.studentPhoto != null && documentInfo.studentPhoto!!.url!!.isNotEmpty()) documentInfo.studentPhoto!!.url!! else "--/--")
                list.put(json3)

                val json4 = JSONObject()
                json4.put("label", "Previous School Marksheet")
                json4.put("type","document")
                json4.put("value", if (documentInfo != null && documentInfo.previousSchoolMarksheet != null && documentInfo.previousSchoolMarksheet!!.url!!.isNotEmpty()) documentInfo.previousSchoolMarksheet!!.url!! else "--/--")
                list.put(json4)

                val json5 = JSONObject()
                json5.put("label", "Transfer Certificate")
                json5.put("type","document")
                json5.put("value", if (documentInfo != null && documentInfo.transferCertificate != null && documentInfo.transferCertificate!!.url!!.isNotEmpty()) documentInfo.transferCertificate!!.url!! else "--/--")
                list.put(json5)

                val json6 = JSONObject()
                json6.put("label", "Parent’s ID Proof")
                json6.put("type","document")
                json6.put("value", if (documentInfo != null && documentInfo.parentIdProof != null && documentInfo.parentIdProof!!.url!!.isNotEmpty()) documentInfo.parentIdProof!!.url!! else "--/--")
                list.put(json6)

                val json7 = JSONObject()
                json7.put("label", "Address Proof")
                json7.put("type","document")
                json7.put("value", if (documentInfo != null && documentInfo.addressProof != null && documentInfo.addressProof!!.url!!.isNotEmpty()) documentInfo.addressProof!!.url!! else "--/--")
                list.put(json7)

                loadRecycler("document",list,binding.documentInfoRecycler)
                binding.documentInfoRecycler.visibility = View.VISIBLE
                binding.personalInfoRecycler.visibility = View.GONE
                binding.parentInfoRecycler.visibility = View.GONE
                binding.academicInfoRecycler.visibility = View.GONE
                UiUtils.relativeLayoutBgDrawable(binding.documentInformation,R.drawable.border_curve_top_10dp)
                UiUtils.relativeLayoutBgTint(binding.documentInformation,"#DAEFFF",null)
                binding.down2.rotation = 180F
            }
            else{
                binding.documentInfoRecycler.visibility = View.GONE
                binding.personalInfoRecycler.visibility = View.GONE
                binding.parentInfoRecycler.visibility = View.GONE
                binding.academicInfoRecycler.visibility = View.GONE
                UiUtils.relativeLayoutBgDrawable(binding.documentInformation,R.drawable.border_curve_6dp)
                binding.down2.rotation = 0F
            }
        }

        binding.attendanceTap.setOnClickListener {
            UiUtils.textviewCustomDrawable( binding.attendanceTap,  R.drawable.border_line_curve_24dp_primary )
            UiUtils.textviewCustomDrawable( binding.feeInsightTap,   R.drawable.border_line_curve_24dp_grey)
            UiUtils.textviewCustomDrawable(  binding.homeworkTap,   R.drawable.border_line_curve_24dp_grey)
            UiUtils.textviewCustomDrawable( binding.assignmentTap, R.drawable.border_line_curve_24dp_grey  )
            UiUtils.textviewCustomDrawable( binding.projectTap,  R.drawable.border_line_curve_24dp_grey )
            UiUtils.textViewTextColor(binding.attendanceTap, null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.feeInsightTap, null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.homeworkTap, null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.assignmentTap, null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.projectTap, null, R.color.black_varient6)
            binding.subOverView.visibility = View.GONE
            binding.attendanceProgress.visibility = View.VISIBLE
            binding.noData.root.visibility = View.GONE
            getStudentAttendance()
        }
        binding.feeInsightTap.setOnClickListener {
            UiUtils.textviewCustomDrawable(  binding.feeInsightTap,   R.drawable.border_line_curve_24dp_primary )
            UiUtils.textviewCustomDrawable(  binding.attendanceTap,  R.drawable.border_line_curve_24dp_grey )
            UiUtils.textviewCustomDrawable( binding.homeworkTap, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textviewCustomDrawable( binding.assignmentTap, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textviewCustomDrawable(  binding.projectTap, R.drawable.border_line_curve_24dp_grey )
            UiUtils.textViewTextColor(binding.attendanceTap, null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.feeInsightTap, null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.homeworkTap, null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.assignmentTap, null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.projectTap, null, R.color.black_varient6)
            binding.subOverView.visibility = View.GONE
            binding.attendanceProgress.visibility = View.GONE
            binding.noData.root.visibility = View.VISIBLE
        }
        binding.homeworkTap.setOnClickListener {
            UiUtils.textviewCustomDrawable( binding.homeworkTap,  R.drawable.border_line_curve_24dp_primary )
            UiUtils.textviewCustomDrawable(  binding.attendanceTap,  R.drawable.border_line_curve_24dp_grey)
            UiUtils.textviewCustomDrawable(  binding.feeInsightTap, R.drawable.border_line_curve_24dp_grey )
            UiUtils.textviewCustomDrawable(   binding.assignmentTap, R.drawable.border_line_curve_24dp_grey )
            UiUtils.textviewCustomDrawable(   binding.projectTap, R.drawable.border_line_curve_24dp_grey )
            UiUtils.textViewTextColor(binding.attendanceTap, null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.feeInsightTap, null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.homeworkTap, null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.assignmentTap, null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.projectTap, null, R.color.black_varient6)
            binding.subOverView.visibility = View.VISIBLE
            binding.attendanceProgress.visibility = View.GONE
            binding.noData.root.visibility = View.GONE
            type = "homework"
            submissionProgress()
        }
        binding.assignmentTap.setOnClickListener {
            UiUtils.textviewCustomDrawable(  binding.assignmentTap,   R.drawable.border_line_curve_24dp_primary )
            UiUtils.textviewCustomDrawable( binding.attendanceTap,  R.drawable.border_line_curve_24dp_grey )
            UiUtils.textviewCustomDrawable(binding.feeInsightTap,  R.drawable.border_line_curve_24dp_grey )
            UiUtils.textviewCustomDrawable( binding.homeworkTap, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textviewCustomDrawable(  binding.projectTap, R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.attendanceTap, null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.feeInsightTap, null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.homeworkTap, null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.assignmentTap, null, R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.projectTap, null, R.color.black_varient6)
            binding.subOverView.visibility = View.VISIBLE
            binding.attendanceProgress.visibility = View.GONE
            binding.noData.root.visibility = View.GONE
            type = "assignment"
            submissionProgress()
        }
        binding.projectTap.setOnClickListener {
            UiUtils.textviewCustomDrawable(  binding.projectTap,  R.drawable.border_line_curve_24dp_primary)
            UiUtils.textviewCustomDrawable(  binding.attendanceTap,  R.drawable.border_line_curve_24dp_grey)
            UiUtils.textviewCustomDrawable( binding.feeInsightTap,R.drawable.border_line_curve_24dp_grey )
            UiUtils.textviewCustomDrawable(binding.homeworkTap, R.drawable.border_line_curve_24dp_grey )
            UiUtils.textviewCustomDrawable( binding.assignmentTap, R.drawable.border_line_curve_24dp_grey )
            UiUtils.textViewTextColor(binding.attendanceTap, null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.feeInsightTap, null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.homeworkTap, null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.assignmentTap, null, R.color.black_varient6)
            UiUtils.textViewTextColor(binding.projectTap, null, R.color.colorPrimary)
            binding.subOverView.visibility = View.VISIBLE
            binding.attendanceProgress.visibility = View.GONE
            binding.noData.root.visibility = View.GONE
            type = "project"
            submissionProgress()
        }
        binding.attendanceTap.performClick()
    }
    fun loadRecycler(type: String, cList: JSONArray, recyclerView: RecyclerView){
        if (type == "text"){
            var mList = cList
            val linearLayoutManager = GridLayoutManager(this, 2,RecyclerView.VERTICAL, false)
            val adapter = UserDetailsAdapter(this,mList)
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.adapter = adapter
        }
        else {
            var mList = cList
            val linearLayoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL, false)
            val adapter = UserDetailsAdapter(this,mList)
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.adapter = adapter
        }
    }
    private fun getStudentAttendance() {
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().studentSingleViewAtt(this, userId).observe(this) {
            DialogUtils.dismissLoader()
            it?.let {
                it.success.let { success ->
                    if (success) {
                        resultAtt = it.result!!
                        if (it.result != null) {
                            loadProgressBar(resultAtt)
                            if (resultAtt!!.progress!!.total != null){
                                binding.totalWorking.text = "Total Working Days : ${resultAtt!!.progress!!.total}"
                            }
                            else{
                                binding.totalWorking.text = "--/--"
                            }
                            if (resultAtt.progress!!.presentCount != null) {
                                binding.presentdays.text = resultAtt!!.progress!!.presentCount.toString()
                            } else {
                                binding.presentdays.text = "--/--"
                            }
                            if (resultAtt.progress!!.absentCount != null) {
                                binding.absentDays.text = resultAtt.progress!!.absentCount.toString()
                            } else {
                                binding.absentDays.text = "--/--"
                            }
                            if (resultAtt.progress!!.presentPercentage != null) {
                                binding.percent.text =
                                    "Overall ${resultAtt.progress!!.presentPercentage} %"
                            } else {
                                binding.percent.text = "--/--"
                            }
//                            if (resultAtt.progress!!.total != null){
//                                binding.totalWarkingDays.text = "Total Working Days : ${resultAtt.progress!!.total}"
//                            }
//                            else{
//                                binding.totalWarkingDays.text = "--/--"
//                            }
                        } else {
                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    } else {
                        UiUtils.showSnack(it.msg, binding.root, false)

                    }
                }
            }
        }
    }
    fun loadProgressBar(result: GetStudentAttenDanceResponse.Result) {
        val total = result.progress!!.total?.toFloat()?.coerceAtLeast(1f) ?: 1f
        val presentCount = result.progress!!.presentPercentage?.toFloat() ?: 0f
        val absentCount = result.progress!!.absentPercentage?.toFloat() ?: 0f
//            val halfDayCount = result.count!!.halfDay?.toFloat() ?: 0f

        val isAllZero = presentCount == 0f && absentCount == 0f

        val entries = if (isAllZero) {
            arrayListOf(
                PieEntry(100f, "No Data")
            )
        } else {
            arrayListOf(
                PieEntry((presentCount / total) * 100f, "Present"),  // Green
                PieEntry((absentCount / total) * 100f, "Absent"),   // Red
//                    PieEntry((halfDayCount / total) * 100f, "Half Day") // Blue
            )
        }

        val colors = if (isAllZero) {
            listOf(Color.parseColor("#f5f5f5")) // Grey for no data
        } else {
            listOf(
                Color.parseColor("#32B138"),  // Green for Present
                Color.parseColor("#FF7475")  // Red for Absent
//                    Color.parseColor("#1170E4")   // Blue for Half Day
            )
        }

        val dataSet = PieDataSet(entries, "").apply {
            setColors(colors)
            sliceSpace = 8f
            selectionShift = 5f
            setDrawValues(false)
        }

        val pieData = PieData(dataSet)

        pieChart.apply {
            data = pieData
            description.isEnabled = false
            isRotationEnabled = false
            setDrawEntryLabels(false)
            setDrawHoleEnabled(true)
            holeRadius = 60f
            transparentCircleRadius = 0f
            setTouchEnabled(false)
            legend.isEnabled = false
            setHoleColor(Color.TRANSPARENT)
            renderer = CurvedPieChartRenderer(this, animator, viewPortHandler)

            // Animate the chart
            animateY(1000, Easing.EaseInOutCubic)

            // Refresh the chart
            invalidate()
        }
    }
    private fun submissionProgress(){
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().singlestudentSubmission(this,type,userId).observe(this) {
            DialogUtils.dismissLoader()
            it?.let {
                it.success.let { success ->
                    if (success) {
                        val result = it.result!!
                        if (result != null ) {
                            setupPieChart(result)
                            if (result.total != null){
                                binding.total.text = "Total Submission : ${result.total}"
                            }
                            else{
                                binding.total.text = "--/--"
                            }
                            if (result.points != null){
                                binding.points.text = "Total Points : ${result.points}"
                            }
                            else{
                                binding.points.text = "--/--"
                            }
                            if (result.percentage != null && result.percentage!!.onTime != null){
                                binding.subPercent.text = "On-time \n ${result.percentage!!.onTime}%"
                            }
                            else{
                                binding.subPercent.text = "--/--"
                            }
                            if (result.missed != null){
                                binding.missed.text = "Missed : ${result.missed}"
                            }
                            else{
                                binding.missed.text = "--/--"
                            }
                            if (result.onTime != null){
                                binding.ontime.text = "Ontime : ${result.onTime}"
                            }
                            else{
                                binding.ontime.text = "--/--"
                            }
                            if (result.late != null){
                                binding.late.text = "Late : ${result.late}"
                            }
                            else{
                                binding.late.text = "--/--"
                            }

                        } else {
                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    } else {
                        UiUtils.showSnack(it.msg, binding.root, false)

                    }
                }
            }
        }
    }
    private fun setupPieChart(result: SubmissionProgressResponse.Result) {
        val total = result.total?.toFloat()?.coerceAtLeast(1f) ?: 1f
        val ontime = result.percentage?.onTime?.toFloat() ?: 0f
        val missed = result.percentage?.missed?.toFloat() ?: 0f
        val late = result.percentage?.late?.toFloat() ?: 0f

//        val entries: ArrayList<PieEntry>
        val isAllZero = ontime == 0f && missed == 0f && late == 0f

        val entries = if (isAllZero) {
            arrayListOf(
                PieEntry(100f, "No Data")
            )
        }
        else {
            arrayListOf(
                PieEntry((ontime / total) * 100f, "Present"),  // Green
                PieEntry((missed / total) * 100f, "Absent"),
                PieEntry((late / total) * 100f, "Late"),

            )
        }
        val colors = if (isAllZero) {
            listOf(Color.parseColor("#f5f5f5")) // Grey for no data
        } else {
            listOf(
                Color.parseColor("#32B138"), // green
                Color.parseColor("#FF7475"), // blue
                Color.parseColor("#FF9900")
            )
        }
            val dataSet = PieDataSet(entries, "").apply {
                setColors(colors)
                sliceSpace = 8f
                setDrawValues(false)
            }
            val pieData = PieData(dataSet)
            binding.pieChart1.apply {
                data = pieData
                description.isEnabled = false
                isRotationEnabled = false
                setDrawEntryLabels(false)
                setDrawHoleEnabled(true)
                holeRadius = 60f
                transparentCircleRadius = 0f
                setTouchEnabled(false)
                legend.isEnabled = false
                setHoleColor(Color.TRANSPARENT)
                renderer = CurvedPieChartRenderer(this, animator, viewPortHandler)
                animateY(1000, Easing.EaseInOutCubic)
                invalidate()
            }
        }
    private fun getStudentList(){
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().getStudentList(this,"",programId,gender).observe(this){
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success){
                        if (it.result != null && it.result!!.rows!!.isNotEmpty()){
                            val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
                            val adapter = StudentProfileAdapter(this, it.result!!.rows!!,object :  OnClickListener {
                                override fun onClickItem(pos: Int) {
                                    userId = it.result!!.rows!![pos].student!!._id!!
                                    Log.d("zgdhgsdhs",userId)
                                    getStudent()
                                    binding.page1.visibility = View.GONE
                                    binding.page2.visibility = View.VISIBLE
                                }
                            })
                            binding.studentListRecycler.layoutManager= layoutManager
                            binding.studentListRecycler.adapter = adapter
                        }
                        else{
//                            binding.noData1.root.visibility = View.VISIBLE
                            binding.studentListRecycler.visibility = View.GONE
                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    }
                    else{
//                        binding.noData1.root.visibility = View.VISIBLE
                        binding.studentListRecycler.visibility = View.GONE
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
    }

    private fun getStudent(){
        Log.d("ghghgout",userId)
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().studentprofile(this,userId).observe(this) {
            Log.d("ghghgout",userId)
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null){
                            result = it.result!!.applicationForm!!
                            resultacademic = it.result!!
                            if (it.result!!.student != null){
                                binding.name.text = it.result!!.student!!.firstName+" "+it.result!!.student!!.lastName
                                binding.email.text = it.result!!.student!!.email
                                binding.mobile.text = "+91 ${it.result!!.student!!.mobile}"
                                binding.stdId.text = it.result!!.student!!.lead_id!!
                                UiUtils.loadImage(binding.profileimg,it.result!!.student!!.imgUrl!!)
                            }
                            binding.personalInfoLay.performClick()
                            binding.attendanceTap.performClick()
                        }
                        else {
                            UiUtils.showSnack(it.msg, binding.root,false)
                        }
                    }
                    else {
                        UiUtils.showSnack(it.msg, binding.root,false)
                    }
                }
            }
        }
    }
    }