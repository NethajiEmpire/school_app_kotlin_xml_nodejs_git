package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class SubmissionAssignmentResponse : BaseModel() {

    @JsonProperty("data")
    var data : Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)

    class Result : Serializable{

        @JsonProperty("totalassignment")
        var totalassignment : Int? = 0

        @JsonProperty("onTime")
        var onTime : Int? = 0

        @JsonProperty("missed")
        var  missed : Int? = 0

        @JsonProperty("late")
        var  late : Int? = 0

        @JsonProperty("points")
        var points : Int? = 0

        @JsonProperty("percentage")
        var  percentage : Percentage? = null

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)

        class Percentage : Serializable{

            @JsonProperty("onTime")
            var onTime : Int? = 0

            @JsonProperty("late")
            var late : Int? = 0

            @JsonProperty("missed")
            var  missed : Int? = 0

        }
    }
}