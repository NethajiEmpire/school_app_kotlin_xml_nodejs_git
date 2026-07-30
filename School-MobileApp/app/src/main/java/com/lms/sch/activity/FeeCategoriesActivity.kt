package com.lms.sch.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.lms.sch.databinding.ActivityFeeCategoriesBinding
import com.lms.sch.fragment.BaseFragment
import com.lms.sch.fragment.MyClassFragment
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils

class FeeCategoriesActivity : AppCompatActivity() {
    lateinit var binding: ActivityFeeCategoriesBinding
    var key=""


   override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       binding = ActivityFeeCategoriesBinding.inflate(layoutInflater)
       setContentView(binding.root)

       if (key!=null){
           key = intent.getStringExtra("key")!!
           UiUtils.log("sdfds",key)
       } else{
           key=""
       }

       if (key=="Exam Fee")
       {
           UiUtils.log("sdfds",Constants.IntentKeys.KEY1)
           binding.fees.text="Exam fees"
       }
       else if (key=="TutionFee"){
           binding.fees.text="Tution Fee"
       }
       else if (key =="BusFee"){
           binding.fees.text="Bus Fee"
       }
       else if (key =="BookFee"){
           binding.fees.text="Book Fee"
       }

   }
}