package com.lms.sch.network

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lms.sch.R
import com.lms.sch.response.AdminSingleViewResponse
import com.lms.sch.helpers.NetworkCheckDialog
import com.lms.sch.helpers.NetworkHelper
import com.lms.sch.models.BaseModel
import com.lms.sch.response.AdminFeesResponse
import com.lms.sch.response.AssignmentUpdCountResponse
import com.lms.sch.response.AttendanceProgressResponse
import com.lms.sch.response.BatchDropdownResponse
import com.lms.sch.response.ClassTestResponse
import com.lms.sch.response.GetClassTestResponse
import com.lms.sch.response.GetClassTimeTableResponse
import com.lms.sch.response.GetDocumentMaster
import com.lms.sch.response.GetDocumentsUploadResponse
import com.lms.sch.response.GetFeesOnlineResponse
import com.lms.sch.response.GetGuestInfoResponse
import com.lms.sch.response.GetGuestProfileResponse
import com.lms.sch.response.GetHomeworkResponse
import com.lms.sch.response.GetMyTeachersResponse
import com.lms.sch.response.GetSectionResponse
import com.lms.sch.response.GetStaffResponse
import com.lms.sch.response.LoginResponse
import com.lms.sch.response.PaymentResponse
import com.lms.sch.response.PincodeResponse
import com.lms.sch.response.ProfileDetailsResponse
import com.lms.sch.response.SendOtpResponse
import com.lms.sch.response.GetStudentAssignmentResponse
import com.lms.sch.response.GetStudentAttenDanceResponse
import com.lms.sch.response.GetStudentClassTestProgress
import com.lms.sch.response.GetStudentExamProgressResponse
import com.lms.sch.response.GetStudentResponse
import com.lms.sch.response.GetTeacherAssignmentResponse
import com.lms.sch.response.GetTeacherAssignmentSingleResponse
import com.lms.sch.response.GetTeacherClassTestResponse
import com.lms.sch.response.GetTeacherHomeWorkResponse
import com.lms.sch.response.GetTeacherNewEventResponse
import com.lms.sch.response.GetTeacherProjectResponse
import com.lms.sch.response.ProjectSingleViewResponse
import com.lms.sch.response.GetTeacherResponse
import com.lms.sch.response.TeacherHwSingleResponse
import com.lms.sch.response.GuestFeesResponse
import com.lms.sch.response.NoticeBoardResponse
import com.lms.sch.response.UploadFileResponse
import com.lms.sch.response.StudentBoardResponse
import com.lms.sch.response.DropdownResponse
import com.lms.sch.response.GetAcademicBatchResponse
import com.lms.sch.response.GetAcademicBoardResponse
import com.lms.sch.response.GetAcademicStandardResponse
import com.lms.sch.response.GetAcademicStatsResponse
import com.lms.sch.response.GetAcademicSubjectResponse
import com.lms.sch.response.GetAttendanceResponse
import com.lms.sch.response.GetComplaintDropDownResponse
import com.lms.sch.response.GetComplaintResponse
import com.lms.sch.response.GetComplaintSingleViewResponse
import com.lms.sch.response.GetComplaintStatResponse
import com.lms.sch.response.GetExamResponse
import com.lms.sch.response.GetExamSingleViewResponse
import com.lms.sch.response.GetExamSubjectResponse
import com.lms.sch.response.GetLeaderboardResponse
import com.lms.sch.response.GetOverAllProgressResponse
import com.lms.sch.response.GetRoleResponse
import com.lms.sch.response.GetScoreboardResponse
import com.lms.sch.response.GetTeacherClsTestSingleViewResponse
import com.lms.sch.response.GetTeacherProfileResponse
import com.lms.sch.response.GetTeacherProgramResponse
import com.lms.sch.response.GetTeacherScheduleResponse
import com.lms.sch.response.GetTeacherStdClsTestResponse
import com.lms.sch.response.GetTransactionResponse
import com.lms.sch.response.LeaveDropdownResponse
import com.lms.sch.response.LeaveRequestResponse
import com.lms.sch.response.ParentProfileResponse
import com.lms.sch.response.ProgramResponse
import com.lms.sch.response.ProjectUpdCountResponse
import com.lms.sch.response.StudentExamResponse
import com.lms.sch.response.StudentExamResultResponse
import com.lms.sch.response.StudentFeeResponse
import com.lms.sch.response.StudentListAnalyticsResponse
import com.lms.sch.response.StudentProjectResponse
import com.lms.sch.response.SubmissionProgressResponse
import com.lms.sch.response.TeacherAssCountResponse
import com.lms.sch.response.AdminAttendanceResponse
import com.lms.sch.response.AssSglViewResponse
import com.lms.sch.response.AvailableLeavesRes
import com.lms.sch.response.GetAdminStatsReponse
import com.lms.sch.response.GetProgressPointsStatsResponse
import com.lms.sch.response.GetOverallStudentAttendProgressRes
import com.lms.sch.response.GetTeacherStudentStatsCountRes
import com.lms.sch.response.ExamResultResponse
import com.lms.sch.response.GetAdminOverallFeeBarchartResponse
import com.lms.sch.response.GetStudentAssignmentRes
import com.lms.sch.response.HwSingleViewResponse
import com.lms.sch.response.MyClassAttendanceResponse
import com.lms.sch.response.PointsHistoryResponse
import com.lms.sch.response.ProfileDetailsTwo
import com.lms.sch.response.ProgramBasedSubjectResponse
import com.lms.sch.response.ProjectResultResponse
import com.lms.sch.response.SchoolDwRes
import com.lms.sch.response.StudentClassTestResponse
import com.lms.sch.response.StudentExamRes
import com.lms.sch.response.StudentProfileDatailstwo
import com.lms.sch.response.StudentProfileResponse
import com.lms.sch.response.SubjectWiseClassExamProResponse
import com.lms.sch.response.SubmissionProgressTeacherSide
import com.lms.sch.response.TeacherAssSingleViewResponse
import com.lms.sch.response.TeacherAssignment2Response
import com.lms.sch.response.TeacherAttendanceResponse
import com.lms.sch.response.TeacherSideStudentProjectResponse
import com.lms.sch.response.TeacherStatsResponse
import com.lms.sch.response.TeacherTimeTableResponse
import com.lms.sch.response.TeacherprofileSignleViewResponse
import com.lms.sch.response.UpdatesCountResponse
import com.lms.sch.response.VerifyOtpResponse
import com.lms.sch.session.Constants.ConstantsHelper.ACADEMIC_BATCH
import com.lms.sch.session.Constants.ConstantsHelper.ACADEMIC_BOARD
import com.lms.sch.session.Constants.ConstantsHelper.ACADEMIC_INFO
import com.lms.sch.session.Constants.ConstantsHelper.ACADEMIC_SECTION
import com.lms.sch.session.Constants.ConstantsHelper.ACADEMIC_STANDARD
import com.lms.sch.session.Constants.ConstantsHelper.ACADEMIC_STATS
import com.lms.sch.session.Constants.ConstantsHelper.ACADEMIC_SUBJECT
import com.lms.sch.session.Constants.ConstantsHelper.ADMIN_ATTENDANCE
import com.lms.sch.session.Constants.ConstantsHelper.ADMIN_DASHBOARD_STATS
import com.lms.sch.session.Constants.ConstantsHelper.ADMIN_FEES
import com.lms.sch.session.Constants.ConstantsHelper.ADMIN_OVERALL_FEE_PROGRESS
import com.lms.sch.session.Constants.ConstantsHelper.APPLICATION_FORM
import com.lms.sch.session.Constants.ConstantsHelper.ASIGNMENT_COUNT
import com.lms.sch.session.Constants.ConstantsHelper.ASSIGNMENT_MARK_UPDATE
import com.lms.sch.session.Constants.ConstantsHelper.ASSIGNMENT_STS_UPDATE
import com.lms.sch.session.Constants.ConstantsHelper.ATTENDANCE
import com.lms.sch.session.Constants.ConstantsHelper.CLASS_TEST
import com.lms.sch.session.Constants.ConstantsHelper.CLASS_TEST_REPORT
import com.lms.sch.session.Constants.ConstantsHelper.CLASS_TIME_TABLE
import com.lms.sch.session.Constants.ConstantsHelper.CLS_TEST_MARK_UPDATE
import com.lms.sch.session.Constants.ConstantsHelper.DOCUMENT_INFO
import com.lms.sch.session.Constants.ConstantsHelper.EXAM
import com.lms.sch.session.Constants.ConstantsHelper.EXAM_PROGRESS_DROPDOWN
import com.lms.sch.session.Constants.ConstantsHelper.GET_DOCUMENT_MASTER
import com.lms.sch.session.Constants.ConstantsHelper.GET_DOCUMENT_UPLOAD
import com.lms.sch.session.Constants.ConstantsHelper.GUEST_FEE
import com.lms.sch.session.Constants.ConstantsHelper.GUEST_LIST
import com.lms.sch.session.Constants.ConstantsHelper.GUEST_PROFILE
import com.lms.sch.session.Constants.ConstantsHelper.HOMEWORK
import com.lms.sch.session.Constants.ConstantsHelper.HOME_WORK_MARK_UPDATE
import com.lms.sch.session.Constants.ConstantsHelper.HOME_WORK_STS_UPDATE
import com.lms.sch.session.Constants.ConstantsHelper.INITIAL_FEE_GENERATE
import com.lms.sch.session.Constants.ConstantsHelper.MY_TEACHER
import com.lms.sch.session.Constants.ConstantsHelper.NOTICE_BOARD
import com.lms.sch.session.Constants.ConstantsHelper.PINCODE
import com.lms.sch.session.Constants.ConstantsHelper.STUDENT_HOMEWORK
import com.lms.sch.session.Constants.ConstantsHelper.STD_ASSIGNMENT_URL
import com.lms.sch.session.Constants.ConstantsHelper.PROFILE
import com.lms.sch.session.Constants.ConstantsHelper.REGISTRATION_FEE
import com.lms.sch.session.Constants.ConstantsHelper.STUDENT_BOARD
import com.lms.sch.session.Constants.ConstantsHelper.STUDENT_CLS_DROPDOWN
import com.lms.sch.session.Constants.ConstantsHelper.STUDENT_FORM
import com.lms.sch.session.Constants.ConstantsHelper.STUDENT_INFO
import com.lms.sch.session.Constants.ConstantsHelper.PARENT_INFO
import com.lms.sch.session.Constants.ConstantsHelper.PROJECT_COUNT
import com.lms.sch.session.Constants.ConstantsHelper.PROJECT_MARK_UPDATE
import com.lms.sch.session.Constants.ConstantsHelper.PROJECT_STS_UPDATE
import com.lms.sch.session.Constants.ConstantsHelper.SECTION
import com.lms.sch.session.Constants.ConstantsHelper.STAFF_LIST
import com.lms.sch.session.Constants.ConstantsHelper.STD_PROJECT_URL
import com.lms.sch.session.Constants.ConstantsHelper.STUDENT_ATTENDANCE
import com.lms.sch.session.Constants.ConstantsHelper.STUDENT_CLASS_TEST
import com.lms.sch.session.Constants.ConstantsHelper.STUDENT_GET_CLASS_TEST_PROGRESS
import com.lms.sch.session.Constants.ConstantsHelper.STUDENT_GET_EXAM_PROGRESS
import com.lms.sch.session.Constants.ConstantsHelper.STUDENT_LIST
import com.lms.sch.session.Constants.ConstantsHelper.TODAY_UPDATES_COUNT
import com.lms.sch.session.Constants.ConstantsHelper.STUDENT_EXAM
import com.lms.sch.session.Constants.ConstantsHelper.STUDENT_PROJECT
import com.lms.sch.session.Constants.ConstantsHelper.ATTENDANCE_PROGRESS
import com.lms.sch.session.Constants.ConstantsHelper.BATCH_DROPDOWN
import com.lms.sch.session.Constants.ConstantsHelper.COMPLAINTS
import com.lms.sch.session.Constants.ConstantsHelper.COMPLAINTS_DROP_DOWN
import com.lms.sch.session.Constants.ConstantsHelper.EXAM_MARK_UPDATE
import com.lms.sch.session.Constants.ConstantsHelper.EXAM_RESULT
import com.lms.sch.session.Constants.ConstantsHelper.COMPLAINT_STATS
import com.lms.sch.session.Constants.ConstantsHelper.COMPLAINT_STATUS
import com.lms.sch.session.Constants.ConstantsHelper.EXAM_PROGRESS
import com.lms.sch.session.Constants.ConstantsHelper.EXAM_SUBJECT
import com.lms.sch.session.Constants.ConstantsHelper.Exam_MARK_UPDATE
import com.lms.sch.session.Constants.ConstantsHelper.LEADER_BOARD
import com.lms.sch.session.Constants.ConstantsHelper.LEAVE_AVAITABLE
import com.lms.sch.session.Constants.ConstantsHelper.LEAVE_DROP
import com.lms.sch.session.Constants.ConstantsHelper.LEAVE_REQUEST
import com.lms.sch.session.Constants.ConstantsHelper.LEAVE_REQUEST_APPROVE
import com.lms.sch.session.Constants.ConstantsHelper.MY_CLASS_STUDENTS
import com.lms.sch.session.Constants.ConstantsHelper.PAYMENT_HISTORY
import com.lms.sch.session.Constants.ConstantsHelper.POINTS_HISTORY
import com.lms.sch.session.Constants.ConstantsHelper.PROGRAM
import com.lms.sch.session.Constants.ConstantsHelper.SUBJECT_DROPDOWN
import com.lms.sch.session.Constants.ConstantsHelper.TEACHER_ASSIGNMENT
import com.lms.sch.session.Constants.ConstantsHelper.TEACHER_CLASS_TEST
import com.lms.sch.session.Constants.ConstantsHelper.TEACHER_LIST
import com.lms.sch.session.Constants.ConstantsHelper.TEACHER_PROFILE
import com.lms.sch.session.Constants.ConstantsHelper.PROJECT
import com.lms.sch.session.Constants.ConstantsHelper.SCORE_BOARD
import com.lms.sch.session.Constants.ConstantsHelper.STAFF_PROFILE
import com.lms.sch.session.Constants.ConstantsHelper.STATS_PROGRESS_POINTS
import com.lms.sch.session.Constants.ConstantsHelper.STUDENTFEES
//import com.lms.sch.session.Constants.ConstantsHelper.STUDENTINFO_ATTENDANCE
import com.lms.sch.session.Constants.ConstantsHelper.STUDENT_ANALYTICS
import com.lms.sch.session.Constants.ConstantsHelper.STUDENT_EXAM_ID
import com.lms.sch.session.Constants.ConstantsHelper.STUDENT_EXAM_PROGRESS
import com.lms.sch.session.Constants.ConstantsHelper.STUDENT_EXAM_RES
import com.lms.sch.session.Constants.ConstantsHelper.STUDENT_OVERALL_PROGRESS
import com.lms.sch.session.Constants.ConstantsHelper.STUDENT_SCHOOL_DROPDOWN
import com.lms.sch.session.Constants.ConstantsHelper.TEACHER_PROGRAM
import com.lms.sch.session.Constants.SessionKeys.ROLE
import com.lms.sch.session.Constants.ConstantsHelper.SUBMISSION_PROGRESS
import com.lms.sch.session.Constants.ConstantsHelper.SUBMISSION_PROGRESS_SINGLE
import com.lms.sch.session.Constants.ConstantsHelper.TEACHER_ASS_COUNT
import com.lms.sch.session.Constants.ConstantsHelper.TEACHER_ATT_PROGRESS
import com.lms.sch.session.Constants.ConstantsHelper.TEACHER_MYCLASS_POINTS_COUNT
import com.lms.sch.session.Constants.ConstantsHelper.TEACHER_SIDE_SUB
import com.lms.sch.session.Constants.ConstantsHelper.TEACHER_STATS
import com.lms.sch.session.Constants.ConstantsHelper.TEACHER_STUDENT_ATTENDANCE
import com.lms.sch.session.Constants.ConstantsHelper.TEACHER_TBL
import com.lms.sch.session.Constants.ConstantsHelper.TIME_TABLE_TEACHER
import com.lms.sch.session.SharedHelper
import com.lms.sch.utils.BaseUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject


class ApiConnection private constructor() {
    companion object {
        private var apiConnection: ApiConnection? = null
        fun getInstance(): ApiConnection {
            if (apiConnection == null) {
                apiConnection = ApiConnection()
            }
            return apiConnection as ApiConnection
        }
    }

    fun uploadDoc(
        context: Context,
        file: MultipartBody.Part,
        code: String
    ): LiveData<UploadFileResponse> {
        val apiResponse: MutableLiveData<UploadFileResponse> = MutableLiveData()
        Log.d("wedrftg", "" + SharedHelper(context).id)
        Log.d("wedrftg", "" + code)
        val body2: MultipartBody.Part =
            MultipartBody.Part.createFormData("user_id", SharedHelper(context).id)
        val body3: MultipartBody.Part = MultipartBody.Part.createFormData("DOC_CODE", code)
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).uploadDoc(file, body2, body3)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<UploadFileResponse>(context, false) {
                override fun onNext(responseModel: UploadFileResponse) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = UploadFileResponse()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context, e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }

    fun uploadFile(context: Context, file: MultipartBody.Part): LiveData<UploadFileResponse> {
        val apiResponse: MutableLiveData<UploadFileResponse> = MutableLiveData()
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).uploadFile(file)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<UploadFileResponse>(context, false) {
                override fun onNext(responseModel: UploadFileResponse) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = UploadFileResponse()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context, e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }

    fun register(
        context: Context,
        fName: String,
        lName: String,
        phone: String,
        email: String,
        preSch: String,
        board: String,
        std: String,
        school: String
    ): LiveData<BaseModel> {
        val apiResponse: MutableLiveData<BaseModel> = MutableLiveData()
        val requestBody: MutableMap<String, String> = HashMap()
        requestBody["firstName"] = fName
        requestBody["lastName"] = lName
        requestBody["mobile"] = phone
        requestBody["email"] = email
        requestBody["pre_school"] = preSch
        requestBody["board"] = board
        requestBody["grade_level"] = std
        requestBody["school"] = school
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).register(requestBody)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<BaseModel>(context, false) {
                override fun onNext(responseModel: BaseModel) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = BaseModel()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context, e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }

    fun sendOTPMobile(context: Context, phone: String): LiveData<SendOtpResponse> {
        val apiResponse: MutableLiveData<SendOtpResponse> = MutableLiveData()
        val requestBody: MutableMap<String, String> = HashMap()
        requestBody["mobile"] = phone
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).sendOTPMobile(requestBody)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<SendOtpResponse>(context, false) {
                override fun onNext(responseModel: SendOtpResponse) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = SendOtpResponse()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context, e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }

    fun verifyOTPMobile(context: Context, phone: String, otp: String): LiveData<VerifyOtpResponse> {
        val apiResponse: MutableLiveData<VerifyOtpResponse> = MutableLiveData()
        val requestBody: MutableMap<String, String> = HashMap()
        requestBody["mobile"] = phone
        requestBody["ip"] = BaseUtils.getIP(context)
        requestBody["device_id"] = BaseUtils.getDeviceID(context)
//        requestBody["fcm_token"] = AppSharedPref.getFcmToken(context)!!
        requestBody["otp"] = otp
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).verifyOTPMobile(requestBody)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<VerifyOtpResponse>(context, false) {
                override fun onNext(responseModel: VerifyOtpResponse) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = VerifyOtpResponse()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context, e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }

    fun login(context: Context, email: String, password: String): LiveData<LoginResponse> {
        val apiResponse: MutableLiveData<LoginResponse> = MutableLiveData()
        val requestBody: MutableMap<String, String> = HashMap()
        requestBody["email"] = email
        requestBody["password"] = password
        requestBody["device_id"] = BaseUtils.getDeviceID(context)
//        requestBody["fcm_token"] = BaseUtils.nullCheckerStr(AppSharedPref.getFcmToken(context))
        requestBody["ip"] = BaseUtils.getIP(context)
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).login(requestBody)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<LoginResponse>(context, false) {
                override fun onNext(responseModel: LoginResponse) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = LoginResponse()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context, e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }

    fun forgotPassword(context: Context, email: String): LiveData<BaseModel> {
        val apiResponse: MutableLiveData<BaseModel> = MutableLiveData()
        val requestBody: MutableMap<String, String> = HashMap()
        requestBody["value"] = email
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).forgotPassword(requestBody)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<BaseModel>(context, false) {
                override fun onNext(responseModel: BaseModel) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                    NetworkCheckDialog(context).stopChecking()
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = BaseModel()
                    response.success = false
                    if (!BaseUtils.isNetworkAvailable(context)) {
                        response.msg = context.getString(R.string.no_internet_connection)
                        NetworkCheckDialog(context).startChecking()
                    } else {
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        NetworkCheckDialog(context).stopChecking()
                    }
                    apiResponse.value = response
                }
            })
        return apiResponse
    }

    /*fun profile(context: Context):LiveData<ProfileResponse>{
        val apiResponse: MutableLiveData<ProfileResponse> = MutableLiveData()
        val url = PROFILE
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).profile(url)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<ProfileResponse>(context, false) {
                override fun onNext(responseModel: ProfileResponse) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }
                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = ProfileResponse()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context,e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }*/

    fun pincode(context: Context, str: String): LiveData<PincodeResponse> {
        val apiResponse: MutableLiveData<PincodeResponse> = MutableLiveData()
        val url = PINCODE + str
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).pincode(url)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<PincodeResponse>(context, false) {
                override fun onNext(responseModel: PincodeResponse) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = PincodeResponse()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context, e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }

    fun studentForm(context: Context): LiveData<BaseModel> {
        val sharedHelper = SharedHelper(context)
        val apiResponse: MutableLiveData<BaseModel> = MutableLiveData()
        val url = STUDENT_FORM
        val studentInformation: JSONObject = JSONObject()
        studentInformation.put(
            "firstName",
            sharedHelper.studentInfo.getJSONObject(0).getString("showValue")
        )
        studentInformation.put(
            "lastName",
            sharedHelper.studentInfo.getJSONObject(1).getString("showValue")
        )
        studentInformation.put(
            "dob",
            sharedHelper.studentInfo.getJSONObject(2).getString("showValue")
        )
        studentInformation.put(
            "gender",
            sharedHelper.studentInfo.getJSONObject(3).getString("showValue").toLowerCase()
        )
        studentInformation.put(
            "blood_group",
            sharedHelper.studentInfo.getJSONObject(4).getString("showValue")
        )
        studentInformation.put(
            "nationality",
            sharedHelper.studentInfo.getJSONObject(5).getString("showValue")
        )
        studentInformation.put(
            "religion",
            sharedHelper.studentInfo.getJSONObject(6).getString("showValue")
        )
        studentInformation.put(
            "category",
            sharedHelper.studentInfo.getJSONObject(7).getString("showValue")
        )
        studentInformation.put(
            "aadhar_number",
            sharedHelper.studentInfo.getJSONObject(8).getString("showValue")
        )
        studentInformation.put(
            "mobile",
            sharedHelper.studentInfo.getJSONObject(9).getString("showValue")
        )
        studentInformation.put(
            "email",
            sharedHelper.studentInfo.getJSONObject(10).getString("showValue")
        )
        studentInformation.put(
            "pincode",
            sharedHelper.studentInfo.getJSONObject(11).getString("showValue")
        )
        studentInformation.put(
            "country",
            sharedHelper.studentInfo.getJSONObject(12).getString("showValue")
        )
        studentInformation.put(
            "state",
            sharedHelper.studentInfo.getJSONObject(13).getString("showValue")
        )
        studentInformation.put(
            "city",
            sharedHelper.studentInfo.getJSONObject(14).getString("showValue")
        )
        studentInformation.put(
            "address",
            sharedHelper.studentInfo.getJSONObject(15).getString("showValue")
        )

        /*val referenceInformation:JSONObject = JSONObject()
        referenceInformation.put("referredLeadId",sharedHelper.referenceInfo.getJSONObject(0).getString("showValue"))
        referenceInformation.put("referredBy",sharedHelper.referenceInfo.getJSONObject(1).getString("showValue"))
        val communicationAddress:JSONObject = JSONObject()
        communicationAddress.put("addressLine1",sharedHelper.communicationAddr.getJSONObject(0).getString("showValue"))
        communicationAddress.put("addressLine2",sharedHelper.communicationAddr.getJSONObject(1).getString("showValue"))
        communicationAddress.put("country",sharedHelper.communicationAddr.getJSONObject(2).getString("value"))
        communicationAddress.put("state",sharedHelper.communicationAddr.getJSONObject(3).getString("value"))
        communicationAddress.put("city",sharedHelper.communicationAddr.getJSONObject(4).getString("value"))
        communicationAddress.put("pincode",sharedHelper.communicationAddr.getJSONObject(5).getString("showValue"))
        val permanentAddress:JSONObject = JSONObject()
        permanentAddress.put("addressLine1",sharedHelper.communicationAddr.getJSONObject(7).getString("showValue"))
        permanentAddress.put("addressLine2",sharedHelper.communicationAddr.getJSONObject(8).getString("showValue"))
        permanentAddress.put("country",sharedHelper.communicationAddr.getJSONObject(9).getString("value"))
        permanentAddress.put("state",sharedHelper.communicationAddr.getJSONObject(10).getString("value"))
        permanentAddress.put("city",sharedHelper.communicationAddr.getJSONObject(11).getString("value"))
        permanentAddress.put("pincode",sharedHelper.communicationAddr.getJSONObject(12).getString("showValue"))
        val courseApplied:JSONObject = JSONObject()
        courseApplied.put("graduation",SharedHelper(context).graduation)
        courseApplied.put("degree",SharedHelper(context).degree)
        courseApplied.put("course",SharedHelper(context).course)
        val studentAcademicInformation: JSONArray = JSONArray()
        val studentAcademicInformationJson:JSONObject = JSONObject()
        studentAcademicInformationJson.put("schoolOrCollegeName",sharedHelper.academicInfo.getJSONObject(0).getString("showValue"))
        studentAcademicInformationJson.put("courseName",sharedHelper.academicInfo.getJSONObject(1).getString("showValue"))
        studentAcademicInformationJson.put("universityName",sharedHelper.academicInfo.getJSONObject(2).getString("showValue"))
        studentAcademicInformationJson.put("percentage",sharedHelper.academicInfo.getJSONObject(3).getString("showValue"))
        studentAcademicInformationJson.put("grade",sharedHelper.academicInfo.getJSONObject(4).getString("showValue"))
        studentAcademicInformationJson.put("yearOfPassing",sharedHelper.academicInfo.getJSONObject(5).getString("showValue"))
        studentAcademicInformation.put(studentAcademicInformationJson)
        val workingProfessionalInformation: JSONArray = JSONArray()
        val workingProfessionalInformationJson:JSONObject = JSONObject()
        workingProfessionalInformationJson.put("sector",sharedHelper.workingInfo.getJSONObject(0).getString("showValue"))
        workingProfessionalInformationJson.put("designation",sharedHelper.workingInfo.getJSONObject(1).getString("showValue"))
        workingProfessionalInformationJson.put("companyName",sharedHelper.workingInfo.getJSONObject(2).getString("showValue"))
        workingProfessionalInformationJson.put("companyLocation",sharedHelper.workingInfo.getJSONObject(3).getString("showValue"))
        workingProfessionalInformationJson.put("salaryPerAnnum",sharedHelper.workingInfo.getJSONObject(4).getString("showValue"))
        workingProfessionalInformationJson.put("startDate",sharedHelper.workingInfo.getJSONObject(6).getString("showValue"))
        workingProfessionalInformationJson.put("endDate",sharedHelper.workingInfo.getJSONObject(7).getString("showValue"))
        workingProfessionalInformation.put(workingProfessionalInformationJson)
        val requestBody = JSONObject()
        requestBody.put("studentInformation",studentInformation)
        requestBody.put("referenceInformation",referenceInformation)
        requestBody.put("communicationAddress",communicationAddress)
        requestBody.put("permanentAddress",permanentAddress)
        requestBody.put("courseApplied",courseApplied)
        requestBody.put("studentAcademicInformation",studentAcademicInformation)
        requestBody.put("workingProfessionalInformation",workingProfessionalInformation)*/

        val body: RequestBody =
            studentInformation.toString().toRequestBody("application/json".toMediaTypeOrNull())
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).applicationSave(url, body)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<BaseModel>(context, false) {
                override fun onNext(responseModel: BaseModel) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = BaseModel()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context, e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }


    fun applicationForm(context: Context): LiveData<BaseModel> {
        val sharedHelper = SharedHelper(context)
        val apiResponse: MutableLiveData<BaseModel> = MutableLiveData()
        val url = APPLICATION_FORM
        val parentInfo: JSONObject = JSONObject()
        parentInfo.put(
            "fatherName",
            sharedHelper.guardianInfo.getJSONObject(0).getString("showValue")
        )
        parentInfo.put(
            "motherName",
            sharedHelper.guardianInfo.getJSONObject(1).getString("showValue")
        )
        parentInfo.put(
            "guardianName",
            sharedHelper.guardianInfo.getJSONObject(2).getString("showValue")
        )
        parentInfo.put(
            "fatherOccupation",
            sharedHelper.guardianInfo.getJSONObject(3).getString("showValue")
        )
        parentInfo.put(
            "motherOccupation",
            sharedHelper.guardianInfo.getJSONObject(4).getString("showValue")
        )
        parentInfo.put(
            "parentsMobile",
            sharedHelper.guardianInfo.getJSONObject(5).getString("showValue")
        )
        parentInfo.put(
            "emergencyMobile",
            sharedHelper.guardianInfo.getJSONObject(6).getString("showValue")
        )
        parentInfo.put(
            "parentsEmail",
            sharedHelper.guardianInfo.getJSONObject(7).getString("showValue")
        )
        parentInfo.put(
            "parentsAdress",
            sharedHelper.guardianInfo.getJSONObject(8).getString("showValue")
        )
        val academicInfo: JSONObject = JSONObject()
        academicInfo.put(
            "previousSchoolName",
            sharedHelper.academicInfo.getJSONObject(0).getString("showValue")
        )
        academicInfo.put(
            "previousClassName",
            sharedHelper.academicInfo.getJSONObject(1).getString("showValue")
        )
        academicInfo.put(
            "classApplying",
            sharedHelper.academicInfo.getJSONObject(2).getString("showValue")
        )
        academicInfo.put(
            "boardOfEducation",
            sharedHelper.academicInfo.getJSONObject(3).getString("showValue")
        )
        val requestBody = JSONObject()
        requestBody.put("student", sharedHelper.id)
        requestBody.put("parentInfo", parentInfo)
        requestBody.put("academicInfo", academicInfo)
        val body: RequestBody =
            requestBody.toString().toRequestBody("application/json".toMediaTypeOrNull())
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).applicationSave(url, body)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<BaseModel>(context, false) {
                override fun onNext(responseModel: BaseModel) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = BaseModel()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context, e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }

    fun getDocumentMaster(context: Context): LiveData<GetDocumentMaster> {
        val apiResponse: MutableLiveData<GetDocumentMaster> = MutableLiveData()
        val url = GET_DOCUMENT_MASTER
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).getDocumentMaster(url)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<GetDocumentMaster>(context, false) {
                override fun onNext(responseModel: GetDocumentMaster) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = GetDocumentMaster()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context, e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }

    fun getDocumentsUpload(context: Context): LiveData<GetDocumentsUploadResponse> {
        val apiResponse: MutableLiveData<GetDocumentsUploadResponse> = MutableLiveData()
        val url = GET_DOCUMENT_UPLOAD + SharedHelper(context).id
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).getDocumentsUpload(url)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<GetDocumentsUploadResponse>(context, false) {
                override fun onNext(responseModel: GetDocumentsUploadResponse) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = GetDocumentsUploadResponse()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context, e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }

    fun guestProfile(context: Context): LiveData<GetGuestProfileResponse> {
        val apiResponse: MutableLiveData<GetGuestProfileResponse> = MutableLiveData()
        val url = GUEST_PROFILE + SharedHelper(context).id
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).guestProfile(url)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<GetGuestProfileResponse>(context, false) {
                override fun onNext(responseModel: GetGuestProfileResponse) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = GetGuestProfileResponse()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context, e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }
    fun studentHomework(context: Context,search:String,sts:String,status: String):LiveData<GetHomeworkResponse>{
        val apiResponse: MutableLiveData<GetHomeworkResponse> = MutableLiveData()
        val url = if (SharedHelper(context).role == "PARENT") {
            STUDENT_HOMEWORK + "?search=$search&status=$sts&markStatus=$status&student=${SharedHelper(context).childId}"
        } else {
            STUDENT_HOMEWORK + "?search=$search&status=$sts&markStatus=$status"
        }
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).getStudentHomework(url)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<GetHomeworkResponse>(context, false) {
                override fun onNext(responseModel: GetHomeworkResponse) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = GetHomeworkResponse()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context, e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }
    fun studentHomework(context: Context,search:String,sts:String,status: String,homework : String):LiveData<GetHomeworkResponse>{
        val apiResponse: MutableLiveData<GetHomeworkResponse> = MutableLiveData()
        val url = if (SharedHelper(context).role == "PARENT") {
            STUDENT_HOMEWORK + "?search=$search&status=$sts&markStatus=$status&student=${SharedHelper(context).childId}"
        } else {
            STUDENT_HOMEWORK + "?search=$search&status=$sts&markStatus=$status&homework=$homework"
        }
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).getStudentHomework(url)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<GetHomeworkResponse>(context, false) {
                override fun onNext(responseModel: GetHomeworkResponse) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = GetHomeworkResponse()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context, e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }
    fun stdSingleHwHw(context: Context,homework : String):LiveData<HwSingleViewResponse>{
        val apiResponse: MutableLiveData<HwSingleViewResponse> = MutableLiveData()
        val url = STUDENT_HOMEWORK+"?homework=${homework}"
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).getstdsgl(url)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<HwSingleViewResponse>(context, false) {
                override fun onNext(responseModel: HwSingleViewResponse) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = HwSingleViewResponse()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context, e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }
    fun getTeacherHomework(context: Context, search: String, program: String, status: String): LiveData<GetTeacherHomeWorkResponse> {
        val apiResponse: MutableLiveData<GetTeacherHomeWorkResponse> = MutableLiveData()
        val url = HOMEWORK + "?search=$search&program=$program&status=$status"
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).getTeacherHomework(url)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<GetTeacherHomeWorkResponse>(context, false) {
                override fun onNext(responseModel: GetTeacherHomeWorkResponse) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = GetTeacherHomeWorkResponse()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context, e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }
    fun getTeacherHomework(
        context: Context,   search: String,   sub: String, program: String, status: String): LiveData<GetTeacherHomeWorkResponse> {
        val apiResponse: MutableLiveData<GetTeacherHomeWorkResponse> = MutableLiveData()
        val url = HOMEWORK + "?search=$search&program=$program&status=$status&subject=$sub"
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).getTeacherHomework(url)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<GetTeacherHomeWorkResponse>(context, false) {
                override fun onNext(responseModel: GetTeacherHomeWorkResponse) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }
                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = GetTeacherHomeWorkResponse()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context, e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }

    fun getHwSingle(context: Context, id: String): LiveData<TeacherHwSingleResponse> {
        val apiResponse: MutableLiveData<TeacherHwSingleResponse> = MutableLiveData()
        val url = HOMEWORK + "/$id"
        ApiClient.getClient(context)!!.create(ApiDetails::class.java)
            .getTeacherHomeworkSingleId(url)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<TeacherHwSingleResponse>(context, false) {
                override fun onNext(responseModel: TeacherHwSingleResponse) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = TeacherHwSingleResponse()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context, e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }


    fun getTeacherProject(  context: Context, search: String, program: String, status: String): LiveData<GetTeacherProjectResponse> {
        val apiResponse: MutableLiveData<GetTeacherProjectResponse> = MutableLiveData()
        val url = PROJECT + "?status=$status&program$program&search=$search"
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).getTeacherProject(url)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<GetTeacherProjectResponse>(context, false) {
                override fun onNext(responseModel: GetTeacherProjectResponse) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = GetTeacherProjectResponse()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context, e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }

    fun getTeacherProject(  context: Context,  search: String,program: String,sub: String, status: String ): LiveData<GetTeacherProjectResponse> {
        val apiResponse: MutableLiveData<GetTeacherProjectResponse> = MutableLiveData()
        val url = PROJECT + "?status=$status&program=$program&search=$search"
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).getTeacherProject(url)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<GetTeacherProjectResponse>(context, false) {
                override fun onNext(responseModel: GetTeacherProjectResponse) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = GetTeacherProjectResponse()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context, e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }

    fun getProjectSingleView(context: Context, id: String): LiveData<ProjectSingleViewResponse> {
        val apiResponse: MutableLiveData<ProjectSingleViewResponse> = MutableLiveData()
        val url = PROJECT + id
        ApiClient.getClient(context)!!.create(ApiDetails::class.java)
            .getTeacherProjectSingleView(url)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<ProjectSingleViewResponse>(context, false) {
                override fun onNext(responseModel: ProjectSingleViewResponse) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }
                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = ProjectSingleViewResponse()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context, e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }

    fun studentProjectRes( context: Context,    status: String ,id : String ): LiveData<TeacherSideStudentProjectResponse> {
        val apiResponse: MutableLiveData<TeacherSideStudentProjectResponse> = MutableLiveData()
        val url = STUDENT_PROJECT + "?markStatus=$status&project=${id}"
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).getStudentProjectRes(url)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object :
                ApiResponseCallback<TeacherSideStudentProjectResponse>(context, false) {
                override fun onNext(responseModel: TeacherSideStudentProjectResponse) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }
                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = TeacherSideStudentProjectResponse()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context, e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }

    fun getClsTestSingleView(  context: Context,   id: String ): LiveData<GetTeacherClsTestSingleViewResponse> {
        val apiResponse: MutableLiveData<GetTeacherClsTestSingleViewResponse> = MutableLiveData()
        val url = TEACHER_CLASS_TEST + id
        ApiClient.getClient(context)!!.create(ApiDetails::class.java)
            .getTeacherClsTestSingleView(url)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object :
                ApiResponseCallback<GetTeacherClsTestSingleViewResponse>(context, false) {
                override fun onNext(responseModel: GetTeacherClsTestSingleViewResponse) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = GetTeacherClsTestSingleViewResponse()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context, e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }

    fun studentClsTestRes( context: Context,  clsTestId: String,   status: String,  mSts: String ): LiveData<GetTeacherStdClsTestResponse> {
        val apiResponse: MutableLiveData<GetTeacherStdClsTestResponse> = MutableLiveData()
        val url = STUDENT_CLASS_TEST + "?classTest=$clsTestId&status=$status&markStatus=$mSts"
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).getStudentClsTestRes(url)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<GetTeacherStdClsTestResponse>(context, false) {
                override fun onNext(responseModel: GetTeacherStdClsTestResponse) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = GetTeacherStdClsTestResponse()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context, e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }

    fun getStudentAssignment(context: Context,search:String,sts: String,mStatus: String):LiveData<GetStudentAssignmentResponse> {
        val apiResponse: MutableLiveData<GetStudentAssignmentResponse> = MutableLiveData()
//        val url = STD_ASSIGNMENT_URL+"?status=$sts"
        val url = if (SharedHelper(context).role == "PARENT") {
            STD_ASSIGNMENT_URL + "?search=$search&status=$sts&student=${SharedHelper(context).childId}"
        } else {
            STD_ASSIGNMENT_URL + "?search=$search&status=$sts&markStatus=$mStatus"
        }
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).getStudentAssignment(url)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<GetStudentAssignmentResponse>(context, false) {
                override fun onNext(responseModel: GetStudentAssignmentResponse) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = GetStudentAssignmentResponse()
                    response.success = false
                    response.msg = (NetworkHelper.getErrorMessage(context, e))
                    apiResponse.value = response
                }
            })
        return apiResponse
    }

    fun getStudentOverallProgress(context: Context): LiveData<GetOverAllProgressResponse> {
        val apiResponse: MutableLiveData<GetOverAllProgressResponse> = MutableLiveData()
        val url = STUDENT_OVERALL_PROGRESS + SharedHelper(context).childId
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).overAllProgress(url)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<GetOverAllProgressResponse>(context, false) {
                override fun onNext(responseModel: GetOverAllProgressResponse) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = GetOverAllProgressResponse()
                    response.success = false
                    response.msg = (NetworkHelper.getErrorMessage(context, e))
                    apiResponse.value = response
                }
            })
        return apiResponse
    }

    fun getStudentOverallProgresss(context: Context): LiveData<GetOverAllProgressResponse> {
        val apiResponse: MutableLiveData<GetOverAllProgressResponse> = MutableLiveData()
        val url =
            if (SharedHelper(context).role == "PARENT" && !SharedHelper(context).childId.isNullOrEmpty()) {
                Log.d(";lkjhgfdghjkl", SharedHelper(context).childId)
                STUDENT_OVERALL_PROGRESS + "?student=${SharedHelper(context).childId}"

            } else {
                Log.d("lkjhgfhj", SharedHelper(context).childId)
                STUDENT_OVERALL_PROGRESS
            }

        ApiClient.getClient(context)!!.create(ApiDetails::class.java).overAllProgress(url)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<GetOverAllProgressResponse>(context, false) {
                override fun onNext(responseModel: GetOverAllProgressResponse) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = GetOverAllProgressResponse()
                    response.success = false
                    response.msg = (NetworkHelper.getErrorMessage(context, e))
                    apiResponse.value = response
                }
            })
        return apiResponse
    }

    fun getStudentAssignment1(context: Context,search:String,sts: String,mStatus: String):LiveData<GetStudentAssignmentRes> {
        val apiResponse: MutableLiveData<GetStudentAssignmentRes> = MutableLiveData()
//        val url = STD_ASSIGNMENT_URL+"?status=$sts"
        val url = if (SharedHelper(context).role == "PARENT") {
            STD_ASSIGNMENT_URL + "?search=$search&status=$sts&student=${SharedHelper(context).childId}"
        } else {
            STD_ASSIGNMENT_URL + "?search=$search&status=$sts&markStatus=$mStatus"
        }
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).getStudentAssignment1(url)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<GetStudentAssignmentRes>(context, false) {
                override fun onNext(responseModel: GetStudentAssignmentRes) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = GetStudentAssignmentRes()
                    response.success = false
                    response.msg = (NetworkHelper.getErrorMessage(context, e))
                    apiResponse.value = response
                }
            })
        return apiResponse
    }

    fun getStudentProject(context: Context,psts: String, search: String): LiveData<StudentProjectResponse> {
        val apiResponse: MutableLiveData<StudentProjectResponse> = MutableLiveData()
//        val url = STD_PROJECT_URL+"?status=$psts"
        val url = if (SharedHelper(context).role == "PARENT") {
            STD_PROJECT_URL + "?status=$psts&search=$search&student=${SharedHelper(context).childId}"
        } else {
            STD_PROJECT_URL + "?status=$psts"
        }
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).getStudentProject(url)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<StudentProjectResponse>(context, false) {
                override fun onNext(responseModel: StudentProjectResponse) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = StudentProjectResponse()
                    response.success = false
                    response.msg = (NetworkHelper.getErrorMessage(context, e))
                    apiResponse.value = response
                }
            })
        return apiResponse
    }
    fun stdProjectRes(context: Context, id: String): LiveData<StudentProjectResponse> {
        val apiResponse: MutableLiveData<StudentProjectResponse> = MutableLiveData()
        val url = STD_PROJECT_URL + "?project=$id"
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).getStudentProject(url)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<StudentProjectResponse>(context, false) {
                override fun onNext(responseModel: StudentProjectResponse) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = StudentProjectResponse()
                    response.success = false
                    response.msg = (NetworkHelper.getErrorMessage(context, e))
                    apiResponse.value = response
                }
            })
        return apiResponse
    }

        fun getTeacherNewsEvent(
            context: Context,
            type: String
        ): LiveData<GetTeacherNewEventResponse> {
            val apiResponse: MutableLiveData<GetTeacherNewEventResponse> = MutableLiveData()
            val url = NOTICE_BOARD + "?dueFilter=$type"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getTeacherNewsEvent(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object :
                    ApiResponseCallback<GetTeacherNewEventResponse>(context, false) {
                    override fun onNext(responseModel: GetTeacherNewEventResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetTeacherNewEventResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getNoticeBoard(
            context: Context,
            ststyp: String,
            eventDate: String
        ): LiveData<NoticeBoardResponse> {
            val apiResponse: MutableLiveData<NoticeBoardResponse> = MutableLiveData()

            val url = if (SharedHelper(context).role == "PARENT") {
                NOTICE_BOARD + "?dateFilter=$ststyp&date=$eventDate&student=${SharedHelper(context).childId}"
            } else {
                NOTICE_BOARD + "?dateFilter=$ststyp&date=$eventDate"
            }
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getNoticeBoard(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<NoticeBoardResponse>(context, false) {
                    override fun onNext(responseModel: NoticeBoardResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = NoticeBoardResponse()
                        response.success = false
                        response.msg = (NetworkHelper.getErrorMessage(context, e))
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getClassTest(context: Context, search: String, status: String): LiveData<ClassTestResponse> {
            val apiResponse: MutableLiveData<ClassTestResponse> = MutableLiveData()
            val url = if (SharedHelper(context).role == "PARENT") {
                CLASS_TEST + "?status=$status&search=$search&student=${SharedHelper(context).childId}"
            } else {
                CLASS_TEST + "?status=$status&search=$search"
            }
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getClassTest(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<ClassTestResponse>(context, false) {
                    override fun onNext(responseModel: ClassTestResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = ClassTestResponse()
                        response.success = false
                        response.msg = (NetworkHelper.getErrorMessage(context, e))
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getStudentClassTest(context: Context, search: String, status: String, attach: ArrayList<String>): LiveData<StudentClassTestResponse> {
            val apiResponse: MutableLiveData<StudentClassTestResponse> = MutableLiveData()
            val url: String = if (SharedHelper(context).role == "PARENT") {
                CLASS_TEST + "?status=$status&search=$search&student=${SharedHelper(context).childId}"
            } else {
                CLASS_TEST + "?status=$status&search=$search"
            }
            val requestBody = JSONObject()
            requestBody.put("attachment", JSONArray(attach))
            val body: RequestBody = requestBody.toString()
                .toRequestBody("application/json".toMediaTypeOrNull())

            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getStuClassTest(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<StudentClassTestResponse>(context, false) {
                    override fun onNext(responseModel: StudentClassTestResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = StudentClassTestResponse()
                        response.success = false
                        response.msg = (NetworkHelper.getErrorMessage(context, e))
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getClassTimetable(
            context: Context,
            type: String,
            date: String
        ): LiveData<GetClassTimeTableResponse> {
            val apiResponse: MutableLiveData<GetClassTimeTableResponse> = MutableLiveData()
            val url = if (SharedHelper(context).role == "PARENT") {
                CLASS_TIME_TABLE + "?type=$type&day=$date&student_id=${SharedHelper(context).childId}"
            } else {
                CLASS_TIME_TABLE + "?type=$type&day=$date"
            }
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getClassTimeTable(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<GetClassTimeTableResponse>(context, false) {
                    override fun onNext(responseModel: GetClassTimeTableResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetClassTimeTableResponse()
                        response.success = false
                        response.msg = (NetworkHelper.getErrorMessage(context, e))
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun timeTableTeacher(
            context: Context,
            type: String,
            day: String
        ): LiveData<GetTeacherScheduleResponse> {
            val apiResponse: MutableLiveData<GetTeacherScheduleResponse> = MutableLiveData()
            val url =
                TIME_TABLE_TEACHER + "?type=$type&teacher=${SharedHelper(context).id}&day=$day"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).timeTableTeacher(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object :
                    ApiResponseCallback<GetTeacherScheduleResponse>(context, false) {
                    override fun onNext(responseModel: GetTeacherScheduleResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetTeacherScheduleResponse()
                        response.success = false
                        response.msg = (NetworkHelper.getErrorMessage(context, e))
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getTimetable(
            context: Context,
            type: String,
            date: String,
            program: String
        ): LiveData<GetClassTimeTableResponse> {
            val apiResponse: MutableLiveData<GetClassTimeTableResponse> = MutableLiveData()
            val url = CLASS_TIME_TABLE + "?type=$type&day=$date&program=$program"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getClassTimeTable(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<GetClassTimeTableResponse>(context, false) {
                    override fun onNext(responseModel: GetClassTimeTableResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetClassTimeTableResponse()
                        response.success = false
                        response.msg = (NetworkHelper.getErrorMessage(context, e))
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getExamSingle(context: Context, id: String): LiveData<GetExamSingleViewResponse> {
            val apiResponse: MutableLiveData<GetExamSingleViewResponse> = MutableLiveData()
            val url = EXAM + id
            ApiClient.getClient(context)!!.create(ApiDetails::class.java)
                .getAdminExamSingleView(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<GetExamSingleViewResponse>(context, false) {
                    override fun onNext(responseModel: GetExamSingleViewResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetExamSingleViewResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getSection(context: Context, program: String): LiveData<GetSectionResponse> {
            val apiResponse: MutableLiveData<GetSectionResponse> = MutableLiveData()
            val url = SECTION + "?program=$program"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getSection(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<GetSectionResponse>(context, false) {
                    override fun onNext(responseModel: GetSectionResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetSectionResponse()
                        response.success = false
                        response.msg = (NetworkHelper.getErrorMessage(context, e))
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getStudentList(
            context: Context,
            str: String,
            program: String
        ): LiveData<GetStudentResponse> {
            val apiResponse: MutableLiveData<GetStudentResponse> = MutableLiveData()
            val url = STUDENT_LIST + "?search=$str&program=$program"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getStudent(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<GetStudentResponse>(context, false) {
                    override fun onNext(responseModel: GetStudentResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetStudentResponse()
                        response.success = false
                        response.msg = (NetworkHelper.getErrorMessage(context, e))
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getStudentList(
            context: Context,
            str: String,
            program: String,
            gender: String
        ): LiveData<GetStudentResponse> {
            val apiResponse: MutableLiveData<GetStudentResponse> = MutableLiveData()
            val url = STUDENT_LIST + "?search=$str&program=$program&gender=$gender"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getStudent(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<GetStudentResponse>(context, false) {
                    override fun onNext(responseModel: GetStudentResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetStudentResponse()
                        response.success = false
                        response.msg = (NetworkHelper.getErrorMessage(context, e))
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getGuestInfo(context: Context, str: String): LiveData<GetGuestInfoResponse> {
            val apiResponse: MutableLiveData<GetGuestInfoResponse> = MutableLiveData()
            val url = GUEST_LIST + "?search=$str"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getGuest(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<GetGuestInfoResponse>(context, false) {
                    override fun onNext(responseModel: GetGuestInfoResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetGuestInfoResponse()
                        response.success = false
                        response.msg = (NetworkHelper.getErrorMessage(context, e))
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getTeacherInfo(context: Context, str: String): LiveData<GetTeacherResponse> {
            val apiResponse: MutableLiveData<GetTeacherResponse> = MutableLiveData()
            val url = TEACHER_LIST + "?search=$str"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getTeacher(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<GetTeacherResponse>(context, false) {
                    override fun onNext(responseModel: GetTeacherResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetTeacherResponse()
                        response.success = false
                        response.msg = (NetworkHelper.getErrorMessage(context, e))
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getStaffInfo(context: Context, str: String): LiveData<GetStaffResponse> {
            val apiResponse: MutableLiveData<GetStaffResponse> = MutableLiveData()
            val url = STAFF_LIST + "?search=$str"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getStaff(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<GetStaffResponse>(context, false) {
                    override fun onNext(responseModel: GetStaffResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetStaffResponse()
                        response.success = false
                        response.msg = (NetworkHelper.getErrorMessage(context, e))
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun classTestReport(context: Context, subject: String, date: String): LiveData<GetClassTestResponse> {
            val apiResponse: MutableLiveData<GetClassTestResponse> = MutableLiveData()
            val url = if (SharedHelper(context).role == "PARENT") {
                Log.d("iuj,ko'p[-]ujikol;j8,./kol'p;[-" + "=]", SharedHelper(context).childId)
                Log.d("lhgdjsgd", SharedHelper(context).role)
                CLASS_TEST_REPORT + "?subject=$subject&date=$date&student=${SharedHelper(context).childId}"
            } else {
                Log.d("lhgdjsgd", SharedHelper(context).role)
                Log.d("lhgdjsgd", SharedHelper(context).childId)
                CLASS_TEST_REPORT + "?subject=$subject&date=$date"
            }
//        val url = CLASS_TEST_REPORT+"?subject=$subject&date=$date"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getClassTestReport(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<GetClassTestResponse>(context, false) {
                    override fun onNext(responseModel: GetClassTestResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetClassTestResponse()
                        response.success = false
                        response.msg = (NetworkHelper.getErrorMessage(context, e))
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun subjectDropdown(context: Context): LiveData<DropdownResponse> {
            val apiResponse: MutableLiveData<DropdownResponse> = MutableLiveData()
            val url = SUBJECT_DROPDOWN
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getDropdown(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<DropdownResponse>(context, false) {
                    override fun onNext(responseModel: DropdownResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = DropdownResponse()
                        response.success = false
                        response.msg = (NetworkHelper.getErrorMessage(context, e))
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun programBasedSubject(context: Context): LiveData<ProgramBasedSubjectResponse> {
            val apiResponse: MutableLiveData<ProgramBasedSubjectResponse> = MutableLiveData()
            val url = SUBJECT_DROPDOWN
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).programBasedSub(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object :
                    ApiResponseCallback<ProgramBasedSubjectResponse>(context, false) {
                    override fun onNext(responseModel: ProgramBasedSubjectResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = ProgramBasedSubjectResponse()
                        response.success = false
                        response.msg = (NetworkHelper.getErrorMessage(context, e))
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun teacherProgram(context: Context): LiveData<GetTeacherProgramResponse> {
            val apiResponse: MutableLiveData<GetTeacherProgramResponse> = MutableLiveData()
            val url = TEACHER_PROGRAM
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).teacherProgram(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<GetTeacherProgramResponse>(context, false) {
                    override fun onNext(responseModel: GetTeacherProgramResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetTeacherProgramResponse()
                        response.success = false
                        response.msg = (NetworkHelper.getErrorMessage(context, e))
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun program(
            context: Context,
            search: String,
            board: String,
            batch: String,
            cls: String
        ): LiveData<ProgramResponse> {
            val apiResponse: MutableLiveData<ProgramResponse> = MutableLiveData()
            val url =
                PROGRAM + "?search=$search&board=$board&batch=$batch&studentClass=$cls&perPage=50&currentPage0"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).program(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<ProgramResponse>(context, false) {
                    override fun onNext(responseModel: ProgramResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = ProgramResponse()
                        response.success = false
                        response.msg = (NetworkHelper.getErrorMessage(context, e))
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }
    fun program( context: Context, search: String,): LiveData<ProgramResponse> {
        val apiResponse: MutableLiveData<ProgramResponse> = MutableLiveData()
        val url = PROGRAM
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).program(url)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<ProgramResponse>(context, false) {
                override fun onNext(responseModel: ProgramResponse) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = ProgramResponse()
                    response.success = false
                    response.msg = (NetworkHelper.getErrorMessage(context, e))
                    apiResponse.value = response
                }
            })
        return apiResponse
    }
        fun studentListAnalytics(
            context: Context,
            program: String,
        ): LiveData<StudentListAnalyticsResponse> {
            val apiResponse: MutableLiveData<StudentListAnalyticsResponse> = MutableLiveData()
            val url = STUDENT_ANALYTICS + "?program=$program"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).studentAnalytics(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object :
                    ApiResponseCallback<StudentListAnalyticsResponse>(context, false) {
                    override fun onNext(responseModel: StudentListAnalyticsResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = StudentListAnalyticsResponse()
                        response.success = false
                        response.msg = (NetworkHelper.getErrorMessage(context, e))
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }
    fun studentExamination( context: Context,  subject: String, exam : String  ): LiveData<StudentExamResponse> {
        val apiResponse: MutableLiveData<StudentExamResponse> = MutableLiveData()
        val url = STUDENT_EXAM_RES + "?subject=$subject&exam=$exam"
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).getStudentExam(url)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object :
                ApiResponseCallback<StudentExamResponse>(context, false) {
                override fun onNext(responseModel: StudentExamResponse) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = StudentExamResponse()
                    response.success = false
                    response.msg = (NetworkHelper.getErrorMessage(context, e))
                    apiResponse.value = response
                }
            })
        return apiResponse
    }

        fun studentExamination(context: Context, exam: String): LiveData<StudentExamResponse> {
            val apiResponse: MutableLiveData<StudentExamResponse> = MutableLiveData()
            val url = STUDENT_EXAM + "?exam_id=$exam"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getStudentExam(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<StudentExamResponse>(context, false) {
                    override fun onNext(responseModel: StudentExamResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = StudentExamResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun examResult(
            context: Context,
            search: String,
            sub: String,
            noDue: String,
            mSts: String,
            atte: String
        ): LiveData<StudentExamResultResponse> {
            val apiResponse: MutableLiveData<StudentExamResultResponse> = MutableLiveData()
            val url =
                EXAM_RESULT + "?exam_subject=$sub&search=$search&noDue=$noDue&markStatus=$mSts&attendance=$atte"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getStudentExam1(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<StudentExamResultResponse>(context, false) {
                    override fun onNext(responseModel: StudentExamResultResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = StudentExamResultResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getExam(context: Context, search: String, ests: String): LiveData<GetExamResponse> {
            val apiResponse: MutableLiveData<GetExamResponse> = MutableLiveData()
//        val url = EXAM+"?completeStatus=$ests"
            val url = if (SharedHelper(context).role == "PARENT") {
                EXAM + "?search=$search&completeStatus=$ests&student=${SharedHelper(context).childId}"
            } else {
                EXAM + "?search=$search&completeStatus=$ests"
            }
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getExam(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<GetExamResponse>(context, false) {
                    override fun onNext(responseModel: GetExamResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetExamResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getExam1(
            context: Context,  program: String,  search: String,  ests: String,  sub: String ): LiveData<GetExamResponse> {
            val apiResponse: MutableLiveData<GetExamResponse> = MutableLiveData()
            val url = EXAM + "?search=$search&program=$program&completeStatus=$ests&sub=$sub"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getExam(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<GetExamResponse>(context, false) {
                    override fun onNext(responseModel: GetExamResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetExamResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }
    fun getExam2(
        context: Context,  program: String,  search: String,  ests: String,  sub: String ): LiveData<StudentExamResponse> {
        val apiResponse: MutableLiveData<StudentExamResponse> = MutableLiveData()
        val url = EXAM + "?search=$search&program=$program&completeStatus=$ests&sub=$sub"
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).studentAnalytics1(url)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<StudentExamResponse>(context, false) {
                override fun onNext(responseModel: StudentExamResponse) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = StudentExamResponse()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context, e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }
    fun getExam1(
        context: Context,
        program: String,
        search: String,
        ests: String
    ): LiveData<GetExamResponse> {
        val apiResponse: MutableLiveData<GetExamResponse> = MutableLiveData()
        val url = EXAM + "?search=$search&program=$program&completeStatus=$ests"
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).getExam(url)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<GetExamResponse>(context, false) {
                override fun onNext(responseModel: GetExamResponse) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = GetExamResponse()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context, e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }

        fun getExam(
            context: Context,
            str: String,
            ests: String,
            board: String
        ): LiveData<GetExamResponse> {
            val apiResponse: MutableLiveData<GetExamResponse> = MutableLiveData()
            val url = EXAM + "?search=$str&completeStatus=$ests&board=$board"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getExam(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<GetExamResponse>(context, false) {
                    override fun onNext(responseModel: GetExamResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetExamResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getExamList(context: Context, search: String, exam: String, nodue: String): LiveData<GetExamSubjectResponse> {
            val apiResponse: MutableLiveData<GetExamSubjectResponse> = MutableLiveData()
            val url = EXAM_SUBJECT+"?majorExam=$exam&search=$search&status=$nodue"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getExamList(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<GetExamSubjectResponse>(context, false) {
                    override fun onNext(responseModel: GetExamSubjectResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetExamSubjectResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getExamStdList(context: Context, search: String, exam: String, status: String): LiveData<StudentExamRes> {
        val apiResponse: MutableLiveData<StudentExamRes> = MutableLiveData()
        val url = STUDENT_EXAM+"?exam_id=$exam&search=$search&noDue=$status"
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).getExamstdList(url)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<StudentExamRes>(context, false) {
                override fun onNext(responseModel: StudentExamRes) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = StudentExamRes()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context, e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }

        fun examProgressDropdown(context: Context): LiveData<DropdownResponse> {
            val apiResponse: MutableLiveData<DropdownResponse> = MutableLiveData()
            val url = EXAM_PROGRESS_DROPDOWN
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getDropdown(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<DropdownResponse>(context, false) {
                    override fun onNext(responseModel: DropdownResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = DropdownResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun examProgressDropdown(context: Context, id: String): LiveData<DropdownResponse> {
            val apiResponse: MutableLiveData<DropdownResponse> = MutableLiveData()
            val url = "${EXAM_PROGRESS_DROPDOWN}/${id}"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getDropdown(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<DropdownResponse>(context, false) {
                    override fun onNext(responseModel: DropdownResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = DropdownResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun StudentExamProgressDropdown(context: Context): LiveData<DropdownResponse> {
            val apiResponse: MutableLiveData<DropdownResponse> = MutableLiveData()
            val url = STUDENT_EXAM_PROGRESS
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getDropdown(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<DropdownResponse>(context, false) {
                    override fun onNext(responseModel: DropdownResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = DropdownResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getAdminDashboardStats(context: Context): LiveData<GetAdminStatsReponse> {
            val apiResponse: MutableLiveData<GetAdminStatsReponse> = MutableLiveData()
            val url = ADMIN_DASHBOARD_STATS
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getAdminStats(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<GetAdminStatsReponse>(context, false) {
                    override fun onNext(responseModel: GetAdminStatsReponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetAdminStatsReponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun studentExamId(context: Context, id: String): LiveData<DropdownResponse> {
            val apiResponse: MutableLiveData<DropdownResponse> = MutableLiveData()
            val url = STUDENT_EXAM_ID + id
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getDropdown(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<DropdownResponse>(context, false) {
                    override fun onNext(responseModel: DropdownResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = DropdownResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun studentExamRes(context: Context, ExamId: String): LiveData<ExamResultResponse> {
            val apiResponse: MutableLiveData<ExamResultResponse> = MutableLiveData()
//        val url = "${STUDENT_EXAM_RES}?exam_id=${ExamId}"
            val url = if (SharedHelper(context).role == "PARENT") {
                "${STUDENT_EXAM_RES}?exam_id=${ExamId}&student=${SharedHelper(context).childId}"
            } else {
                "${STUDENT_EXAM_RES}?exam_id=${ExamId}"
            }
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).examRes(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<ExamResultResponse>(context, false) {
                    override fun onNext(responseModel: ExamResultResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)

                        val response = ExamResultResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }


        fun getAcademicStats(context: Context): LiveData<GetAcademicStatsResponse> {
            val apiResponse: MutableLiveData<GetAcademicStatsResponse> = MutableLiveData()
            val url = ACADEMIC_STATS
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getAcademicStats(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<GetAcademicStatsResponse>(context, false) {
                    override fun onNext(responseModel: GetAcademicStatsResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetAcademicStatsResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getComplaintStats(context: Context, id: String): LiveData<GetComplaintStatResponse> {
            val apiResponse: MutableLiveData<GetComplaintStatResponse> = MutableLiveData()
            val url = COMPLAINT_STATS + "?role=$id"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getComplaintStats(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<GetComplaintStatResponse>(context, false) {
                    override fun onNext(responseModel: GetComplaintStatResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetComplaintStatResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getAcademicBatch(context: Context, search: String): LiveData<GetAcademicBatchResponse> {
            val apiResponse: MutableLiveData<GetAcademicBatchResponse> = MutableLiveData()
            val url = ACADEMIC_BATCH + "?search=$search"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getAcademicBatch(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<GetAcademicBatchResponse>(context, false) {
                    override fun onNext(responseModel: GetAcademicBatchResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetAcademicBatchResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun batchDropdown(context: Context): LiveData<BatchDropdownResponse> {
            val apiResponse: MutableLiveData<BatchDropdownResponse> = MutableLiveData()
            val url = BATCH_DROPDOWN
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).batchDropdown(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<BatchDropdownResponse>(context, false) {
                    override fun onNext(responseModel: BatchDropdownResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = BatchDropdownResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getAllComplaints(context: Context, search: String, sts: String, role: String, program: String): LiveData<GetComplaintResponse> {
            val apiResponse: MutableLiveData<GetComplaintResponse> = MutableLiveData()
            val url = COMPLAINTS + "?search=$search&status=$sts&role=$role&program=${program}"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getaAllComplaints(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<GetComplaintResponse>(context, false) {
                    override fun onNext(responseModel: GetComplaintResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetComplaintResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getAllComplaints(
            context: Context,
            search: String,
            sts: String,
            role: String,
            program: String,
            status: String
        ): LiveData<GetComplaintResponse> {
            val apiResponse: MutableLiveData<GetComplaintResponse> = MutableLiveData()
            val url =
                COMPLAINTS + "?search=$search&status=$sts&role=$role&program=${program}&status=${status}"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getaAllComplaints(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<GetComplaintResponse>(context, false) {
                    override fun onNext(responseModel: GetComplaintResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetComplaintResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getComplaintDropDown(context: Context): LiveData<GetComplaintDropDownResponse> {
            val apiResponse: MutableLiveData<GetComplaintDropDownResponse> = MutableLiveData()
            val url = COMPLAINTS_DROP_DOWN
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getComplaintDropDrown(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object :
                    ApiResponseCallback<GetComplaintDropDownResponse>(context, false) {
                    override fun onNext(responseModel: GetComplaintDropDownResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetComplaintDropDownResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getSingleViewComplaints(
            context: Context,
            id: String
        ): LiveData<GetComplaintSingleViewResponse> {
            val apiResponse: MutableLiveData<GetComplaintSingleViewResponse> = MutableLiveData()
            val url = COMPLAINTS + "/$id"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getaSVComplaints(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object :
                    ApiResponseCallback<GetComplaintSingleViewResponse>(context, false) {
                    override fun onNext(responseModel: GetComplaintSingleViewResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetComplaintSingleViewResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getRole(context: Context): LiveData<GetRoleResponse> {
            val apiResponse: MutableLiveData<GetRoleResponse> = MutableLiveData()
            val url = ROLE
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getRole(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<GetRoleResponse>(context, false) {
                    override fun onNext(responseModel: GetRoleResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetRoleResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getAcademicBoard(context: Context, search: String): LiveData<GetAcademicBoardResponse> {
            val apiResponse: MutableLiveData<GetAcademicBoardResponse> = MutableLiveData()
            val url = ACADEMIC_BOARD + "?search=$search"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getAcademicBoard(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<GetAcademicBoardResponse>(context, false) {
                    override fun onNext(responseModel: GetAcademicBoardResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetAcademicBoardResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getAcademicStandard(
            context: Context,
            search: String
        ): LiveData<GetAcademicStandardResponse> {
            val apiResponse: MutableLiveData<GetAcademicStandardResponse> = MutableLiveData()
            val url = ACADEMIC_STANDARD + "?search=$search"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getAcademicStandard(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object :
                    ApiResponseCallback<GetAcademicStandardResponse>(context, false) {
                    override fun onNext(responseModel: GetAcademicStandardResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetAcademicStandardResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getAcademicSection(context: Context, search: String): LiveData<GetSectionResponse> {
            val apiResponse: MutableLiveData<GetSectionResponse> = MutableLiveData()
            val url = ACADEMIC_SECTION + "?search=$search"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getSection(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<GetSectionResponse>(context, false) {
                    override fun onNext(responseModel: GetSectionResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetSectionResponse()
                        response.success = false
                        response.msg = (NetworkHelper.getErrorMessage(context, e))
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getAcademicSubject(
            context: Context,
            search: String
        ): LiveData<GetAcademicSubjectResponse> {
            val apiResponse: MutableLiveData<GetAcademicSubjectResponse> = MutableLiveData()
            val url = ACADEMIC_SUBJECT + "?search=$search"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getAcademicSubject(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object :
                    ApiResponseCallback<GetAcademicSubjectResponse>(context, false) {
                    override fun onNext(responseModel: GetAcademicSubjectResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetAcademicSubjectResponse()
                        response.success = false
                        response.msg = (NetworkHelper.getErrorMessage(context, e))
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun studentClassTestProgress(context: Context): LiveData<GetStudentClassTestProgress> {
            val apiResponse: MutableLiveData<GetStudentClassTestProgress> = MutableLiveData()
            val url = if (SharedHelper(context).role == "PARENT") {
                "$STUDENT_GET_CLASS_TEST_PROGRESS?student=${SharedHelper(context).childId}"
            } else {
                STUDENT_GET_CLASS_TEST_PROGRESS
            }
            ApiClient.getClient(context)!!.create(ApiDetails::class.java)
                .getStdClassTestProgress(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object :
                    ApiResponseCallback<GetStudentClassTestProgress>(context, false) {
                    override fun onNext(responseModel: GetStudentClassTestProgress) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetStudentClassTestProgress()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getStudentExamProgress(context: Context): LiveData<GetStudentExamProgressResponse> {
            val apiResponse: MutableLiveData<GetStudentExamProgressResponse> = MutableLiveData()
            val url = if (SharedHelper(context).role == "PARENT") {
                "$STUDENT_GET_EXAM_PROGRESS?student=${SharedHelper(context).childId}"
            } else {
                STUDENT_GET_EXAM_PROGRESS
            }
            ApiClient.getClient(context)!!.create(ApiDetails::class.java)
                .getStdExaminationProgress(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object :
                    ApiResponseCallback<GetStudentExamProgressResponse>(context, false) {
                    override fun onNext(responseModel: GetStudentExamProgressResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetStudentExamProgressResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getStudentAttendance(context: Context): LiveData<GetStudentAttenDanceResponse> {
            val apiResponse: MutableLiveData<GetStudentAttenDanceResponse> = MutableLiveData()
//        val url = ATTENDANCE_PROGRESS
            val url = if (SharedHelper(context).role == "PARENT") {
                Log.d("hghsd", SharedHelper(context).role)
                ATTENDANCE_PROGRESS + "?student=${SharedHelper(context).childId}"
            } else {
                ATTENDANCE_PROGRESS
            }
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getStdAttendance(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object :
                    ApiResponseCallback<GetStudentAttenDanceResponse>(context, false) {
                    override fun onNext(responseModel: GetStudentAttenDanceResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetStudentAttenDanceResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getParentChildAttendance(context: Context): LiveData<GetStudentAttenDanceResponse> {
            val apiResponse: MutableLiveData<GetStudentAttenDanceResponse> = MutableLiveData()
            val url = if (SharedHelper(context).role == "PARENT") {
                Log.d("hghsd", SharedHelper(context).role)
                ATTENDANCE_PROGRESS+"/${SharedHelper(context).childId}"
            } else {
                ATTENDANCE_PROGRESS
            }
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getStdAttendance(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object :
                    ApiResponseCallback<GetStudentAttenDanceResponse>(context, false) {
                    override fun onNext(responseModel: GetStudentAttenDanceResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetStudentAttenDanceResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getStudentAttendance(
            context: Context,
            status: String,
            dateFilter: String
        ): LiveData<GetStudentAttenDanceResponse> {
            val apiResponse: MutableLiveData<GetStudentAttenDanceResponse> = MutableLiveData()
//        val url = ATTENDANCE_PROGRESS
            val url = if (SharedHelper(context).role == "PARENT") {
                Log.d("hghsd", SharedHelper(context).role)
                ATTENDANCE_PROGRESS + "?status=$status&dateFilter=$dateFilter&student=${
                    SharedHelper(
                        context
                    ).childId
                }"
            } else {
                ATTENDANCE_PROGRESS + "?status=$status&dateFilter=$dateFilter"
            }
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getStdAttendance(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object :
                    ApiResponseCallback<GetStudentAttenDanceResponse>(context, false) {
                    override fun onNext(responseModel: GetStudentAttenDanceResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetStudentAttenDanceResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun profile(context: Context): LiveData<ProfileDetailsResponse> {
            val apiResponse: MutableLiveData<ProfileDetailsResponse> = MutableLiveData()
            Log.d("hgdhsg", SharedHelper(context).id)
            val url = if (SharedHelper(context).role == "PARENT") {
                PROFILE + SharedHelper(context).childId
            } else {
                PROFILE + SharedHelper(context).id
            }
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).profile(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<ProfileDetailsResponse>(context, false) {
                    override fun onNext(responseModel: ProfileDetailsResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                        Log.d("API_RESPONSE", responseModel.toString())
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = ProfileDetailsResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun profile(context: Context, id: String): LiveData<ProfileDetailsResponse> {
            val apiResponse: MutableLiveData<ProfileDetailsResponse> = MutableLiveData()
            val url = PROFILE + id
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).profile(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())

                .subscribe(object : ApiResponseCallback<ProfileDetailsResponse>(context, false) {
                    override fun onNext(responseModel: ProfileDetailsResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                        Log.d("API_RESPONSE", responseModel.toString())
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = ProfileDetailsResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun parentChildProfile(context: Context): LiveData<ProfileDetailsResponse> {
            val apiResponse: MutableLiveData<ProfileDetailsResponse> = MutableLiveData()
            val url = if (SharedHelper(context).role == "PARENT") {
                Log.d("hghsd", SharedHelper(context).role)
                PROFILE + "${SharedHelper(context).childId}"
            } else {
                PROFILE + "${SharedHelper(context).childId}"
            }
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).profile(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<ProfileDetailsResponse>(context, false) {
                    override fun onNext(responseModel: ProfileDetailsResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                        Log.d("API_RESPONSE", responseModel.toString())
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = ProfileDetailsResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }
    fun ParentProfile(context: Context): LiveData<ProfileDetailsResponse> {
        val apiResponse: MutableLiveData<ProfileDetailsResponse> = MutableLiveData()
        Log.d("hgdhsg", SharedHelper(context).id)
        val url =  PROFILE + SharedHelper(context).id

        ApiClient.getClient(context)!!.create(ApiDetails::class.java).profile(url)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<ProfileDetailsResponse>(context, false) {
                override fun onNext(responseModel: ProfileDetailsResponse) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                    Log.d("API_RESPONSE", responseModel.toString())
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = ProfileDetailsResponse()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context, e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }

        fun student(context: Context, id: String): LiveData<ProfileDetailsResponse> {
            val apiResponse: MutableLiveData<ProfileDetailsResponse> = MutableLiveData()
            val url = "/${STUDENT_LIST}/${id}"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).profile(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())

                .subscribe(object : ApiResponseCallback<ProfileDetailsResponse>(context, false) {
                    override fun onNext(responseModel: ProfileDetailsResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                        Log.d("API_RESPONSE", responseModel.toString())
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = ProfileDetailsResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun studentprofile(context: Context, id: String): LiveData<StudentProfileResponse> {
            val apiResponse: MutableLiveData<StudentProfileResponse> = MutableLiveData()
            val url = "/${STUDENT_LIST}/${id}"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).stdprofile(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())

                .subscribe(object : ApiResponseCallback<StudentProfileResponse>(context, false) {
                    override fun onNext(responseModel: StudentProfileResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                        Log.d("API_RESPONSE", responseModel.toString())
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = StudentProfileResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun studentProfileTwo(context: Context, id: String): LiveData<ProfileDetailsTwo> {
            val apiResponse: MutableLiveData<ProfileDetailsTwo> = MutableLiveData()
            val url = "/${STUDENT_LIST}/${id}"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).profiletwo(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())

                .subscribe(object : ApiResponseCallback<ProfileDetailsTwo>(context, false) {
                    override fun onNext(responseModel: ProfileDetailsTwo) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                        Log.d("API_RESPONSE", responseModel.toString())
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = ProfileDetailsTwo()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun studentProfilethree(context: Context, id: String): LiveData<StudentProfileDatailstwo> {
            val apiResponse: MutableLiveData<StudentProfileDatailstwo> = MutableLiveData()
            val url = "/${STUDENT_LIST}/${id}"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).profilethree(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<StudentProfileDatailstwo>(context, false) {
                    override fun onNext(responseModel: StudentProfileDatailstwo) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                        Log.d("API_RESPONSE", responseModel.toString())
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = StudentProfileDatailstwo()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun teacherProfile(context: Context, id: String): LiveData<GetTeacherProfileResponse> {
            val apiResponse: MutableLiveData<GetTeacherProfileResponse> = MutableLiveData()
            val url = PROFILE + id
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).teacherProfile1(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<GetTeacherProfileResponse>(context, false) {
                    override fun onNext(responseModel: GetTeacherProfileResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                        Log.d("API_RESPONSE", responseModel.toString())
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetTeacherProfileResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun payRegFee(context: Context): LiveData<PaymentResponse> {
            val apiResponse: MutableLiveData<PaymentResponse> = MutableLiveData()
            val url = REGISTRATION_FEE
            val requestBody = JSONObject()
            requestBody.put("redirectUrl", "https://appschool.aimwindow.in/student-registeration")
            val body: RequestBody =
                requestBody.toString().toRequestBody("application/json".toMediaTypeOrNull())
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).payment(url,body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<PaymentResponse>(context, false) {
                    override fun onNext(responseModel: PaymentResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }
                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = PaymentResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun teacherProfile(context: Context): LiveData<TeacherprofileSignleViewResponse> {
            val apiResponse: MutableLiveData<TeacherprofileSignleViewResponse> = MutableLiveData()
            val url = TEACHER_PROFILE + SharedHelper(context).id
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).teacherProfile(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object :
                    ApiResponseCallback<TeacherprofileSignleViewResponse>(context, false) {
                    override fun onNext(responseModel: TeacherprofileSignleViewResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                        Log.d("API_RESPONSE", responseModel.toString())
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = TeacherprofileSignleViewResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun teacherProfiles(
            context: Context,
            id: String
        ): LiveData<TeacherprofileSignleViewResponse> {
            val apiResponse: MutableLiveData<TeacherprofileSignleViewResponse> = MutableLiveData()
            val url = TEACHER_PROFILE + id
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).teacherProfile(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object :
                    ApiResponseCallback<TeacherprofileSignleViewResponse>(context, false) {
                    override fun onNext(responseModel: TeacherprofileSignleViewResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                        Log.d("API_RESPONSE", responseModel.toString())
                    }
                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = TeacherprofileSignleViewResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun studentBoard(context: Context,id : String): LiveData<StudentBoardResponse> {
            val apiResponse: MutableLiveData<StudentBoardResponse> = MutableLiveData()
            val url = "${STUDENT_BOARD}?school=$id"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).studentBoard(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<StudentBoardResponse>(context, false) {
                    override fun onNext(responseModel: StudentBoardResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }
                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = StudentBoardResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }
    fun studentBoard(context: Context): LiveData<StudentBoardResponse> {
        val apiResponse: MutableLiveData<StudentBoardResponse> = MutableLiveData()
        val url = STUDENT_BOARD
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).studentBoard(url)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<StudentBoardResponse>(context, false) {
                override fun onNext(responseModel: StudentBoardResponse) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = StudentBoardResponse()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context, e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }

        fun studentClsDropdown(context: Context,id: String): LiveData<DropdownResponse> {
            val apiResponse: MutableLiveData<DropdownResponse> = MutableLiveData()
            val url = STUDENT_CLS_DROPDOWN+"?board=$id"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).studentClsDropdown(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<DropdownResponse>(context, false) {
                    override fun onNext(responseModel: DropdownResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = DropdownResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }
    fun studentClsDropdown(context: Context): LiveData<DropdownResponse> {
        val apiResponse: MutableLiveData<DropdownResponse> = MutableLiveData()
        val url = STUDENT_CLS_DROPDOWN
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).studentClsDropdown(url)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<DropdownResponse>(context, false) {
                override fun onNext(responseModel: DropdownResponse) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = DropdownResponse()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context, e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }

        fun schoolDropdown(context: Context): LiveData<SchoolDwRes> {
        val apiResponse: MutableLiveData<SchoolDwRes> = MutableLiveData()
        val url = STUDENT_SCHOOL_DROPDOWN
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).schoolDropdown(url)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<SchoolDwRes>(context, false) {
                override fun onNext(responseModel: SchoolDwRes) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = SchoolDwRes()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context, e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }

        fun studentInfo(context: Context): LiveData<BaseModel> {
            val sharedHelper = SharedHelper(context)
            val apiResponse: MutableLiveData<BaseModel> = MutableLiveData()
            val url = STUDENT_INFO
            val studentInformation: JSONObject = JSONObject()
            studentInformation.put(
                "firstName",
                sharedHelper.studentInfo.getJSONObject(0).getString("showValue")
            )
            studentInformation.put(
                "lastName",
                sharedHelper.studentInfo.getJSONObject(1).getString("showValue")
            )
            studentInformation.put(
                "dob",
                sharedHelper.studentInfo.getJSONObject(2).getString("showValue")
            )
            studentInformation.put(
                "gender",
                sharedHelper.studentInfo.getJSONObject(3).getString("value").toLowerCase()
            )
            studentInformation.put(
                "blood_group",
                sharedHelper.studentInfo.getJSONObject(4).getString("value")
            )
            studentInformation.put(
                "nationality",
                sharedHelper.studentInfo.getJSONObject(5).getString("value")
            )
            studentInformation.put(
                "religion",
                sharedHelper.studentInfo.getJSONObject(6).getString("value")
            )
            studentInformation.put(
                "category",
                sharedHelper.studentInfo.getJSONObject(7).getString("value")
            )
            studentInformation.put(
                "aadhar_number",
                sharedHelper.studentInfo.getJSONObject(8).getString("showValue")
            )
            studentInformation.put(
                "mobile",
                sharedHelper.studentInfo.getJSONObject(9).getString("showValue")
            )
            studentInformation.put(
                "email",
                sharedHelper.studentInfo.getJSONObject(10).getString("showValue")
            )
            studentInformation.put(
                "pincode",
                sharedHelper.studentInfo.getJSONObject(11).getString("showValue")
            )
            studentInformation.put(
                "country",
                sharedHelper.studentInfo.getJSONObject(12).getString("showValue")
            )
            studentInformation.put(
                "state",
                sharedHelper.studentInfo.getJSONObject(13).getString("showValue")
            )
            studentInformation.put(
                "city",
                sharedHelper.studentInfo.getJSONObject(14).getString("showValue")
            )
            studentInformation.put(
                "address",
                sharedHelper.studentInfo.getJSONObject(15).getString("showValue")
            )
            val body: RequestBody =
                studentInformation.toString().toRequestBody("application/json".toMediaTypeOrNull())
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).applicationSave(url, body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<BaseModel>(context, false) {
                    override fun onNext(responseModel: BaseModel) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = BaseModel()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun parentInfo(context: Context): LiveData<BaseModel> {
            val sharedHelper = SharedHelper(context)
            val apiResponse: MutableLiveData<BaseModel> = MutableLiveData()
            val url = PARENT_INFO
            val parentInfo: JSONObject = JSONObject()
            parentInfo.put(
                "fatherName",
                sharedHelper.guardianInfo.getJSONObject(0).getString("showValue")
            )
            parentInfo.put(
                "motherName",
                sharedHelper.guardianInfo.getJSONObject(1).getString("showValue")
            )
            parentInfo.put(
                "guardianName",
                sharedHelper.guardianInfo.getJSONObject(2).getString("showValue")
            )
            parentInfo.put(
                "fatherOccupation",
                sharedHelper.guardianInfo.getJSONObject(3).getString("showValue")
            )
            parentInfo.put(
                "motherOccupation",
                sharedHelper.guardianInfo.getJSONObject(4).getString("showValue")
            )
            parentInfo.put(
                "parentsMobile",
                sharedHelper.guardianInfo.getJSONObject(5).getString("showValue")
            )
            parentInfo.put(
                "emergencyMobile",
                sharedHelper.guardianInfo.getJSONObject(6).getString("showValue")
            )
            parentInfo.put(
                "parentsEmail",
                sharedHelper.guardianInfo.getJSONObject(7).getString("showValue")
            )
            parentInfo.put(
                "parentsAdress",
                sharedHelper.guardianInfo.getJSONObject(8).getString("showValue")
            )
            val body: RequestBody =
                parentInfo.toString().toRequestBody("application/json".toMediaTypeOrNull())
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).applicationSave(url, body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<BaseModel>(context, false) {
                    override fun onNext(responseModel: BaseModel) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = BaseModel()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun academicInfo(context: Context): LiveData<BaseModel> {
            val sharedHelper = SharedHelper(context)
            val apiResponse: MutableLiveData<BaseModel> = MutableLiveData()
            val url = ACADEMIC_INFO
            val academicInfo: JSONObject = JSONObject()
            academicInfo.put(
                "previousSchoolName",
                sharedHelper.academicInfo.getJSONObject(2).getString("showValue")
            )
//        academicInfo.put("previousClassName",sharedHelper.academicInfo.getJSONObject(1).getString("showValue"))
            academicInfo.put(
                "classApplying",
                sharedHelper.academicInfo.getJSONObject(1).getString("value")
            )
            academicInfo.put(
                "boardOfEducation",
                sharedHelper.academicInfo.getJSONObject(0).getString("value")
            )
            val body: RequestBody =
                academicInfo.toString().toRequestBody("application/json".toMediaTypeOrNull())
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).applicationSave(url, body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<BaseModel>(context, false) {
                    override fun onNext(responseModel: BaseModel) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = BaseModel()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun documentInfo(context: Context): LiveData<BaseModel> {
            val sharedHelper = SharedHelper(context)
            val apiResponse: MutableLiveData<BaseModel> = MutableLiveData()
            val url = DOCUMENT_INFO
            val documentInfo: JSONObject = JSONObject()
            documentInfo.put(
                "birthCertificate",
                sharedHelper.documentUpload.getJSONObject(0).getString("value")
            )
            documentInfo.put(
                "aadharCard",
                sharedHelper.documentUpload.getJSONObject(1).getString("value")
            )
            documentInfo.put(
                "studentPhoto",
                sharedHelper.documentUpload.getJSONObject(2).getString("value")
            )
            documentInfo.put(
                "previousSchoolMarksheet",
                sharedHelper.documentUpload.getJSONObject(3).getString("value")
            )
            documentInfo.put(
                "transferCertificate",
                sharedHelper.documentUpload.getJSONObject(4).getString("value")
            )
            documentInfo.put(
                "parentIdProof",
                sharedHelper.documentUpload.getJSONObject(5).getString("value")
            )
            documentInfo.put(
                "addressProof",
                sharedHelper.documentUpload.getJSONObject(6).getString("value")
            )
            val body: RequestBody =
                documentInfo.toString().toRequestBody("application/json".toMediaTypeOrNull())
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).applicationSave(url, body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<BaseModel>(context, false) {
                    override fun onNext(responseModel: BaseModel) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = BaseModel()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun feesPay(
            context: Context,
            fees: String,
            isAdFee: Boolean,
            pOption: String,
            terms: JSONArray
        ): LiveData<PaymentResponse> {
            val apiResponse: MutableLiveData<PaymentResponse> = MutableLiveData()
            val url = INITIAL_FEE_GENERATE + fees
            val requestBody = JSONObject()
            requestBody.put("admissionFee", isAdFee)
            requestBody.put("paymentOptions", pOption)
            requestBody.put("terms", terms)
            requestBody.put("redirectUrl", "https://appschool.aimwindow.in/student-registeration")
            val body: RequestBody =
                requestBody.toString().toRequestBody("application/json".toMediaTypeOrNull())
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).payFees(url, body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<PaymentResponse>(context, false) {
                    override fun onNext(responseModel: PaymentResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = PaymentResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun acknowledge(context: Context, url1: String, url2: String): LiveData<BaseModel> {
            val apiResponse: MutableLiveData<BaseModel> = MutableLiveData()
            val requestBody = JSONObject()
            requestBody.put("signature_url", url1)
            requestBody.put("parent_signature_url", url2)
            val body: RequestBody =
                requestBody.toString().toRequestBody("application/json".toMediaTypeOrNull())
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).approve(body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<BaseModel>(context, false) {
                    override fun onNext(responseModel: BaseModel) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = BaseModel()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun homeStsUpdate(context: Context, homeworkId: String, attach: ArrayList<String>): LiveData<BaseModel> {
            val apiResponse: MutableLiveData<BaseModel> = MutableLiveData()
            val requestBody = JSONObject()
            val url = HOME_WORK_STS_UPDATE + homeworkId
            requestBody.put("attachment", JSONArray(attach))
            val body: RequestBody =
                requestBody.toString().toRequestBody("application/json".toMediaTypeOrNull())
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).hwStsUpd(url, body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<BaseModel>(context, false) {
                    override fun onNext(responseModel: BaseModel) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = BaseModel()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun classTestStsUpdate(context: Context, classTestId: String, attach: ArrayList<String>): LiveData<BaseModel> {
            val apiResponse: MutableLiveData<BaseModel> = MutableLiveData()
            val requestBody = JSONObject()
            val url = CLS_TEST_MARK_UPDATE + classTestId
            requestBody.put("attachment", JSONArray(attach))
            val body: RequestBody =
                requestBody.toString().toRequestBody("application/json".toMediaTypeOrNull())
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).clsTestStsUpd(url, body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<BaseModel>(context, false) {
                    override fun onNext(responseModel: BaseModel) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = BaseModel()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun undoHomework(context: Context, homeworkId: String): LiveData<BaseModel> {
            val apiResponse: MutableLiveData<BaseModel> = MutableLiveData()
            val requestBody = JSONObject()
            val url = HOME_WORK_STS_UPDATE + homeworkId
            requestBody.put("status", "pending")
            val body: RequestBody =
                requestBody.toString().toRequestBody("application/json".toMediaTypeOrNull())
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).hwStsUpd(url, body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<BaseModel>(context, false) {
                    override fun onNext(responseModel: BaseModel) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = BaseModel()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun homeworkMarkUpd(
            context: Context,
            homeworkId: String,
            studentId: String,
            remark: String,
            marks: String
        ): LiveData<BaseModel> {
            val apiResponse: MutableLiveData<BaseModel> = MutableLiveData()
            val requestBody = JSONObject()
            val url = HOME_WORK_MARK_UPDATE + homeworkId
            requestBody.put("student", studentId)
            requestBody.put("remarks", remark)
            requestBody.put("mark", marks)
            val body: RequestBody =
                requestBody.toString().toRequestBody("application/json".toMediaTypeOrNull())
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).hwStsUpd(url, body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<BaseModel>(context, false) {
                    override fun onNext(responseModel: BaseModel) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = BaseModel()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun projectMarkUpd(  context: Context,  projectId: String,   studentId: String, remark: String, mark: String ): LiveData<BaseModel> {
            val apiResponse: MutableLiveData<BaseModel> = MutableLiveData()
            val requestBody = JSONObject()
            val url = PROJECT_MARK_UPDATE + projectId
            requestBody.put("student", studentId)
            requestBody.put("remarks", remark)
            requestBody.put("scored_marks", mark)
            val body: RequestBody =
                requestBody.toString().toRequestBody("application/json".toMediaTypeOrNull())
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).prjStsUpd(url, body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<BaseModel>(context, false) {
                    override fun onNext(responseModel: BaseModel) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = BaseModel()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun assignmentStsUpdate(
            context: Context,
            assignmentId: String,
            attach: ArrayList<String>
        ): LiveData<BaseModel> {
            val apiResponse: MutableLiveData<BaseModel> = MutableLiveData()
            val requestBody = JSONObject()
            val url = ASSIGNMENT_STS_UPDATE + assignmentId
            requestBody.put("attachment", JSONArray(attach))
            val body: RequestBody =  requestBody.toString().toRequestBody("application/json".toMediaTypeOrNull())
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).assStsUpd(url, body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<BaseModel>(context, false) {
                    override fun onNext(responseModel: BaseModel) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = BaseModel()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

    fun ExamMarkUpd(  context: Context,attendance: String,scoredMark: String, scoredPracticalMark: Int, attach: String ,resid : String): LiveData<BaseModel> {
        val apiResponse: MutableLiveData<BaseModel> = MutableLiveData()
        val requestBody = JSONObject()
        val url = Exam_MARK_UPDATE + resid
        requestBody.put("attendance", attendance)
        requestBody.put("scoredMark", scoredMark)
        requestBody.put("scoredPracticalMark", scoredPracticalMark)
        requestBody.put("answerSheet", attach)
        val body: RequestBody =requestBody.toString().toRequestBody("application/json".toMediaTypeOrNull())
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).prjStsUpd1 (url, body)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<BaseModel>(context, false) {
                override fun onNext(responseModel: BaseModel) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = BaseModel()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context, e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }

        fun complaintSolve(
            context: Context,
            id: String,
            attach: ArrayList<String>,
            title: String
        ): LiveData<BaseModel> {
            val apiResponse: MutableLiveData<BaseModel> = MutableLiveData()
            val requestBody = JSONObject()
            val url = COMPLAINT_STATUS + id
            requestBody.put("attachment", JSONArray(attach))
            requestBody.put("status", "solved")
            requestBody.put("solution", title)
            val body: RequestBody =
                requestBody.toString().toRequestBody("application/json".toMediaTypeOrNull())
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).assStsUpd(url, body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<BaseModel>(context, false) {
                    override fun onNext(responseModel: BaseModel) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = BaseModel()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun complaintRaise(
            context: Context,
            title: String,
            des: String,
            type: String,
            toWhom: String,
            attach: ArrayList<String>
        ): LiveData<BaseModel> {
            val apiResponse: MutableLiveData<BaseModel> = MutableLiveData()
            val requestBody = JSONObject()
            val url = COMPLAINTS
            requestBody.put("title", title)
            requestBody.put("description", des)
            requestBody.put("type", type)
            requestBody.put("toWhom", toWhom)
            requestBody.put("attachment", JSONArray(attach))
            val body: RequestBody =
                requestBody.toString().toRequestBody("application/json".toMediaTypeOrNull())
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).complaintSolve(url, body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<BaseModel>(context, false) {
                    override fun onNext(responseModel: BaseModel) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = BaseModel()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun clsTestMarkUpd(
            context: Context,
            clsTestId: String,
            attach: ArrayList<String>,
            studentId: String,
            mark: String,
            remark: String
        ): LiveData<BaseModel> {
            val apiResponse: MutableLiveData<BaseModel> = MutableLiveData()
            val requestBody = JSONObject()
            val url = CLS_TEST_MARK_UPDATE + clsTestId
            requestBody.put("student", studentId)
            requestBody.put("scored_marks", mark)
            requestBody.put("remarks", remark)
            requestBody.put("attachment", JSONArray(attach))
            val body: RequestBody =
                requestBody.toString().toRequestBody("application/json".toMediaTypeOrNull())
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).clsTestStsUpd(url, body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<BaseModel>(context, false) {
                    override fun onNext(responseModel: BaseModel) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = BaseModel()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun examMarkUpd(
            context: Context,
            examId: String,
            attendance: String,
            scoredPracticalMark: String,
            scoredMark: String,
            ansSheet: String
        ): LiveData<BaseModel> {
            val apiResponse: MutableLiveData<BaseModel> = MutableLiveData()
            val requestBody = JSONObject()
            val url = EXAM_MARK_UPDATE + examId
            requestBody.put("attendance", attendance)
            requestBody.put("scoredPracticalMark", scoredPracticalMark.toInt())
            requestBody.put("scoredMark", scoredMark.toInt())
            requestBody.put("answerSheet", ansSheet)
            val body: RequestBody =
                requestBody.toString().toRequestBody("application/json".toMediaTypeOrNull())
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).examStsUpd(url, body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<BaseModel>(context, false) {
                    override fun onNext(responseModel: BaseModel) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = BaseModel()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun assignmentMarkUpdate(
            context: Context,
            assignmentId: String,
            studentId: String,
            remark: String,
            mark: String
        ): LiveData<BaseModel> {
            val apiResponse: MutableLiveData<BaseModel> = MutableLiveData()
            val requestBody = JSONObject()
            val url = ASSIGNMENT_MARK_UPDATE + assignmentId
            requestBody.put("student", studentId)
            requestBody.put("remarks", remark)
            requestBody.put("scored_marks", mark)
            val body: RequestBody =
                requestBody.toString().toRequestBody("application/json".toMediaTypeOrNull())
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).assStsUpd(url, body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<BaseModel>(context, false) {
                    override fun onNext(responseModel: BaseModel) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = BaseModel()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun projectStsUpdate(
            context: Context,
            projectId: String,
            attach: ArrayList<String>
        ): LiveData<BaseModel> {
            val apiResponse: MutableLiveData<BaseModel> = MutableLiveData()
            val requestBody = JSONObject()
            val url = PROJECT_STS_UPDATE + projectId
            requestBody.put("attachment", JSONArray(attach))
            val body: RequestBody =
                requestBody.toString().toRequestBody("application/json".toMediaTypeOrNull())
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).prjStsUpd(url, body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<BaseModel>(context, false) {
                    override fun onNext(responseModel: BaseModel) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = BaseModel()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun attendanceUpdate(
            context: Context,
            student: String,
            date: String,
            sts: String
        ): LiveData<BaseModel> {
            val apiResponse: MutableLiveData<BaseModel> = MutableLiveData()
            val requestBody = JSONObject()
            val url = ATTENDANCE
            requestBody.put("student", student)
            requestBody.put("date", date)
            requestBody.put("status", sts)
            val body: RequestBody =
                requestBody.toString().toRequestBody("application/json".toMediaTypeOrNull())
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).prjStsUpd(url, body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<BaseModel>(context, false) {
                    override fun onNext(responseModel: BaseModel) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = BaseModel()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }


        fun feesOnline(context: Context): LiveData<GetFeesOnlineResponse> {
            val apiResponse: MutableLiveData<GetFeesOnlineResponse> = MutableLiveData()
            val url = GUEST_FEE
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).feesOnline(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<GetFeesOnlineResponse>(context, false) {
                    override fun onNext(responseModel: GetFeesOnlineResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetFeesOnlineResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun guestFees(context: Context): LiveData<GuestFeesResponse> {
            val apiResponse: MutableLiveData<GuestFeesResponse> = MutableLiveData()
            val url = GUEST_FEE
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).feesGuest(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<GuestFeesResponse>(context, false) {
                    override fun onNext(responseModel: GuestFeesResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GuestFeesResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun updatesCount(context: Context): LiveData<UpdatesCountResponse> {
            val apiResponse: MutableLiveData<UpdatesCountResponse> = MutableLiveData()
            val url = TODAY_UPDATES_COUNT
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).updateCount(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<UpdatesCountResponse>(context, false) {
                    override fun onNext(responseModel: UpdatesCountResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = UpdatesCountResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun projectCount(context: Context): LiveData<ProjectUpdCountResponse> {
            val apiResponse: MutableLiveData<ProjectUpdCountResponse> = MutableLiveData()
            val url = PROJECT_COUNT
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).projectCount(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<ProjectUpdCountResponse>(context, false) {
                    override fun onNext(responseModel: ProjectUpdCountResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = ProjectUpdCountResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun assignmentCount(context: Context): LiveData<AssignmentUpdCountResponse> {
            val apiResponse: MutableLiveData<AssignmentUpdCountResponse> = MutableLiveData()
            val url = ASIGNMENT_COUNT
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).assignmentCount(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object :
                    ApiResponseCallback<AssignmentUpdCountResponse>(context, false) {
                    override fun onNext(responseModel: AssignmentUpdCountResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = AssignmentUpdCountResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun attendance(context: Context, datefilter: String): LiveData<AttendanceProgressResponse> {
            val apiResponse: MutableLiveData<AttendanceProgressResponse> = MutableLiveData()
//        val url = STUDENT_ATTENDANCE+"?datefilter=$datefilter"
            val url = if (SharedHelper(context).role == "PARENT") {
                Log.d("hghsjhjhjhjhd", SharedHelper(context).role)
                STUDENT_ATTENDANCE + "?datefilter=$datefilter&student=${SharedHelper(context).childId}"
            } else {
                STUDENT_ATTENDANCE + "?datefilter=$datefilter"
            }
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).attendance(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object :
                    ApiResponseCallback<AttendanceProgressResponse>(context, false) {
                    override fun onNext(responseModel: AttendanceProgressResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = AttendanceProgressResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        /* fun studentInfoAttendance(context: Context,role:String):LiveData<GetOverallAttendanceProgressResponse>{
        val apiResponse: MutableLiveData<GetOverallAttendanceProgressResponse> = MutableLiveData()
        val url = STUDENTINFO_ATTENDANCE+"?role=$role"
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).getOverallAttendanceProgress(url)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<GetOverallAttendanceProgressResponse>(context, false) {
                override fun onNext(responseModel: GetOverallAttendanceProgressResponse) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }
                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = GetOverallAttendanceProgressResponse()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context,e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }*/

        fun studentAttendance(context: Context, program: String,status: String,search: String): LiveData<GetAttendanceResponse> {
            val apiResponse: MutableLiveData<GetAttendanceResponse> = MutableLiveData()
//        val url = ATTENDANCE+"?date=$date&program=$program&perPage=100&currentPage=0"
            val url = ATTENDANCE + "?program=$program&status=$status&search=$search&perPage=100&currentPage=0"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getAttendance(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<GetAttendanceResponse>(context, false) {
                    override fun onNext(responseModel: GetAttendanceResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetAttendanceResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getTeacherAssignment(
            context: Context,
            search: String,
            program: String,
            status: String,
            sub: String
        ): LiveData<GetTeacherAssignmentResponse> {
            val apiResponse: MutableLiveData<GetTeacherAssignmentResponse> = MutableLiveData()
            val url =
                "$TEACHER_ASSIGNMENT?search=$search&program=$program&status=$status&subject=$sub"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getTeacherAssignment(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object :
                    ApiResponseCallback<GetTeacherAssignmentResponse>(context, false) {
                    override fun onNext(responseModel: GetTeacherAssignmentResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetTeacherAssignmentResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getAssignmentSingleView(
            context: Context,
            id: String
        ): LiveData<GetTeacherAssignmentSingleResponse> {
            val apiResponse: MutableLiveData<GetTeacherAssignmentSingleResponse> = MutableLiveData()
            val url = TEACHER_ASSIGNMENT + "/$id"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getSingleAssignments(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object :
                    ApiResponseCallback<GetTeacherAssignmentSingleResponse>(context, false) {
                    override fun onNext(responseModel: GetTeacherAssignmentSingleResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetTeacherAssignmentSingleResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getAssignmentSingleView2(
            context: Context,
            id: String
        ): LiveData<TeacherAssignment2Response> {
            val apiResponse: MutableLiveData<TeacherAssignment2Response> = MutableLiveData()
            val url = TEACHER_ASSIGNMENT + "/$id"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getSingleAssignments2(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object :
                    ApiResponseCallback<TeacherAssignment2Response>(context, false) {
                    override fun onNext(responseModel: TeacherAssignment2Response) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = TeacherAssignment2Response()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getAssignmentSingleView3(
            context: Context,
            id: String,
            markStatus: String,
            search: String,
            status: String
        ): LiveData<TeacherAssSingleViewResponse> {
            val apiResponse: MutableLiveData<TeacherAssSingleViewResponse> = MutableLiveData()
            val url =
                "${STD_ASSIGNMENT_URL}/?assignment=${id}&search=$search&markStatus=${markStatus}&status=$status"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getSingleAssignments3(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object :
                    ApiResponseCallback<TeacherAssSingleViewResponse>(context, false) {
                    override fun onNext(responseModel: TeacherAssSingleViewResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = TeacherAssSingleViewResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getTeacherClassTest(
            context: Context,
            search: String,
            sub: String,
            program: String,
            status: String
        ): LiveData<GetTeacherClassTestResponse> {
            val apiResponse: MutableLiveData<GetTeacherClassTestResponse> = MutableLiveData()
            val url =
                TEACHER_CLASS_TEST + "?status=$status&program=$program&search=$search&subject=$sub"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getTeacherClassTest(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object :
                    ApiResponseCallback<GetTeacherClassTestResponse>(context, false) {
                    override fun onNext(responseModel: GetTeacherClassTestResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetTeacherClassTestResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }
        fun getCompletedStudents(context: Context, id: String): LiveData<AssSglViewResponse> {
            val apiResponse: MutableLiveData<AssSglViewResponse> = MutableLiveData()
            val url = STD_ASSIGNMENT_URL + "?assignment=$id"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).stdass(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<AssSglViewResponse>(context, false) {
                    override fun onNext(responseModel: AssSglViewResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = AssSglViewResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getMyTeachers(context: Context): LiveData<GetMyTeachersResponse> {
            val apiResponse: MutableLiveData<GetMyTeachersResponse> = MutableLiveData()
            val url = MY_TEACHER
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).myTeachers(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<GetMyTeachersResponse>(context, false) {
                    override fun onNext(responseModel: GetMyTeachersResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetMyTeachersResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun adminFees(context: Context, id: String, search: String): LiveData<AdminFeesResponse> {
            val apiResponse: MutableLiveData<AdminFeesResponse> = MutableLiveData()
            val url = if (SharedHelper(context).role == "ADMIN") {
                Log.d("hghsd", SharedHelper(context).role)
                ADMIN_FEES + "?board=$id&search=$search"
            } else {
                ADMIN_FEES + "?board=$id"
            }
            Log.d("api_url_debug", "AdminFees URL: $url")
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getAdminFees(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<AdminFeesResponse>(context, false) {
                    override fun onNext(responseModel: AdminFeesResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = AdminFeesResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun adminFees1(context: Context, id: String, search: String): LiveData<AdminSingleViewResponse> {
        val apiResponse: MutableLiveData<AdminSingleViewResponse> = MutableLiveData()
        val url =   "${ADMIN_FEES}/$id"
        Log.d("api_url_debug", "AdminFees URL: $url")
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).studentFees1(url)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<AdminSingleViewResponse>(context, false) {
                override fun onNext(responseModel: AdminSingleViewResponse) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = AdminSingleViewResponse()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context, e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }

        fun submissionProgress(
            context: Context,
            type: String
        ): LiveData<SubmissionProgressResponse> {
            val apiResponse: MutableLiveData<SubmissionProgressResponse> = MutableLiveData()
//        val url = SUBMISSION_PROGRESS+type
            val url = if (SharedHelper(context).role == "PARENT") {
                Log.d("hghsd", SharedHelper(context).role)
                SUBMISSION_PROGRESS + type + "&student=${SharedHelper(context).childId}"
            } else {
                SUBMISSION_PROGRESS + type
            }
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).submitionHomework(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object :
                    ApiResponseCallback<SubmissionProgressResponse>(context, false) {
                    override fun onNext(responseModel: SubmissionProgressResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = SubmissionProgressResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun studentFees(context: Context): LiveData<StudentFeeResponse> {
            val apiResponse: MutableLiveData<StudentFeeResponse> = MutableLiveData()
            val url = if (SharedHelper(context).role == "PARENT") {
                STUDENTFEES + "?student=${SharedHelper(context).childId}"
            } else {
                STUDENTFEES
            }
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).studentFees(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<StudentFeeResponse>(context, false) {
                    override fun onNext(responseModel: StudentFeeResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = StudentFeeResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getStatsProgressPoints(context: Context): LiveData<GetProgressPointsStatsResponse> {
            val apiResponse: MutableLiveData<GetProgressPointsStatsResponse> = MutableLiveData()
            val url = if (SharedHelper(context).role == "PARENT") {
                STATS_PROGRESS_POINTS + "?student=${SharedHelper(context).childId}"
            } else {
                STATS_PROGRESS_POINTS
            }
            ApiClient.getClient(context)!!.create(ApiDetails::class.java)
                .getStatsProgressPoints(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object :
                    ApiResponseCallback<GetProgressPointsStatsResponse>(context, false) {
                    override fun onNext(responseModel: GetProgressPointsStatsResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetProgressPointsStatsResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun adminFeeChartbar(
            context: Context,
            id: String
        ): LiveData<GetAdminOverallFeeBarchartResponse> {
            val apiResponse: MutableLiveData<GetAdminOverallFeeBarchartResponse> = MutableLiveData()
            val url = ADMIN_OVERALL_FEE_PROGRESS + "?classId=$id"
            Log.d("api_url_debug", "AdminFees URL: $url")
            ApiClient.getClient(context)!!.create(ApiDetails::class.java)
                .adminOverallFeeBarchartProgress(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object :
                    ApiResponseCallback<GetAdminOverallFeeBarchartResponse>(context, false) {
                    override fun onNext(responseModel: GetAdminOverallFeeBarchartResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetAdminOverallFeeBarchartResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun getTeacherStudentStatsCounts(context: Context): LiveData<GetTeacherStudentStatsCountRes> {
            val apiResponse: MutableLiveData<GetTeacherStudentStatsCountRes> = MutableLiveData()
            val url = TEACHER_MYCLASS_POINTS_COUNT

            ApiClient.getClient(context)!!.create(ApiDetails::class.java)
                .GetTeacherStudentStatsCount(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object :
                    ApiResponseCallback<GetTeacherStudentStatsCountRes>(context, false) {
                    override fun onNext(responseModel: GetTeacherStudentStatsCountRes) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetTeacherStudentStatsCountRes()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun paymentHistory(context: Context): LiveData<GetTransactionResponse> {
            val apiResponse: MutableLiveData<GetTransactionResponse> = MutableLiveData()
            val url = if (SharedHelper(context).role == "PARENT") {
                PAYMENT_HISTORY + "?student=${SharedHelper(context).childId}"
            } else {
                PAYMENT_HISTORY
            }
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).paymentHistory(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<GetTransactionResponse>(context, false) {
                    override fun onNext(responseModel: GetTransactionResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetTransactionResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun TeacherAssCount(context: Context): LiveData<TeacherAssCountResponse> {
            val apiResponse: MutableLiveData<TeacherAssCountResponse> = MutableLiveData()
            val url = TEACHER_ASS_COUNT
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).teacherAsscnt(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<TeacherAssCountResponse>(context, false) {
                    override fun onNext(responseModel: TeacherAssCountResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = TeacherAssCountResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun leaderBoard(context: Context, search: String, exam: String): LiveData<GetLeaderboardResponse> {
            val apiResponse: MutableLiveData<GetLeaderboardResponse> = MutableLiveData()
            val url = if (SharedHelper(context).role == "PARENT") {
                LEADER_BOARD + "?search=$search&exam=$exam&${SharedHelper(context).childId}"
            } else {
                LEADER_BOARD + "?search=$search&exam=$exam"
            }
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).leaderBoard(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<GetLeaderboardResponse>(context, false) {
                    override fun onNext(responseModel: GetLeaderboardResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetLeaderboardResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun scoreBoard(context: Context): LiveData<GetScoreboardResponse> {
            val apiResponse: MutableLiveData<GetScoreboardResponse> = MutableLiveData()
            val url = SCORE_BOARD
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).scoreBoard(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<GetScoreboardResponse>(context, false) {
                    override fun onNext(responseModel: GetScoreboardResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetScoreboardResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun pointsHistory(context: Context): LiveData<PointsHistoryResponse> {
            val apiResponse: MutableLiveData<PointsHistoryResponse> = MutableLiveData()
            val url = POINTS_HISTORY
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).pointsHistory(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<PointsHistoryResponse>(context, false) {
                    override fun onNext(responseModel: PointsHistoryResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = PointsHistoryResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun parentProfile(context: Context): LiveData<ParentProfileResponse> {
            val apiResponse: MutableLiveData<ParentProfileResponse> = MutableLiveData()
            val url = PROFILE
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).parentProfile(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<ParentProfileResponse>(context, false) {
                    override fun onNext(responseModel: ParentProfileResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = ParentProfileResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

    fun leaveRequest(  context: Context,  status: String, str: String, role: String): LiveData<LeaveRequestResponse> {
            val apiResponse: MutableLiveData<LeaveRequestResponse> = MutableLiveData()
            val url = LEAVE_REQUEST + "?search=$str&role=$role&status=$status&perPage=10&currentPage=0"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).leaveRequest(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<LeaveRequestResponse>(context, false) {
                    override fun onNext(responseModel: LeaveRequestResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = LeaveRequestResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }
    fun leaveRequest(  context: Context,  status: String, str: String): LiveData<LeaveRequestResponse> {
        val apiResponse: MutableLiveData<LeaveRequestResponse> = MutableLiveData()
        val url = LEAVE_REQUEST + "?search=$str&status=$status&perPage=10&currentPage=0"
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).leaveRequest(url)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<LeaveRequestResponse>(context, false) {
                override fun onNext(responseModel: LeaveRequestResponse) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = LeaveRequestResponse()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context, e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }

    fun availableleaves( context: Context): LiveData<AvailableLeavesRes> {
        val apiResponse: MutableLiveData<AvailableLeavesRes> = MutableLiveData()
        val url = LEAVE_AVAITABLE
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).availableLeave(url)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<AvailableLeavesRes>(context, false) {
                override fun onNext(responseModel: AvailableLeavesRes) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = AvailableLeavesRes()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context, e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }

        fun leaveUpd(context: Context, title: String, description: String, type: String, startDate: String, endDate: String, attach: ArrayList<String>): LiveData<BaseModel> {
            val apiResponse: MutableLiveData<BaseModel> = MutableLiveData()
            val requestBody = JSONObject()
            val url = LEAVE_REQUEST
            requestBody.put("title", title)
            requestBody.put("description", description)
            requestBody.put("type", type)
            requestBody.put("startDate", startDate)
            requestBody.put("endDate", endDate)
            requestBody.put("attachment", JSONArray(attach))
            val body: RequestBody =
                requestBody.toString().toRequestBody("application/json".toMediaTypeOrNull())
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).leaverequest(url, body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<BaseModel>(context, false) {
                    override fun onNext(responseModel: BaseModel) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = BaseModel()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun leaveDropdown(context: Context): LiveData<LeaveDropdownResponse> {
            val apiResponse: MutableLiveData<LeaveDropdownResponse> = MutableLiveData()
            val url = LEAVE_DROP
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).leaveDrop(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<LeaveDropdownResponse>(context, false) {
                    override fun onNext(responseModel: LeaveDropdownResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = LeaveDropdownResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun examResultall(context: Context, status: String, id: String, search: String,sub: String): LiveData<StudentExamResultResponse> {
            val apiResponse: MutableLiveData<StudentExamResultResponse> = MutableLiveData()
            val url = EXAM_RESULT + "?completeStatus=$status&exam_id=$id&search=$search&subject=$sub"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getStudentExam1(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<StudentExamResultResponse>(context, false) {
                    override fun onNext(responseModel: StudentExamResultResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = StudentExamResultResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }
    fun examResultall1(context: Context, status: String, id: String, search: String,sub: String): LiveData<StudentExamResultResponse> {
        val apiResponse: MutableLiveData<StudentExamResultResponse> = MutableLiveData()
        val url = EXAM_RESULT + "?status=$status&exam_id=$id&search=$search&subject=$sub"
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).getStudentExam1(url)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<StudentExamResultResponse>(context, false) {
                override fun onNext(responseModel: StudentExamResultResponse) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = StudentExamResultResponse()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context, e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }

        fun leaveApprove(context: Context,  status: String,  id: String,    reason: String): LiveData<BaseModel> {
            val apiResponse: MutableLiveData<BaseModel> = MutableLiveData()
            val requestBody = JSONObject()
            val url = LEAVE_REQUEST_APPROVE + id
            requestBody.put("status", status)
            requestBody.put("rejectReason", reason)
            val body: RequestBody =
                requestBody.toString().toRequestBody("application/json".toMediaTypeOrNull())
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).leaveApprove(url, body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<BaseModel>(context, false) {
                    override fun onNext(responseModel: BaseModel) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = BaseModel()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }
        fun leaveApprove(context: Context,  status: String, id: String): LiveData<BaseModel> {
        val apiResponse: MutableLiveData<BaseModel> = MutableLiveData()
        val requestBody = JSONObject()
        val url = LEAVE_REQUEST_APPROVE + id
        requestBody.put("status", status)
        val body: RequestBody =
            requestBody.toString().toRequestBody("application/json".toMediaTypeOrNull())
        ApiClient.getClient(context)!!.create(ApiDetails::class.java).leaveApprove(url, body)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiResponseCallback<BaseModel>(context, false) {
                override fun onNext(responseModel: BaseModel) {
                    super.onNext(responseModel)
                    responseModel.success = true
                    apiResponse.value = responseModel
                }
                override fun onError(e: Throwable) {
                    super.onError(e)
                    val response = BaseModel()
                    response.success = false
                    response.msg = NetworkHelper.getErrorMessage(context, e)
                    apiResponse.value = response
                }
            })
        return apiResponse
    }

        fun adminattendance(
            context: Context,
            datefilter: String,
            role: String
        ): LiveData<AdminAttendanceResponse> {
            val apiResponse: MutableLiveData<AdminAttendanceResponse> = MutableLiveData()
            var url = ADMIN_ATTENDANCE + "?role=$role&dateFilter=${datefilter}"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).adminAtt(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<AdminAttendanceResponse>(context, false) {
                    override fun onNext(responseModel: AdminAttendanceResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = AdminAttendanceResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun teacherattendance(
            context: Context,
            datefilter: String,
            role: String,
            id: String
        ): LiveData<AdminAttendanceResponse> {
            val apiResponse: MutableLiveData<AdminAttendanceResponse> = MutableLiveData()
            var url = ADMIN_ATTENDANCE + "?role=$role&dateFilter=${datefilter}&program=$id"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).adminAtt(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<AdminAttendanceResponse>(context, false) {
                    override fun onNext(responseModel: AdminAttendanceResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = AdminAttendanceResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun teacherStudentAttendance(
            context: Context,
            datefilter: String,
            role: String,
            id: String
        ): LiveData<GetOverallStudentAttendProgressRes> {
            val apiResponse: MutableLiveData<GetOverallStudentAttendProgressRes> = MutableLiveData()
            var url =
                TEACHER_STUDENT_ATTENDANCE + "?role=$role&dateFilter=${datefilter}&program=$id"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java)
                .GetTeacherOverallStudentAttendance(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object :
                    ApiResponseCallback<GetOverallStudentAttendProgressRes>(context, false) {
                    override fun onNext(responseModel: GetOverallStudentAttendProgressRes) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetOverallStudentAttendProgressRes()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun teacheroverlAtt(
            context: Context,
            datefilter: String
        ): LiveData<TeacherAttendanceResponse> {
            val apiResponse: MutableLiveData<TeacherAttendanceResponse> = MutableLiveData()
            var url = TEACHER_ATT_PROGRESS + "dateFilter=${datefilter}"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).teacherAtt(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<TeacherAttendanceResponse>(context, false) {
                    override fun onNext(responseModel: TeacherAttendanceResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = TeacherAttendanceResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun submissionteacherSide(
            context: Context,
            type: String,
            id: String
        ): LiveData<SubmissionProgressTeacherSide> {
            val apiResponse: MutableLiveData<SubmissionProgressTeacherSide> = MutableLiveData()
            var url = TEACHER_SIDE_SUB + id + "?type=${type}"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).teacherSub(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object :
                    ApiResponseCallback<SubmissionProgressTeacherSide>(context, false) {
                    override fun onNext(responseModel: SubmissionProgressTeacherSide) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = SubmissionProgressTeacherSide()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun examClassTestProgress(context: Context, type: String, id: String): LiveData<SubjectWiseClassExamProResponse> {
            val apiResponse: MutableLiveData<SubjectWiseClassExamProResponse> = MutableLiveData()
            var url = EXAM_PROGRESS + type +"&program=${id}"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).exampro(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object :
                    ApiResponseCallback<SubjectWiseClassExamProResponse>(context, false) {
                    override fun onNext(responseModel: SubjectWiseClassExamProResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = SubjectWiseClassExamProResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun studentSingleViewAtt(
            context: Context,
            id: String
        ): LiveData<GetStudentAttenDanceResponse> {
            val apiResponse: MutableLiveData<GetStudentAttenDanceResponse> = MutableLiveData()
            val url = ATTENDANCE_PROGRESS
//        val url = ATTENDANCE_PROGRESS + "/${id}"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).getStdAttendance(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object :
                    ApiResponseCallback<GetStudentAttenDanceResponse>(context, false) {
                    override fun onNext(responseModel: GetStudentAttenDanceResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = GetStudentAttenDanceResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun singlestudentSubmission(
            context: Context,
            type: String,
            id: String
        ): LiveData<SubmissionProgressResponse> {
            val apiResponse: MutableLiveData<SubmissionProgressResponse> = MutableLiveData()
//        val url = SUBMISSION_PROGRESS+type
            val url = SUBMISSION_PROGRESS_SINGLE + "?type=${type}" + "&student=${id}"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).submitionHomework(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object :
                    ApiResponseCallback<SubmissionProgressResponse>(context, false) {
                    override fun onNext(responseModel: SubmissionProgressResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = SubmissionProgressResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun staffProfile(context: Context, id: String): LiveData<TeacherprofileSignleViewResponse> {
            val apiResponse: MutableLiveData<TeacherprofileSignleViewResponse> = MutableLiveData()
            val url = STAFF_PROFILE + id
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).teacherProfile(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object :
                    ApiResponseCallback<TeacherprofileSignleViewResponse>(context, false) {
                    override fun onNext(responseModel: TeacherprofileSignleViewResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                        Log.d("API_RESPONSE", responseModel.toString())
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = TeacherprofileSignleViewResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun myClassStudents(
            context: Context,
            id: String,
            role: String
        ): LiveData<MyClassAttendanceResponse> {
            val apiResponse: MutableLiveData<MyClassAttendanceResponse> = MutableLiveData()
            val url = MY_CLASS_STUDENTS + "?role=${role}&program=${id}"
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).myclsstd(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<MyClassAttendanceResponse>(context, false) {
                    override fun onNext(responseModel: MyClassAttendanceResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                        Log.d("API_RESPONSE", responseModel.toString())
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = MyClassAttendanceResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun teacherStats(context: Context): LiveData<TeacherStatsResponse> {
            val apiResponse: MutableLiveData<TeacherStatsResponse> = MutableLiveData()
            val url = TEACHER_STATS
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).teacherstats(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<TeacherStatsResponse>(context, false) {
                    override fun onNext(responseModel: TeacherStatsResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                        Log.d("API_RESPONSE", responseModel.toString())
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = TeacherStatsResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }

        fun teacherTimeTable(context: Context): LiveData<TeacherTimeTableResponse> {
            val apiResponse: MutableLiveData<TeacherTimeTableResponse> = MutableLiveData()
            val url = TEACHER_TBL
            ApiClient.getClient(context)!!.create(ApiDetails::class.java).teachertimeTbl(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiResponseCallback<TeacherTimeTableResponse>(context, false) {
                    override fun onNext(responseModel: TeacherTimeTableResponse) {
                        super.onNext(responseModel)
                        responseModel.success = true
                        apiResponse.value = responseModel
                        Log.d("API_RESPONSE", responseModel.toString())
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        val response = TeacherTimeTableResponse()
                        response.success = false
                        response.msg = NetworkHelper.getErrorMessage(context, e)
                        apiResponse.value = response
                    }
                })
            return apiResponse
        }
    }


