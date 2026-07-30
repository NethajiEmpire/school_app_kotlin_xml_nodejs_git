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
import android.widget.PopupWindow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.lms.sch.R
import com.lms.sch.adapter.ExamListAdapter
import com.lms.sch.databinding.ActivityExaminationBinding
import com.lms.sch.databinding.FilterHomeworkBinding
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.StudentBoardResponse
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils

class ExaminationActivity : BaseActivity() {
    lateinit var binding: ActivityExaminationBinding
    var examStatus = ""
    var boardId = ""
    var search = ""
    var board = ArrayList<StudentBoardResponse.Result>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityExaminationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backarrow.setOnClickListener{
            onBackPressed()
        }
        examStatus = ""
        search = ""
        binding.search.setText("")
        exam()
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                search = binding.search.text.toString()
                exam()
            }
            override fun beforeTextChanged(s: CharSequence, start: Int,count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        binding.search.setOnEditorActionListener(TextView.OnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                search = binding.search.text.toString()
                exam()
            }
            false
        })

        DialogUtils.showLoader(this)
        ApiConnection.getInstance().studentBoard(this).observe(this) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success){
                        if (it.result != null && it.result!!.isNotEmpty()){
                            board = it.result!!
                            initAdapter(layoutInflater, binding.root)
                        }
                    }
                    else {
                        UiUtils.showSnack(it.msg,binding.root,false)
                    }
                }
            }
        }
                binding.filter3.setOnClickListener {
                    val inflater = LayoutInflater.from(this)
                    val bind : FilterHomeworkBinding = FilterHomeworkBinding.inflate(inflater)
                    val popupView : View = bind.root
                    bind.today.text = "Ongoing"
                    bind.pending.text = "Upcoming"

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
                    if (examStatus == ""){
                        UiUtils.textviewImgDrawable(bind.all,R.drawable.hugeicons_tick,"start")
                        UiUtils.textviewImgDrawable(bind.today,null,"start")
                        UiUtils.textviewImgDrawable(bind.pending,null,"start")
                        UiUtils.textviewImgDrawable(bind.completed,null,"start")
                    }
                    else if (examStatus == "ongoing"){
                        UiUtils.textviewImgDrawable(bind.today,R.drawable.hugeicons_tick,"start")
                        UiUtils.textviewImgDrawable(bind.all,null,"start")
                        UiUtils.textviewImgDrawable(bind.pending,null,"start")
                        UiUtils.textviewImgDrawable(bind.completed,null,"start")
                    }
                    else if (examStatus == "upcomming"){
                        UiUtils.textviewImgDrawable(bind.pending,R.drawable.hugeicons_tick,"start")
                        UiUtils.textviewImgDrawable(bind.all,null,"start")
                        UiUtils.textviewImgDrawable(bind.today,null,"start")
                        UiUtils.textviewImgDrawable(bind.completed,null,"start")
                    }
                    else if (examStatus == "completed"){
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
                        examStatus = ""
                        search = ""
                        binding.search.setText("")
                        exam()
                    }
                    bind.today.setOnClickListener {
                        examStatus = "ongoing"
                        search = ""
                        binding.search.setText("")
                        exam()
                    }
                    bind.pending.setOnClickListener {
                        examStatus = "upcomming"
                        search = ""
                        binding.search.setText("")
                        exam()
                    }
                    bind.completed.setOnClickListener {
                        examStatus = "completed"
                        search = ""
                        binding.search.setText("")
                        exam()
                    }
                    val anchorView = binding.filter3
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

//        binding.ongingTab.setOnClickListener {
//            UiUtils.linearLayoutBgDrawable(binding.ongingTab,R.drawable.border_line_curve_24dp_primary)
//            UiUtils.linearLayoutBgDrawable(binding.examUpcomingTab,R.drawable.border_line_curve_24dp_grey)
//            UiUtils.linearLayoutBgDrawable(binding.examCompletedTab,R.drawable.border_line_curve_24dp_grey)
//            UiUtils.textViewTextColor(binding.ongoingId,null, R.color.colorPrimary)
//            UiUtils.textViewTextColor(binding.upcomingId,null, R.color.black_varient6)
//            UiUtils.textViewTextColor(binding.completedId,null, R.color.black_varient6)
//            examStatus = "ongoing"
//            search = ""
//            binding.search.setText("")
//            exam()
//        }
//        binding.examUpcomingTab.setOnClickListener {
//            UiUtils.linearLayoutBgDrawable(binding.examUpcomingTab,R.drawable.border_line_curve_24dp_primary)
//            UiUtils.linearLayoutBgDrawable(binding.ongingTab,R.drawable.border_line_curve_24dp_grey)
//            UiUtils.linearLayoutBgDrawable(binding.examCompletedTab,R.drawable.border_line_curve_24dp_grey)
//            UiUtils.textViewTextColor(binding.upcomingId,null, R.color.colorPrimary)
//            UiUtils.textViewTextColor(binding.ongoingId,null, R.color.black_varient6)
//            UiUtils.textViewTextColor(binding.completedId,null, R.color.black_varient6)
//            examStatus = "notcompleted"
//            search = ""
//            binding.search.setText("")
//            exam()
//        }
//        binding.examCompletedTab.setOnClickListener {
//            UiUtils.linearLayoutBgDrawable(binding.examCompletedTab,R.drawable.border_line_curve_24dp_primary)
//            UiUtils.linearLayoutBgDrawable(binding.examUpcomingTab,R.drawable.border_line_curve_24dp_grey)
//            UiUtils.linearLayoutBgDrawable(binding.ongingTab,R.drawable.border_line_curve_24dp_grey)
//            UiUtils.textViewTextColor(binding.completedId,null, R.color.colorPrimary)
//            UiUtils.textViewTextColor(binding.ongoingId,null, R.color.black_varient6)
//            UiUtils.textViewTextColor(binding.upcomingId,null, R.color.black_varient6)
//            examStatus = "completed"
//            search = ""
//            binding.search.setText("")
//            exam()
//        }
        binding.ongingTab.performClick()
    }

    private fun exam(){
        if (search.isEmpty()){
            DialogUtils.showLoader(this)
        }
        ApiConnection.getInstance().getExam(this,search, examStatus,boardId).observe(this){
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if(success){
                        if (it.result != null && it.result!!.rows!!.isNotEmpty()){
                            binding.noData.root.visibility = View.GONE
                            binding.examRecycler.visibility = View.VISIBLE
                            val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
                            val adapter = ExamListAdapter(this,it.result!!.rows!!)
                            binding.examRecycler.layoutManager = layoutManager
                            binding.examRecycler.adapter = adapter
                        }  else{
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

        private fun initAdapter(inflater: LayoutInflater, container: ViewGroup) {
            if (binding.tabLayout.tabCount == 0) {
                for (i in 0 until board.size) {
                    binding.tabLayout.addTab(binding.tabLayout.newTab().setText(""))
                    val tabView: View = inflater.inflate(R.layout.custom_tab, container, false)
                    val tabText = tabView.findViewById<TextView>(R.id.tab)

                    tabText.text = board[i].name
                    UiUtils.textViewTextColor(tabText, null, R.color.colorPrimary)
                    tabText.setTextAppearance(R.style.FontMedium)

                    binding.tabLayout.getTabAt(i)?.customView = tabView
                }

                binding.tabLayout.tabGravity = TabLayout.GRAVITY_CENTER
                binding.tabLayout.tabMode = TabLayout.MODE_SCROLLABLE

                binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab) {
                        val linear0: View = tab.customView!!
                        val txttab0 = linear0.findViewById<TextView>(R.id.tab)
                        UiUtils.textViewTextColor(txttab0, null, R.color.colorPrimary)
                        txttab0.setTextAppearance(R.style.FontMedium)
                        if (tab.position < board.size) {
                            boardId = board[tab.position]._id!!
                            exam()
                        }
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab) {
                        val linear1: View = tab.customView!!
                        val txttab1 = linear1.findViewById<TextView>(R.id.tab)
                        UiUtils.textViewTextColor(txttab1, null, R.color.black)
                        txttab1.setTextAppearance(R.style.FontMedium)
                    }

                    override fun onTabReselected(tab: TabLayout.Tab) {}
                })
            } else {
                binding.tabLayout.removeAllTabs()
                binding.tabLayout.clearOnTabSelectedListeners()
                initAdapter(inflater, container)
            }
        }
    
}