package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import com.lms.sch.response.AdminFeesResponse.Result.Rows.Board
import com.lms.sch.response.AdminFeesResponse.Result.Rows.StudentClass
import com.lms.sch.response.GetGuestInfoResponse.Result.Row.Section
import java.io.Serializable

class ProfileDetailsTwo : BaseModel() {

    @JsonProperty("data")
    var data: Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {

        @JsonProperty("rollNo")
        var rollNo : String? = ""

        @JsonProperty("program")
        var program : String? = ""

        @JsonProperty("user")
        var user: UserProfile? = null

        @JsonProperty("applicationForm")
        var applicationForm: ApplicationForm? = null

        @JsonProperty("studentPreference")
        var studentPreference: StudentPreference? = null
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class UserProfile : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("role")
        var role: Role? = null

        @JsonProperty("mobile")
        var mobile: String? = ""

        @JsonProperty("current_status")
        var current_status: String? = ""

        @JsonProperty("currentStep")
        var currentStep: Int? = 0

        @JsonProperty("totalStep")
        var totalStep: String? = ""

        @JsonProperty("status")
        var status: String? = ""

        @JsonProperty("registrationFee")
        var registrationFee: Boolean? = null

        @JsonProperty("createdAt")
        var createdAt: String? = ""

        @JsonProperty("updatedAt")
        var updatedAt: String? = ""

        @JsonProperty("email")
        var email: String? = ""

        @JsonProperty("firstName")
        var firstName: String? = ""

        @JsonProperty("grade_level")
        var grade_level: StudentClass? = null

        @JsonProperty("lastName")
        var lastName: String? = ""

        @JsonProperty("pre_school")
        var pre_school: String? = ""

        @JsonProperty("rollNo")
        var rollNo: String? = ""

        @JsonProperty("board")
        var board: Board? = null

        @JsonProperty("student_enrollment")
        var studentEnrollment: Boolean? = null

        @JsonProperty("activeStatus")
        var activeStatus: String? = ""

        @JsonProperty("lead_id")
        var lead_id: String? = ""

        @JsonProperty("dob")
        var dob: String? = ""

        @JsonProperty("blood_group")
        var blood_group: String? = ""

        @JsonProperty("address")
        var address: String? = ""

        @JsonProperty("gender")
        var gender: String? = ""

        @JsonProperty("img_url")
        var img_url: String? = ""

        @JsonProperty("batchJoined")
        var batchJoined: String? = ""

        @JsonProperty("lastLogin")
        var lastLogin: String? = ""
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Role : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("name")
        var name: String? = ""
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class ApplicationForm : Serializable {

        @JsonProperty("studentInfo")
        var studentInfo: StudentInfo? = null

        @JsonProperty("parentInfo")
        var parentInfo: ParentInfo? = null

        @JsonProperty("academicInfo")
        var academicInfo: AcademicInfo? = null

        @JsonProperty("documentInfo")
        var documentInfo: DocumentInfo? = null

        @JsonProperty("feesInfo")
        var feesInfo: FeesInfo? = null

        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("student")
        var student: String? = ""

        @JsonProperty("isDeleted")
        var isDeleted: Boolean? = false

        @JsonProperty("createdAt")
        var createdAt: String? = ""

        @JsonProperty("updatedAt")
        var updatedAt: String? = ""

        @JsonProperty("signature_url")
        var signatureUrl: String? = ""
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class StudentInfo : Serializable {

        @JsonProperty("firstName")
        var firstName: String? = ""

        @JsonProperty("lastName")

        var lastName: String? = ""

        @JsonProperty("email")
        var email: String? = ""

        @JsonProperty("mobile")
        var mobile: String? = ""

        @JsonProperty("dob")
        var dob: String? = ""

        @JsonProperty("gender")
        var gender: String? = ""

        @JsonProperty("blood_group")
        var blood_group: String? = ""

        @JsonProperty("nationality")
        var nationality: String? = ""

        @JsonProperty("religion")
        var religion: String? = ""

        @JsonProperty("category")
        var category: String? = ""

        @JsonProperty("aadhar_number")
        var aadhar_number: String? = ""

        @JsonProperty("pincode")
        var pincode: String? = ""

        @JsonProperty("country")
        var country: String? = ""

        @JsonProperty("state")
        var state: String? = ""

        @JsonProperty("city")
        var city: String? = ""

        @JsonProperty("address")
        var address: String? = ""

        @JsonProperty("isApprove")
        var isApprove: String? = ""
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class ParentInfo : Serializable {
        @JsonProperty("fatherName")
        var fatherName: String? = ""

        @JsonProperty("motherName")
        var motherName: String? = ""

        @JsonProperty("guardianName")
        var guardianName: String? = ""

        @JsonProperty("fatherOccupation")
        var fatherOccupation: String? = ""

        @JsonProperty("motherOccupation")
        var motherOccupation: String? = ""

        @JsonProperty("parentsMobile")
        var parentsMobile: String? = ""

        @JsonProperty("emergencyMobile")
        var emergencyMobile: String? = ""

        @JsonProperty("parentsEmail")
        var parentsEmail: String? = ""

        @JsonProperty("parentsAdress")
        var parentsAddress: String? = ""

        @JsonProperty("isApprove")
        var isApprove: String? = ""
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class AcademicInfo : Serializable {
        @JsonProperty("previousSchoolName")
        var previousSchoolName: String? = ""

//        @JsonProperty("classApplying")
//        var classApplying: ClassApplying? = null

        @JsonProperty("classApplying")
        var classApplying: String? = ""

        @JsonProperty("boardOfEducation")
        var boardOfEducation: String? = ""

        @JsonProperty("isApprove")
        var isApprove: String? = ""

//        @JsonProperty("batch")
//        var batch: Batch? = null

        @JsonProperty("batch")
        var batch: String? = ""

        @JsonProperty("previousClassName")
        var previousClassName: String? = ""
    }


//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    class ClassApplying : Serializable {
//        @JsonProperty("_id")
//        var _id: String? = ""
//
//        @JsonProperty("name")
//        var name: String? = ""
//    }

//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    class BoardOfEducation : Serializable {
//
//        @JsonProperty("_id")
//        var _id: String? = ""
//
//        @JsonProperty("name")
//        var name: String? = ""
//    }

//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    class Batch : Serializable {
//        @JsonProperty("_id")
//        var _id: String? = ""
//
//        @JsonProperty("name")
//        var name: String? = ""
//    }

//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    class PreviousClassName : java.io.Serializable {
//        @JsonProperty("_id")
//        var _id: String? = ""
//
//        @JsonProperty("name")
//        var name: String? = ""
//    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class DocumentInfo : Serializable {
        @JsonProperty("birthCertificate")
        var birthCertificate: Document? = null

        @JsonProperty("aadharCard")
        var aadharCard: Document? = null

        @JsonProperty("studentPhoto")
        var studentPhoto: Document? = null

        @JsonProperty("previousSchoolMarksheet")
        var previousSchoolMarksheet: Document? = null

        @JsonProperty("transferCertificate")
        var transferCertificate: Document? = null

        @JsonProperty("parentIdProof")
        var parentIdProof: Document? = null

        @JsonProperty("addressProof")
        var addressProof: Document? = null

        @JsonProperty("isApprove")
        var isApprove: String? = ""
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Document : Serializable {
        @JsonProperty("url")
        var url: String? = ""

        @JsonProperty("status")
        var status: String? = ""
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class FeesInfo : Serializable {
        @JsonProperty("transaction")
        var transaction: String? = ""

        @JsonProperty("isApprove")
        var isApprove: String? = ""
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class StudentPreference : Serializable {

        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("board")
        var board: Board? = null

        @JsonProperty("studentClass")
        var studentClass: StudentClass? = null

        @JsonProperty("section")
        var section: Section? = null

//        @JsonProperty("batch")
//        var batch: Batch? = null

        @JsonProperty("rollNo")
        var rollNo: String? = ""
    }
}
