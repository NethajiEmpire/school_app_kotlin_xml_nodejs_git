package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class StudentProfileResponse : BaseModel() {

    @JsonProperty("result")
    var result: Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {
        @JsonProperty("_id")
        var id: String? = ""

        @JsonProperty("rollNo")
        var rollNo: String? = ""

        @JsonProperty("parent")
        var parent : String? = ""

        @JsonProperty("student")
        var student: Student? = null

        @JsonProperty("board")
        var board: Board? = null

        @JsonProperty("studentClass")
        var studentClass: StudentClass? = null

        @JsonProperty("section")
        var section: Section? = null

        @JsonProperty("batch")
        var batch: Batch? = null

        @JsonProperty("program")
        var program: String? = ""

        @JsonProperty("createdAt")
        var createdAt: String? = ""

        @JsonProperty("school")
        var school: String? = ""

        @JsonProperty("applicationForm")
        var applicationForm: ApplicationForm? = null
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)

    class Student : Serializable {
        @JsonProperty("activeStatus")
        var activeStatus: String? = ""

        @JsonProperty("verifyStep")
        var verifyStep : String? = ""

        @JsonProperty("_id")
        var id: String? = ""

        @JsonProperty("firstName")
        var firstName: String? = ""

        @JsonProperty("lastName")
        var lastName: String? = ""

        @JsonProperty("email")
        var email: String? = ""

        @JsonProperty("mobile")
        var mobile: String? = ""

        @JsonProperty("lead_id")
        var lead_id: String? = ""

        @JsonProperty("dob")
        var dob: String? = ""

        @JsonProperty("gender")
        var gender: String? = ""

        @JsonProperty("img_url")
        var imgUrl: String? = ""
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)

    class ApplicationForm : Serializable {

        @JsonProperty("verifyStep")
        var verifyStep : String? = ""

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
        var id: String? = ""

        @JsonProperty("student")
        var student: String? = ""

        @JsonProperty("signature_url")
        var signatureUrl: String? = ""

        @JsonProperty("parent_signature_url")
        var parentSignatureUrl: String? = ""
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)

    class StudentInfo : Serializable {

        @JsonProperty("verifyStep")
        var verifyStep : String? = ""

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

        @JsonProperty("verifyStep")
        var verifyStep : String? = ""

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

        @JsonProperty("verifyStep")
        var verifyStep : String? = ""

        @JsonProperty("previousSchoolName")
        var previousSchoolName: String? = ""

        @JsonProperty("boardOfEducation")
        var boardOfEducation: String? = ""

        @JsonProperty("classApplying")
        var classApplying: String? = ""

        @JsonProperty("batch")
        var batch: String? = ""

        @JsonProperty("previousClassName")
        var previousClassName: String? = ""

        @JsonProperty("isApprove")
        var isApprove: String? = ""

        @JsonProperty("program")
        var program: String? = ""
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)

    class DocumentInfo : Serializable {

        @JsonProperty("verifyStep")
        var verifyStep : String? = ""

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
        var isApprove: Boolean? = null
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)

    class Document : Serializable {

        @JsonProperty("verifyStep")
        var verifyStep : String? = ""

        @JsonProperty("url")
        var url: String? = ""

        @JsonProperty("status")
        var status: String? = ""
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)

    class FeesInfo : Serializable {

        @JsonProperty("verifyStep")
        var verifyStep : String? = ""

        @JsonProperty("transaction")
        var transaction: String? = ""

        @JsonProperty("isApprove")
        var isApprove: String? = ""
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)

    class Board : Serializable {

        @JsonProperty("verifyStep")
        var verifyStep : String? = ""

        @JsonProperty("_id")
        var id: String? = ""

        @JsonProperty("name")
        var name: String? = ""
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)

    class StudentClass : Serializable {
        @JsonProperty("verifyStep")
        var verifyStep : String? = ""

        @JsonProperty("_id")
        var id: String? = ""

        @JsonProperty("name")
        var name: String? =""
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)

    class Section : Serializable {
        @JsonProperty("verifyStep")
        var verifyStep : String? = ""

        @JsonProperty("_id")
        var id: String? = ""

        @JsonProperty("name")
        var name: String? = ""
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)

    class Batch : Serializable {
        @JsonProperty("verifyStep")
        var verifyStep : String? = ""

        @JsonProperty("_id")
        var id: String? = ""

        @JsonProperty("name")
        var name: String? = ""
    }
}