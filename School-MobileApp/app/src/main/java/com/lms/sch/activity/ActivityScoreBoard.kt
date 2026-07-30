package com.lms.sch.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.adapter.PointsBoardAdapter
import com.lms.sch.adapter.ScoreBoardAdapter
import com.lms.sch.databinding.ActivityScoreBoardBinding
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.GetScoreboardResponse
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils

class ActivityScoreBoard : BaseActivity() {
    lateinit var binding : ActivityScoreBoardBinding
    var scoreBoardRes = ArrayList<GetScoreboardResponse.Result>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityScoreBoardBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.back.setOnClickListener{
            onBackPressed()
        }
        scoreBoard()

        DialogUtils.showLoader(this)
        ApiConnection.getInstance().getStatsProgressPoints(this).observe(this){
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if(success){
                        if (it.result != null){

                            if (it.result!!.score != null){
                                binding.totalPoints.text = it.result!!.score!!.toString()
                            }
                            else{
                                binding.totalPoints.text = "0"
                            }

                            if (it.result!!.rank != null) {
                                binding.rank.text = "${it.result!!.rank.toString()} th Place"
                            } else {
                                binding.rank.text = "0 th Place"
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
        binding.pointsLeaderBoard.setOnClickListener{
            UiUtils.textviewCustomDrawable(binding.pointsLeaderBoard,R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.pointsLeaderBoard,null,R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.tabpointHistory,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.tabpointHistory,null,R.color.black_varient6)
            scoreBoard()
        }
        binding.tabpointHistory.setOnClickListener{
            UiUtils.textviewCustomDrawable(binding.tabpointHistory,R.drawable.border_line_curve_24dp_primary)
            UiUtils.textViewTextColor(binding.tabpointHistory,null,R.color.colorPrimary)
            UiUtils.textviewCustomDrawable(binding.pointsLeaderBoard,R.drawable.border_line_curve_24dp_grey)
            UiUtils.textViewTextColor(binding.pointsLeaderBoard,null,R.color.black_varient6)
            pointsboard()
        }
//        binding.tabpointHistory.performClick()
    }
    fun scoreBoard(){
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().scoreBoard(this).observe(this){
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if(success){
                        if (it.result != null && it.result!!.isNotEmpty()){
                            scoreBoardRes = it.result!!
                            val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
                            val adapter = ScoreBoardAdapter(this,scoreBoardRes)
                            binding.scoreBoard.layoutManager = layoutManager
                            binding.scoreBoard.adapter = adapter
                        }  else{
                            UiUtils.showSnack(it.msg, binding.root, false)
                            binding.noData.root.visibility = View.VISIBLE
                            binding.scoreBoard.visibility = View.GONE
                        }
                    }
                    else{
                        UiUtils.showSnack(it.msg, binding.root, false)
                        binding.noData.root.visibility = View.VISIBLE
                        binding.scoreBoard.visibility = View.GONE
                    }
                }
            }
        }
    }
    fun pointsboard(){
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().pointsHistory(this).observe(this){
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if(success){
                        if (it.result != null){
                            binding.totalPoints.text = it.result!!.totalPoints!!.toString()
                            val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
                            val adapter = PointsBoardAdapter(this,it.result!!.finalList!!)
                            binding.scoreBoard.layoutManager = layoutManager
                            binding.scoreBoard.adapter = adapter
                        }  else{
                            UiUtils.showSnack(it.msg, binding.root, false)
                            binding.noData.root.visibility = View.VISIBLE
                            binding.scoreBoard.visibility = View.GONE
                        }
                    }
                    else{
                        UiUtils.showSnack(it.msg, binding.root, false)
                        binding.noData.root.visibility = View.VISIBLE
                        binding.scoreBoard.visibility = View.GONE
                    }
                }
            }
        }
    }
}
