package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import com.lms.sch.response.UpdatesCountResponse.Result
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

class ProjectUpdCountResponse : BaseModel() {

    @JsonProperty("result")
    @JsonIgnore
    var result: Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)

    class Result : Serializable {

        @JsonProperty("totalProject")
        var totalProject: String? = ""

        @JsonProperty("completed")
        var completed: String? = ""

        @JsonProperty("ongoing")
        var ongoing: String? = ""

        @JsonProperty("notcompletd")
        var notcompletd: String? = ""

    }
}