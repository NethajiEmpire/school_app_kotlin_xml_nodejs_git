package com.lms.sch.utils

import android.Manifest.permission
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.database.Cursor
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.media.MediaMetadataRetriever
import android.net.ConnectivityManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.Settings
import android.speech.RecognizerIntent
import android.text.Spannable
import android.text.SpannableString
import android.text.format.DateUtils
import android.text.format.Formatter
import android.text.style.ForegroundColorSpan
import android.util.DisplayMetrics
import android.util.Log
import android.util.Patterns
import android.view.Menu
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.lms.sch.BuildConfig
import com.lms.sch.R
import com.lms.sch.activity.*
import com.lms.sch.databinding.CardBottomNavigationBinding
import com.lms.sch.interfaces.DialogCallBack
import com.lms.sch.session.AppSharedPref
import com.lms.sch.session.Constants
import com.lms.sch.session.SharedHelper
import com.lms.sch.session.SharedPref
import com.lms.sch.session.TempSingleton
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.json.JSONObject
import java.io.*
import java.net.URL
import java.net.URLConnection
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.Security
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import javax.crypto.*
import javax.crypto.spec.SecretKeySpec
import java.text.ParseException as ParseException1

@SuppressLint("SimpleDateFormat")
object BaseUtils {
    fun bottomNavigation(includeBottom: CardBottomNavigationBinding,pos:Int) {
        if(pos == 0){
            UiUtils.imageviewDrawable(includeBottom.imgHome,R.drawable.ic_home_selected)
            UiUtils.imageviewDrawable(includeBottom.imgChat,R.drawable.ic_chat)
            UiUtils.imageviewDrawable(includeBottom.imgForum,R.drawable.ic_forum)
            UiUtils.imageviewDrawable(includeBottom.imgSetting,R.drawable.ic_settings)
        }
        else if(pos == 1){
            UiUtils.imageviewDrawable(includeBottom.imgHome,R.drawable.ic_home)
            UiUtils.imageviewDrawable(includeBottom.imgChat,R.drawable.ic_chat_selected)
            UiUtils.imageviewDrawable(includeBottom.imgForum,R.drawable.ic_forum)
            UiUtils.imageviewDrawable(includeBottom.imgSetting,R.drawable.ic_settings)
        }
        else if(pos == 2){
            UiUtils.imageviewDrawable(includeBottom.imgHome,R.drawable.ic_home)
            UiUtils.imageviewDrawable(includeBottom.imgChat,R.drawable.ic_chat)
            UiUtils.imageviewDrawable(includeBottom.imgForum,R.drawable.ic_forum_selected)
            UiUtils.imageviewDrawable(includeBottom.imgSetting,R.drawable.ic_settings)
        }
        else if(pos == 3){
            UiUtils.imageviewDrawable(includeBottom.imgHome,R.drawable.ic_home)
            UiUtils.imageviewDrawable(includeBottom.imgChat,R.drawable.ic_chat)
            UiUtils.imageviewDrawable(includeBottom.imgForum,R.drawable.ic_forum)
            UiUtils.imageviewDrawable(includeBottom.imgSetting,R.drawable.ic_setting_selected)
        }
        includeBottom.imgHome.setOnClickListener {

        }
        includeBottom.imgChat.setOnClickListener {
            UiUtils.showSnack("Coming Soon - Under Working",includeBottom.root)
        }
        includeBottom.imgForum.setOnClickListener {
            UiUtils.showSnack("Coming Soon - Under Working",includeBottom.root)
        }
        includeBottom.imgSetting.setOnClickListener {
            UiUtils.showSnack("Coming Soon - Under Working",includeBottom.root)
        }
    }

    fun logout(context: Context, apiUrl: String){
        TempSingleton.clearAllValues()
        SharedPref(context).clearAll()
        val bundle = Bundle()
        bundle.putInt(Constants.IntentKeys.KEY,0)
        bundle.putString(Constants.IntentKeys.KEY1,apiUrl)
        if(context is BaseActivity){
            startActivity(context as BaseActivity, OtpActivity(),bundle,true)
        }
    }
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun secretKey(): SecretKey? {
        try {
            val keyGenerator = KeyGenerator.getInstance("AES")
            keyGenerator.init(256)
            val secretKey = keyGenerator.generateKey()
            // get base64 encoded version of the key
            val encodedKey: String = Base64.getEncoder().encodeToString(secretKey.encoded)
            val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            val x:String = encodedKey.substring(0,12)+"$hour"+encodedKey.substring(12,encodedKey.length-2)
            //decode the base64 encoded string
            val decodedKey = Base64.getDecoder().decode(x)
            // rebuild key using SecretKeySpec
            val newCustomKey: SecretKey = SecretKeySpec(decodedKey, "AES")
            return  secretKey
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun encrypt(strToEncrypt: String, secret_key: String): String {
        Security.addProvider(BouncyCastleProvider())
        var keyBytes: ByteArray
        try {
            keyBytes = secret_key.toByteArray(charset("UTF8"))
            val skey = SecretKeySpec(keyBytes, "AES")
            val input = strToEncrypt.toByteArray(charset("UTF8"))

            synchronized(Cipher::class.java) {
                val cipher = Cipher.getInstance("AES/ECB/PKCS7Padding")
                cipher.init(Cipher.ENCRYPT_MODE, skey)

                val cipherText = ByteArray(cipher.getOutputSize(input.size))
                var ctLength = cipher.update(
                    input, 0, input.size,
                    cipherText, 0
                )
                ctLength += cipher.doFinal(cipherText, ctLength)
                return String(Base64.getEncoder().encode(cipherText))
            }
        } catch (uee: UnsupportedEncodingException) {
            uee.printStackTrace()
        } catch (ibse: IllegalBlockSizeException) {
            ibse.printStackTrace()
        } catch (bpe: BadPaddingException) {
            bpe.printStackTrace()
        } catch (ike: InvalidKeyException) {
            ike.printStackTrace()
        } catch (nspe: NoSuchPaddingException) {
            nspe.printStackTrace()
        } catch (nsae: NoSuchAlgorithmException) {
            nsae.printStackTrace()
        } catch (e: ShortBufferException) {
            e.printStackTrace()
        }
        return ""
    }

    fun decrypt(strToDecrypt: String?,key: String): String {
        Security.addProvider(BouncyCastleProvider())
        var keyBytes: ByteArray
        try {
            keyBytes = key.toByteArray(charset("UTF8"))
            val skey = SecretKeySpec(keyBytes, "AES")
            val input = org.bouncycastle.util.encoders.Base64
                .decode(strToDecrypt?.trim { it <= ' ' }?.toByteArray(charset("UTF8")))

            synchronized(Cipher::class.java) {
                val cipher = Cipher.getInstance("AES/ECB/PKCS7Padding")
                cipher.init(Cipher.DECRYPT_MODE, skey)

                val plainText = ByteArray(cipher.getOutputSize(input.size))
                var ptLength = cipher.update(input, 0, input.size, plainText, 0)
                ptLength += cipher.doFinal(plainText, ptLength)
                val decryptedString = String(plainText)
                return decryptedString.trim { it <= ' ' }
            }
        } catch (uee: UnsupportedEncodingException) {
            uee.printStackTrace()
        } catch (ibse: IllegalBlockSizeException) {
            ibse.printStackTrace()
        } catch (bpe: BadPaddingException) {
            bpe.printStackTrace()
        } catch (ike: InvalidKeyException) {
            ike.printStackTrace()
        } catch (nspe: NoSuchPaddingException) {
            nspe.printStackTrace()
        } catch (nsae: NoSuchAlgorithmException) {
            nsae.printStackTrace()
        } catch (e: ShortBufferException) {
            e.printStackTrace()
        }
        return ""
    }
    fun getIP(context: Context): String {
        // val context = requireContext().applicationContext
        val wm = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return Formatter.formatIpAddress(wm.connectionInfo.ipAddress)
    }

    fun getDeviceID(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }
     fun getVideoDimension(file:File):String {
        var retriever:MediaMetadataRetriever? = null
        var bmp:Bitmap? = null
         var inputStream:FileInputStream? = null
        var dimen:String = ""
        try {
            retriever = MediaMetadataRetriever()
            inputStream = FileInputStream(file.absolutePath)
            retriever.setDataSource(inputStream.fd)
            bmp = retriever.frameAtTime
            val imageHeight = bmp!!.height
            val imageWidth = bmp!!.width
            val inter = "x"
            dimen = "$imageWidth$inter$imageHeight"
        } catch (e: FileNotFoundException) {
            e.printStackTrace();
        } catch (e:IOException) {
            e.printStackTrace();
        } catch (e:RuntimeException) {
            e.printStackTrace();
        } finally{
            retriever?.release()
            inputStream?.close()
        }
        return dimen
    }
    fun getVideoDimension(filepath:String):String {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(filepath)
        val width = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH))
        val height = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT))
        retriever.release()
        val imageHeight = height
        val imageWidth = width
        val inter = "x"
        val dimen = "$imageWidth$inter$imageHeight"
        return dimen
    }

    fun disableUserInteraction(context: Context) {
            try {
                (context as BaseActivity).window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            } catch (e: Exception) {
            }
        }

        fun enableUserInteraction(context: Context) {
            try {
                (context as BaseActivity).window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            } catch (e: Exception) {
            }
        }

        fun sendRegistrationTokenToServer(context: Context, token: String) {
            AppSharedPref.setFcmToken(context, token)
            // AuthKeyHelper.getInstance().token = token
            SharedHelper(context).fcmToken = token
        }

        private fun isHardKeyboardAvailable(view: View): Boolean {
            return view.context.resources.configuration.keyboard != Configuration.KEYBOARD_NOKEYS
        }

        fun showKeyboard(view: View) {
            view.requestFocus()
            if (!isHardKeyboardAvailable(view)) {
                val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(view, 0)
            }
        }

        fun showForceKeyboard(view: View) {
            view.requestFocus()
            val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        }

        fun hideForceKeyboard(view: View) {
            val inputMethodManager = view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun hideKeyboard(view: View) {
            try {
                val inputManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (view.windowToken != null)
                    inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            } catch (e: Exception) {
            }
        }

        fun hideKeyboard(activity: Activity) {
            try {
                val inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (activity.currentFocus!!.windowToken != null)
                    inputManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            } catch (e: Exception) {
            }
        }

        fun getMd5String(stringToConvert: String): String {
            try {
                // Create MD5 Hash
                val digest = java.security.MessageDigest
                    .getInstance(Constants.ApplicationConstants.API_AUTH_TYPE)
//                        .getInstance("MD5")
                digest.update(stringToConvert.toByteArray())
                val messageDigest = digest.digest()

                // Create Hex String
                val hexString = StringBuilder()
                for (aMessageDigest in messageDigest) {
                    var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
                    while (h.length < 2)
                        h = "0$h"
                    hexString.append(h)
                }
                return hexString.toString()

            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
            return ""
        }

        /* fun showShakeError(context: Context, viewToAnimate: View) {
             try {
                 viewToAnimate.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake_error))
             } catch (e: Resources.NotFoundException) {
                 e.printStackTrace()
             }
         }*/

        fun convertDpToPixel(dp: Float, context: Context): Float {
            val resources = context.resources
            val metrics = resources.displayMetrics
            return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        }

        fun isVoiceAvailable(context: Context): Boolean {
            return context.packageManager.queryIntentActivities(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0).size > 0
        }

        /* fun shareProduct(context: Context, url: String) {
             FirebaseAnalyticsHelper.logShareEvent(url)
             val sendIntent = Intent()
             sendIntent.action = Intent.ACTION_SEND
             sendIntent.putExtra(Intent.EXTRA_TEXT, url)
             sendIntent.type = "text/plain"
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                 context.startActivity(Intent.createChooser(sendIntent, context.getString(R.string.choose_an_action), null))
             } else {
                 context.startActivity(sendIntent)
             }
         }
 */
        fun setSpinnerError(spinner: Spinner, errorString: String) {
            val selectedView = spinner.selectedView
            if (selectedView != null && selectedView is TextView) {
                spinner.requestFocus()
                selectedView.setTextColor(Color.RED)
                selectedView.text = errorString
                spinner.performClick()
            }
        }

        /* fun setBadgeCount(context: Context, icon: LayerDrawable, cartCount: Int) {
             val badge: BadgeDrawable
             // Reuse drawable if possible
             val reuse = icon.findDrawableByLayerId(R.id.ic_menu_badge)
             if (reuse != null && reuse is BadgeDrawable) {
                 badge = reuse
             } else {
                 badge = BadgeDrawable(context)
             }
             badge.setCount(cartCount.toString())
             icon.mutate()
             icon.setDrawableByLayerId(R.id.ic_menu_badge, badge)
         }*/

        fun logoutAndGoToHome(context: Context,activity: Activity) {
            val customerSharedPrefEditor = AppSharedPref.getSharedPreferenceEditor(context, AppSharedPref.CUSTOMER_PREF)
            customerSharedPrefEditor.clear()
            customerSharedPrefEditor.apply()

            val intent = Intent(context, activity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }

        fun generateRandomPassword(): String {
            val random = SecureRandom()
            val letters = "abcdefghjklmnopqrstuvwxyzABCDEFGHJKMNOPQRSTUVWXYZ1234567890"
            val numbers = "1234567890"
            val specialChars = "!@#$%^&*_=+-/"
            var pw = ""
            for (i in 0..7) {
                val index = (random.nextDouble() * letters.length).toInt()
                pw += letters.substring(index, index + 1)
            }
            val indexA = (random.nextDouble() * numbers.length).toInt()
            pw += numbers.substring(indexA, indexA + 1)
            val indexB = (random.nextDouble() * specialChars.length).toInt()
            pw += specialChars.substring(indexB, indexB + 1)
            return pw
        }


        fun isValidEmailId(email: String): Boolean {
            return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches()
        }

        fun formatDate(dateFormat: String?, year: Int, month: Int, day: Int): String {
            val cal = Calendar.getInstance()
            cal.timeInMillis = 0
            cal.set(year, month, day)
            val date = cal.time
            val sdf = SimpleDateFormat(dateFormat ?: Constants.ConstantsHelper.DEFAULT_DATE_FORMAT, Locale.getDefault())
            return sdf.format(date)
        }

        fun generateRandomId(): String {
            val random = SecureRandom()
            val letters = "abcdefghjklmnopqrstuvwxyzABCDEFGHJKMNOPQRSTUVWXYZ1234567890"
            val numbers = "1234567890"
            var pw = ""
            for (i in 0..7) {
                val index = (random.nextDouble() * letters.length).toInt()
                pw += letters.substring(index, index + 1)
            }
            val indexA = (random.nextDouble() * numbers.length).toInt()
            pw += numbers.substring(indexA, indexA + 1)
            return pw
        }

        fun validateUrlForSpecialCharacter(url: String): Boolean {
            if (url.contains(" "))
                return false
            return url.matches("[A-Za-z0-9^.-]*".toRegex())
        }

        fun isValidPhone(phone: String): Boolean {
            val PHONE_PATTERN = "^(91)?[6-9][0-9]{9}$"
            val pattern = Pattern.compile(PHONE_PATTERN)
            val matcher = pattern.matcher(phone)
            return matcher.matches()
        }

        @JvmStatic
        fun getVersionName(ctx: Context): String {
            return try {
                ctx.packageManager.getPackageInfo(ctx.packageName, 0).versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                "1.0"
            }
        }

        fun setToolbarThemeColor(toolbar: Toolbar?) {

            if (Constants.ApplicationConstants.ENABLE_DYNAMIC_THEME_COLOR && toolbar !=null) {

                if (AppSharedPref.getAppThemeColor(toolbar.context).isNotEmpty()) {
                    toolbar.background = ColorDrawable(Color.parseColor(AppSharedPref.getAppThemeColor(toolbar.context)))
                } else {
                    toolbar.setBackgroundColor(ContextCompat.getColor(toolbar.context, R.color.colorPrimary))
                }
                val foreground = if (AppSharedPref.getAppThemeTextColor(toolbar.context).isNotEmpty()) {
                    Color.parseColor(AppSharedPref.getAppThemeTextColor(toolbar.context))
                } else {
                    ContextCompat.getColor(toolbar.context, R.color.colorPrimary)
                }
                toolbar.setTitleTextColor(foreground)
                toolbar.setSubtitleTextColor(foreground)
                val colorFilter = PorterDuffColorFilter(foreground, PorterDuff.Mode.MULTIPLY)

                // Overflow icon
                val overflowIcon: Drawable? = toolbar.overflowIcon
                if (overflowIcon != null) {
                    overflowIcon.colorFilter = colorFilter
                    toolbar.overflowIcon = overflowIcon
                }

                //Overflow navigation icon
                val navigationIcon: Drawable? = toolbar.navigationIcon
                if (navigationIcon != null) {
                    navigationIcon.colorFilter = colorFilter
                    toolbar.navigationIcon = navigationIcon
                }

            }
        }

        fun setActionBarThemeColor(actionBar: androidx.appcompat.app.ActionBar?, context: Context){

            if (Constants.ApplicationConstants.ENABLE_DYNAMIC_THEME_COLOR && actionBar != null) {

                val backgroundColor= if (AppSharedPref.getAppThemeColor(context).isNotEmpty()) {
                    Color.parseColor(AppSharedPref.getAppThemeColor(context))
                } else {
                    ContextCompat.getColor(context, R.color.colorPrimary)
                }

                val foregroundColor = if (AppSharedPref.getAppThemeTextColor(context).isNotEmpty()) {
                    Color.parseColor(AppSharedPref.getAppThemeTextColor(context))
                } else {
                    ContextCompat.getColor(context, R.color.colorPrimary)
                }

                actionBar.apply {
                    val text = SpannableString(title ?: "")
                    text.setSpan(ForegroundColorSpan(foregroundColor),0,text.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                    val upArrow =ContextCompat.getDrawable(context, R.drawable.ic_back_arrow)
                    upArrow?.colorFilter= PorterDuffColorFilter(foregroundColor, PorterDuff.Mode.SRC_ATOP)
                    title = text
                    setBackgroundDrawable(ColorDrawable(backgroundColor))
                    setHomeAsUpIndicator(upArrow)
                }

            }
        }


        fun setMenuItemIconColor(menu: Menu?, context: Context) {
            if (Constants.ApplicationConstants.ENABLE_DYNAMIC_THEME_COLOR && menu!=null) {

                val foregroundColor = if (AppSharedPref.getAppThemeTextColor(context).isNotEmpty()) {
                    Color.parseColor(AppSharedPref.getAppThemeTextColor(context))
                } else {
                    ContextCompat.getColor(context, R.color.colorPrimary)
                }
                try {
                    val iconTint = PorterDuffColorFilter(foregroundColor, PorterDuff.Mode.SRC_ATOP)
                    for (i in 0 until menu.size()) {
                        val drawable = menu.getItem(i).icon
                        drawable?.apply {
                            mutate()
                            drawable.colorFilter = iconTint
                        }

                    }

                } catch (e: Exception) {
                    e.printStackTrace();
                }
            }
        }

        fun setDrawerIconColor(mDrawerToggle: ActionBarDrawerToggle?, context: Context) {
            if (Constants.ApplicationConstants.ENABLE_DYNAMIC_THEME_COLOR && AppSharedPref.getAppThemeTextColor(context).isNotEmpty()) {
                mDrawerToggle?.drawerArrowDrawable?.color = Color.parseColor(AppSharedPref.getAppThemeTextColor(context))
            } else {
                mDrawerToggle?.drawerArrowDrawable?.color = ContextCompat.getColor(context, R.color.colorPrimary)
            }
        }

        /* fun lottieTint(view: LottieAnimationView?) {
             if (view != null) {
                 if (AppSharedPref.getAppBgButtonColor(view?.context).isNotEmpty()) {
                     view?.addValueCallback(
                             KeyPath("**"),
                             LottieProperty.COLOR_FILTER,
                             { PorterDuffColorFilter(ColorUtils.setAlphaComponent(Color.parseColor(AppSharedPref.getAppBgButtonColor(view?.context)), 180), PorterDuff.Mode.SRC_ATOP) }
                     )

                 }
             }
         }*/

    fun urlParser(url:String):String{
        val u = URL(url)
       /* println("The scheme is " + u.protocol)
        println("The user info is " + u.userInfo)
        println("The port is " + u.port)
        println("The path is " + u.path)
        println("The path is " + u.path.split("/"))
        println("The path is " + u.path.split("/").last())
        println("The path is " + u.path.split("/").last().removeSuffix(".glb"))
        println("The ref is " + u.ref)
        println("The query string is " + u.query)
        var host = u.host
        if (host != null) {
            val atSign = host.indexOf('@')
            if (atSign != -1) host = host.substring(atSign + 1)
            println("The host is $host")
        } else {
            println("The host is null.")
        }*/
        return u.path.split("/").last().removeSuffix(".glb")
    }
    fun loginSuccess(activity: Activity){
        SharedHelper(activity).isOrganization = false
        SharedHelper(activity).isPersonal = true
        SharedHelper(activity).loggedIn = true
        SharedHelper(activity).isActive = Constants.SessionKeys.IS_PERSONAL
        startDashboardActivity(activity, true, null)
    }

    fun openPlayStore(context: Context){
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://play.google.com/store/apps/details?id="+context.packageName)
       // intent.data = Uri.parse("https://play.google.com/store/apps/developer?id=Piechips")
        context.startActivity(intent)
    }

    fun isUpdateAvailable(latestVersion:String):Boolean{
        val currentVersion = BuildConfig.VERSION_NAME
        if(java.lang.Float.valueOf(currentVersion) < java.lang.Float.valueOf(latestVersion)) {
            return true
        }
        return false
    }

    fun isContainsUpper(value: String):Boolean{
        val upperCasePatten = Pattern.compile("[A-Z ]")
        if (upperCasePatten.matcher(value).find()) {
            return true
        }
        return false
    }

    fun isContainsLower(value: String):Boolean{
        val lowerCasePatten = Pattern.compile("[a-z ]")
        if (lowerCasePatten.matcher(value).find()) {
            return true
        }
        return false
    }

    fun isContainsChar(value: String):Boolean{
        val specialChars = "!@#$%&*()'+,-/:;<=>?[]^_`{|}"
        for (i in value) {
            if (specialChars.contains(i)) {
                return true
            }
        }
        return false
    }
    fun nullCheckerStr(value : String?):String{
        if(value == null || value == "" || value == Constants.IntentKeys.NULL || value == Constants.IntentKeys.UNDEFINED || value.isEmpty() || value == "0" || value == "0.00"){
            return ""
        }
        return value
    }

    fun nullCheckerInt(value : Int?):Int{
        if(value == null || value == 0 || value.toDouble() == 0.0){
            return 0
        }
        return value
    }

    fun nullCheckerBoolean(value : Boolean?):Boolean{
        if(value == null){
            return false
        }
        return value
    }

    fun jsonChecker(json:JSONObject):JSONObject{
        val keys: MutableIterator<String> = json.keys()
        while (keys.hasNext()){
            val key = keys.next()
//            if(json.isNull(key) || json.get(key).equals("null")){
//                json.put(key,"")
//            }

            if(json.isNull(key) || json.get(key).equals("1.5")){
                json.put(key,"1.6")
            }
        }
        return json
    }

    fun closeKeyBoard(editText: EditText, context: Context){
        editText.clearFocus()
        val input = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        input!!.hideSoftInputFromWindow(editText.windowToken, 0)
    }
    fun getCurrentDate(outputFormat: String): String? {
        val dateFormat = SimpleDateFormat(outputFormat)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val today = Calendar.getInstance().time
        return dateFormat.format(today)
    }
    fun clearActivity(from: Activity, to :Activity, bundle: Bundle?){
        val send = Intent(from, to::class.java)
        send.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        if(bundle != null){
            send.putExtras(bundle)
            from.startActivity(send)
            from.overridePendingTransition(R.anim.screen_in, R.anim.screen_out);
        }
        else{
            from.startActivity(send)
            from.overridePendingTransition(R.anim.screen_in, R.anim.screen_out);
        }
        from.finish()
    }
    fun startActivity(from: Activity, to :Activity, bundle: Bundle?, isfinish:Boolean){
        val send = Intent(from, to::class.java)
        if(bundle != null){
            send.putExtras(bundle)
            from.startActivity(send)
            from.overridePendingTransition(R.anim.screen_in, R.anim.screen_out);
        }
        else{
            from.startActivity(send)
            from.overridePendingTransition(R.anim.screen_in, R.anim.screen_out);
        }

        if(isfinish){
            from.finish()
        }
    }
    fun startDashboardActivity(activity: Activity,finish: Boolean,bundle: Bundle?){
        TempSingleton.clearAllValues()
        if(bundle != null){
            activity.startActivity(Intent(activity, DashBoardActivity::class.java)
                .putExtras(bundle)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
        }
        else{
            activity.startActivity(Intent(activity, DashBoardActivity::class.java))
        }
        if(finish){
            activity.finish()
        }
    }
    fun isArray(x: Any): Boolean {
        return x.toString().contains("[") && x.toString().contains("]")
    }

    fun getImageDimensionRatio(uri: Uri): String {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(File(uri.path).absolutePath, options)
        val imageHeight = options.outHeight
        val imageWidth = options.outWidth
        val inter = "x"
        return "$imageWidth$inter$imageHeight"
    }

    fun getImageDimensionRatio(path: String):String {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(File(path).absolutePath, options)
        val imageHeight = options.outHeight
        val imageWidth = options.outWidth
        val inter = "x"
        return "$imageWidth$inter$imageHeight"
    }

    fun isPastDateTime(givenDateTime: String,inputFormat: String): Boolean {
        val simpleDateFormat = SimpleDateFormat(inputFormat, Locale.ENGLISH)
        var date1 = simpleDateFormat.parse(givenDateTime)
        var date2 = simpleDateFormat.parse(SimpleDateFormat(inputFormat, Locale.ENGLISH).format(Date()))
        if (date1 != null && date2 != null) {
            val difference = date1.time - date2.time
            val days = difference / (1000 * 60 * 60 * 24)
            val hours = difference / (1000 * 60 * 60)
            val mins = difference / (1000 * 60) % 60
//            var hours = ((difference - 1000 * 60 * 60 * 24 * days) / (1000 * 60 * 60))
//            val min = (difference - 1000 * 60 * 60 * 24 * days - 1000 * 60 * 60 * hours) / (1000 * 60)
            if(days < 0){
                return true
            }
            else if (hours < 0) {
                return true
            }
            else {
                if (hours > 0) {
                    return false
                }
                return mins <= 0
            }
        }
        else {
            return true
        }
    }

    fun getTimeAfter(date: String, inputFormat: String): String {
        // Date Calculation
        val dateFormat: DateFormat = SimpleDateFormat(inputFormat,Locale.ENGLISH)
        dateFormat.timeZone = TimeZone.getTimeZone("GMT")
        // get current date time with Calendar()
        val currenttime: String = dateFormat.format(Calendar.getInstance().time)
        var createdAt: Date? = null
        var current: Date? = null
        try {
            createdAt = dateFormat.parse(date)
            current = dateFormat.parse(currenttime)
        } catch (e: java.text.ParseException) {
            e.printStackTrace()
        }

        // Get msec from each, and subtract.
        val diff = createdAt!!.time - current!!.time
        val diffSeconds = diff / 1000
        val diffMinutes = diff / (60 * 1000) % 60
        val diffHours = diff / (60 * 60 * 1000) % 24
        val diffDays = diff / (24 * 60 * 60 * 1000)
        var time = ""
        if (diffDays > 0) {
            time = if (diffDays == 1L) {
                diffDays.toString() + "day"
            } else {
                diffDays.toString() + "days"
            }
        }
        else {
            if (diffHours > 0) {
                time = if (diffHours == 1L) {
                    diffHours.toString() + "hr"
                } else {
                    diffHours.toString() + "hrs"
                }
            }
            else {
                if (diffMinutes > 0) {
                    time = if (diffMinutes == 1L) {
                        diffMinutes.toString() + "min"
                    } else {
                        diffMinutes.toString() + "mins"
                    }
                }
                else {
                    if (diffSeconds > 0) {
                        time = diffSeconds.toString() + "secs"
                    }
                    else{
                        time = "1sec"
                    }
                }
            }
        }
        return time
    }
    fun convertBytes(bytes: Long): String {
        val kilobytes = bytes / 1024.0
        val megabytes = kilobytes / 1024.0

        return when {
            bytes >= 1024 * 1024 -> String.format("%.2f MB", megabytes)
            bytes >= 1024 -> String.format("%.2f KB", kilobytes)
            else -> String.format("%d Bytes", bytes)
        }
    }
    fun getTimeAgoNew(date: String, inputFormat: String): String {
        // Date Calculation
        val dateFormat: DateFormat = SimpleDateFormat(inputFormat,Locale.ENGLISH)
        dateFormat.timeZone = TimeZone.getTimeZone("GMT")
        // get current date time with Calendar()
        val currenttime: String = dateFormat.format(Calendar.getInstance().time)
        var createdAt: Date? = null
        var current: Date? = null
        try {
            createdAt = dateFormat.parse(date)
            current = dateFormat.parse(currenttime)
        } catch (e: java.text.ParseException) {
            e.printStackTrace()
        }

        // Get msec from each, and subtract.
        val diff = current!!.time - createdAt!!.time
        val diffSeconds = diff / 1000
        val diffMinutes = diff / (60 * 1000) % 60
        val diffHours = diff / (60 * 60 * 1000) % 24
        val diffDays = diff / (24 * 60 * 60 * 1000)
        var time = ""
        if (diffDays > 0) {
            time = if (diffDays == 1L) {
                diffDays.toString() + "day"
            } else {
                diffDays.toString() + "days"
            }
        }
        else {
            if (diffHours > 0) {
                time = if (diffHours == 1L) {
                    diffHours.toString() + "hr"
                } else {
                    diffHours.toString() + "hrs"
                }
            }
            else {
                if (diffMinutes > 0) {
                    time = if (diffMinutes == 1L) {
                        diffMinutes.toString() + "min"
                    } else {
                        diffMinutes.toString() + "mins"
                    }
                }
                else {
                    if (diffSeconds > 0) {
                        time = diffSeconds.toString() + "secs"
                    }
                    else{
                        time = "1sec"
                    }
                }
            }
        }
        return time
    }
    fun getTimeAgo(date: String, inputFormat: String): String{
        val sdf = SimpleDateFormat(inputFormat, Locale.ENGLISH)
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        try {
            val time = sdf.parse(date)!!.time
            val now = System.currentTimeMillis()
            val ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.SECOND_IN_MILLIS)
            return ago.toString()
        } catch (e: ParseException1) {
            e.printStackTrace()
        }
        return ""
    }
    fun getTimeAgoTrim(date: String, inputFormat: String): String{
        val sdf = SimpleDateFormat(inputFormat, Locale.ENGLISH)
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        try {
            val time = sdf.parse(date)!!.time
            val now = System.currentTimeMillis()
            val ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.SECOND_IN_MILLIS)
            return trimStr(ago.toString())
        } catch (e: ParseException1) {
            e.printStackTrace()
        }
        return ""
    }
    private fun trimStr(txt:String):String{
        /*var str = ""
        val splitStr = txt.split("\\s+".toRegex()).toTypedArray()
        if(txt.contains("sec")){
            if(txt.contains("In")){
                str = "1Sec"
            }
            else{
                str = splitStr[0]+"Sec"
            }
        }
        else if(txt.contains("min")){
            if(txt.contains("In")){
                str = "1Min"
            }
            else{
                str = splitStr[0]+"Min"
            }
        }
        else if(txt.contains("hour")){
            str = splitStr[0]+"Hrs"
        }
        else if(txt.contains("day")){
            if(txt.contains("Yesterday")){
                str = "1Day"
            }
            else{
                str = splitStr[0]+"Day"
            }
        }
        else if(txt.contains("week")){
            str = splitStr[0]+"Week"
        }
        else if(txt.contains("month")){
            str = splitStr[0]+"Month"
        }
        else if(txt.contains("year")){
            str = splitStr[0]+"Year"
        }
        else{
            str = txt
        }*/
        return txt
    }
    fun convertSeconds(duration: Int): String {
        val hours = duration / 60
        val minutes = duration % 60
        val hourText = if (hours == 1) "hr" else "hrs"
        return if (minutes == 0) {
            "$hours $hourText"
        } else {
            "$hours $hourText $minutes mins"
        }
    }

    fun formatTime(startTime: String): String {
        if (startTime == null || startTime.isEmpty()) {
            return ""
        }
        val inputFormat = SimpleDateFormat("H:mm", Locale.getDefault())
        val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val date = inputFormat.parse(startTime)
        return outputFormat.format(date!!)
    }

    fun daysLeft(inputDate: String): String {
        if (inputDate == null || inputDate.isEmpty()) {
            return ""
        }
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")

        return try {
            val targetDate = inputFormat.parse(inputDate)?.time ?: 0L
            val currentDate = Calendar.getInstance().timeInMillis
            val differenceInDays = ((targetDate - currentDate) / (1000 * 60 * 60 * 24)).toInt()

            when {
                differenceInDays > 1 -> "$differenceInDays days left"
                differenceInDays == 1 -> "1 day left"
                differenceInDays == 0 -> "Last day(Today)"
                else -> "Days over"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
    fun getFormattedDate(date: String, inputFormat: String, outputFormat: String): String {
        var spf = SimpleDateFormat(inputFormat, Locale.ENGLISH)
        var newDate: Date? = null
        try {
            newDate = spf.parse(date)
        } catch (e: ParseException1) {
            e.printStackTrace()
        }


        spf = SimpleDateFormat(outputFormat, Locale.ENGLISH)
        return if (newDate != null)
            spf.format(newDate)
        else
            ""

    }
    fun getFormattedDateUtc(date: String, inputFormat: String?, outputFormat: String?): String? {
        var spf = SimpleDateFormat(inputFormat, Locale.ENGLISH)
        spf.timeZone = TimeZone.getTimeZone("UTC")
        var newDate: Date? = null
        try {
            newDate = spf.parse(date)
        } catch (e: java.text.ParseException) {
            e.printStackTrace()
        }
        spf = SimpleDateFormat(outputFormat, Locale.ENGLISH)
        return spf.format(newDate!!)
    }
//    fun openCamera(activity: Activity) {
//        /* val sharedHelper = SharedHelper(activity)
//         val file = getFileTostoreImage(activity)
//         val uri: Uri*/
//        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
//            // Ensure that there's a camera activity to handle the intent
//            takePictureIntent.resolveActivity(activity.packageManager)?.also {
//                // Create the File where the photo should go
//                val photoFile: File? = try {
//                    getFileToStoreImage(activity,"jpg")
//                } catch (ex: IOException) {
//                    // Error occurred while creating the File
//                    null
//                }
//                // Continue only if the File was successfully created
//                photoFile?.also {
//                    val photoURI: Uri = FileProvider.getUriForFile(
//                        activity,
//                        activity.packageName+".fileprovider",
//                        it
//                    )
//                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//                    val chatActivity:ChatActivity = activity as ChatActivity
//                    chatActivity.requestCode = Constants.RequestCode.CAMERA_REQUEST
//                    chatActivity.resultLauncher.launch(takePictureIntent)
//                    //activity.startActivityForResult(takePictureIntent, Constants.RequestCode.CAMERA_REQUEST)
//                }
//            }
//        }
//
//    }
//    fun openVideoCamera(activity: Activity) {
//        /* val sharedHelper = SharedHelper(activity)
//        val file = getFileTostoreImage(activity)
//        val uri: Uri*/
//        Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { takePictureIntent ->
//            // Ensure that there's a camera activity to handle the intent
//            takePictureIntent.resolveActivity(activity.packageManager)?.also {
//                // Create the File where the photo should go
//                val photoFile: File? = try {
//                    getFileToStoreImage(activity,"mp4")
//                } catch (ex: IOException) {
//                    // Error occurred while creating the File
//                    null
//                }
//                // Continue only if the File was successfully created
//                photoFile?.also {
//                    val photoURI: Uri = FileProvider.getUriForFile(
//                        activity,
//                        activity.packageName+".fileprovider",
//                        it
//                    )
//                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//                    val chatActivity:ChatActivity = activity as ChatActivity
//                    chatActivity.requestCode = Constants.RequestCode.VIDEO_REQUEST
//                    chatActivity.resultLauncher.launch(takePictureIntent)
//                   // activity.startActivityForResult(takePictureIntent, Constants.RequestCode.VIDEO_REQUEST)
//                }
//            }
//        }
//    }
//    fun openGallery(activity: Activity) {
//        var i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        if(TempSingleton.getInstance().isMultiple){
//            if(TempSingleton.getInstance().isImage){
//                i = Intent(Intent.ACTION_PICK)
//                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//                i.type = "image/*"
//            }
//            else if(TempSingleton.getInstance().isVideo){
//                i = Intent(Intent.ACTION_PICK)
//                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//                i.type = "video/*"
//            }
//            else if(TempSingleton.getInstance().isFile){
//                i = Intent(Intent.ACTION_PICK)
//                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//                i.type = "image/* video/*"
//            }
//        }
//        else{
//            if(TempSingleton.getInstance().isImage){
//                i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            }
//            else if(TempSingleton.getInstance().isVideo){
//                i = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
//            }
//            else if(TempSingleton.getInstance().isFile){
//                i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//                i.type = "image/* video/*"
//            }
//        }
//
//        val chatActivity:ChatActivity = activity as ChatActivity
//        chatActivity.requestCode = Constants.RequestCode.GALLERY_REQUEST
//        chatActivity.resultLauncher.launch(i)
//       // activity.startActivityForResult(i, Constants.RequestCode.GALLERY_REQUEST)
//    }
//    fun openGalleryFileManager(activity: Activity) {
//        var i = Intent(Intent.ACTION_GET_CONTENT)
//        i.addCategory(Intent.CATEGORY_OPENABLE)
//        if(TempSingleton.getInstance().isMultiple){
//            if(TempSingleton.getInstance().isImage){
//                i = Intent(Intent.ACTION_GET_CONTENT)
//                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//                i.type = "image/*"
//            }
//            else if(TempSingleton.getInstance().isVideo){
//                i = Intent(Intent.ACTION_GET_CONTENT)
//                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//                i.type = "video/*"
//            }
//            else if(TempSingleton.getInstance().isFile){
//                i = Intent(Intent.ACTION_OPEN_DOCUMENT)
//                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//                i.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*", "video/*"))
//                i.type = "image/* video/*"
//            }
//        }
//        else{
//            if(TempSingleton.getInstance().isImage){
//                i = Intent(Intent.ACTION_GET_CONTENT)
//                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
//                i.type = "image/*"
//            }
//            else if(TempSingleton.getInstance().isVideo){
//                i = Intent(Intent.ACTION_GET_CONTENT)
//                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
//                i.type = "video/*"
//            }
//            else if(TempSingleton.getInstance().isFile){
//                i = Intent(Intent.ACTION_OPEN_DOCUMENT)
//                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
//                i.type = "image/* video/*"
//               // i.type = "*/*"
//            }
//        }
//
//        val chatActivity:ChatActivity = activity as ChatActivity
//        chatActivity.requestCode = Constants.RequestCode.GALLERY_REQUEST
//        chatActivity.resultLauncher.launch(i)
//        //activity.startActivityForResult(i, Constants.RequestCode.GALLERY_REQUEST)
//    }
    fun getFileTypeFromUri(uri: Uri,context: Context):String{
        return context.contentResolver.getType(uri).toString()
    }
    fun getFileTypeFromPath(path: String): String {
        return URLConnection.guessContentTypeFromName(path)
    }
    private fun getFileToStoreImage(activity: Activity,type:String): File {
        val sharedHelper = SharedHelper(activity)
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".$type", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            sharedHelper.imageUploadPath = absolutePath
        }
    }
    @SuppressLint("NewApi")
    fun getRealPathFromUriNew(context: Context, uri: Uri): String? {
        Log.d("dvdf",""+DocumentsContract.isDocumentUri(context,uri))
        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            Log.d("hgv",""+ isExternalStorageDocument(uri)+".."+ isDownloadsDocument(uri)+".."+ isMediaDocument(uri))
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }

                //  handle non-primary volumes
            }
            else if (isDownloadsDocument(uri)) {
                val fileName: String? = getFilePath(context, uri)
                if (fileName != null) {
                    return Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName
                }

                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
                )

                return getDataColumn(context, contentUri, null, null)
            }
            else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                var contentUri: Uri? = null
                when (type) {
                    "image" -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    "video" -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    "audio" -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    "document" -> contentUri = MediaStore.Files.getContentUri("external")
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])

                return getDataColumn(context, contentUri, selection, selectionArgs)
            }// MediaProvider
            // DownloadsProvider
        }
        else if ("content".equals(uri.scheme!!, ignoreCase = true)) {
            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                context,
                uri,
                null,
                null
            )

        }
        else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
            return uri.path
        }// File
        // MediaStore (and general)
        return null
    }

    fun getFilePath(context: Context, uri: Uri): String? {
        var cursor: Cursor? = null
        val projection = arrayOf(MediaStore.MediaColumns.DISPLAY_NAME)
        try {
            cursor = context.contentResolver.query(uri, projection, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
                return cursor.getString(index)
            }
        }
        finally {
            cursor?.close()
        }
        return null
    }

    private fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {

        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)

        try {
            cursor =
                context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }
    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }
    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }
    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }
    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }
    fun getRealPathFromURI(context: Context, contentURI: Uri): String? {
        val result: String?
        val filePathColumn = arrayOf(MediaStore.Images.Media._ID)
        val cursor = context.contentResolver.query(contentURI, filePathColumn, null, null, null)

        if (cursor == null) {
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx = cursor
                .getColumnIndex(filePathColumn[0])
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }
    @SuppressLint("InlinedApi")
    fun isPermissionsEnabled(context: Context, name:String): Boolean {
        val result1 = ContextCompat.checkSelfPermission(context, permission.ACCESS_FINE_LOCATION)
        val result2 = ContextCompat.checkSelfPermission(context, permission.CAMERA)
        val result3 = ContextCompat.checkSelfPermission(context, permission.WRITE_EXTERNAL_STORAGE)
        val result4 = ContextCompat.checkSelfPermission(context, permission.READ_EXTERNAL_STORAGE)
        val result41 = ContextCompat.checkSelfPermission(context, permission.READ_MEDIA_IMAGES)
        val result42 = ContextCompat.checkSelfPermission(context, permission.READ_MEDIA_VIDEO)
        val result43 = ContextCompat.checkSelfPermission(context, permission.READ_MEDIA_AUDIO)
        val result5 = ContextCompat.checkSelfPermission(context, permission.CALL_PHONE)
        val result7 = ContextCompat.checkSelfPermission(context, permission.READ_PHONE_NUMBERS)
        val result8 = ContextCompat.checkSelfPermission(context, permission.READ_PHONE_STATE)
        val result9 = ContextCompat.checkSelfPermission(context, permission.READ_CONTACTS)
        val result10 = ContextCompat.checkSelfPermission(context, permission.RECORD_AUDIO)
        val result11 = ContextCompat.checkSelfPermission(context, permission.READ_SMS)
        val result12 = ContextCompat.checkSelfPermission(context, permission.RECEIVE_SMS)
        val result13 = ContextCompat.checkSelfPermission(context, permission.SEND_SMS)
        return when (name) {
            Constants.IntentKeys.LOCATION -> {
                result1 == PackageManager.PERMISSION_GRANTED
            }
            Constants.IntentKeys.CAMERA -> {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    result2 == PackageManager.PERMISSION_GRANTED && result41 == PackageManager.PERMISSION_GRANTED && result42 == PackageManager.PERMISSION_GRANTED
                }
                else{
                    result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED && result4 == PackageManager.PERMISSION_GRANTED
                }
            }
            Constants.IntentKeys.GALLERY -> {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    result41 == PackageManager.PERMISSION_GRANTED && result42 == PackageManager.PERMISSION_GRANTED
                }
                else{
                    result4 == PackageManager.PERMISSION_GRANTED
                }
            }
            Constants.IntentKeys.STORAGE -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    // Log.e("dvndf",""+result3+".."+result4+".."+result41+".."+result42)
                    result43 == PackageManager.PERMISSION_GRANTED && result41 == PackageManager.PERMISSION_GRANTED && result42 == PackageManager.PERMISSION_GRANTED
                }
                else {
                    result3 == PackageManager.PERMISSION_GRANTED && result4 == PackageManager.PERMISSION_GRANTED
                }
            }
            Constants.IntentKeys.PHONE -> {
                result5 == PackageManager.PERMISSION_GRANTED
            }
            Constants.IntentKeys.CONTACTS -> {
                result9 == PackageManager.PERMISSION_GRANTED
            }
            Constants.IntentKeys.MIC -> {
                result10 == PackageManager.PERMISSION_GRANTED
            }
            Constants.IntentKeys.SMS -> {
                result11 == PackageManager.PERMISSION_GRANTED && result12 == PackageManager.PERMISSION_GRANTED && result13 == PackageManager.PERMISSION_GRANTED
            }
            else -> {
                result1 == PackageManager.PERMISSION_GRANTED &&
                        result2 == PackageManager.PERMISSION_GRANTED &&
                        result3 == PackageManager.PERMISSION_GRANTED &&
                        result4 == PackageManager.PERMISSION_GRANTED &&
                        result5 == PackageManager.PERMISSION_GRANTED &&
                        result7 == PackageManager.PERMISSION_GRANTED &&
                        result8 == PackageManager.PERMISSION_GRANTED &&
                        result9 == PackageManager.PERMISSION_GRANTED &&
                        result10 == PackageManager.PERMISSION_GRANTED &&
                        result11 == PackageManager.PERMISSION_GRANTED &&
                        result12 == PackageManager.PERMISSION_GRANTED &&
                        result13 == PackageManager.PERMISSION_GRANTED
            }
        }
    }
    fun isDeniedPermission(activity: Activity, name: String):Boolean{
        val result1 = activity.shouldShowRequestPermissionRationale(permission.ACCESS_FINE_LOCATION)
        val result2 = activity.shouldShowRequestPermissionRationale(permission.CAMERA)
        val result3 = activity.shouldShowRequestPermissionRationale(permission.WRITE_EXTERNAL_STORAGE)
        val result4 = activity.shouldShowRequestPermissionRationale(permission.READ_EXTERNAL_STORAGE)
        val result41 = activity.shouldShowRequestPermissionRationale(permission.READ_MEDIA_IMAGES)
        val result42 = activity.shouldShowRequestPermissionRationale(permission.READ_MEDIA_VIDEO)
        val result43 = activity.shouldShowRequestPermissionRationale(permission.READ_MEDIA_AUDIO)
        val result5 = activity.shouldShowRequestPermissionRationale(permission.CALL_PHONE)
        val result7 = activity.shouldShowRequestPermissionRationale(permission.READ_PHONE_NUMBERS)
        val result8 = activity.shouldShowRequestPermissionRationale(permission.READ_PHONE_STATE)
        val result9 = activity.shouldShowRequestPermissionRationale(permission.READ_CONTACTS)
        val result10 = activity.shouldShowRequestPermissionRationale(permission.RECORD_AUDIO)
        val result11 = activity.shouldShowRequestPermissionRationale(permission.READ_SMS)
        val result12 = activity.shouldShowRequestPermissionRationale(permission.RECEIVE_SMS)
        val result13 = activity.shouldShowRequestPermissionRationale(permission.SEND_SMS)

        return when (name) {
            Constants.IntentKeys.LOCATION -> {
                result1
            }
            Constants.IntentKeys.CAMERA -> {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    result2 && result41 && result42
                }
                else{
                    result2 && result3 && result4
                }
            }
            Constants.IntentKeys.GALLERY -> {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    result41 && result42
                }
                else{
                    result4
                }
            }
            Constants.IntentKeys.STORAGE -> {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    result41 && result42 && result43
                }
                else{
                    result3 && result4
                }
            }
            Constants.IntentKeys.PHONE -> {
                result5
            }
            Constants.IntentKeys.CONTACTS -> {
                result9
            }
            Constants.IntentKeys.MIC -> {
                result10
            }
            Constants.IntentKeys.SMS -> {
                result11 && result12 && result13
            }
            else -> {
                result1 || result2 || result3 || result5 || result7 || result8 || result9 || result10 || result11 || result12 || result13
            }
        }
    }
    fun permissionsEnableRequest(activity: Activity,name: String) {
        when (name) {
            Constants.IntentKeys.LOCATION -> {
                ActivityCompat.requestPermissions(activity,
                    Constants.Permission.LOCATION_LIST,
                    Constants.RequestCode.LOCATION_REQUEST
                )
            }
            Constants.IntentKeys.CAMERA -> {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    ActivityCompat.requestPermissions(activity,
                        Constants.Permission.CAMERA_LIST_NEW,
                        Constants.RequestCode.CAMERA_REQUEST
                    )
                    /* if (!Environment.isExternalStorageManager()) {
                         val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                         val uri = Uri.fromParts("package", activity.packageName, null)
                         intent.setData(uri)
                         activity.startActivity(intent)
                     }*/
                }
                else{
                    ActivityCompat.requestPermissions(activity,
                        Constants.Permission.CAMERA_LIST,
                        Constants.RequestCode.CAMERA_REQUEST
                    )
                }
            }
            Constants.IntentKeys.GALLERY -> {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    ActivityCompat.requestPermissions(activity,
                        Constants.Permission.GALLERY_LIST_NEW,
                        Constants.RequestCode.GALLERY_REQUEST
                    )
                }
                else{
                    ActivityCompat.requestPermissions(activity,
                        Constants.Permission.GALLERY_LIST,
                        Constants.RequestCode.GALLERY_REQUEST
                    )
                }
            }
            Constants.IntentKeys.STORAGE -> {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    ActivityCompat.requestPermissions(activity,
                        Constants.Permission.STORAGE_LIST_NEW,
                        Constants.RequestCode.STORAGE_REQUEST
                    )
                }
                else{
                    ActivityCompat.requestPermissions(activity,
                        Constants.Permission.STORAGE_LIST,
                        Constants.RequestCode.STORAGE_REQUEST
                    )
                }
            }
            Constants.IntentKeys.PHONE -> {
                ActivityCompat.requestPermissions(activity,
                    Constants.Permission.PHONE_LIST,
                    Constants.RequestCode.PHONE_REQUEST
                )
            }
            Constants.IntentKeys.CONTACTS -> {
                ActivityCompat.requestPermissions(activity,
                    Constants.Permission.CONTACT_LIST,
                    Constants.RequestCode.CONTACT_REQUEST)
            }
            Constants.IntentKeys.MIC -> {
                ActivityCompat.requestPermissions(activity,
                    Constants.Permission.MIC_LIST,
                    Constants.RequestCode.MIC_REQUEST)
            }
            Constants.IntentKeys.SMS -> {
                ActivityCompat.requestPermissions(activity,
                    Constants.Permission.SMS_LIST,
                    Constants.RequestCode.SMS_REQUEST)
            }
            else -> {
                ActivityCompat.requestPermissions(activity,
                    Constants.Permission.ALL_LIST,
                    Constants.RequestCode.ALL_PERMISSION_REQUEST
                )
            }
        }
    }
    fun grantResults(grantResults: IntArray):Boolean {
        for(items in grantResults){
            if(items != PackageManager.PERMISSION_GRANTED){
                return false
            }
        }
        return true
    }
    fun content(name: String):String{
        return   """
            We need to access $name for performing necessary task. Please permit the permission through Settings screen.
            
            Select App Permissions -> Enable permission($name)
            """.trimIndent()
    }
    fun displayManuallyEnablePermissionsDialog(activity: Activity,name: String,callBack: DialogCallBack?) {
        val builder = AlertDialog.Builder(activity)
        when (name) {
            Constants.IntentKeys.LOCATION -> {
                builder.setMessage(content(activity.getString(R.string.location)))
            }
            Constants.IntentKeys.CAMERA -> {
                val msg = "${activity.getString(R.string.camera)}${","}${activity.getString(
                    R.string.storage)}"
                builder.setMessage(content(msg))
            }
            Constants.IntentKeys.GALLERY -> {
                builder.setMessage(content(activity.getString(R.string.storage)))
            }
            Constants.IntentKeys.STORAGE -> {
                builder.setMessage(content(activity.getString(R.string.storage)))
            }
            Constants.IntentKeys.PHONE -> {
                builder.setMessage(content(activity.getString(R.string.call)))
            }
            Constants.IntentKeys.CONTACTS -> {
                builder.setMessage(content("Contacts"))
            }
            Constants.IntentKeys.MIC -> {
                builder.setMessage(content("MicroPhone"))
            }
            Constants.IntentKeys.SMS -> {
                builder.setMessage(content("Sms"))
            }
            else -> {
                val msg = "${activity.getString(R.string.location)}${","}${activity.getString(
                    R.string.storage)}${","}${activity.getString(R.string.camera)}${","}${activity.getString(R.string.call)}"
                builder.setMessage(content(msg))
            }
        }
        builder.setCancelable(false)
        builder.setPositiveButton(activity.getString(R.string.ok)) { dialog, _ ->
            dialog.dismiss()
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", activity.packageName, null)
            intent.data = uri
            activity.startActivity(intent)
            callBack?.onPositiveClick("")
        }
        builder.setNegativeButton(activity.getString(R.string.cancel)) { dialog, _ ->
            dialog.dismiss()
            callBack?.onNegativeClick()
        }
        builder.show()
    }
    fun isGpsEnabled(activity: Activity): Boolean {
        val locationManager: LocationManager?
        locationManager = activity.applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
    fun gpsEnableRequest(activity: Activity) {
        val locationRequest = LocationRequest.create()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(activity)
        val task = settingsClient.checkLocationSettings(builder.build())
        task.addOnSuccessListener(activity) {
            //setIsGps(true)
        }
        task.addOnFailureListener(activity) { e ->
            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(activity, Constants.RequestCode.GPS_REQUEST)
                } catch (e1: SendIntentException) {
                    e1.printStackTrace()
                }
            }
        }


/*
        task.addOnCompleteListener(activity, OnCompleteListener { task ->
            if(task.isSuccessful){

            }
            else{
                val e = task.exception
                if (e is ResolvableApiException) {
                    try {
                        e.startResolutionForResult(activity, Constants.RequestCode.GPS_REQUEST)
                    } catch (e1: SendIntentException) {
                        e1.printStackTrace()
                    }
                }
            }
        })
*/
    }

    fun getUtcTime(date: String, dateFormat: String): String {
        val spf = SimpleDateFormat(dateFormat, Locale.ENGLISH)
        var newDate: Date? = null
        try {
            newDate = spf.parse(date)
        } catch (e: ParseException1) {
            e.printStackTrace()
        }

        newDate?.let {
            val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.ENGLISH)
            simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
            return simpleDateFormat.format(newDate)
        }

        return ""
    }

    fun getTimeFromTimeStamp(value: String): String {
        val date = Date(value.toLong())
        val simpleDateFormat = SimpleDateFormat("hh:mm aa", Locale.ENGLISH)
        return simpleDateFormat.format(date)
    }

    fun numberFormat(value: Int): String {
        return String.format(Locale.ENGLISH,"%02d", value)
    }
    fun numberFormat(value: Double): String {
        return String.format(Locale.ENGLISH,"%.2f", value)
    }

    fun isMobileNumber(str: String):Boolean{
        val digitCasePatten = Pattern.compile("[0-9 ]")
        for(items in str){
            if(!digitCasePatten.matcher(items.toString()).find()){
                return false
            }
        }
        return true
    }

    fun isValidTime(date: String, format: String, time: Int): Boolean {
        val spf = SimpleDateFormat(format, Locale.ENGLISH)
        var newDate: Date? = null
        try {
            newDate = spf.parse(date)
        } catch (e: ParseException1) {
            e.printStackTrace()
        }
        val secondsInMilli = 1000
        val minutesInMilli = secondsInMilli * 60
        val currentDate: Date = Calendar.getInstance().time
        val currentString = spf.format(currentDate)
        var curretDate: Date? = null
        try {
            curretDate = spf.parse(currentString)
        } catch (e: ParseException1) {
            e.printStackTrace()
        }
        newDate?.let { nDate ->
            curretDate?.let { cDate ->
                val diff = nDate.time - cDate.time
                val elapsedMinutes: Int = (diff / minutesInMilli).toInt()
                Log.d("diff", elapsedMinutes.toString())
                return elapsedMinutes >= time
            }
        }
        return false
    }

    fun isValidPassword(context: Context, password: String): String {
        val specailCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE)
        val upperCasePatten = Pattern.compile("[A-Z ]")
        val lowerCasePatten = Pattern.compile("[a-z ]")
        val digitCasePatten = Pattern.compile("[0-9 ]")

        return if (password.length < 8) {
            context.resources.getString(R.string.password_length_should_be_eight_characters)
        } else if (!specailCharPatten.matcher(password).find()) {
            context.resources.getString(R.string.password_should_contain_atleast_one_special_character)
        } else if (!upperCasePatten.matcher(password).find()) {
            context.resources.getString(R.string.password_should_contain_atleast_one_uppercase_character)
        } else if (!lowerCasePatten.matcher(password).find()) {
            context.resources.getString(R.string.password_should_contain_atleast_one_lowercase_character)
        } /*else if (!digitCasePatten.matcher(password).find()) {
            context.resources.getString(R.string.password_should_contain_atleast_one_numeric)
        } */else {
            "true"
        }
    }
    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
/*
    fun getBitmapFromUrl(imageUrl: String?): Bitmap? {
        return try {
            // val urlImage = "https://thumbs.dreamstime.com/z/hands-holding-blue-earth-cloud-sky" + "-elements-imag-background-image-furnished-nasa-61052787.jpg"
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            Log.e("awesome", "Error in getting notification image: " + e.localizedMessage)
            null
        }
    }
*/

    /* fun getCurrentTime(): String? {
         val dateFormat = SimpleDateFormat("hh:mm a")
         dateFormat.timeZone = TimeZone.getTimeZone("UTC")
         val today = Calendar.getInstance().time
         return dateFormat.format(today)
     }
     fun navigateToFragment(id: Int, fragmentName: Fragment, fragmentManager: FragmentManager) {
         val fragmentTransaction =
             fragmentManager.beginTransaction()
         fragmentTransaction.setCustomAnimations(
             R.anim.enter_from_left,
             R.anim.exit_out_right,
             R.anim.enter_from_right,
             R.anim.exit_out_right
         )
         fragmentTransaction.replace(id, fragmentName)
         fragmentTransaction.addToBackStack(fragmentName.javaClass.simpleName)
         fragmentTransaction.commit()
     }


     fun navigateToFragmentFromBottom(id: Int, fragmentName: Fragment, fragmentManager: FragmentManager) {
         val fragmentTransaction =
             fragmentManager.beginTransaction()
         fragmentTransaction.setCustomAnimations(
             R.anim.slide_in_from_bottom,
             R.anim.slide_down_to_bottom
         )
         fragmentTransaction.replace(id, fragmentName)
         fragmentTransaction.addToBackStack(fragmentName.javaClass.simpleName)
         fragmentTransaction.commit()
     }

     fun getAge(year: Int, month: Int, day: Int): String {
         val dob = Calendar.getInstance()
         val today = Calendar.getInstance()
         dob[year, month] = day
         var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
         if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
             age--
         }
         val ageInt = age
         return ageInt.toString()
     }
     fun addEndTime(date: String, intputFormat: String, outputFormat: String, minutes: Int): String? {

         var spf = SimpleDateFormat(intputFormat, Locale.ENGLISH)
         var newDate: Date? = null
         try {
             newDate = spf.parse(date)
         } catch (e: ParseException1) {
             e.printStackTrace()
         }

         newDate?.let {
             val calander = getCalanderInstance(newDate)
             calander.add(Calendar.MINUTE, minutes)
             spf = SimpleDateFormat(outputFormat, Locale.ENGLISH)
             return spf.format(calander.time)

         }

         return ""

     }
     private fun getCalanderInstance(date: Date): Calendar {
         val calander = Calendar.getInstance()
         calander.time = date
         return calander
     }
     fun isPastTime(givenTime: String): Boolean {
         val simpleDateFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
         val date1 = simpleDateFormat.parse(givenTime)
         val date2 = simpleDateFormat.parse(SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date()))

         if (date1 != null && date2 != null) {
             val difference = date1.time - date2.time
             // val days = difference / (1000 * 60 * 60 * 24)

             val hours = difference / (1000 * 60 * 60)
             val mins = difference / (1000 * 60) % 60
 //            var hours = ((difference - 1000 * 60 * 60 * 24 * days) / (1000 * 60 * 60))
 //            val min = (difference - 1000 * 60 * 60 * 24 * days - 1000 * 60 * 60 * hours) / (1000 * 60)

             if (hours < 0) {
                 return true
             } else {
                 if (hours > 0) {
                     return false
                 }
                 return mins <= 0
             }
         } else {
             return true
         }
     }
     fun stringToCalendering(dateString: String): Calendar {
         val date: Date = stringToDate(dateString)
         return dateToCalender(date)
     }
     fun dpToPx(dp: Float): Int {
         val density = Resources.getSystem().displayMetrics.density
         return ceil((dp * density).toDouble()).toInt()
     }
     fun differenceBetweenDates(appDate: String): Float {
         val currentDate: Date = Calendar.getInstance(Locale.ENGLISH).time
         val appoinmentdate: Date = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(appDate)!!
         val diff = appoinmentdate.time - currentDate.time
         return diff.toFloat() / (24 * 60 * 60 * 1000)
     }
     fun differenceBetweenDatesInInt(appDate: String): Int {
         val currentDate: Date = Calendar.getInstance(Locale.ENGLISH).time
         val appoinmentdate: Date = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(appDate)!!
         val diff = appoinmentdate.time - currentDate.time
         val dayCount = diff.toFloat() / (24 * 60 * 60 * 1000)
         return dayCount.toInt()
     }
     fun getDayFromDate(calenderInstance: Calendar): String {
         return getDay(calenderInstance.get(Calendar.DAY_OF_WEEK))
     }
     fun isValidPassword(context: Context, password: String): String {
         val specailCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE)
         val upperCasePatten = Pattern.compile("[A-Z ]")
         val lowerCasePatten = Pattern.compile("[a-z ]")
         val digitCasePatten = Pattern.compile("[0-9 ]")

         return if (password.length < 8) {
             context.resources.getString(0)
         } else if (!specailCharPatten.matcher(password).find()) {
             context.resources.getString(0)
         } else if (!upperCasePatten.matcher(password).find()) {
             context.resources.getString(0)
         } else if (!lowerCasePatten.matcher(password).find()) {
             context.resources.getString(0)
         } else if (!digitCasePatten.matcher(password).find()) {
             context.resources.getString(0)
         } else {
             "true"
         }
     }
     fun isValidMobile(phone: String): Boolean {
         var check = false
         if (!Pattern.matches("[a-zA-Z]+", phone)) {
             if (phone.length in 6..13) {
                 check = true
             }
         }
         return check
     }
     fun isValidEmail(email: String): Boolean {
         return Patterns.EMAIL_ADDRESS.matcher(email).matches()
     }
     private fun getFileTostoreImageold(context: Context): File {
         val filepath = context.getExternalFilesDir(null)!!
         val zoeFolder = File(
             filepath.absolutePath,
             context.getString(R.string.app_name)
         ).absoluteFile
         if (!zoeFolder.exists()) {
             zoeFolder.mkdir()
         }
         val newFolder = File(
             zoeFolder,
             context.getString(R.string.app_name) + " Image"
         ).absoluteFile
         if (!newFolder.exists()) {
             newFolder.mkdir()
         }

         val filename = System.currentTimeMillis()
         val cameracaptureFile = "IMG_" + filename + "_" + System.currentTimeMillis().toString() + "_"
         return File(newFolder, "$cameracaptureFile.jpg")
     }
     fun openCameraold(activity: Activity) {
         val sharedHelper = SharedHelper(activity)
         val file = getFileTostoreImage(activity)
         val uri: Uri = FileProvider.getUriForFile(activity, activity.packageName + ".fileprovider", file)
         sharedHelper.imageUploadPath = file.absolutePath
         val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
         takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
         takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
         activity.startActivityForResult(takePictureIntent, Constants.RequestCode.CAMERA_INTENT)
     }
     fun checkPermission(context: Context, permissionName: String): Boolean {
         val res = context.checkCallingPermission(permissionName)
         return res == PackageManager.PERMISSION_GRANTED
     }
     private fun stringToDate(date: String): Date {
         return SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(date)!!
     }
     private fun dateToCalender(date: Date): Calendar {
         val cal = Calendar.getInstance(Locale.ENGLISH)
         cal.time = date
         return cal
     }
     private fun getDay(day: Int): String {
         return when (day) {
             1 -> "Sun"
             2 -> "Mon"
             3 -> "Tue"
             4 -> "Wed"
             5 -> "Thu"
             6 -> "Fri"
             7 -> "Sat"
             else -> ""
         }
     }
     fun isValidTime(date: String, format: String, time: Int): Boolean {
         val spf = SimpleDateFormat(format, Locale.ENGLISH)
         var newDate: Date? = null
         try {
             newDate = spf.parse(date)
         } catch (e: ParseException1) {
             e.printStackTrace()
         }
         val secondsInMilli = 1000
         val minutesInMilli = secondsInMilli * 60
         val currentDate: Date = Calendar.getInstance().time
         val currentString = spf.format(currentDate)
         var curretDate: Date? = null
         try {
             curretDate = spf.parse(currentString)
         } catch (e: ParseException1) {
             e.printStackTrace()
         }
         newDate?.let { nDate ->
             curretDate?.let { cDate ->
                 val diff = nDate.time - cDate.time
                 val elapsedMinutes: Int = (diff / minutesInMilli).toInt()
                 Log.d("diff", elapsedMinutes.toString())
                 return elapsedMinutes >= time
             }
         }
         return false
     }
      fun convertDate(date: Date, format: String?): String? {
         val simpleDateFormat = SimpleDateFormat(format, Locale.ENGLISH)
         return simpleDateFormat.format(date)
     }
     fun convertDate(date: String, inputFormat: String?, outputFormat: String?): String? {
         val simpleDateFormat = SimpleDateFormat(inputFormat, Locale.ENGLISH)
         var date1: Date? = null
         try {
             date1 = simpleDateFormat.parse(date)
         } catch (e: ParseException) {
             e.printStackTrace()
         }
         val simpleOutputFormat = SimpleDateFormat(outputFormat, Locale.ENGLISH)
         return if (date1 != null) {
             simpleOutputFormat.format(date1)
         } else null
     }
     fun convertDateutc(date: String, inputFormat: String?, outputFormat: String?): String? {
         val simpleDateFormat = SimpleDateFormat(inputFormat, Locale.ENGLISH)
         simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
         var date1: Date? = null
         try {
             date1 = simpleDateFormat.parse(date)
         } catch (e: ParseException) {
             e.printStackTrace()
         }
         val simpleOutputFormat = SimpleDateFormat(outputFormat, Locale.ENGLISH)
         return if (date1 != null) {
             simpleOutputFormat.format(date1)
         } else null
     }
     fun getConvertedTime(createdTime: String): String {
         val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'", Locale.ENGLISH)
         simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
         var targetdatevalue = ""
         try {
             val startDate: Date? = simpleDateFormat.parse(createdTime)
             val targetFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
             targetFormat.timeZone = Calendar.getInstance().timeZone
             targetdatevalue = targetFormat.format(startDate!!)
         } catch (e: ParseException) {
             e.printStackTrace()
         }
         return targetdatevalue
     }
     fun getFormatedString(createdTime: String, format: String?): String {
         val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'", Locale.ENGLISH)
         var targetdatevalue = ""
         try {
             val startDate: Date? = simpleDateFormat.parse(createdTime)
             val targetFormat = SimpleDateFormat(format, Locale.ENGLISH)
             targetdatevalue = targetFormat.format(startDate!!)
         } catch (e: ParseException) {
             e.printStackTrace()
         }
         return targetdatevalue
     }
     */

}