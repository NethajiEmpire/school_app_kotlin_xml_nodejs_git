package com.lms.sch.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.lms.sch.R
import com.lms.sch.databinding.ActivityNotificationBinding
import com.lms.sch.databinding.ActivityProfileBinding
import com.lms.sch.databinding.ActivityTeacherProfileDetailsBinding

class NotificationActivity : BaseActivity() {
    lateinit var binding : ActivityNotificationBinding
    var key = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.backarrow.setOnClickListener {
            onBackPressed()
        }
        key = intent.getStringExtra("key")!!
        if (key == "Notification"){
            binding.pageHeading.text = key
            binding.notificate.visibility = View.VISIBLE
            binding.privacy.visibility = View.GONE
        }
        else if (key == "Privacy & policy"){
            binding.pageHeading.text = key
            binding.notificate.visibility = View.GONE
            binding.privacy.visibility = View.VISIBLE
        }
        else if ( key == "Settings") {
            binding.pageHeading.text = key
            binding.notificate.visibility = View.VISIBLE
            binding.privacy.visibility = View.GONE
        }

    }

}