package com.lms.sch.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.adapter.LeaderBoardAdapter
import com.lms.sch.adapter.LeaderboardFilterAdapter
import com.lms.sch.adapter.LeaderboardFilterRemoveAdapter
import com.lms.sch.databinding.ActivityLeaderBoardBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.GetExamResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils

class LeaderBoardActivity : BaseActivity() {
    lateinit var binding: ActivityLeaderBoardBinding
    var type = 0
    var selectedPos = -1
    var search = ""
    var examName = ""
    var examId = ""
    var filterArr = ArrayList<String>()
    var examRes = ArrayList<GetExamResponse.Row>()
    var examArr = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLeaderBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backarrow.setOnClickListener {
            finish()
        }
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().getExam(this,"","").observe(this) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success->
                    if (success) {
                        if (it.result != null && it.result!!.rows!!.isNotEmpty()) {
                            examRes = it.result!!.rows!!
                            for (items in examRes){
                                if (items.examType != null){
                                    examArr.add(items.examType!!.name!!)
                                }
                            }
                        } else {
                            UiUtils.showSnack(it.msg,binding.root,false)
                        }
                    }
                }
            }
        }
        binding.search.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?,p1: Int,p2: Int,p3: Int) { }
            override fun onTextChanged(p0: CharSequence?,p1: Int,p2: Int,p3: Int) { }
            override fun afterTextChanged(p0: Editable?) {
                search = binding.search.text.toString()
                leaderBoard()
            }
        })
        leaderBoard()
        binding.tabPointsBoard.setOnClickListener {
            UiUtils.textViewTextColor(binding.tabPointsBoard,null,R.color.white)
            UiUtils.textviewCustomDrawable(binding.tabPointsBoard, R.drawable.border_curve_8dp)
            UiUtils.textViewBgTint(binding.tabPointsBoard,null,R.color.colorPrimary)
            UiUtils.textViewTextColor(binding.tabScoreBoard,null,R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.tabScoreBoard, R.drawable.border_line_curve_8dp_grey)

        }
        binding.tabScoreBoard.setOnClickListener {
            UiUtils.textViewTextColor(binding.tabScoreBoard,null,R.color.white)
            UiUtils.textviewCustomDrawable(binding.tabScoreBoard, R.drawable.border_curve_8dp)
            UiUtils.textViewBgTint(binding.tabScoreBoard,null,R.color.colorPrimary)
            UiUtils.textViewBgTint(binding.tabPointsBoard,null,R.color.white)
            UiUtils.textViewTextColor(binding.tabPointsBoard,null,R.color.black_varient6)
            UiUtils.textviewCustomDrawable(binding.tabPointsBoard, R.drawable.border_line_curve_8dp_grey)

        }
        binding.dialogFilter.tabExam.setOnClickListener {
            UiUtils.textviewCustomDrawable(binding.dialogFilter.tabExam,R.drawable.border_curve_0dp)
            UiUtils.textViewBgTint(binding.dialogFilter.tabExam,"#F7FBFE",null)
            binding.dialogFilter.title.text = "Select Exam"
            loadExam()
        }
        binding.filter.setOnClickListener {
            binding.view.visibility = View.GONE
            binding.topGradeLay.visibility = View.GONE
            binding.dialogFilter.tabExam.performClick()
            binding.dialogFilter.root.visibility = View.VISIBLE
            UiUtils.animation(this,binding.dialogFilter.root,R.anim.slide_in_from_bottom,true)
        }
        binding.dialogFilter.cancel.setOnClickListener {
            binding.dialogFilter.root.visibility = View.GONE
            binding.view.visibility = View.VISIBLE
            binding.topGradeLay.visibility = View.VISIBLE
            if (filterArr.isNotEmpty()){
                loadFilter()
            }
        }
        binding.dialogFilter.close.setOnClickListener {
            binding.dialogFilter.root.visibility = View.GONE
            binding.view.visibility = View.VISIBLE
            binding.topGradeLay.visibility = View.VISIBLE
            if (filterArr.isNotEmpty()){
                loadFilter()
            }
        }
        binding.dialogFilter.apply.setOnClickListener {
            binding.view.visibility = View.VISIBLE
            binding.topGradeLay.visibility = View.VISIBLE
            if (filterArr.isNotEmpty()){
                loadFilter()
            }
            binding.dialogFilter.root.visibility = View.GONE
            leaderBoard()
        }
        binding.recycler1.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var isScrollingDown = false
            private var lastScrollY = 0
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && !isScrollingDown) {
                    isScrollingDown = true
                    binding.view.visibility = View.GONE
                    binding.topGradeLay.visibility = View.VISIBLE
                } else if (dy < 0 && isScrollingDown) {
                    isScrollingDown = false
                    binding.view.visibility = View.VISIBLE
                    binding.topGradeLay.visibility = View.VISIBLE
                }
            }
        })
        binding.tabPointsBoard.performClick()
    }
    fun loadExam(){
        if (examArr.isNotEmpty()){
            val layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
            val adapter = LeaderboardFilterAdapter(this,examArr,object : OnClickListener {
                override fun onClickItem(pos: Int) {
                    examId = examRes[pos]._id!!
                    examName = examRes[pos].examType!!.name!!
                    filterArr.clear()
                    filterArr.add(examName)
                }
            })

            binding.dialogFilter.recycler.layoutManager = layoutManager
            binding.dialogFilter.recycler.adapter = adapter
        }
        else {
            DialogUtils.showLoader(this)
            ApiConnection.getInstance().getExam(this,"","").observe(this) {
                it.let {
                    DialogUtils.dismissLoader()
                    it.success.let { success->
                        if (success) {
                            if (it.result != null && it.result!!.rows!!.isNotEmpty()) {
                                examRes = it.result!!.rows!!
                                for (items in examRes){
                                    if (items.examType != null){
                                        examArr.add(items.examType!!.name!!)
                                    }
                                }
                            } else {
                                UiUtils.showSnack(it.msg,binding.root,false)
                            }
                        }
                    }
                }
            }
        }
    }

    fun loadFilter(){
        val adapter = LeaderboardFilterRemoveAdapter(this,filterArr,object : OnClickListener {
            override fun onClickItem(pos: Int) {
                val value = filterArr[pos]
                leaderBoard()
            }
        })
        val layoutManager = LinearLayoutManager(this,RecyclerView.HORIZONTAL,false)
        binding.filterRecycler.layoutManager = layoutManager
        binding.filterRecycler.adapter = adapter
    }

    fun leaderBoard(){
        if (search.isEmpty()){
            DialogUtils.showLoader(this)
        }
        ApiConnection.getInstance().leaderBoard(this,search,examId).observe(this){
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if(success){
                        if (it.result != null && it.result!!.rows!!.isNotEmpty()){
                            if (it.result!!.rows!![0].img_url != null) {
                                UiUtils.loadImage(binding.img, it.result!!.rows!![0].img_url)
                            }else if (it.result!!.rows!![1].img_url != null){
                                UiUtils.loadImage(binding.img1, it.result!!.rows!![1].img_url)
                            }else if (it.result!!.rows!![2].img_url != null){
                                UiUtils.loadImage(binding.img2, it.result!!.rows!![2].img_url)
                            }
                            binding.noData.root.visibility = View.GONE
                            binding.recycler1.visibility = View.VISIBLE
                            val adapter = LeaderBoardAdapter(this,it.result!!.rows!!)
                            val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
                            binding.recycler1.layoutManager = layoutManager
                            binding.recycler1.adapter = adapter
                            if (it.result!!.examDetails != null){
                                binding.exam.text = it.result!!.examDetails!!.examName
                                val start = BaseUtils.getFormattedDate(it.result!!.examDetails!!.startDate!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
                                val end = BaseUtils.getFormattedDate(it.result!!.examDetails!!.endDate!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
                                binding.date.text = "$start - $end"
                            }
                            when(it.result!!.rows!!.size){
                                0 -> { }
                                1 -> {
                                    binding.rank1Name.text = "${it.result!!.rows!![0].name}"
                                    binding.points.text = it.result!!.rows!![0].scoredMark
                                    binding.status1.text = it.result!!.rows!![0].status
                                }
                                2 -> {
                                    binding.rank1Name.text = "${it.result!!.rows!![0].name}"
                                    binding.points.text = it.result!!.rows!![0].scoredMark
                                    binding.status1.text = it.result!!.rows!![0].status

                                    binding.rank2Name.text = "${it.result!!.rows!![1].name}"
                                    binding.points2.text = it.result!!.rows!![1].scoredMark
                                    binding.status2.text = it.result!!.rows!![1].status
                                }
                                3 -> {
                                    binding.rank1Name.text = "${it.result!!.rows!![0].name}"
                                    binding.points.text = it.result!!.rows!![0].scoredMark
                                    binding.status1.text = it.result!!.rows!![0].status

                                    binding.rank2Name.text = "${it.result!!.rows!![1].name}"
                                    binding.points2.text = it.result!!.rows!![1].scoredMark
                                    binding.status2.text = it.result!!.rows!![1].status

                                    binding.rank3Name.text = "${it.result!!.rows!![2].name}"
                                    binding.points3.text = it.result!!.rows!![2].scoredMark
                                    binding.status3.text = it.result!!.rows!![2].status
                                }
                                else -> {
                                    binding.rank1Name.text = "${it.result!!.rows!![0].name}"
                                    binding.points.text = it.result!!.rows!![0].scoredMark
                                    binding.status1.text = it.result!!.rows!![0].status

                                    binding.rank2Name.text = "${it.result!!.rows!![1].name}"
                                    binding.points2.text = it.result!!.rows!![1].scoredMark
                                    binding.status2.text = it.result!!.rows!![1].status

                                    binding.rank3Name.text = "${it.result!!.rows!![2].name}"
                                    binding.points3.text = it.result!!.rows!![2].scoredMark
                                    binding.status3.text = it.result!!.rows!![2].status
                                }
                            }
                        }
                        else{
                            if (it.result != null && it.result!!.examDetails != null){
                                binding.examLay.visibility = View.VISIBLE
                                binding.exam.text = it.result!!.examDetails!!.examName
                                val start = BaseUtils.getFormattedDate(it.result!!.examDetails!!.startDate!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
                                val end = BaseUtils.getFormattedDate(it.result!!.examDetails!!.endDate!!, Constants.ApiKeys.TIME_INPUT_FORMAT, Constants.ApiKeys.DATE_FORMAT)
                                binding.date.text = "$start - $end"
                            }
                            else {
                                binding.examLay.visibility = View.GONE
                            }
                            UiUtils.showSnack(it.msg, binding.root, false)
                            binding.noData.root.visibility = View.VISIBLE
                            binding.view.visibility = View.GONE
                            binding.topGradeLay.visibility = View.GONE
                            binding.recycler1.visibility = View.GONE
                        }
                    }
                    else{
                        UiUtils.showSnack(it.msg, binding.root, false)
                        binding.noData.root.visibility = View.VISIBLE
                        binding.recycler1.visibility = View.GONE
                    }
                }
            }
        }
    }
}