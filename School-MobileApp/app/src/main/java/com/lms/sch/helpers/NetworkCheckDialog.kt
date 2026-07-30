package com.lms.sch.helpers

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.lms.sch.R
import com.lms.sch.databinding.PopupNoNetworkBinding
import com.lms.sch.utils.UiUtils

class NetworkCheckDialog(private val context: Context) {
    private var dialog: Dialog? = null
    private val handler = Handler(Looper.getMainLooper())
    private val checkInterval = 5000L // 5 seconds

    private val checkNetworkRunnable = object : Runnable {
        override fun run() {
            if (!isNetworkAvailable(context)) {
                showDialog()
            } else {
                dismissDialog()
            }
            handler.postDelayed(this, checkInterval)
        }
    }

    fun startChecking() {
        handler.post(checkNetworkRunnable)
    }

    fun stopChecking() {
        handler.removeCallbacks(checkNetworkRunnable)
        dismissDialog()
    }

    private fun showDialog() {
        if (dialog == null || dialog?.isShowing == false) {
            dialog = Dialog(context)
            dialog!!.setCancelable(false)
            dialog!!.setCanceledOnTouchOutside(false)
            val bind: PopupNoNetworkBinding = PopupNoNetworkBinding.inflate(LayoutInflater.from(context))
            dialog!!.setContentView(bind.root)
            dialog!!.window?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context, R.color.transparent)))
            dialog!!.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            dialog!!.window?.setGravity(Gravity.BOTTOM)
            UiUtils.animation(context, bind.bot, R.anim.slide_in_from_bottom, true)
            bind.oops.text = "Ooops!"
            bind.txt.text = "No internet connection found,\nCheck your connection"
            bind.tryagain.setOnClickListener {
                if (isNetworkAvailable(context)) {
                    bind.oops.text = "You're Back Online!"
                    bind.txt.text = "Network is back! Proceeding..."
                    dialog!!.dismiss()
                    stopChecking()
                } else {
                    bind.txt.text = "Still offline!,\nCheck your connection"
                }
            }
            dialog?.show()
        }
    }

    private fun dismissDialog() {
        dialog?.dismiss()
        dialog = null
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }
}
