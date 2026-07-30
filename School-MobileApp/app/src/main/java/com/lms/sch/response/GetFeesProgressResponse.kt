package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class GetFeesProgressResponse : BaseModel() {

    @JsonProperty("result")
    var result: Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {

        @JsonProperty("classId")
        var classId: String = ""

        @JsonProperty("className")
        var className: Int? = null

        @JsonProperty("studentCount")
        var studentCount: Int? = null

        @JsonProperty("feesAmount")
        var feesAmount: Int? = null

        @JsonProperty("admissionFee")
        var admissionFee: Int? = null

        @JsonProperty("collected")
        var collected: Int? = null

        @JsonProperty("totalCount")
        var totalCount: Int? = null
    }
}
