package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class GetOverallAttendanceProgressResponse : BaseModel() {

    @JsonProperty("data")
    var data: Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {

        @JsonProperty("totalWorkingDays")
        var totalWorkingDays: String? = ""

        @JsonProperty("percent")
        var percent: Percent? = null

        @JsonProperty("count")
        var count: Count? = null

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Percent : Serializable {

            @JsonProperty("present")
            var present: Float? = null

            @JsonProperty("absent")
            var absent: Float? = null

            @JsonProperty("halfDay")
            var halfDay: Float? = null
        }
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Count : Serializable {

            @JsonProperty("present")
            var present: Float? = null

            @JsonProperty("absent")
            var absent: Float? = null

            @JsonProperty("halfDay")
            var halfDay: Float? = null
        }
    }
}