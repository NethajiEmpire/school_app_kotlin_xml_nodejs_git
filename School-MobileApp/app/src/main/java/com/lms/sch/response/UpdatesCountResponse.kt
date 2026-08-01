package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
open class UpdatesCountResponse : BaseModel() {

    @JsonProperty("data")
    @JsonIgnore
    var data: Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {

        @JsonProperty("assignmentcount")
        var assignmentcount: String? = ""

        @JsonProperty("projectcount")
        var projectcount: String? = ""

        @JsonProperty("homeworkcount")
        var homeworkcount: String? = ""

        @JsonProperty("classTestcount")
        var classTestcount: String? = ""

        @JsonProperty("Examcount")
        var Examcount: String? = ""

    }
}