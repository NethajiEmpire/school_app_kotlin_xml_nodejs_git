package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class TeacherAttendanceResponse : BaseModel() {

    @JsonProperty("result")
    var result: Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {

        @JsonProperty("teAttendance")
        var teAttendance: ArrayList<TeAttendance>? = ArrayList()

        @JsonProperty("progress")
        var progress: Progress? = null

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class TeAttendance : Serializable {

            @JsonProperty("_id")
            var _id: String? = null

            @JsonProperty("date")
            var date: String? = ""

            @JsonProperty("status")
            var status: String? = ""
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Progress : Serializable {

            @JsonProperty("totalWorkingDays")
            var totalWorkingDays: Int? = 0

            @JsonProperty("total")
            var total: Int? = 0

            @JsonProperty("presentCount")
            var presentCount: Int? = 0

            @JsonProperty("absentCount")
            var absentCount: Int? = 0

            @JsonProperty("presentPercentage")
            var presentPercentage: Int? = 0

            @JsonProperty("absentPercentage")
            var absentPercentage: Int? = 0

            @JsonProperty("halfDay")
            var halfDay: Int? = 0

        }
    }
}