package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
open class LoginResponse : BaseModel() {
    @JsonProperty("data")
    var data: Result = Result()

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {
        @JsonProperty("accessToken")
        var accessToken: String? = ""

        @JsonProperty("refreshToken")
        var refreshToken: String? = ""

        @JsonProperty("user")
        var user: User = User()

        @JsonProperty("school")
        var school: School = School()

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
            var role: String? = "" // Role is now a String
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class School : Serializable {
            @JsonProperty("schoolCode")
            var schoolCode: String? = ""

            @JsonProperty("schoolName")
            var schoolName: String? = ""

            @JsonProperty("logoUrl")
            var logoUrl: String? = ""
        }
    }
}
