package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class GetTeacherResponse : BaseModel() {

    @JsonProperty("result")
    var result: Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {

        @JsonProperty("rows")
        var rows: ArrayList<Row>? = ArrayList()

        @JsonProperty("pagination")
        var pagination: Pagination? = null

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Row : Serializable {

            @JsonProperty("_id")
            var _id: String? = ""

            @JsonProperty("firstName")
            var firstName: String? = ""

            @JsonProperty("lastName")
            var lastName: String? = ""

            @JsonProperty("img_url")
            var img_url: String? = ""

            @JsonProperty("role")
            var role: Role? = null

            @JsonProperty("email")
            var email: String? = ""

            @JsonProperty("mobile")
            var mobile: String? = ""

            @JsonProperty("address")
            var address: String? = ""

            @JsonProperty("gender")
            var gender: String? = ""

            @JsonProperty("student_enrollment")
            var student_enrollment: Boolean? = false

            @JsonProperty("lead_id")
            var lead_id: String? = ""

            @JsonProperty("leadNo")
            var leadNo: Int? = null

            @JsonProperty("lastLogin")
            var lastLogin: String? = ""

            @JsonProperty("createdAt")
            var createdAt: String? = ""

            @JsonProperty("teacherPreference")
            var teacherPreference: TeacherPreference? = null
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
        class TeacherPreference : Serializable {
            @JsonProperty("_id")
            var _id: String? = ""

            @JsonProperty("user_id")
            var user_id: String? = ""

            @JsonProperty("teacherType")
            var teacherType: String? = ""

            @JsonProperty("highestQualification")
            var highestQualification: String? = ""

            @JsonProperty("overallExperience")
            var overallExperience: String? = ""

            @JsonProperty("majorSubjects")
            var majorSubjects: ArrayList<MajorSubject>? = ArrayList()

            @JsonProperty("myStudentClass")
            var myStudentClass: String? = ""
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class MajorSubject : Serializable {
            @JsonProperty("_id")
            var _id: String? = ""

            @JsonProperty("name")
            var name: String? = ""
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Pagination : Serializable {
            @JsonProperty("currentPage")
            var currentPage: Int? = null

            @JsonProperty("pages")
            var pages: Int? = null

            @JsonProperty("total")
            var total: Int? = null
        }
    }
}
