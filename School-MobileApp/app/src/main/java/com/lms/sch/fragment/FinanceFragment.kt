package com.lms.sch.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.TeacherProfileActivity
import com.lms.sch.adapter.AdminFeesAdapter
import com.lms.sch.adapter.StuBoardAdapter
import com.lms.sch.databinding.FragmentAdminFinanceBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.AdminFeesResponse
import com.lms.sch.response.StudentBoardResponse
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils

class FinanceFragment : BaseFragment() {
    lateinit var binding: FragmentAdminFinanceBinding
    var boardResult = ArrayList<StudentBoardResponse.Result>()
    var fees = ArrayList<AdminFeesResponse.Result.Rows>()
    var boardId = ""
    var search = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentAdminFinanceBinding.inflate(inflater, container, false)
        val view = binding.root
        mActivity.selectBottomNav(2)

        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().profile(mActivity).observe(mActivity) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null && it.result!!.userprofile != null) {
                            UiUtils.loadImage(binding.profile,it.result!!.userprofile!!.img_url!!)
                        }
                        else {
                            UiUtils.loadImage(binding.profile,R.drawable.ic_user_profile.toString())
                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    } else {
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
                fees()
            }
        })
        binding.search.setOnEditorActionListener(TextView.OnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                search = binding.search.text.toString()
                fees()
            }
            false
        })
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().studentBoard(mActivity).observe(mActivity) {
            DialogUtils.dismissLoader()
            it?.let {
                it.success.let { success ->
                    if (success) {
                        if (it.result != null && it.result!!.isNotEmpty()) {
                            boardResult = it.result!!
                            boardId = boardResult[0]._id!!
                            fees()
                            overAllCollectAmount()
                            val linearLayoutManager = LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false)
                            val adapter = StuBoardAdapter(mActivity,boardResult, object : OnClickListener {
                                override fun onClickItem(pos: Int) {
                                    boardId = boardResult[pos]._id!!
                                    Log.d("hhhh", "onClickItem: ${boardResult[pos]._id!!} ")
                                    Log.d("hhhghh", "onClickItem: ${boardResult[pos].name!!} ")
                                    fees()
                                    overAllCollectAmount()
                                }
                            })
                            binding.getBoard.layoutManager = linearLayoutManager
                            binding.getBoard.adapter = adapter
                        } else {
                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    } else {
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }


        binding.profile.setOnClickListener {
            BaseUtils.startActivity(mActivity, TeacherProfileActivity(), null, false)
        }

//        binding.scrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
//            if (scrollY > binding.statsLay.height) {
//                binding.tab.translationY = scrollY.toFloat() - binding.statsLay.height
//                binding.tab.elevation = 8f
//            } else {
//                binding.tab.translationY = 0f
//                binding.tab.elevation = 0f
//            }
//        }

        return view
    }

    private fun  overAllCollectAmount(){
        DialogUtils.showLoader(mActivity)
        ApiConnection.getInstance().adminFees(mActivity, boardId,"" ).observe(mActivity){
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if(success){
                        if (it.result != null){

                            if (it.result!!.overall != null && it.result!!.overall!!.collectedAmount != null) {
                                binding.totalCollectAmt.text = "₹ ${it.result!!.overall!!.collectedAmount}"
                            } else {
                                binding.totalCollectAmt.text = "0"
                            }

                            if (it.result!!.overall != null && it.result!!.overall!!.pendingAmount != null){
                                binding.totalPendingAmt.text = "₹ ${it.result!!.overall!!.pendingAmount}"
                            }
                            else{
                                binding.totalPendingAmt.text = "0"
                            }
                            if (it.result!!.overall != null && it.result!!.overall!!.overdueAmount != null){
                                binding.totalOverDueAmt.text = "₹ ${it.result!!.overall!!.overdueAmount}"
                            }
                            else {
                                binding.totalOverDueAmt.text = "0"
                            }
                            Log.d("hjsghghghghghghdghgdjdsgf",it.result!!.overall!!.overallFees!!.toString())
                            if (it.result!!.overall != null){
                                Log.d("hjsgdjdsgf",it.result!!.overall!!.overallFees!!.toString())
                                binding.overAllCltAmt.text = "₹ ${it.result!!.overall!!.overallFees}"
                            }
                            else {
                                binding.overAllCltAmt.text = "0"
                                Log.d("hjsghghdghgdjdsgf",it.result!!.overall!!.overallFees!!.toString())
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

    private fun fees() {
        if (search.isEmpty()){
            DialogUtils.showLoader(mActivity)
        }
        ApiConnection.getInstance().adminFees(mActivity,boardId, search).observe(mActivity) {
            DialogUtils.dismissLoader()
            it?.let {
                it.success.let { success ->
                    if (success) {
                        if (it.result != null && it.result!!.rows!!.isNotEmpty()) {
                            binding.noData.root.visibility = View.GONE
                            binding.feeslay.visibility = View.VISIBLE
                            fees = it.result!!.rows!!
                            val linearLayoutManager = LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false)
                            val adapter = AdminFeesAdapter(mActivity,fees,object : OnClickListener{
                                override fun onClickItem(pos: Int) {
                                    //
                                }
                            })
                            binding.feeslay.layoutManager = linearLayoutManager
                            binding.feeslay.adapter = adapter
                        } else {
                            binding.noData.root.visibility = View.VISIBLE
                            binding.feeslay.visibility = View.GONE
                        }
                    } else {
                        binding.noData.root.visibility = View.VISIBLE
                        binding.feeslay.visibility = View.GONE
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
    }
}