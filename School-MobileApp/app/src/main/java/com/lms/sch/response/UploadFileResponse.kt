package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
open class UploadFileResponse : BaseModel() {
    @JsonProperty("result")
    @JsonIgnore
    var result: ArrayList<Result> = ArrayList()

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {
        @JsonProperty("location")
        @JsonIgnore
        var location: String? = ""
    }
}
