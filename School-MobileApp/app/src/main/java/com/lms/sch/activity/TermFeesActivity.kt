package com.lms.sch.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.lms.sch.R
import com.lms.sch.response.AdminSingleViewResponse
import com.lms.sch.adapter.StudentList2Adapter
import com.lms.sch.adapter.TermFeesAdapter
import com.lms.sch.adapter.TermsAdapter
import com.lms.sch.databinding.ActivityTermFeesBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.AdminFeesResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils

class TermFeesActivity : BaseActivity() {
    lateinit var binding: ActivityTermFeesBinding
    var id = ""
    var search = ""
    var result1 = ArrayList<AdminSingleViewResponse.Result.Terms>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTermFeesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        id = intent.getStringExtra(Constants.IntentKeys.KEY)!!
        binding.backarrow.setOnClickListener {
            onBackPressed()
        }
        getExamResult()
    }
    fun getExamResult() {
        if (search.isEmpty()) {
            DialogUtils.showLoader(this)
        }
        ApiConnection.getInstance().adminFees1(this, id , "")
            .observe(this) {
                it.let {
                    DialogUtils.dismissLoader()
                    it.success.let { success ->
                        if (success) {
                            if (it.result != null) {
                                result1 = it.result!!.terms!!
                                binding.head.text = "${it.result!!.title.toString()} Fees"
                                binding.std.text = it.result!!.title.toString()
                                binding.batch.text = "(${it.result!!.batch!!.name})"
                                binding.admissionAmountt.text = "₹${it.result!!.admissionFee.toString()!!}"
                                if (it.result!!.status == "active"){
                                    binding.status.text = "Active"
                                    UiUtils.textviewCustomDrawable(binding.status, R.drawable.border_curve_24dp)
                                    UiUtils.textViewBgTint(binding.status,"#D8FFDA",null)
                                    UiUtils.textViewTextColor(binding.status,"#32B138",null)
                                }else{
                                    binding.status.text = "InActive"
                                    UiUtils.textviewCustomDrawable(binding.status, R.drawable.border_curve_24dp)
                                    UiUtils.textViewBgTint(binding.status,"#FFDDDD",null)
                                    UiUtils.textViewTextColor(binding.status,"#EA5455",null)
                                }
                                binding.noData.root.visibility = View.GONE
                                binding.personalInfoRecycler.visibility = View.VISIBLE
                                val adapter = TermFeesAdapter( this, result1,object : OnClickListener {
                                    override fun onClickItem(pos: Int) {
                                    }
                                })
                                val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                                binding.personalInfoRecycler.layoutManager = layoutManager
                                binding.personalInfoRecycler.adapter = adapter

                                val termsAdapter = TermsAdapter(this, result1, object : OnClickListener {
                                    override fun onClickItem(pos: Int) {
                                    }
                                })
                                binding.terms.layoutManager =
                                    LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                                binding.terms.adapter = termsAdapter
                            } else {
                                binding.noData.root.visibility = View.VISIBLE
                                binding.personalInfoRecycler.visibility = View.GONE
                            }
                        } else {
                            binding.noData.root.visibility = View.VISIBLE
                            binding.personalInfoRecycler.visibility = View.GONE
                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    }
                }
            }
    }
}