package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class GetTeacherScheduleResponse : BaseModel() {

    @JsonProperty("result")
    var result: TeacherScheduleResult? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class TeacherScheduleResult : Serializable {

        @JsonProperty("day")
        var day: String? = ""

        @JsonProperty("teacher")
        var teacher: TeacherDetails? = null

        @JsonProperty("periods")
        var periods: ArrayList<PeriodDetails>? = ArrayList()
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class TeacherDetails : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("firstName")
        var firstName: String? = ""

        @JsonProperty("lastName")
        var lastName: String? = ""

        @JsonProperty("email")
        var email: String? = ""
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class PeriodDetails : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("startTime")
        var startTime: String? = ""

        @JsonProperty("endTime")
        var endTime: String? = ""

        @JsonProperty("type")
        var type: String? = ""

        @JsonProperty("studentClass")
        var studentClass: Int? = null

        @JsonProperty("board")
        var board: String? = ""

        @JsonProperty("batch")
        var batch: String? = ""

        @JsonProperty("section")
        var section: String? = ""

        @JsonProperty("subject")
        var subject: SubjectDetails? = null
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
