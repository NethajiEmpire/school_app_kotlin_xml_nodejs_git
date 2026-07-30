package com.lms.sch.network

import com.lms.sch.response.AdminSingleViewResponse
import com.lms.sch.models.BaseModel
import com.lms.sch.response.AdminFeesResponse
import com.lms.sch.response.AssignmentUpdCountResponse
import com.lms.sch.response.AttendanceProgressResponse
import com.lms.sch.response.BatchDropdownResponse
import com.lms.sch.response.ClassTestResponse
import com.lms.sch.response.GetClassTestResponse
import com.lms.sch.response.GetClassTimeTableResponse
import com.lms.sch.response.GetDocumentMaster
import com.lms.sch.response.ProgramBasedSubjectResponse
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
import com.lms.sch.response.ParentProfileResponse
import com.lms.sch.response.GetTeacherProjectResponse
import com.lms.sch.response.ProjectSingleViewResponse
import com.lms.sch.response.GetTeacherResponse
import com.lms.sch.response.GetTimeTableResponse
import com.lms.sch.response.TeacherHwSingleResponse
import com.lms.sch.response.GuestFeesResponse
import com.lms.sch.response.NoticeBoardResponse
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
import com.lms.sch.response.GetOverallAttendanceProgressResponse
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
import com.lms.sch.response.ProgramResponse
import com.lms.sch.response.ProjectUpdCountResponse
import com.lms.sch.response.StudentExamResponse
import com.lms.sch.response.StudentExamResultResponse
import com.lms.sch.response.StudentFeeResponse
import com.lms.sch.response.StudentListAnalyticsResponse
import com.lms.sch.response.StudentProjectResponse
import com.lms.sch.response.StudentSingleVIewResponse
import com.lms.sch.response.SubmissionAssignmentResponse
import com.lms.sch.response.SubmissionProgressResponse
import com.lms.sch.response.SubmissionProjectResponse
import com.lms.sch.response.TeacherAssCountResponse
import com.lms.sch.response.AdminAttendanceResponse
import com.lms.sch.response.AssSglViewResponse
import com.lms.sch.response.AvailableLeavesRes
import com.lms.sch.response.GetAdminStatsReponse
import com.lms.sch.response.GetProgressPointsStatsResponse
import com.lms.sch.response.GetOverallStudentAttendProgressRes
import com.lms.sch.response.ExamResultResponse
import com.lms.sch.response.GetAdminOverallFeeBarchartResponse
import com.lms.sch.response.GetStudentAssignmentRes
import com.lms.sch.response.GetTeacherStudentStatsCountRes
import com.lms.sch.response.HwSingleViewResponse
import com.lms.sch.response.MyClassAttendanceResponse
import com.lms.sch.response.PointsHistoryResponse
import com.lms.sch.response.ProfileDetailsTwo
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
import com.lms.sch.response.TeacherSideStudentAssignmentResponse
import com.lms.sch.response.TeacherSideStudentProjectResponse
import com.lms.sch.response.TeacherStatsResponse
import com.lms.sch.response.TeacherTimeTableResponse
import com.lms.sch.response.TeacherprofileSignleViewResponse
import com.lms.sch.response.UpdatesCountResponse
import com.lms.sch.response.UploadFileResponse
import com.lms.sch.response.VerifyOtpResponse
import com.lms.sch.session.Constants.ConstantsHelper.APPROVE_ACKNOWLEDGE
import com.lms.sch.session.Constants.ConstantsHelper.FORGOT
import com.lms.sch.session.Constants.ConstantsHelper.LOGIN
import com.lms.sch.session.Constants.ConstantsHelper.REGISTER
import com.lms.sch.session.Constants.ConstantsHelper.SEND_OTP_MOBILE
import com.lms.sch.session.Constants.ConstantsHelper.UPLOAD_FILE
import com.lms.sch.session.Constants.ConstantsHelper.VERIFY_OTP_MOBILE
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiDetails {

    @Multipart
    @POST(UPLOAD_FILE)
    fun uploadFile(@Part file: MultipartBody.Part): Observable<UploadFileResponse>

    @Multipart
    @POST(UPLOAD_FILE)
    fun uploadDoc(@Part file:MultipartBody.Part,@Part userid:MultipartBody.Part,@Part doccode:MultipartBody.Part): Observable<UploadFileResponse>

    @POST(REGISTER)
    fun register(@Body body: Map<String, String>): Observable<BaseModel>

    @POST(SEND_OTP_MOBILE)
    fun sendOTPMobile(@Body body: Map<String, String>): Observable<SendOtpResponse>

    @POST(VERIFY_OTP_MOBILE)
    fun verifyOTPMobile(@Body body: Map<String, String>): Observable<VerifyOtpResponse>

    @POST(LOGIN)
    fun login(@Body body: Map<String, String>): Observable<LoginResponse>

    @POST(FORGOT)
    fun forgotPassword(@Body body: Map<String, String>): Observable<BaseModel>

    @POST
    fun applicationSave(@Url url: String, @Body body: RequestBody): Observable<BaseModel>

    @GET
    fun pincode(@Url url: String): Observable<PincodeResponse>

    @GET
    fun getDocumentMaster(@Url url:String): Observable<GetDocumentMaster>

    @GET
    fun getDocumentsUpload(@Url url:String): Observable<GetDocumentsUploadResponse>
    @GET
    fun guestProfile(@Url url:String): Observable<GetGuestProfileResponse>
    @GET
    fun profile(@Url url:String): Observable<ProfileDetailsResponse>
    @GET
    fun stdprofile(@Url url:String): Observable<StudentProfileResponse>
    @GET
    fun profiletwo(@Url url:String): Observable<ProfileDetailsTwo>
    @GET
    fun profilethree(@Url url:String): Observable<StudentProfileDatailstwo>
    @GET
    fun teacherProfile1(@Url url:String): Observable<GetTeacherProfileResponse>

    @GET
    fun teacherProfile(@Url url:String): Observable<TeacherprofileSignleViewResponse>

    @POST
    fun payment(@Url url: String, @Body body: RequestBody): Observable<PaymentResponse>

    @POST(APPROVE_ACKNOWLEDGE)
    fun approve(@Body body: RequestBody): Observable<BaseModel>

    @POST
    fun hwStsUpd(@Url url: String, @Body body: RequestBody): Observable<BaseModel>

    @PUT
    fun assStsUpd(@Url url: String, @Body body: RequestBody) : Observable<BaseModel>

    @POST
    fun complaintSolve(@Url url: String, @Body body: RequestBody) : Observable<BaseModel>

    @POST
    fun prjStsUpd(@Url url: String, @Body body: RequestBody) : Observable<BaseModel>
    @PUT
    fun prjStsUpd1(@Url url: String, @Body body: RequestBody) : Observable<BaseModel>

    @PUT
    fun leaveApprove(@Url url: String, @Body body: RequestBody) : Observable<BaseModel>

    @POST
    fun clsTestStsUpd(@Url url: String, @Body body: RequestBody) : Observable<BaseModel>
    @PUT
    fun examStsUpd(@Url url: String, @Body body: RequestBody) : Observable<BaseModel>
    @POST
    fun leaverequest(@Url url: String, @Body body: RequestBody) : Observable<BaseModel>

    @GET
    fun studentBoard(@Url url:String): Observable<StudentBoardResponse>

    @POST
    fun payFees(@Url url:String,@Body body: RequestBody): Observable<PaymentResponse>

    @GET
    fun studentClsDropdown(@Url url:String): Observable<DropdownResponse>
    @GET
    fun schoolDropdown(@Url url:String): Observable<SchoolDwRes>

    @GET
    fun feesOnline(@Url url:String): Observable<GetFeesOnlineResponse>
    @GET
    fun feesGuest(@Url url:String): Observable<GuestFeesResponse>
    @GET
    fun updateCount(@Url url:String): Observable<UpdatesCountResponse>
    @GET
    fun projectCount(@Url url:String): Observable<ProjectUpdCountResponse>
    @GET
    fun assignmentCount(@Url url:String): Observable<AssignmentUpdCountResponse>
    @GET
    fun attendance(@Url url:String): Observable<AttendanceProgressResponse>
    @GET
    fun getOverallAttendanceProgress(@Url url:String): Observable<GetOverallAttendanceProgressResponse>
    @GET
    fun getAttendance(@Url url:String): Observable<GetAttendanceResponse>

    @GET
    fun getStudentAssignment(@Url url: String): Observable<GetStudentAssignmentResponse>
    @GET
    fun stdass(@Url url: String): Observable<AssSglViewResponse>
    @GET
    fun overAllProgress(@Url url: String): Observable<GetOverAllProgressResponse>

    @GET
    fun getStudentHomework(@Url url: String): Observable<GetHomeworkResponse>
    @GET
    fun getstdsgl(@Url url: String): Observable<HwSingleViewResponse>
    @GET
    fun teacherAss(@Url url: String): Observable<TeacherAssSingleViewResponse>

    @GET
    fun getStudentProjectRes(@Url url: String): Observable<TeacherSideStudentProjectResponse>

    @GET
    fun getStudentAssignment1(@Url url: String): Observable<GetStudentAssignmentRes>

    @GET
    fun getStudentClsTestRes(@Url url: String): Observable<GetTeacherStdClsTestResponse>

    @GET
    fun getStudentProject(@Url url: String): Observable<StudentProjectResponse>
    @GET
    fun prResult(@Url url: String): Observable<ProjectResultResponse>

    @GET
    fun getTeacherHomework(@Url url: String): Observable<GetTeacherHomeWorkResponse>

    @GET
    fun getTeacherNewsEvent(@Url url: String): Observable<GetTeacherNewEventResponse>

    @GET
    fun getTeacherProject(@Url url: String): Observable<GetTeacherProjectResponse>

    @GET
    fun getTeacherProjectSingleView(@Url url: String): Observable<ProjectSingleViewResponse>

    @GET
    fun getTeacherClsTestSingleView(@Url url: String): Observable<GetTeacherClsTestSingleViewResponse>

    @GET
    fun getAdminExamSingleView(@Url url: String): Observable<GetExamSingleViewResponse>

    @GET
    fun getTeacherHomeworkSingleId(@Url url: String): Observable<TeacherHwSingleResponse>

    @GET
    fun getNoticeBoard(@Url url: String): Observable<NoticeBoardResponse>
    @GET
    fun myTeachers(@Url url: String): Observable<GetMyTeachersResponse>
    @GET
    fun leaveDrop(@Url url: String): Observable<LeaveDropdownResponse>

    @GET
    fun getClassTest(@Url url: String): Observable<ClassTestResponse>

    @GET
    fun getStuClassTest(@Url url: String): Observable<StudentClassTestResponse>

    @GET
    fun getClassTimeTable(@Url url: String): Observable<GetClassTimeTableResponse>
    @GET
    fun timeTableTeacher(@Url url: String): Observable<GetTeacherScheduleResponse>
    @GET
    fun getTimeTable(@Url url: String): Observable<GetTimeTableResponse>
    @GET
    fun getSection(@Url url: String): Observable<GetSectionResponse>
    @GET
    fun getAcademicSubject(@Url url: String): Observable<GetAcademicSubjectResponse>
    @GET
    fun getStudent(@Url url: String): Observable<GetStudentResponse>
    @GET
    fun getGuest(@Url url: String): Observable<GetGuestInfoResponse>
    @GET
    fun getTeacher(@Url url: String): Observable<GetTeacherResponse>
    @GET
    fun getStaff(@Url url: String): Observable<GetStaffResponse>
    @GET
    fun getClassTestReport(@Url url: String): Observable<GetClassTestResponse>

    @GET
    fun getStudentExam(@Url url: String): Observable<StudentExamResponse>
    @GET
    fun getStudentExam1(@Url url: String): Observable<StudentExamResultResponse>
    @GET
    fun getExam(@Url url: String): Observable<GetExamResponse>
    @GET
    fun getExamList(@Url url: String): Observable<GetExamSubjectResponse>
    @GET
    fun getExamstdList(@Url url: String): Observable<StudentExamRes>

    @GET
    fun getAcademicStats(@Url url: String): Observable<GetAcademicStatsResponse>

    @GET
    fun getAdminStats(@Url url: String): Observable<GetAdminStatsReponse>

    @GET
    fun getComplaintStats(@Url url: String): Observable<GetComplaintStatResponse>

    @GET
    fun getAcademicBatch(@Url url: String): Observable<GetAcademicBatchResponse>
    @GET
    fun batchDropdown(@Url url: String): Observable<BatchDropdownResponse>

    @GET
    fun getaAllComplaints(@Url url: String): Observable<GetComplaintResponse>

    @GET
    fun getComplaintDropDrown(@Url url: String): Observable<GetComplaintDropDownResponse>

    @GET
    fun getaSVComplaints(@Url url: String): Observable<GetComplaintSingleViewResponse>

    @GET
    fun getRole(@Url url: String): Observable<GetRoleResponse>

    @GET
    fun getAcademicBoard(@Url url: String): Observable<GetAcademicBoardResponse>

    @GET
    fun getAcademicStandard(@Url url: String): Observable<GetAcademicStandardResponse>

    @GET
    fun getStdClassTestProgress(@Url url: String): Observable<GetStudentClassTestProgress>
    @GET
    fun getDropdown(@Url url: String): Observable<DropdownResponse>
    @GET
    fun examRes(@Url url: String): Observable<ExamResultResponse>
    @GET
    fun programBasedSub(@Url url: String): Observable<ProgramBasedSubjectResponse>
    @GET
    fun teacherProgram(@Url url: String): Observable<GetTeacherProgramResponse>
    @GET
    fun program(@Url url: String): Observable<ProgramResponse>
    @GET
    fun studentAnalytics(@Url url: String): Observable<StudentListAnalyticsResponse>
    @GET
    fun studentAnalytics1(@Url url: String): Observable<StudentExamResponse>

    @GET
    fun getStdExaminationProgress(@Url url: String): Observable<GetStudentExamProgressResponse>

    @GET
    fun getStdAttendance(@Url url : String):  Observable<GetStudentAttenDanceResponse>

    @GET
    fun getTeacherAssignment(@Url url : String) : Observable<GetTeacherAssignmentResponse>
    @GET
    fun getTeacherClassTest(@Url url : String) : Observable<GetTeacherClassTestResponse>
    @GET
    fun getSingleAssignments(@Url url : String) : Observable<GetTeacherAssignmentSingleResponse>
    @GET
    fun getSingleAssignments2(@Url url : String) : Observable<TeacherAssignment2Response>
    @GET
    fun getSingleAssignments3(@Url url : String) : Observable<TeacherAssSingleViewResponse>
    @GET
    fun getCompletedStudents(@Url url : String) : Observable<TeacherSideStudentAssignmentResponse>
    @GET
    fun getStudentProfile(@Url url : String) : Observable<StudentSingleVIewResponse>
    @GET
    fun getAdminFees(@Url url : String) : Observable<AdminFeesResponse>
    @GET
    fun submitionProject(@Url url : String) : Observable<SubmissionProjectResponse>
    @GET
    fun submitionAssignment(@Url url : String) : Observable<SubmissionAssignmentResponse>
    @GET
    fun submitionHomework(@Url url : String) : Observable<SubmissionProgressResponse>
    @GET
    fun studentFees(@Url url : String) : Observable<StudentFeeResponse>
    @GET
    fun studentFees1(@Url url : String) : Observable<AdminSingleViewResponse>
    @GET
    fun getStatsProgressPoints(@Url url : String) : Observable<GetProgressPointsStatsResponse>
    @GET
    fun GetTeacherStudentStatsCount(@Url url : String) : Observable<GetTeacherStudentStatsCountRes>
    @GET
    fun adminOverallFeeBarchartProgress(@Url url : String) : Observable<GetAdminOverallFeeBarchartResponse>
    @GET
    fun paymentHistory(@Url url : String) : Observable<GetTransactionResponse>
    @GET
    fun teacherAsscnt(@Url url : String) : Observable<TeacherAssCountResponse>
    @GET
    fun leaderBoard(@Url url : String) : Observable<GetLeaderboardResponse>
    @GET
    fun scoreBoard(@Url url : String) : Observable<GetScoreboardResponse>
    @GET
    fun pointsHistory(@Url url : String) : Observable<PointsHistoryResponse>
    @GET
    fun parentProfile(@Url url : String) : Observable<ParentProfileResponse>
    @GET
    fun leaveRequest(@Url url : String) : Observable<LeaveRequestResponse>
    @GET
    fun availableLeave(@Url url : String) : Observable<AvailableLeavesRes>
    @GET
    fun studentAttendanceProgress(@Url url:String): Observable<AttendanceProgressResponse>
    @GET
    fun adminAtt(@Url url:String): Observable<AdminAttendanceResponse>
    @GET
    fun GetTeacherOverallStudentAttendance(@Url url:String): Observable<GetOverallStudentAttendProgressRes>
    @GET
    fun teacherAtt(@Url url:String): Observable<TeacherAttendanceResponse>
    @GET
    fun teacherSub(@Url url:String): Observable<SubmissionProgressTeacherSide>
    @GET
    fun exampro(@Url url:String): Observable<SubjectWiseClassExamProResponse>
    @GET
    fun myclsstd(@Url url:String): Observable<MyClassAttendanceResponse>
    @GET
    fun teacherstats(@Url url:String): Observable<TeacherStatsResponse>
    @GET
    fun teachertimeTbl(@Url url:String): Observable<TeacherTimeTableResponse>


}

