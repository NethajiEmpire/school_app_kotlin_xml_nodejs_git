package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class GetTeacherStudentStatsCountRes: BaseModel() {

    @JsonProperty("result")
    var result: Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {

        @JsonProperty("totalStudents")
        var totalStudents: Int? = null

        @JsonProperty("boysStudent")
        var boysStudent: Any? = null

        @JsonProperty("girlsStudent")
        var girlsStudent: Int? = null

        @JsonProperty("inactiveStudents")
        var inactiveStudents: Int? = null

    }
}