package com.lms.sch.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.adapter.PaymentHistoryAdapter
import com.lms.sch.databinding.ActivityPaymentHistoryBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.network.ApiConnection
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils

class PaymentHistoryActivity : BaseActivity() {
    lateinit var binding : ActivityPaymentHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPaymentHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        paymentHistory()
    }
    private fun paymentHistory() {
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().paymentHistory(this).observe(this) {
            DialogUtils.dismissLoader()
            it?.let {
                it.success.let { success ->
                    if (success) {
                        if (it.result != null && it.result!!.rows!!.isNotEmpty()) {
                            binding.recycler.visibility = View.VISIBLE
                            binding.noData.root.visibility = View.GONE
                            val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
                            val adapter = PaymentHistoryAdapter(this,false,it.result!!.rows!!,object : OnClickListener {
                                override fun onClickItem(pos: Int) {
                                    //getPaymentHistory(pos)
                                }
                            })
                            binding.recycler.layoutManager = linearLayoutManager
                            binding.recycler.adapter = adapter
                        } else {
                            binding.recycler.visibility = View.GONE
                            binding.noData.root.visibility = View.VISIBLE
//                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    } else {
                        binding.recycler.visibility = View.GONE
                        binding.noData.root.visibility = View.VISIBLE
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
    }
}