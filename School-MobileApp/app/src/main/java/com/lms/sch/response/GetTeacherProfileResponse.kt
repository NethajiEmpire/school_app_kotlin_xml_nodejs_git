package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class GetTeacherProfileResponse : BaseModel() {

    @JsonProperty("data")
    var data: TeacherProfileResult? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class TeacherProfileResult : Serializable {

        @JsonProperty("user")
        var user: UserProfileDetails? = null

        @JsonProperty("teacherPreference")
        var teacherPreference: TeacherPreferenceDetails? = null
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class UserProfileDetails : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("firstName")
        var firstName: String? = ""

        @JsonProperty("lastName")
        var lastName: String? = ""

        @JsonProperty("img_url")
        var imgUrl: String? = ""

        @JsonProperty("role")
        var role: RoleDetails? = null

        @JsonProperty("email")
        var email: String? = ""

        @JsonProperty("mobile")
        var mobile: String? = ""

        @JsonProperty("address")
        var address: String? = ""

        @JsonProperty("gender")
        var gender: String? = ""

        @JsonProperty("lead_id")
        var leadId: String? = ""

        @JsonProperty("dob")
        var dob: String? = ""
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class RoleDetails : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("name")
        var name: String? = ""
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class TeacherPreferenceDetails : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("teacherType")
        var teacherType: String? = ""

        @JsonProperty("highestQualification")
        var highestQualification: String? = ""

        @JsonProperty("overallExperience")
        var overallExperience: String? = ""

        @JsonProperty("board")
        var board: ArrayList<BoardDetails>? = ArrayList()

        @JsonProperty("myStudentClass")
        var myStudentClass: MyStudentClassDetails? = null

        @JsonProperty("majorSubjects")
        var majorSubjects: ArrayList<SubjectDetails>? = ArrayList()
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class BoardDetails : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("name")
        var name: String? = ""
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class MyStudentClassDetails : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("board")
        var board: BoardDetails? = null

        @JsonProperty("studentClass")
        var studentClass: StudentClassDetails? = null

        @JsonProperty("section")
        var section: SectionDetails? = null
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class StudentClassDetails : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("name")
        var name: Int? = null
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class SectionDetails : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("name")
        var name: String? = ""
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class SubjectDetails : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("name")
        var name: String? = ""
    }
}
