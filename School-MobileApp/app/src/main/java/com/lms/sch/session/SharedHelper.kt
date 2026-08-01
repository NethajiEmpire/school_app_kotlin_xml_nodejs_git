package com.lms.sch.session

import android.content.Context
import org.json.JSONArray
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lms.sch.response.ParentProfileResponse


class SharedHelper(context: Context) {

    private var sharedPreference: SharedPref = SharedPref(context)

    var isGuestLandingOpen: Boolean
        get() : Boolean {
            return sharedPreference.getBoolean(Constants.SessionKeys.GUEST_OPEN, false)
        }
        set(value) {
            sharedPreference.putBoolean(Constants.SessionKeys.GUEST_OPEN, value)
        }

    var chatMessages: List<String>
        get() {
            val json = sharedPreference.getString(Constants.SessionKeys.CHAT_MESSAGES, "[]")
            return Gson().fromJson(json, object : TypeToken<List<String>>() {}.type)
        }
        set(value) {
            sharedPreference.putString(Constants.SessionKeys.CHAT_MESSAGES, Gson().toJson(value))
        }


    var isGuestFeePaid: Boolean
        get(): Boolean {
            return sharedPreference.getBoolean(Constants.SessionKeys.GUEST_FEE_PAID, false)
        }
        set(value) {
            sharedPreference.putBoolean(Constants.SessionKeys.GUEST_FEE_PAID, value)
        }


    var systemThemeMode: String
        get() : String {
            return sharedPreference.getKey(Constants.SessionKeys.SYSTEM_THEME_MODE)
        }
        set(value) {
            sharedPreference.putKey(Constants.SessionKeys.SYSTEM_THEME_MODE, value)
        }

    var appThemeMode: String
        get() : String {
            return sharedPreference.getKey(Constants.SessionKeys.APP_THEME_MODE)
        }
        set(value) {
            sharedPreference.putKey(Constants.SessionKeys.APP_THEME_MODE, value)
        }

    var isDarkMode: Boolean
        get() : Boolean {
            return sharedPreference.getBoolean(Constants.SessionKeys.IS_DARK_MODE, true)
        }
        set(value) {
            sharedPreference.putBoolean(Constants.SessionKeys.IS_DARK_MODE, value)
        }

    var isInstallFirst: Boolean
        get() : Boolean {
            return sharedPreference.getBoolean(Constants.SessionKeys.IS_INSTALL_FIRST, true)
        }
        set(value) {
            sharedPreference.putBoolean(Constants.SessionKeys.IS_INSTALL_FIRST, value)
        }

    var isPieOpenFirst: Boolean
        get() : Boolean {
            return sharedPreference.getBoolean(Constants.SessionKeys.IS_PIE_OPEN_FIRST, true)
        }
        set(value) {
            sharedPreference.putBoolean(Constants.SessionKeys.IS_PIE_OPEN_FIRST, value)
        }

    var isChipOpenFirst: Boolean
        get() : Boolean {
            return sharedPreference.getBoolean(Constants.SessionKeys.IS_CHIP_OPEN_FIRST, true)
        }
        set(value) {
            sharedPreference.putBoolean(Constants.SessionKeys.IS_CHIP_OPEN_FIRST, value)
        }

    var token: String
        get() : String {
            return sharedPreference.getKey(Constants.SessionKeys.TOKEN)
        }
        set(value) {
            sharedPreference.putKey(Constants.SessionKeys.TOKEN, value)
        }

    var id: String
        get() : String {
            return sharedPreference.getKey(Constants.SessionKeys.ID)
        }
        set(value) {
            sharedPreference.putKey(Constants.SessionKeys.ID, value)
        }

    var fcmToken: String
        get() : String {
            return sharedPreference.getKey(Constants.SessionKeys.FCM_TOKEN)
        }
        set(value) {
            sharedPreference.putKey(Constants.SessionKeys.FCM_TOKEN, value)
        }

    var isfcmTokenUpdated: Boolean
        get() : Boolean {
            return sharedPreference.getBoolean(Constants.SessionKeys.UPDATED_FCM, true)
        }
        set(value) {
            sharedPreference.putBoolean(Constants.SessionKeys.UPDATED_FCM, value)
        }

    var language: String
        get() : String {
            return if (sharedPreference.getKey(Constants.SessionKeys.LANGUAGE) == "") {
                "en"
            } else {
                sharedPreference.getKey(Constants.SessionKeys.LANGUAGE)
            }

        }
        set(value) {
            sharedPreference.putKey(Constants.SessionKeys.LANGUAGE, value)
        }

    var dob: String
        get() : String {
            return sharedPreference.getKey(Constants.SessionKeys.DOB)
        }
        set(value) {
            sharedPreference.putKey(Constants.SessionKeys.DOB, value)
        }

    var role: String
        get() : String {
            return sharedPreference.getKey(Constants.SessionKeys.ROLE)
        }
        set(value) {
            sharedPreference.putKey(Constants.SessionKeys.ROLE, value)
        }

    var roleId: String
        get() : String {
            return sharedPreference.getKey(Constants.SessionKeys.ROLE_ID)
        }
        set(value) {
            sharedPreference.putKey(Constants.SessionKeys.ROLE_ID, value)
        }

    var name: String
        get() : String {
            return sharedPreference.getKey(Constants.SessionKeys.NAME)
        }
        set(value) {
            sharedPreference.putKey(Constants.SessionKeys.NAME, value)
        }
    var standard: String
        get() : String {
            return sharedPreference.getKey(Constants.SessionKeys.STANDARD)
        }
        set(value) {
            sharedPreference.putKey(Constants.SessionKeys.STANDARD, value)
        }

    var imgUrl: String
        get() : String {
            return sharedPreference.getKey(Constants.SessionKeys.IMG_URL)
        }
        set(value) {
            sharedPreference.putKey(Constants.SessionKeys.IMG_URL, value)
        }

    var firstname: String
        get() : String {
            return sharedPreference.getKey(Constants.SessionKeys.FIRST_NAME)
        }
        set(value) {
            sharedPreference.putKey(Constants.SessionKeys.FIRST_NAME, value)
        }


    var lastname: String
        get() : String {
            return sharedPreference.getKey(Constants.SessionKeys.LAST_NAME)
        }
        set(value) {
            sharedPreference.putKey(Constants.SessionKeys.LAST_NAME, value)
        }

    var email: String
        get() : String {
            return sharedPreference.getKey(Constants.SessionKeys.EMAIL)
        }
        set(value) {
            sharedPreference.putKey(Constants.SessionKeys.EMAIL, value)
        }

    var mobileNumber: String
        get() : String {
            return sharedPreference.getKey(Constants.SessionKeys.MOBILE_NUMBER)
        }
        set(value) {
            sharedPreference.putKey(Constants.SessionKeys.MOBILE_NUMBER, value)
        }

    var countryCode: String
        get() : String {
            return sharedPreference.getKey(Constants.SessionKeys.COUNTRY_CODE)
        }
        set(value) {
            sharedPreference.putKey(Constants.SessionKeys.COUNTRY_CODE, value)
        }

    var loggedIn: Boolean
        get() : Boolean {
            return sharedPreference.getBoolean(Constants.SessionKeys.LOGGED_IN, false)
        }
        set(value) {
            sharedPreference.putBoolean(Constants.SessionKeys.LOGGED_IN, value)
        }

    var isBioMetric: Boolean
        get() : Boolean {
            return sharedPreference.getBoolean(Constants.SessionKeys.BIO_METRIC, true)
        }
        set(value) {
            sharedPreference.putBoolean(Constants.SessionKeys.BIO_METRIC, value)
        }

    var pin: String
        get() : String {
            return sharedPreference.getKey(Constants.SessionKeys.PIN)
        }
        set(value) {
            sharedPreference.putKey(Constants.SessionKeys.PIN, value)
        }

    var location1: String
        get() : String {
            return sharedPreference.getKey(Constants.SessionKeys.LOCATION1)
        }
        set(value) {
            sharedPreference.putKey(Constants.SessionKeys.LOCATION1, value)
        }

    var myLat: Float
        get() : Float {
            return sharedPreference.getFloat(Constants.SessionKeys.LAT)
        }
        set(value) {
            sharedPreference.putFloat(Constants.SessionKeys.LAT, value)
        }

    var myLng: Float
        get() : Float {
            return sharedPreference.getFloat(Constants.SessionKeys.LNG)
        }
        set(value) {
            sharedPreference.putFloat(Constants.SessionKeys.LNG, value)
        }

    var imageUploadPath: String
        get() : String {
            return sharedPreference.getKey(Constants.SessionKeys.IMAGE_UPLOAD_PATH)
        }
        set(value) {
            sharedPreference.putKey(Constants.SessionKeys.IMAGE_UPLOAD_PATH, value)
        }

    var notifyCount: Int
        get() : Int {
            return sharedPreference.getInt(Constants.SessionKeys.NOTIFY_COUNT)
        }
        set(value) {
            sharedPreference.putInt(Constants.SessionKeys.NOTIFY_COUNT, value)
        }

    var pOrderCount: Int
        get() : Int {
            return sharedPreference.getInt(Constants.SessionKeys.P_ORDER_COUNT)
        }
        set(value) {
            sharedPreference.putInt(Constants.SessionKeys.P_ORDER_COUNT, value)
        }

    var cOrderCount: Int
        get() : Int {
            return sharedPreference.getInt(Constants.SessionKeys.C_ORDER_COUNT)
        }
        set(value) {
            sharedPreference.putInt(Constants.SessionKeys.C_ORDER_COUNT, value)
        }

    var tOrderCount: Int
        get() : Int {
            return sharedPreference.getInt(Constants.SessionKeys.T_ORDER_COUNT)
        }
        set(value) {
            sharedPreference.putInt(Constants.SessionKeys.T_ORDER_COUNT, value)
        }

    var lastNotify: Int
        get() : Int {
            return sharedPreference.getInt(Constants.SessionKeys.LAST_NOTIFY)
        }
        set(value) {
            sharedPreference.putInt(Constants.SessionKeys.LAST_NOTIFY, value)
        }

    var topic: String
        get() : String {
            return sharedPreference.getKey(Constants.SessionKeys.TOPIC)
        }
        set(value) {
            sharedPreference.putKey(Constants.SessionKeys.TOPIC, value)
        }

    var topicId: String
        get() : String {
            return sharedPreference.getKey(Constants.SessionKeys.TOPIC_ID)
        }
        set(value) {
            sharedPreference.putKey(Constants.SessionKeys.TOPIC_ID, value)
        }

    var isActive: String
        get() : String {
            return sharedPreference.getKey(Constants.SessionKeys.IS_ACTIVE)
        }
        set(value) {
            sharedPreference.putKey(Constants.SessionKeys.IS_ACTIVE, value)
        }

    var isOrganization: Boolean
        get() : Boolean {
            return sharedPreference.getBoolean(Constants.SessionKeys.IS_ORGANIZATION, true)
        }
        set(value) {
            sharedPreference.putBoolean(Constants.SessionKeys.IS_ORGANIZATION, value)
        }

    var isPersonal: Boolean
        get() : Boolean {
            return sharedPreference.getBoolean(Constants.SessionKeys.IS_PERSONAL, true)
        }
        set(value) {
            sharedPreference.putBoolean(Constants.SessionKeys.IS_PERSONAL, value)
        }

    var isSwap: Boolean
        get() : Boolean {
            return sharedPreference.getBoolean(Constants.SessionKeys.IS_SWAP, true)
        }
        set(value) {
            sharedPreference.putBoolean(Constants.SessionKeys.IS_SWAP, value)
        }

    var calendarTiming: Int
        get() : Int {
            return sharedPreference.getInt("calendarTiming")
        }
        set(value) {
            sharedPreference.putInt("calendarTiming", value)
        }

    var isHrAccessed: Boolean
        get() : Boolean {
            return sharedPreference.getBoolean(Constants.SessionKeys.IS_HR_ACCESSED, true)
        }
        set(value) {
            sharedPreference.putBoolean(Constants.SessionKeys.IS_HR_ACCESSED, value)
        }

    var statusBarColor: Int
        get() : Int {
            return sharedPreference.getInt(Constants.SessionKeys.STATUS_BAR_COLOR)
        }
        set(value) {
            sharedPreference.putInt(Constants.SessionKeys.STATUS_BAR_COLOR, value)
        }

    var isMute: Boolean
        get() : Boolean {
            return sharedPreference.getBoolean(Constants.SessionKeys.IS_MUTE, true)
        }
        set(value) {
            sharedPreference.putBoolean(Constants.SessionKeys.IS_MUTE, value)
        }

    var primaryColor: String
        get() : String {
            return sharedPreference.getKey(Constants.SessionKeys.PRIMARY_COLOR,"#A3A3A3")
        }
        set(value) {
            sharedPreference.putKey(Constants.SessionKeys.PRIMARY_COLOR, value)
        }

    var isCreateCircle: Boolean
        get() : Boolean {
            return sharedPreference.getBoolean(Constants.SessionKeys.IS_CREATE_CIRCLE, true)
        }
        set(value) {
            sharedPreference.putBoolean(Constants.SessionKeys.IS_CREATE_CIRCLE, value)
        }

    var cookies: MutableSet<String>?
        get() : MutableSet<String>? {
            return sharedPreference.getStringSet(Constants.SessionKeys.COOKIES)
        }
        set(value) {
            sharedPreference.putStringSet(Constants.SessionKeys.COOKIES, value)
        }

//    var sampleList: ArrayList<SampleJson.Sample>
//        get() : ArrayList<SampleJson.Sample> {
//            val myType = object : TypeToken<List<SampleJson.Sample>>() {}.type
//            val vsl = sharedPreference.getKey(Constants.SessionKeys.BRAND_LIST)
//
//            if (vsl == "") {
//                return ArrayList()
//            }
//            val logs: ArrayList<SampleJson.Sample> = Gson().fromJson(vsl, myType)
//            return logs as ArrayList<SampleJson.Sample>
//        }
//        set(value) {
//            val jsonString = Gson().toJson(value)
//            sharedPreference.putKey(Constants.SessionKeys.BRAND_LIST, jsonString)
//        }

//    var roleScope: GetRoleScopeResponse.Result
//        get() : GetRoleScopeResponse.Result {
//            val myType = object : TypeToken<GetRoleScopeResponse.Result>() {}.type
//            val vsl = sharedPreference.getKey(Constants.SessionKeys.PRODUCT_UNIT_LIST)
//
//            if (vsl == "") {
//                return GetRoleScopeResponse.Result()
//            }
//            val logs: GetRoleScopeResponse.Result = Gson().fromJson(vsl, myType)
//            return logs as GetRoleScopeResponse.Result
//        }
//        set(value) {
//            val jsonString = Gson().toJson(value)
//            sharedPreference.putKey(Constants.SessionKeys.PRODUCT_UNIT_LIST, jsonString)
//        }

    var postList : ArrayList<ParentProfileResponse.Result.UserProfile.Students>
        get() : ArrayList<ParentProfileResponse.Result.UserProfile.Students> {
            val mType = object : TypeToken<ArrayList<ParentProfileResponse.Result.UserProfile.Students>>() {}.type
            val vsl = sharedPreference.getKey("posts")
            if (vsl == ""){
                return ArrayList()
            }
            val logs : ArrayList<String> = Gson().fromJson(vsl,mType)
            return logs as ArrayList<ParentProfileResponse.Result.UserProfile.Students>
        }
        set(value){
            var jsonString = Gson().toJson(value)
            sharedPreference.putKey("posts", jsonString)
        }

    var childId: String
        get() : String {
            return sharedPreference.getKey(Constants.SessionKeys.CHILD_ID)
        }
        set(value) {
            sharedPreference.putKey(Constants.SessionKeys.CHILD_ID, value)
        }
    var childName: String
        get() : String {
            return sharedPreference.getKey(Constants.SessionKeys.CHILD_NAME)
        }
        set(value) {
            sharedPreference.putKey(Constants.SessionKeys.CHILD_NAME, value)
        }

    var childList : ArrayList<ParentProfileResponse.Result.UserProfile.Students>
        get() : ArrayList<ParentProfileResponse.Result.UserProfile.Students> {
            val mType = object : TypeToken<ArrayList<ParentProfileResponse.Result.UserProfile.Students>>() {}.type
            val vsl = sharedPreference.getKey("childList")
            if (vsl == ""){
                return ArrayList()
            }
            val logs : ArrayList<String> = Gson().fromJson(vsl,mType)
            return logs as ArrayList<ParentProfileResponse.Result.UserProfile.Students>
        }
        set(value){
            var jsonString = Gson().toJson(value)
            sharedPreference.putKey("childList", jsonString)
        }

    var studentInfo: JSONArray
        get() : JSONArray {
            val str = sharedPreference.getKey("studentInformation","")
            if(str.isEmpty()){
                return JSONArray()
            }
            else{
                return JSONArray(str)
            }
        }
        set(value) {
            sharedPreference.putKey("studentInformation", value.toString())
        }

    var guardianInfo: JSONArray
        get() : JSONArray {
            val str = sharedPreference.getKey("guardianInfo","")
            if(str.isEmpty()){
                return JSONArray()
            }
            else{
                return JSONArray(str)
            }
        }
        set(value) {
            sharedPreference.putKey("guardianInfo", value.toString())
        }

    var academicInfo: JSONArray
        get() : JSONArray {
            val str = sharedPreference.getKey("academicInfo","")
            if(str.isEmpty()){
                return JSONArray()
            }
            else{
                return JSONArray(str)
            }
        }
        set(value) {
            sharedPreference.putKey("academicInfo", value.toString())
        }

    var documentUpload: JSONArray
        get() : JSONArray {
            val str = sharedPreference.getKey("documentUpload","")
            if(str.isEmpty()){
                return JSONArray()
            }
            else{
                return JSONArray(str)
            }
        }
        set(value) {
            sharedPreference.putKey("documentUpload", value.toString())
        }

    var graduation: String
        get() : String {
            return sharedPreference.getKey("graduation","")
        }
        set(value) {
            sharedPreference.putKey("graduation", value)
        }

    var degree: String
        get() : String {
            return sharedPreference.getKey("degree","")
        }
        set(value) {
            sharedPreference.putKey("degree", value)
        }

    var course: String
        get() : String {
            return sharedPreference.getKey("course","")
        }
        set(value) {
            sharedPreference.putKey("course", value)
        }

    var section: String
        get() : String {
            return sharedPreference.getKey("name", "")
        }
        set(value) {
            sharedPreference.putKey("name", value)
        }

    var studentClass: Int
        get() : Int {
            return sharedPreference.getKey("studentClass", "0").toInt()
        }
        set(value) {
            sharedPreference.putKey("studentClass", value.toString())
        }

    var stdClassSec: String
        get() : String {
            return sharedPreference.getKey("StdClassSec", "")
        }
        set(value) {
            sharedPreference.putKey("StdClassSec", value)
        }
}