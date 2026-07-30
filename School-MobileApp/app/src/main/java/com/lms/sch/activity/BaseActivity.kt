package com.lms.sch.activity

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.lms.sch.broadcast_receivers.NetworkStateReceiver
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.network.ApiClient
import com.lms.sch.BuildConfig
import com.lms.sch.network.ApiConnection
import com.lms.sch.network.local.ApiDataDialog
import com.lms.sch.session.Constants.ApplicationConstants.SOCKET_BASE_URL
import com.lms.sch.session.SharedHelper
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.LocaleUtils
import com.lms.sch.utils.UiUtils
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.reactivex.disposables.CompositeDisposable
import io.socket.client.Ack
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import java.net.URISyntaxException
import java.util.concurrent.Executor

open class BaseActivity : AppCompatActivity(), NetworkStateReceiver.NetworkStateReceiverListener, OnClickListener {
    companion object {
        val mObjectMapper: ObjectMapper by lazy {
            ObjectMapper()
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        }
    }

    var mToast: Toast? = null
    var mHashIdentifier = ""
    var mCustomDialog: AlertDialog? = null
    var mCompositeDisposable = CompositeDisposable()
    var mNetworkStateReceiver: NetworkStateReceiver = NetworkStateReceiver()
    open lateinit var sharedHelper: SharedHelper
    lateinit var apiConnection: ApiConnection
    lateinit var view: View
    /*var requestCode = 0
    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
        }
    }*/


//    val activityLauncher: BetterActivityResult<Intent, ActivityResult> by lazy { BetterActivityResult.registerActivityForResult(this) }
     var mSocket: Socket? = null
    var reportDialog: BottomSheetDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbarUpView()
        //mDataBaseHandler = DatabaseHelper(this)
        sharedHelper = SharedHelper(this)
        apiConnection = ApiConnection.getInstance()
        mNetworkStateReceiver = NetworkStateReceiver()
        mNetworkStateReceiver.addListener(this)
        registerReceiver(mNetworkStateReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        initSocket()
       /* if(SocketHandler.getSocket() == null){
            initSocket()
        }
        else{
            mSocket = SocketHandler.getSocket()
        }*/
    }

    fun apiDetailsShown(){
        if(BuildConfig.DEBUG){
            ApiDataDialog(this).show(this)
        }
    }

    fun initSocket(){
        /*  val options = IO.Options()
          options.forceNew = true
          options.reconnection = false
          options.path = "/ws";
          options.reconnectionAttempts = 1
          options.transports = arrayOf(WebSocket.NAME) //or Polling.NAME
          options.reconnectionDelay = 2000
          options.reconnectionDelayMax = 5000
  */


      /*  SocketHandler.setSocket()
        SocketHandler.establishConnection()
        mSocket = SocketHandler.getSocket()*/

        try {
            mSocket = IO.socket(SOCKET_BASE_URL)
            mSocket.let {
                if(!it!!.connected()){
                    mSocket!!.connect()
                }
            }
        } catch (e: URISyntaxException) {
            throw RuntimeException(e)
        }



        mSocket!!.on(Socket.EVENT_DISCONNECT){
            Log.d("Socket","DisConnected")
            // initSocket()
        }
        mSocket!!.on(Socket.EVENT_CONNECT){
            Log.d("Socket","Connected")
            //  initSocket()
        }
        mSocket!!.on(Socket.EVENT_CONNECT_ERROR){
            Log.d("Socket","Error"+it[0].toString())
            //  initSocket()
        }

        val jsonObject = JSONObject()
        jsonObject.put("username",sharedHelper.name)
        jsonObject.put("user_id",sharedHelper.id)
        mSocket!!.emit("connect-me", jsonObject,object : Ack {
            override fun call(vararg args: Any?) {
                Log.d("vnnv9", "....$args")
            }
        })
        mSocket!!.on("user-connected") {
            UiUtils.showLog(" listener ", it[0].toString())
        }
        mSocket!!.on("user-disconnect") {
            UiUtils.showLog(" listener ", it[0].toString())
        }
    }

    fun removeSocket(){
        if(mSocket != null){
            mSocket!!.emit("disconnect", null,object : Ack {
                override fun call(vararg args: Any?) {
                    Log.d("vnnv9", "....$args")
                }
            })
            mSocket!!.disconnect()
        }
    }

    open fun setToolbarUpView() {
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase)
    }

    override fun onResume() {
        super.onResume()
        BaseUtils.hideKeyboard(this)
        LocaleUtils.updateConfig(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        removeSocket()
        mCompositeDisposable.clear()
        ApiClient.getDispatcher().cancelAll()
        BaseUtils.hideKeyboard(this)
        BaseUtils.enableUserInteraction(this)
        mNetworkStateReceiver.removeListener(this)
        unregisterReceiver(mNetworkStateReceiver)
    }

    override fun networkAvailable() {
    }

    override fun networkUnavailable() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100 && resultCode == RESULT_OK){
            onClickItem(1)
        }
    }

    fun bioMetric(){
        var isBioMetric = false
        val biometricManager: BiometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                isBioMetric = true
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                isBioMetric = false
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                isBioMetric = false
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                isBioMetric = false
            }
        }
        // creating a variable for our Executor
        val executor: Executor = ContextCompat.getMainExecutor(this)
        // this will give us result of AUTHENTICATION
        val biometricPrompt = BiometricPrompt(this@BaseActivity, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                onClickItem(0)
            }
            // THIS METHOD IS CALLED WHEN AUTHENTICATION IS SUCCESS
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onClickItem(1)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
            }
        })

        // creating a variable for our promptInfo
        // BIOMETRIC DIALOG
        val promptInfo = BiometricPrompt.PromptInfo.Builder().setTitle("MGR").setDescription("Use your fingerprint to login ").setNegativeButtonText("Cancel").build()

        if (isBioMetric) {
            biometricPrompt.authenticate(promptInfo)
        }
        else {
            val keyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
            if (keyguardManager.isKeyguardSecure) {
                val intent = keyguardManager.createConfirmDeviceCredentialIntent(
                    "MGR",
                    "Use your auth to login"
                )
                startActivityForResult(intent, 100)
            }
        }
    }

    override fun onClickItem(pos: Int) {

    }



}