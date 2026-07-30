package com.lms.sch.response

import com.lms.sch.models.BaseModel
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
open class GetGuestProfileResponse : BaseModel() {

    @JsonProperty("result")
    @JsonIgnore
    var result: Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {

        @JsonProperty("currentStep")
        var currentStep: Int? = null

        @JsonProperty("userProfile")
        var userProfile: UserProfile? = null

        @JsonProperty("feesDetails")
        var feesDetails: FeesDetails? = null

        @JsonProperty("applicationForm")
        var applicationForm: ApplicationForm? = null
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class UserProfile : Serializable {

        @JsonProperty("currentStep")
        var currentStep: Int? = null

        @JsonProperty("_id")
        var id: String? = ""

        @JsonProperty("firstName")
        var firstName: String? = ""

        @JsonProperty("lastName")
        var lastName: String? = ""

        @JsonProperty("mobile")
        var mobile: String? = ""

        @JsonProperty("mobileVerified")
        var mobileVerified: Boolean? = null

        @JsonProperty("email")
        var email: String? = ""

        @JsonProperty("emailVerified")
        var emailVerified: Boolean? = null

        @JsonProperty("dob")
        var dob: String? = ""

        @JsonProperty("gender")
        var gender: String? = ""

        @JsonProperty("blood_group")
        var bloodGroup: String? = ""

        @JsonProperty("nationality")
        var nationality: String? = ""

        @JsonProperty("religion")
        var religion: String? = ""

        @JsonProperty("category")
        var category: String? = ""

        @JsonProperty("aadhar_number")
        var aadharNumber: Long? = null

        @JsonProperty("pincode")
        var pincode: Int? = null

        @JsonProperty("country")
        var country: String? = ""

        @JsonProperty("state")
        var state: String? = ""

        @JsonProperty("city")
        var city: String? = ""

        @JsonProperty("address")
        var address: String? = ""

        @JsonProperty("img_url")
        var imgUrl: String? = ""

        @JsonProperty("role")
        var role: Role? = null

        @JsonProperty("password")
        var password: String? = ""

        @JsonProperty("status")
        var status: String? = ""

        @JsonProperty("isDeleted")
        var isDeleted: Boolean? = null

        @JsonProperty("lastLogin")
        var lastLogin: String? = ""

        @JsonProperty("createdAt")
        var createdAt: String? = ""

        @JsonProperty("updatedAt")
        var updatedAt: String? = ""

        @JsonProperty("application_status")
        var application_status: String? = ""

        @JsonProperty("student_enrollment")
        var student_enrollment: Boolean? = null
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Role : Serializable {

        @JsonProperty("_id")
        var id: String? = ""

        @JsonProperty("name")
        var name: String? = ""
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class FeesDetails : Serializable {

        @JsonProperty("_id")
        var id: String? = ""

        @JsonProperty("verifyStatus")
        var verifyStatus: String? = ""
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class ApplicationForm : Serializable {

        @JsonProperty("parentInfo")
        var parentInfo: ParentInfo? = null

        @JsonProperty("academicInfo")
        var academicInfo: AcademicInfo? = null

        @JsonProperty("_id")
        var id: String? = ""

        @JsonProperty("student")
        var student: String? = ""

        @JsonProperty("isDeleted")
        var isDeleted: Boolean? = null

        @JsonProperty("createdAt")
        var createdAt: String? = ""

        @JsonProperty("updatedAt")
        var updatedAt: String? = ""
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
        var parentsMobile: Long? = null

        @JsonProperty("emergencyMobile")
        var emergencyMobile: Long? = null

        @JsonProperty("parentsEmail")
        var parentsEmail: String? = ""

        @JsonProperty("parentsAdress")
        var parentsAddress: String? = ""
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class AcademicInfo : Serializable {

        @JsonProperty("previousSchoolName")
        var previousSchoolName: String? = ""

        @JsonProperty("previousClassName")
        var previousClassName: ClassName? = null

        @JsonProperty("classApplying")
        var classApplying: ClassName? = null

        @JsonProperty("boardOfEducation")
        var boardOfEducation: BoardOfEducation? = null
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class ClassName : Serializable {

        @JsonProperty("_id")
        var id: String? = ""

        @JsonProperty("name")
        var name: Int? = null
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class BoardOfEducation : Serializable {

        @JsonProperty("_id")
        var id: String? = ""

        @JsonProperty("name")
        var name: String? = ""
    }
}
