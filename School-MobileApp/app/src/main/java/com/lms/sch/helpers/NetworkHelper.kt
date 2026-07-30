package com.lms.sch.helpers

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.view.Gravity
import androidx.appcompat.app.ActionBar.LayoutParams
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.lms.sch.R
import com.lms.sch.databinding.PopupNoNetworkBinding
import com.lms.sch.models.BaseModel
import com.lms.sch.session.TempSingleton
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils
import okhttp3.ResponseBody
//import org.jetbrains.anko.layoutInflater
import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
import kotlin.math.roundToInt

class NetworkHelper {

    companion object {

        @Suppress("DEPRECATION")
        private fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo?.isConnected==true
        }

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

        fun getErrorMessage(context: Context?, error: Throwable): String {
            if (context != null) {
                /*if (!isNetworkAvailable(context)){
//                    return context.getString(R.string.error_internet)
                    NetworkCheckDialog(context).startChecking()
                    *//*val dialog = Dialog(context)
                    dialog.setCancelable(false)
                    dialog.setCanceledOnTouchOutside(false)
                    dialog.setContentView(R.layout.popup_no_network)
                    val bind : PopupNoNetworkBinding = PopupNoNetworkBinding.inflate(context.layoutInflater)
                    dialog.setContentView(bind.root)
                    dialog.window?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context, R.color.transparent)))
                    dialog.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
                    dialog.window?.setGravity(Gravity.BOTTOM)
                    UiUtils.animation(context,bind.bot,R.anim.slide_in_from_bottom,true)
                    bind.oops.text = "Ooops!"
                    bind.txt.text = "No internet connection found,\nCheck your connection"
                    bind.tryagain.setOnClickListener {
                        if (isNetworkAvailable(context)) {
                            bind.oops.text = "You're Back Online!"
                            bind.txt.text = "Network is back! Proceeding..."
                            dialog.dismiss()
                        } else {
                            bind.txt.text = "Still offline!,\nCheck your connection"
                        }
                    }
                    dialog.show()*//*
                }
                else */
                if (error is HttpException) {
                     if(error.code() == 403 || error.code() == 401 || error.code() == 500){
                         BaseUtils.logout(context,TempSingleton.getInstance().apiUrl)
                     }
                     else if (error.code() in 500..599) {
                         return context.getString(R.string.error_server)
                     }
                     else{
                         val body: ResponseBody? = error.response()?.errorBody()
                         val str  = body?.string()
                         return BaseUtils.nullCheckerStr(Gson().fromJson(str, BaseModel::class.java).msg)
                     }
                 }
                else if (error is SocketTimeoutException || error is SocketException) {
                     return context.getString(R.string.error_timeout)
                }
                else{
                     return context.getString(R.string.error_request)
                }
            }
            return ""
        }
    }
}