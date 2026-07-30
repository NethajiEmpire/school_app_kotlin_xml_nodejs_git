package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
open class BatchDropdownResponse : BaseModel() {

    @JsonProperty("result")
    @JsonIgnore
    var result: ArrayList<Result>? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {

        @JsonProperty("label")
        @JsonIgnore
        var label: String? = ""

        @JsonProperty("value")
        @JsonIgnore
        var value: String? = ""

        @JsonProperty("currentBtach")
        @JsonIgnore
        var currentBtach: Boolean? = false
    }

}
