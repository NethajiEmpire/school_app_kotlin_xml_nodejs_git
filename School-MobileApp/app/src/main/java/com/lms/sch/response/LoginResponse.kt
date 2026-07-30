package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
open class LoginResponse : BaseModel() {
    @JsonProperty("result")
    @JsonIgnore
    var result: Result = Result()

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {
        @JsonProperty("tokens")
        @JsonIgnore
        var tokens: Token = Token()

        @JsonProperty("userDetails")
        @JsonIgnore
        var userDetails: User = User()

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Token : Serializable {
            @JsonProperty("accessToken")
            @JsonIgnore
            var accessToken: String? = ""

            @JsonProperty("refreshToken")
            @JsonIgnore
            var refreshToken: String? = ""
        }
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class User : Serializable {
            @JsonProperty("id")
            var id: String? = ""

            @JsonProperty("name")
            var name: String? = ""

            @JsonProperty("userName")
            var userName: String? = ""

            @JsonProperty("firstName")
            var firstName: String? = ""

            @JsonProperty("lastName")
            var lastName: String? = ""

            @JsonProperty("email")
            var email: String? = ""

            @JsonProperty("mobile")
            var mobile: String? = ""

            @JsonProperty("provider")
            var provider: String? = ""

            @JsonProperty("role")
            var role: Role? = null

            @JsonProperty("mobileVerified")
            var mobileVerified: Boolean? = false

            @JsonProperty("emailVerified")
            var emailVerified: Boolean? = false

            @JsonProperty("initialized")
            var initialized: Boolean? = false

            @JsonProperty("dob")
            var dob: String? = ""

            @JsonProperty("img_url")
            var img_url: String? = ""

            @JsonProperty("isDeleted")
            var isDeleted: Boolean? = false

            @JsonProperty("createdAt")
            var createdAt: String? = ""

            @JsonProperty("updatedAt")
            var updatedAt: String? = ""

            @JsonProperty("roleType")
            var roleType: String? = ""

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @JsonIgnoreProperties(ignoreUnknown = true)
            class Role : Serializable {
                @JsonProperty("_id")
                var _id: String? = ""

                @JsonProperty("name")
                var name: String? = ""

                /*  @JsonProperty("permissions")
                  var permissions: String? = ""*/
            }
        }
    }
}


