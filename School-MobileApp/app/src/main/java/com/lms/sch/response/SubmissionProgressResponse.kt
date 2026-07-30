package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

class SubmissionProgressResponse : BaseModel() {

    @JsonProperty("result")
    var result : Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {

        @JsonProperty("total")
        var total : String?= ""

        @JsonProperty("onTime")
        var onTime : String?= ""

        @JsonProperty("missed")
        var  missed : String?= ""

        @JsonProperty("late")
        var  late : String?= ""

        @JsonProperty("points")
        var points : String?= ""

        @JsonProperty("percentage")
        var  percentage : Percentage? = null

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)

        class Percentage : Serializable {

            @JsonProperty("onTime")
            var onTime : String?= ""

            @JsonProperty("late")
            var late : String?= ""

            @JsonProperty("missed")
            var  missed : String?= ""

        }
    }
}
