package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class GetProgressPointsStatsResponse : BaseModel() {

    @JsonProperty("result")
    var result: Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {

        @JsonProperty("score")
        var score: Int? = null

        @JsonProperty("rank")
        var rank: Any? = null

        @JsonProperty("streaks")
        var streaks: Int? = null

        @JsonProperty("grade")
        var grade: String = ""

        @JsonProperty("majorExamId")
        var majorExamId: String = ""

        @JsonProperty("examType")
        var examType: String = ""

    }
}

