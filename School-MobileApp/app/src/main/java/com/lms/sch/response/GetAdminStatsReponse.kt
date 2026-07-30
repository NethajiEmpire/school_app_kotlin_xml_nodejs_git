package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class GetAdminStatsReponse : BaseModel() {

    @JsonProperty("result")
    var result: Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {

        @JsonProperty("totalStudents")
        var totalStudents: Int? = null

        @JsonProperty("totalTeachers")
        var totalTeachers: Int? = null

        @JsonProperty("totalEmployees")
        var totalEmployees: Int? = null

        @JsonProperty("totalRevenue")
        var totalRevenue: Int? = null

        @JsonProperty("totalGuest")
        var totalGuest: Int? = null
    }
}
