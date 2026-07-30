package com.lms.sch.network.local

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lms.sch.R
import com.lms.sch.databinding.DialogApiBinding
import com.lms.sch.session.Constants
import okhttp3.Headers
import org.json.JSONObject
import kotlin.math.roundToInt

class ApiDataDialog(var context: Context) {
    var binding: DialogApiBinding? = null
    private var sharedPreferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
    private var apiDataAdapter: ApiDataAdapter? = null
    companion object {
        const val Temp = "Temp"
    }

    fun initiateShow(doubleBackToExitPressedOnce:Boolean,activity: Activity){
        if(doubleBackToExitPressedOnce){
            Constants.Common.IS_FIRST_OPEN_1++
            if(Constants.Common.IS_FIRST_OPEN_1 == 2){
                Constants.Common.IS_FIRST_OPEN_1--
                show(activity)
            }
        }
    }

    fun show(activity: Activity) {
        val dialog = Dialog(context)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.dialog_api)
        dialog.window?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context, R.color.transparent)))
        val width: Int = (context.resources.displayMetrics.widthPixels * 0.9).roundToInt()
        val height: Int = (context.resources.displayMetrics.heightPixels * 0.7).roundToInt()
        dialog.window?.setLayout(width, height)

        val swipe = dialog.findViewById(R.id.swiperefresh) as SwipeRefreshLayout
        val close = dialog.findViewById(R.id.close) as ImageView
        val title = dialog.findViewById(R.id.txt1) as TextView
        val shareAll = dialog.findViewById(R.id.share_all) as TextView
        val recycler = dialog.findViewById(R.id.api_recycler) as RecyclerView

//        title.setOnClickListener {
//            dialog.dismiss()
//            activity.finish()
//            val send = Intent(activity, SplashActivity::class.java)
//            activity.startActivity(send)
//        }
        close.setOnClickListener {
            dialog.dismiss()
        }
        shareAll.setOnClickListener {
            apiDataAdapter!!.shareAll()
        }
        swipe.setOnRefreshListener {
            loadRecycler(recycler,activity)
            swipe.isRefreshing = false
        }

        loadRecycler(recycler,activity)
        if (!dialog.isShowing) {
            dialog.show()
        }
    }
    private fun loadRecycler(recycler: RecyclerView,activity: Activity) {
        apiDataAdapter = ApiDataAdapter(activity,context,apiDataStore)
        linearLayoutManager.stackFromEnd = true
        recycler.layoutManager = linearLayoutManager
        recycler.adapter = apiDataAdapter
    }

    fun load(url: String, method: String, body: JSONObject, header: Headers?, responseCode: Int, response: String){
        val rurl = "${context.getString(R.string.url)} ${":"} ${url}"
        val rmethod = "${context.getString(R.string.method)} ${":"} ${method}"
        val rcode = "${context.getString(R.string.response_code)} ${":"} ${responseCode}"
        val rbody = "${context.getString(R.string.request_body)} ${":"} ${body}"
        val rheader = "${context.getString(R.string.request_header)} ${":"} ${header}"
        val rresponse = "${context.getString(R.string.response)} ${":"} ${response}"
        val code = rurl+"\n"+rmethod+"\n"+rcode+"\n"+rbody+"\n"+rresponse+"\n"+rheader
        Log.d("API : ",code)
        if((apiCount == 0 && Constants.Common.IS_FIRST_OPEN_1 == 0) || (apiCount == 2 && Constants.Common.IS_FIRST_OPEN_1 == 0)){
            Constants.Common.IS_FIRST_OPEN_1 = 1
            apiDataStore = ArrayList()
            apiCount = 1
            addData(ApiDataStore(apiCount,url,method,body,header,responseCode,response))
        }
        else if(apiCount == 1 && Constants.Common.IS_FIRST_OPEN_1 == 0){
            Constants.Common.IS_FIRST_OPEN_1 = 1
            apiCount = 2
            addData(ApiDataStore(apiCount,url,method,body,header,responseCode,response))
        }
        else if(apiCount == 1){
            Constants.Common.IS_FIRST_OPEN_1 = 1
            addData(ApiDataStore(1,url,method,body,header,responseCode,response))
        }
        else if(apiCount == 2){
            Constants.Common.IS_FIRST_OPEN_1 = 1
            addData(ApiDataStore(2,url,method,body,header,responseCode,response))
        }
    }

    private fun addData(data: ApiDataStore) {
        val arrayList:ArrayList<ApiDataStore> = ArrayList()
        apiDataStore = if(apiDataStore.size == 0){
            arrayList.add(0,data)
            arrayList
        } else{
            for(items in apiDataStore){
                arrayList.add(items)
            }
            arrayList.add(arrayList.size,data)
            arrayList
        }
    }

    private var apiDataStore: ArrayList<ApiDataStore>
        get() : ArrayList<ApiDataStore> {
            val myType = object : TypeToken<List<ApiDataStore>>() {}.type
            val vsl = getKey()

            if (vsl == "") {
                return ArrayList()
            }
            val logs: List<ApiDataStore> = Gson().fromJson(vsl, myType)
            return logs as ArrayList<ApiDataStore>
        }
        set(value) {
            val jsonString = Gson().toJson(value)
            putKey(jsonString)
        }

    private var apiCount: Int
        get() : Int {
            return getInt()
        }
        set(value) {
            putInt(value)
        }

    private fun putKey(value: String) {
        sharedPreferences = context.getSharedPreferences(Temp, Context.MODE_PRIVATE)
        editor = sharedPreferences!!.edit()
        editor!!.putString("apiDataStore", value)
        editor!!.apply()
    }

    private fun getKey(): String {
        sharedPreferences = context.getSharedPreferences(Temp, Context.MODE_PRIVATE)
        return sharedPreferences!!.getString("apiDataStore", "").toString()
    }

    private fun putInt(value: Int) {
        sharedPreferences = context.getSharedPreferences(Temp, Context.MODE_PRIVATE)
        editor = sharedPreferences!!.edit()
        editor!!.putInt("apiCount", value)
        editor!!.apply()
    }

    private fun getInt(): Int {
        sharedPreferences = context.getSharedPreferences(Temp, Context.MODE_PRIVATE)
        return sharedPreferences!!.getInt("apiCount", 0)
    }
}