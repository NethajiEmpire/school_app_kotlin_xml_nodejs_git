package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
open class VerifyOtpResponse : BaseModel() {
    @JsonProperty("result")
    @JsonIgnore
    var result: Result? = null

    class Result : Serializable {
        @JsonProperty("tokens")
        @JsonIgnore
        var tokens: Token? = null

        @JsonProperty("userDetails")
        @JsonIgnore
        var userDetails: User? = null
    }
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
        @JsonProperty("progressBar")
        var progressBar: String? = ""

        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("name")
        var name: String? = ""

        @JsonProperty("email")
        var email: String? = ""

        @JsonProperty("mobile")
        var mobile: String? = ""

        @JsonProperty("provider")
        var provider: String? = ""

        @JsonProperty("role")
        var role: Roles? = null

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

        @JsonProperty("lastLogin")
        var lastLogin: String? = ""

        @JsonProperty("isDeclared")
        var isDeclared: Boolean? = false

        @JsonProperty("isDeleted")
        var isDeleted: Boolean? = false

        @JsonProperty("currentStep")
        var currentStep: String? = ""

        @JsonProperty("appStatus")
        var appStatus: String? = ""

        @JsonProperty("createdAt")
        var createdAt: String? = ""

        @JsonProperty("updatedAt")
        var updatedAt: String? = ""

        @JsonProperty("ad_counselor")
        var ad_counselor: AdCounselor? = null

        @JsonProperty("roleType")
        var roleType: String? = ""

        @JsonProperty("lead_id")
        var lead_id: String? = ""

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Roles : Serializable {
            @JsonProperty("_id")
            var _id: String? = ""

            @JsonProperty("name")
            var name: String? = ""

           /* @JsonProperty("permissions")
            @JsonIgnore
            var permissions: String? = ""*/
        }
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class AdCounselor : Serializable {
            @JsonProperty("_id")
            var _id: String? = ""

            @JsonProperty("name")
            var name: String? = ""

            @JsonProperty("email")
            var email: String? = ""

            @JsonProperty("mobile")
            var mobile: String? = ""
        }
    }
}
