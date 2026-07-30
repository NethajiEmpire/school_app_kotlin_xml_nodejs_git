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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.adapter.AttendanceAdapter
import com.lms.sch.adapter.MySubjectsAdapter
import com.lms.sch.adapter.StudentProfileAdapter
import com.lms.sch.databinding.ActivityStudentAttendanceBinding
import com.lms.sch.databinding.FilterHomeworkBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.network.ApiConnection
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils

class StudentAttendanceActivity : BaseActivity() {
    private lateinit var binding : ActivityStudentAttendanceBinding
    var  programId = ""
    var status = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityStudentAttendanceBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.backarrow.setOnClickListener {
            onBackPressed()
        }
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().teacherProfile(this, this.sharedHelper.id).observe(this) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null && it.result!!.teacherPreference != null && it.result!!.teacherPreference!!.myStudentClass != null && it.result!!.teacherPreference!!.myStudentClass!!.studentClass != null && it.result!!.teacherPreference!!.myStudentClass!!.studentClass!!.name != null && it.result!!.teacherPreference!!.myStudentClass!!.section!!.name != null) {
                            programId = it.result!!.teacherPreference!!.myStudentClass!!._id!!
                            Log.d("jhjzgdfjds",programId)
                            getAttendance()
                        } else {
                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    } else {
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
        binding.filter.setOnClickListener {
            val inflater = LayoutInflater.from(this)
            val bind : FilterHomeworkBinding = FilterHomeworkBinding.inflate(inflater)
            val popupView : View = bind.root
            bind.today.text = "Present"
            bind.pending.text = "Absent"
            bind.completed.text = "Half Day"
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
            if (status == ""){
                UiUtils.textviewImgDrawable(bind.all,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.today,null,"start")
                UiUtils.textviewImgDrawable(bind.pending,null,"start")
                UiUtils.textviewImgDrawable(bind.completed,null,"start")
            }
            else if (status == "Present"){
                UiUtils.textviewImgDrawable(bind.today,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.all,null,"start")
                UiUtils.textviewImgDrawable(bind.pending,null,"start")
                UiUtils.textviewImgDrawable(bind.completed,null,"start")
            }
            else if (status == "Absent"){
                UiUtils.textviewImgDrawable(bind.pending,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.all,null,"start")
                UiUtils.textviewImgDrawable(bind.today,null,"start")
                UiUtils.textviewImgDrawable(bind.completed,null,"start")
            }
            else if (status == "halfDay"){
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
                getAttendance()
                popupWindow.dismiss()
            }
            bind.today.setOnClickListener {
                status = "present"
                getAttendance()
                popupWindow.dismiss()
            }
            bind.pending.setOnClickListener {
                status = "absent"
                getAttendance()
                popupWindow.dismiss()
            }
            bind.completed.setOnClickListener {
                status = "halfDay"
                getAttendance()
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
    fun getAttendance(){
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().studentAttendance(this,programId,status,"").observe(this){
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null && it.result!!.rows!!.isNotEmpty()){
                            if (it.result!!.stats != null){
                                binding.count1.text = it.result!!.stats!!.present!!
                                binding.count2.text = it.result!!.stats!!.absent!!
                                binding.count3.text = it.result!!.stats!!.total!!
                                binding.totalstudents.text = it.result!!.stats!!.total
                            }else{
                                binding.count1.text = "--/--"
                                binding.count2.text = "--/--"
                                binding.count3.text = "--/--"
                            }
                            val adapter = AttendanceAdapter(this, it.result!!.rows!!)
                            val layoutManager = LinearLayoutManager(this,  RecyclerView.VERTICAL, false)
                            binding.attendanceRecycler.layoutManager = layoutManager
                            binding.attendanceRecycler.adapter = adapter
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