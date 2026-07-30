package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

class TeacherAssCountResponse : BaseModel() {

    @JsonProperty("result")
    @JsonIgnore
    var result: Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)

    class Result : Serializable {

        @JsonProperty("totalassignment")
        var totalassignment: String? = ""

        @JsonProperty("completed")
        var completed: String? = ""

        @JsonProperty("pending")
        var pending: String? = ""

    }
}