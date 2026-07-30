package com.lms.sch.utils

import android.content.Context
import android.net.ConnectivityManager
import android.content.Intent
import android.view.WindowManager
import android.app.Activity
import android.app.AlertDialog
import android.widget.TextView
import com.lms.sch.R

object NetworkUtils {
    @Suppress("DEPRECATION")
    fun isNetworkConnected(context: Context?):Boolean{
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val isConnected: Boolean
        val activeNetwork = connectivityManager.activeNetworkInfo
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
        /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          val networkCapabilities = connectivityManager.activeNetwork ?: return false
          val actNw =
              connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
          isConnected = when {
              actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
              actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
              actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
              else -> false
          }
      }
      else {
          connectivityManager.run {
              connectivityManager.activeNetworkInfo?.run {
                  isConnected = when (type) {
                      ConnectivityManager.TYPE_WIFI -> true
                      ConnectivityManager.TYPE_MOBILE -> true
                      ConnectivityManager.TYPE_ETHERNET -> true
                      else -> false
                  }

              }
          }
      }*/
        return isConnected
    }

    fun noNetworkDialog(activity: Activity) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setView(R.layout.popup_no_network)
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
        alertDialog.setCancelable(true)
        alertDialog.setOnCancelListener {
            if (isNetworkConnected(activity)) {
                alertDialog.dismiss()
                activity.finish()
                val intent = Intent(activity, activity.javaClass)
                activity.startActivity(intent)
            } else {
                //alertDialog.show()
                alertDialog.dismiss()
                activity.finish()
            }
        }
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(alertDialog.window!!.attributes)
        layoutParams.width = 500
        layoutParams.height = 500
       // alertDialog.window!!.setBackgroundDrawable(null)
        //alertDialog.window!!.attributes = layoutParams
            //alertDialog.window!!.setGravity(Gravity.CENTER)
      //  alertDialog.getWindow()?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
        val btnClose: TextView = alertDialog.findViewById(R.id.tryagain) as TextView
        btnClose.setOnClickListener {
            if (isNetworkConnected(activity)) {
                alertDialog.dismiss()
                activity.finish()
                val intent = Intent(activity, activity.javaClass)
                activity.startActivity(intent)
            }
            else {
                alertDialog.show()
            }
        }
    }


}