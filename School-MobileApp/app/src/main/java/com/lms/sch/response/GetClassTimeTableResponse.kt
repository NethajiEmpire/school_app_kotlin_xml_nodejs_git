package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class GetClassTimeTableResponse : BaseModel() {

    @JsonProperty("data")
    var data: Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {

        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("program")
        var program: String? = ""

        @JsonProperty("board")
        var board: BoardDetails? = null

        @JsonProperty("batch")
        var batch: BatchDetails? = null

        @JsonProperty("studentClass")
        var studentClass: StudentClassDetails? = null

        @JsonProperty("section")
        var section: SectionDetails? = null

        @JsonProperty("day")
        var day: String? = ""

        @JsonProperty("periods")
        var periods: ArrayList<Period>? = ArrayList()

        @JsonProperty("isDeleted")
        var isDeleted: Boolean? = false

        @JsonProperty("createdBy")
        var createdBy: CreatedByDetails? = null

        @JsonProperty("createdAt")
        var createdAt: String? = ""

        @JsonProperty("updatedAt")
        var updatedAt: String? = ""
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
    class BatchDetails : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("name")
        var name: String? = ""
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
    class Period : Serializable {
        @JsonProperty("timePeriod")
        var timePeriod: TimePeriod? = null

        @JsonProperty("subject")
        var subject: SubjectDetails? = null

        @JsonProperty("teacher")
        var teacher: TeacherDetails? = null

        @JsonProperty("type")
        var type: String? = ""

        @JsonProperty("sortOrder")
        var sortOrder: Int? = null

        @JsonProperty("_id")
        var _id: String? = ""
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class TimePeriod : Serializable {
        @JsonProperty("type")
        var type: String? = ""

        @JsonProperty("isDeleted")
        var isDeleted: Boolean? = false

        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("startTime")
        var startTime: String? = ""

        @JsonProperty("endTime")
        var endTime: String? = ""
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class SubjectDetails : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("isDeleted")
        var isDeleted: Boolean? = false

        @JsonProperty("status")
        var status: String? = ""

        @JsonProperty("name")
        var name: String? = ""
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class TeacherDetails : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("currentStep")
        var currentStep: Int? = null

        @JsonProperty("totalStep")
        var totalStep: Int? = null

        @JsonProperty("status")
        var status: String? = ""

        @JsonProperty("registrationFee")
        var registrationFee: Boolean? = false

        @JsonProperty("student_enrollment")
        var studentEnrollment: Boolean? = false

        @JsonProperty("isDeleted")
        var isDeleted: Boolean? = false

        @JsonProperty("firstName")
        var firstName: String? = ""

        @JsonProperty("lastName")
        var lastName: String? = ""

        @JsonProperty("students")
        var students: ArrayList<Any>? = ArrayList()

        @JsonProperty("lastLogin")
        var lastLogin: String? = ""
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class CreatedByDetails : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("firstName")
        var firstName: String? = ""

        @JsonProperty("lastName")
        var lastName: String? = ""
    }
}
