package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class GetComplaintStatResponse : BaseModel() {

    @JsonProperty("data")
    var data: Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {

        @JsonProperty("total")
        var total: Int? = null

        @JsonProperty("solved")
        var solved: Int? = null

        @JsonProperty("unsolved")
        var unsolved: Int? = null

        @JsonProperty("today")
        var today: Int? = null

    }
}
