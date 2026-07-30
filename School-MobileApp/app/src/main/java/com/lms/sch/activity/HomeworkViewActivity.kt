package com.lms.sch.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.lms.sch.R
import com.lms.sch.adapter.MySubjectsAdapter
import com.lms.sch.adapter.StudentHwListAdapter
import com.lms.sch.databinding.ActivityHomeworkViewBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.GetHomeworkResponse
import com.lms.sch.response.TeacherHwSingleResponse.Result.CreatedBy
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
import kotlin.collections.get

class HomeworkViewActivity : BaseActivity() {
    private lateinit var binding : ActivityHomeworkViewBinding
    var homeworkId = ""
    var studentHwRes = ArrayList<GetHomeworkResponse.Result>()
    var  search = ""
    var  status = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityHomeworkViewBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        enableEdgeToEdge()
        binding.backarrow.setOnClickListener {
            onBackPressed()
        }
        homeworkId = intent.getStringExtra("id")!!
        Log.d("hgfds",homeworkId!!)
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().getHwSingle(this, homeworkId).observe(this) {
            it?.let {
                DialogUtils.dismissLoader()

                it.success.let { success ->
                    if (success) {
                        if (it.result != null) {
                            Log.d("hsgdfdshf","${it.result!!}")
                            binding.subject.text = it.result!!.subject!!.name!!
                            binding.teacher.text = "${it.result!!.createdBy!!.firstName} ${it.result!!.createdBy!!.lastName}"
                        } else {
                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    } else {
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
        getStudentDetails()
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int,p2: Int,p3: Int) {}
            override fun onTextChanged(p0: CharSequence?,p1: Int,p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                search = binding.search.text.toString()
                getStudentDetails()
            }
        })
        binding.search.setOnEditorActionListener(TextView.OnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                search = binding.search.text.toString()
                getStudentDetails()
            }
            false
        })

    }
    fun getStudentDetails(){
        if (search.isEmpty()){
            DialogUtils.showLoader(this)
        }
        ApiConnection.getInstance().studentHomework(this,search,"",status,homeworkId).observe(this){
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success){
                        if (it.result != null && it.result!!.isNotEmpty()){
                            binding.noData.root.visibility = View.GONE
                            binding.recycle.visibility = View.VISIBLE
                            studentHwRes = it.result!!
                            val adapter = StudentHwListAdapter(this,status,it.result!!,object: OnClickListener{
                                override fun onClickItem(pos: Int) {
//                                    updMark(pos)
                                }
                            })
                            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
                            binding.recycle.layoutManager = layoutManager
                            binding.recycle.adapter = adapter
                        }
                        else {
                            binding.noData.root.visibility = View.VISIBLE
                            binding.recycle.visibility = View.GONE
//                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    }
                    else {
                        binding.noData.root.visibility = View.VISIBLE
                        binding.recycle.visibility = View.GONE
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
    }
}