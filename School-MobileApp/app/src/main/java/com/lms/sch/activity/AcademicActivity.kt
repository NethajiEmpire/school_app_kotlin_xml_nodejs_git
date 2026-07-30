package com.lms.sch.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.adapter.GetAcademicBatchAdapter
import com.lms.sch.adapter.GetAcademicBoardAdapter
import com.lms.sch.adapter.GetAcademicSubjectAdapter
import com.lms.sch.databinding.ActivityAdminAcademicBinding
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.GetAcademicStatsResponse
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils

class AcademicActivity : BaseActivity() {
    lateinit var binding: ActivityAdminAcademicBinding
    var academicStatsRes : GetAcademicStatsResponse.Result? = null
    var search = ""
    var currentTab = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAdminAcademicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getAcademicStats()
        binding.backarrow.setOnClickListener {
            onBackPressed()
        }
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                search = p0.toString()
                when(currentTab) {
                    "batch" -> getBatch(search)
                    "board" ->  getBoard(search)
                    "standard" ->  getStandard(search)
                    "section" ->  getSection(search)
                    "subject" -> getSubject(search)
                    else ->
                        UiUtils.log("hgbnv", currentTab)
                }
                UiUtils.log("hgbnv", currentTab)
            }
        })

        binding.batchTab.setOnClickListener{
            binding.topTitle.text = "Batch List"
            UiUtils.constraintLayoutBgDrawable(binding.batchTab, R.drawable.border_line_curve_8dp_light_blues)
            UiUtils.constraintLayoutBgDrawable(binding.boardTab, R.drawable.border_line_curve_8dp_grey)
            UiUtils.constraintLayoutBgDrawable(binding.standTab, R.drawable.border_line_curve_8dp_grey)
            UiUtils.constraintLayoutBgDrawable(binding.sectionTab, R.drawable.border_line_curve_8dp_grey)
            UiUtils.constraintLayoutBgDrawable(binding.subjectTab, R.drawable.border_line_curve_8dp_grey)
            currentTab = "batch"
            search = ""
            binding.search.setText("")
            getBatch(search)
        }
        binding.boardTab.setOnClickListener{
            binding.topTitle.text = "Board List"
            UiUtils.constraintLayoutBgDrawable(binding.boardTab, R.drawable.border_line_curve_8dp_red)
            UiUtils.constraintLayoutBgDrawable(binding.batchTab, R.drawable.border_line_curve_8dp_grey)
            UiUtils.constraintLayoutBgDrawable(binding.standTab, R.drawable.border_line_curve_8dp_grey)
            UiUtils.constraintLayoutBgDrawable(binding.sectionTab, R.drawable.border_line_curve_8dp_grey)
            UiUtils.constraintLayoutBgDrawable(binding.subjectTab, R.drawable.border_line_curve_8dp_grey)
            currentTab = "board"
            search = ""
            binding.search.setText("")
            getBoard(search)
        }
        binding.standTab.setOnClickListener{
            binding.topTitle.text = "Standard List"
            UiUtils.constraintLayoutBgDrawable(binding.standTab, R.drawable.border_line_curve_8dp_green)
            UiUtils.constraintLayoutBgDrawable(binding.boardTab, R.drawable.border_line_curve_8dp_grey)
            UiUtils.constraintLayoutBgDrawable(binding.batchTab, R.drawable.border_line_curve_8dp_grey)
            UiUtils.constraintLayoutBgDrawable(binding.sectionTab, R.drawable.border_line_curve_8dp_grey)
            UiUtils.constraintLayoutBgDrawable(binding.subjectTab, R.drawable.border_line_curve_8dp_grey)
            currentTab = "standard"
            search = ""
            binding.search.setText("")
            getStandard(search)
        }
        binding.sectionTab.setOnClickListener{
            binding.topTitle.text = "Section List"
            UiUtils.constraintLayoutBgDrawable(binding.sectionTab, R.drawable.border_line_curve_8dp_orange)
            UiUtils.constraintLayoutBgDrawable(binding.batchTab, R.drawable.border_line_curve_8dp_grey)
            UiUtils.constraintLayoutBgDrawable(binding.boardTab, R.drawable.border_line_curve_8dp_grey)
            UiUtils.constraintLayoutBgDrawable(binding.standTab, R.drawable.border_line_curve_8dp_grey)
            UiUtils.constraintLayoutBgDrawable(binding.subjectTab, R.drawable.border_line_curve_8dp_grey)
            currentTab = "section"
            search = ""
            binding.search.setText("")
            getSection(search)
        }
        binding.subjectTab.setOnClickListener{
            binding.topTitle.text = "Subject List"
            UiUtils.constraintLayoutBgDrawable(binding.subjectTab, R.drawable.border_line_curve_8dp_voilet)
            UiUtils.constraintLayoutBgDrawable(binding.batchTab, R.drawable.border_line_curve_8dp_grey)
            UiUtils.constraintLayoutBgDrawable(binding.boardTab, R.drawable.border_line_curve_8dp_grey)
            UiUtils.constraintLayoutBgDrawable(binding.sectionTab, R.drawable.border_line_curve_8dp_grey)
            UiUtils.constraintLayoutBgDrawable(binding.standTab, R.drawable.border_line_curve_8dp_grey)
            currentTab = "subject"
            search = ""
            binding.search.setText("")
            getSubject(search)
        }
        binding.batchTab.performClick()
    }

    private fun getAcademicStats(){
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().getAcademicStats(this).observe(this){
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if(success){
                        if (it.result != null){
                            academicStatsRes = it.result!!

                            if (academicStatsRes!!.batch != null){
                                binding.btNum.text = it.result!!.batch.toString()
                            }
                            else{
                                binding.btNum.text = "--/--"
                            }
                            if (academicStatsRes!!.board != null){
                                binding.boardNum.text = it.result!!.board.toString()
                            }
                            else{
                                binding.boardNum.text = "--/--"
                            }
                            if (academicStatsRes!!.standard != null){
                                binding.stdNum.text = it.result!!.standard.toString()
                            }
                            else{
                                binding.stdNum.text = "--/--"
                            }
                            if (academicStatsRes!!.section != null){
                                binding.secNum.text = it.result!!.section.toString()
                            }
                            else{
                                binding.secNum.text = "--/--"
                            }
                            if (academicStatsRes!!.subject != null){
                                binding.subNum.text = it.result!!.subject.toString()
                            }
                            else{
                                binding.subNum.text = "--/--"
                            }

                        }
                        else{
                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    }
                    else{
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
    }

    private fun getBatch(search: String){
        if (search.isEmpty()) {
            DialogUtils.showLoader(this)
        }
        ApiConnection.getInstance().getAcademicBatch(this, search).observe(this) {
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null) {
                            if (it.result != null && it.result!!.rows!!.isNotEmpty()) {
                                val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
                                val adapter = GetAcademicBatchAdapter(this, it.result!!.rows!!)
                                binding.academicRecyclerView.layoutManager = layoutManager
                                binding.academicRecyclerView.adapter = adapter
                                binding.noData.root.visibility = View.GONE
                                binding.academicRecyclerView.visibility = View.VISIBLE
                            } else {
                                binding.noData.root.visibility = View.VISIBLE
                                binding.academicRecyclerView.visibility = View.GONE
                            }
                        } else {
                           // UiUtils.showSnack(it.msg, binding.root, false)
                            binding.noData.root.visibility = View.VISIBLE
                            binding.academicRecyclerView.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun getBoard(search: String){
        if (search.isEmpty()) {
            DialogUtils.showLoader(this)
        }
        ApiConnection.getInstance().getAcademicBoard(this, search).observe(this) {
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null) {
                            if (it.result != null && it.result!!.isNotEmpty()) {
                                val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
                                val adapter = GetAcademicBoardAdapter(this, it.result!!, ArrayList(), ArrayList(), 0 )
                                binding.academicRecyclerView.layoutManager = layoutManager
                                binding.academicRecyclerView.adapter = adapter


                                binding.noData.root.visibility = View.GONE
                                binding.academicRecyclerView.visibility = View.VISIBLE
                            } else {
                                binding.noData.root.visibility = View.VISIBLE
                                binding.academicRecyclerView.visibility = View.GONE
                            }
                        } else {
                           // UiUtils.showSnack(it.msg, binding.root, false)
                            binding.noData.root.visibility = View.VISIBLE
                            binding.academicRecyclerView.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun getStandard(search: String){
        if (search.isEmpty()) {
            DialogUtils.showLoader(this)
        }
        ApiConnection.getInstance().getAcademicStandard(this, search).observe(this) {
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null) {
                            if (it.result != null && it.result!!.isNotEmpty()) {
                                val layoutManager = GridLayoutManager(this,2, RecyclerView.VERTICAL, false)
                                val adapter = GetAcademicBoardAdapter(this, ArrayList(), it.result!!, ArrayList(), 1 )
                                binding.academicRecyclerView.layoutManager = layoutManager
                                binding.academicRecyclerView.adapter = adapter


                                binding.noData.root.visibility = View.GONE
                                binding.academicRecyclerView.visibility = View.VISIBLE
                            } else {
                                binding.noData.root.visibility = View.VISIBLE
                                binding.academicRecyclerView.visibility = View.GONE
                            }
                        } else {
                           // UiUtils.showSnack(it.msg, binding.root, false)
                            binding.noData.root.visibility = View.VISIBLE
                            binding.academicRecyclerView.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun getSection(search: String){
        if (search.isEmpty()) {
            DialogUtils.showLoader(this)
        }
        ApiConnection.getInstance().getAcademicSection(this, search).observe(this) {
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null) {
                            if (it.result != null && it.result!!.isNotEmpty()) {
                                val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
                                val adapter = GetAcademicBoardAdapter(this, ArrayList(), ArrayList(), it.result!!, 2 )
                                binding.academicRecyclerView.layoutManager = layoutManager
                                binding.academicRecyclerView.adapter = adapter
                                binding.noData.root.visibility = View.GONE
                                binding.academicRecyclerView.visibility = View.VISIBLE
                            } else {
                                binding.noData.root.visibility = View.VISIBLE
                                binding.academicRecyclerView.visibility = View.GONE
                            }
                        } else {
                           // UiUtils.showSnack(it.msg, binding.root, false)
                            binding.noData.root.visibility = View.VISIBLE
                            binding.academicRecyclerView.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun getSubject(search: String){
        if (search.isEmpty()) {
            DialogUtils.showLoader(this)
        }
        ApiConnection.getInstance().getAcademicSubject(this, search).observe(this) {
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null) {
                            if (it.result != null && it.result!!.isNotEmpty()) {
                                val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
                                val adapter = GetAcademicSubjectAdapter(this, it.result!! )
                                binding.academicRecyclerView.layoutManager = layoutManager
                                binding.academicRecyclerView.adapter = adapter
                                binding.noData.root.visibility = View.GONE
                                binding.academicRecyclerView.visibility = View.VISIBLE
                            } else {
                                binding.noData.root.visibility = View.VISIBLE
                                binding.academicRecyclerView.visibility = View.GONE
                            }
                        } else {
                           // UiUtils.showSnack(it.msg, binding.root, false)
                            binding.noData.root.visibility = View.VISIBLE
                            binding.academicRecyclerView.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

}