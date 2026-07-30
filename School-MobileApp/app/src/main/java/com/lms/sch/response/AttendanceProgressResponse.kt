package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class AttendanceProgressResponse : BaseModel() {

    @JsonProperty("result")
    var result: Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {

        @JsonProperty("attendance")
        var attendance: ArrayList<Attendance>? = ArrayList()

        @JsonProperty("attendanceDate")
        var attendanceDate: String? = ""

        @JsonProperty("progress")
        var progress: Progress? = null

        @JsonProperty("streaks")
        var streaks: String? = ""

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Attendance : Serializable {

            @JsonProperty("_id")
            var _id: String? = ""

            @JsonProperty("status")
            var status: String? = ""

            @JsonProperty("date")
            var date: String? = ""

        }
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Progress : Serializable {

            @JsonProperty("total")
            var total: String? = ""

            @JsonProperty("presentCount")
            var presentCount: String? = ""

            @JsonProperty("absentCount")
            var absentCount: String? = ""

            @JsonProperty("halfDayCount")
            var halfDayCount: String? = ""

            @JsonProperty("halfDayPercentage")
            var halfDayPercentage: String? = ""

            @JsonProperty("presentPercentage")
            var presentPercentage: String? = ""

            @JsonProperty("absentPercentage")
            var absentPercentage: String? = ""
        }
    }
}
