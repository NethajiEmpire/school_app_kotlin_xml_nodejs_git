package com.lms.sch.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import com.lms.sch.adapter.MyTeachersAdapter
import com.lms.sch.databinding.ActivityMyTeacherBinding
import com.lms.sch.network.ApiConnection
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils

class MyTeacherActivity : BaseActivity() {
    lateinit var binding : ActivityMyTeacherBinding
    var key = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMyTeacherBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.backarrow.setOnClickListener {
            onBackPressed()
        }
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().getMyTeachers(this).observe(this){
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null){
                            if (it.result!!.classTeacher != null){
                                binding.name.text = it.result!!.classTeacher!!.firstName+" "+it.result!!.classTeacher!!.lastName
                                if (it.result!!.classTeacher!!.subjectName != null){
                                    binding.sub.text = "Class Incharge | ${it.result!!.classTeacher!!.subjectName}"
                                }
                                else {
                                    binding.sub.text = "Class Incharge"
                                }
                                binding.mobile.text = it.result!!.classTeacher!!.mobile
                                binding.email.text = it.result!!.classTeacher!!.email
                            }
                            else {
                                binding.name.text = "--/--"
                                binding.sub.text = "--/--"
                                binding.mobile.text = "--/--"
                                binding.email.text = "--/--"
                            }

                            if (it.result!!.subjectTeachers != null){
                                binding.noData.root.visibility = View.GONE
                                binding.teacherRecycler.visibility = View.VISIBLE
                                val adapter = MyTeachersAdapter(this,it.result!!.subjectTeachers!!)
                                val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
                                binding.teacherRecycler.layoutManager = layoutManager
                                binding.teacherRecycler.adapter = adapter
                            }
                            else {
                                binding.noData.root.visibility = View.VISIBLE
                                binding.teacherRecycler.visibility = View.GONE
                            }
                        }
                    }
                    else {
                        binding.noData.root.visibility = View.VISIBLE
                        binding.teacherRecycler.visibility = View.GONE
                        UiUtils.showSnack(it.msg,binding.root,false)
                    }
                }
            }
        }
    }
}