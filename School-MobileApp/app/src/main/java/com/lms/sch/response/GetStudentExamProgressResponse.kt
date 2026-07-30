package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class GetStudentExamProgressResponse : BaseModel() {

    @JsonProperty("result")
    var result: ArrayList<Result>? = ArrayList()

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {
        @JsonProperty("subject")
        var subject: String? = ""

        @JsonProperty("scoredMarks")
        var scoredMarks: Int? = null

        @JsonProperty("totalMarks")
        var totalMarks: Int? = null

        @JsonProperty("percentage")
        var percentage: String? = ""

        @JsonProperty("examType")
        var examType: ExamType? = null

        @JsonProperty("startsDate")
        var startsDate: String? = ""

        @JsonProperty("EndsDate")
        var endsDate: String? = ""
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class ExamType : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("name")
        var name: String? = ""
    }
}
