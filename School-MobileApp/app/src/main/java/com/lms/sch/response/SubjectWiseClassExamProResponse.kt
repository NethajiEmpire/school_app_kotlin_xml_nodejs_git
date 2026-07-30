package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

class SubjectWiseClassExamProResponse : BaseModel() {

    @JsonProperty("result")
    var result : ArrayList<Result> = ArrayList()

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable{

        @JsonProperty("subject")
        var subject : String? = ""

        @JsonProperty("percentage")
        var percentage : Int? = 0
    }
}