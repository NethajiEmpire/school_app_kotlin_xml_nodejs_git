package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class GetStudentAttenDanceResponse : BaseModel() {

    @JsonProperty("data")
    var data: Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable{

        @JsonProperty("attendance")
        var attendance : ArrayList<Attendance>? = ArrayList()

        @JsonProperty("progress")
        var progress : Progress? = null

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Attendance : Serializable{

            @JsonProperty("_id")
            var _id : String? = ""

            @JsonProperty("status")
            var status : String? = ""

            @JsonProperty("date")
            var date :  String? = ""
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Progress : Serializable{

            @JsonProperty("totalWorkingDays")
            var totalWorkingDays : Int? = null

            @JsonProperty("total")
            var total : Int? = null

            @JsonProperty("presentCount")
            var  presentCount : Int? = null

            @JsonProperty("absentCount")
            var absentCount : Int? = null

            @JsonProperty("halfDayCount")
            var halfDayCount : Int? = null

            @JsonProperty("presentPercentage")
            var presentPercentage : Int? = null

            @JsonProperty("absentPercentage")
            var absentPercentage : Int? = null

            @JsonProperty("halfDayPercentage")
            var halfDayPercentage : Int? = null
        }
        @JsonProperty("streaks")
        var streaks : Int? = null
    }

}