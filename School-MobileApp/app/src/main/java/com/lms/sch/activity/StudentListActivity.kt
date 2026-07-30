package com.lms.sch.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import com.lms.sch.R
import com.lms.sch.adapter.SubjectStudentsAdapter
import com.lms.sch.adapter.SubjectStudentsOverviewAdapter
import com.lms.sch.databinding.ActivityStudentListBinding
import com.lms.sch.network.ApiConnection
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils

class StudentListActivity : BaseActivity() {
    lateinit var binding: ActivityStudentListBinding
    var programId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStudentListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        programId = intent.getStringExtra(Constants.IntentKeys.KEY).toString()
        Log.d("hgdfhsgdf",programId)
        if (programId != null){
            getStudentsList()
        }
        else {
            binding.recycler.visibility = View.GONE
            binding.noData.root.visibility = View.VISIBLE
        }
        var isOpened = false
        binding.click.setOnClickListener {
            isOpened = !isOpened
            if (isOpened) {
                binding.downLay.visibility = View.VISIBLE
                binding.click.rotation = 180f
            }
            else {
                binding.downLay.visibility = View.GONE
                binding.click.rotation = 0f
            }
        }

        binding.back.setOnClickListener {
            finish()
        }

    }
    fun getStudentsList(){
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().studentListAnalytics(this,programId).observe(this) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null && it.result!!.rows!!.isNotEmpty()){
                            binding.noData.root.visibility = View.GONE
                            binding.recycler.visibility = View.VISIBLE
                            val adapter1 = SubjectStudentsOverviewAdapter(this,true,it.result!!.rows!!)
                            val layoutManager1 = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                            binding.recycler.layoutManager = layoutManager1
                            binding.recycler.adapter = adapter1
                        }
                        else {
                            binding.noData.root.visibility = View.VISIBLE
                            binding.recycler.visibility = View.GONE
                        }
                    }
                    else {
                        binding.noData.root.visibility = View.VISIBLE
                        binding.recycler.visibility = View.GONE
                        UiUtils.showSnack(it.msg,binding.root,false)
                    }
                }
            }
        }
    }
}