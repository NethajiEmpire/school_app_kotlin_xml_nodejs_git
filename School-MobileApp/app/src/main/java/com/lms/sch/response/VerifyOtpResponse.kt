package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
open class VerifyOtpResponse : BaseModel() {
    @JsonProperty("data")
    var data: Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {
        @JsonProperty("accessToken")
        var accessToken: String? = ""

        @JsonProperty("refreshToken")
        var refreshToken: String? = ""

        @JsonProperty("user")
        var user: User? = null

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class User : Serializable {
            @JsonProperty("id")
            var id: String? = ""

            @JsonProperty("name")
            var name: String? = ""

            @JsonProperty("email")
            var email: String? = ""

            @JsonProperty("mobile")
            var mobile: String? = ""

            @JsonProperty("role")
            var role: String? = ""
        }
    }
}
