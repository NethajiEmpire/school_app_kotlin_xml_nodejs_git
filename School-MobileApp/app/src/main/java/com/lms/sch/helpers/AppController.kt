package com.lms.sch.helpers

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.android.volley.RequestQueue
import com.lms.sch.BuildConfig
import com.lms.sch.R
import com.lms.sch.activity.DashBoardActivity
import com.lms.sch.session.AppSharedPref
import com.lms.sch.session.StaticData.Companion.simpleCache
import com.lms.sch.utils.UiUtils
import com.google.android.exoplayer2.database.DatabaseProvider
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.gms.security.ProviderInstaller
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import java.io.File

open class AppController : MultiDexApplication(), LifecycleObserver {
    companion object {

        private val TAG: String = AppController::class.java.simpleName
        private var instance: AppController? = null
        private var requestQueue: RequestQueue? = null
        private var mFirebaseAnalytics: FirebaseAnalytics? = null
        private var mFirebaseCrashlytics: FirebaseCrashlytics? = null
        private val callTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString()

        @Synchronized
        fun getInstance(): AppController {
            return instance as AppController
        }

    }

    @SuppressLint("MissingPermission")
    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        setUpForPreCaching()

        if(!BuildConfig.DEBUG){
            RxJavaPlugins.setErrorHandler { e ->
                if (e is UndeliverableException) {
                    // Merely log undeliverable exceptions
                    Log.d("Error",""+e.message)
                    UiUtils.showToast(this,e.message.toString())
                } else {
                    // Forward all others to current thread's uncaught exception handler
                    Thread.currentThread().also { thread ->
                        thread.uncaughtExceptionHandler.uncaughtException(thread, e)
                    }
                }
            }
        }

        /*  ViewPump.init(ViewPump.builder()
                  .addInterceptor(CalligraphyInterceptor(
                          CalligraphyConfig.Builder()
                                  .setDefaultFontPath(CALLIGRAPHY_FONT_PATH_REGULAR)
                                  .setFontAttrId(R.attr.fontPath)
                                  .build()))
                  .build())*/

        //AuthKeyHelper.getInstance().token = AppSharedPref.getFcmToken(this)?:""
        //AuthKeyHelper.getInstance().token = SharedHelper(this).token

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
        upgradeSecurityProvider()
        if (BuildConfig.DEBUG) {
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(false)
        }
        else{
            FirebaseAnalyticsHelper.initFirebaseAnalytics(this)
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
            mFirebaseCrashlytics = FirebaseCrashlytics.getInstance()
            mFirebaseAnalytics?.setAnalyticsCollectionEnabled(true)
            mFirebaseCrashlytics?.setCrashlyticsCollectionEnabled(true)
            // FacebookSdk.setIsDebugEnabled(true)
            // FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS)
        }
    }

    fun setUpForPreCaching() {

        val exoPlayerCacheSize = 50 * 1024 * 1024.toLong()// Set the size of cache for video
        var leastRecentlyUsedCacheEvictor: LeastRecentlyUsedCacheEvictor? = null
        var databaseProvider: DatabaseProvider? = null

        if (leastRecentlyUsedCacheEvictor == null) {
            leastRecentlyUsedCacheEvictor = LeastRecentlyUsedCacheEvictor(exoPlayerCacheSize)
        }

        if (databaseProvider == null) {
            databaseProvider = StandaloneDatabaseProvider(this)
        }

        if (simpleCache == null) {
            val cache: File = File(getCacheDir(), "Video_Cache")
            if (!cache.exists()) {
                cache.mkdirs()
            }
            simpleCache =
                SimpleCache(cache, leastRecentlyUsedCacheEvictor, databaseProvider)
        }

    }

    private  fun upgradeSecurityProvider() {
        ProviderInstaller.installIfNeededAsync(this, object : ProviderInstaller.ProviderInstallListener {
            override fun onProviderInstalled() {}
            override fun onProviderInstallFailed(errorCode: Int, recoveryIntent: Intent?) {
            }
        })
    }
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        instance = this
       // MultiDex.install(base)
        MultiDex.install(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        if (AppSharedPref.getCartCount(this) != 0) {
            //   AbandonedCartAlarmHelper.scheduleAlarm(this)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        //  AbandonedCartAlarmHelper.cancelAlarm(this)
    }

    fun getHomePageClass(): Class<*> {
        return DashBoardActivity::class.java
    }

    open fun getDashBoardActivity(context: Context): Intent {
        return Intent(context, DashBoardActivity::class.java)
    }

    /* open fun getLoginBottomSheetHandler(loginBottomSheetFragment: LoginBottomSheetFragment): LoginBottomSheetHandler {
         return LoginBottomSheetHandler(loginBottomSheetFragment)
     }

     open fun getSignUpBottomSheetHandler(signUpBottomSheetFragment: SignUpBottomSheetFragment): SignUpBottomSheetHandler {
         return SignUpBottomSheetHandler(signUpBottomSheetFragment)
     }

     open fun getNavDrawerStartFragment(): NavDrawerStartFragment {
         return NavDrawerStartFragment()
     }*/

    open fun getCustomerListActivity(mContext: Context): Intent? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {

        val attributesCall = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()


        val notificationManager =
            AppController.instance?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        val message =
            NotificationChannel(getString(R.string.notification_channel_id), getString(R.string.notification_channel_name), NotificationManager.IMPORTANCE_HIGH)

        message.setSound(Uri.parse(AppController.callTone), attributesCall)
        notificationManager.createNotificationChannel(message)

    }

}

