package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class TeacherStatsResponse : BaseModel() {
    @JsonProperty("data")
    var data : Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable{
        @JsonProperty("totalStudents")
        var totalStudents : String? = ""

        @JsonProperty("totalsubject")
        var totalsubject : String? = ""

        @JsonProperty("totalclasses")
        var totalclasses : String? = ""

        @JsonProperty("todayperiods")
        var todayperiods : String? = ""
    }
}