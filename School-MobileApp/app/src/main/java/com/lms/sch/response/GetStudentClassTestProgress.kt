package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import com.lms.sch.response.NoticeBoardResponse.Result
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
open class GetStudentClassTestProgress : BaseModel() {

    @JsonProperty("result")
    val result : ArrayList<Result> = ArrayList()

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable{

        @JsonProperty("title")
        var title : String? = ""

        @JsonProperty("subject")
        var subject : String? = ""

        @JsonProperty("scheduledOn")
        var scheduledOn : String? = ""

        @JsonProperty("dueDate")
        var dueDate : String? = ""

        @JsonProperty("scoredMarks")
        var scoredMarks : String? = ""

        @JsonProperty("totalMarks")
        var totalMarks : String? = ""

        @JsonProperty("percentage")
        var percentage : String? = ""

        @JsonProperty("remarks")
        var remarks : String? = ""

        @JsonProperty("credits")
        var credits : String? = ""

    }

}