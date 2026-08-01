package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

class SubmissionProjectResponse : BaseModel() {

    @JsonProperty("data")
    var data : Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)

    class Result : Serializable {

        @JsonProperty("totalProject")
        var totalProject : Int? = 0

        @JsonProperty("onTime")
        var onTime : Int? = 0

        @JsonProperty("late")
        var  late : Int? = 0

        @JsonProperty("missed")
        var  missed : Int? = 0

        @JsonProperty("percentage")
        var  percentage : Percentage? = null

        @JsonProperty("points")
        var points : Int? = 0

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)

        class Percentage : Serializable {

            @JsonProperty("onTime")
            var onTime : Int? = 0

            @JsonProperty("late")
            var late : Int? = 0

            @JsonProperty("missed")
            var  missed : Int? = 0

        }
    }
}
