package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class AdminAttendanceResponse : BaseModel(){

    @JsonProperty("data")
    var data :  Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)

    class Result : Serializable{

        @JsonProperty("totalWorkingDays")
        var totalWorkingDays : Int? = 0

        @JsonProperty("percent")
        var percent : Percent? = null

        @JsonProperty("count")
        var count : Count? = null

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)

        class Percent : Serializable{

            @JsonProperty("total")
            var total : Int? = 0

            @JsonProperty("present")
            var present : Int? = 0

            @JsonProperty("absent")
            var absent : Int? = 0

            @JsonProperty("halfDay")
            var halfDay : Int? = 0
        }
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)

        class Count : Serializable{

            @JsonProperty("total")
            var total : Int? = 0

            @JsonProperty("present")
            var present : Int? = 0

            @JsonProperty("absent")
            var absent : Int? = 0

            @JsonProperty("halfDay")
            var halfDay : Int? = 0

        }
    }
}