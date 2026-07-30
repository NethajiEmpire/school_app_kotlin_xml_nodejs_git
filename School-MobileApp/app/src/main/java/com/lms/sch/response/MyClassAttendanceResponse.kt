package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class MyClassAttendanceResponse : BaseModel() {

    @JsonProperty("result")
    var result: Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {

        @JsonProperty("totalWorkingDays")
        var totalWorkingDays: Int? = null

        @JsonProperty("totalStudents")
        var totalStudents: Int? = null

        @JsonProperty("percent")
        var percent: Percent? = null

        @JsonProperty("count")
        var count: Count? = null

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Percent : Serializable {
            @JsonProperty("total")
            var total: Int? = null

            @JsonProperty("present")
            var present: Int? = null

            @JsonProperty("absent")
            var absent: Int? = null

            @JsonProperty("halfDay")
            var halfDay: Int? = null
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Count : Serializable {
            @JsonProperty("total")
            var total: Int? = null

            @JsonProperty("present")
            var present: Int? = null

            @JsonProperty("absent")
            var absent: Int? = null

            @JsonProperty("halfDay")
            var halfDay: Int? = null
        }
    }
}