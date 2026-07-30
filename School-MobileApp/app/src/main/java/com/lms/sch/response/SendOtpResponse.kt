package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.lms.sch.models.BaseModel

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
open class SendOtpResponse : BaseModel() {
   /* @JsonProperty("result")
    @JsonIgnore
    var result: Boolean? = false*/
}