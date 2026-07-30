package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
open class ParentProfileResponse : BaseModel() {

    @JsonProperty("result")
    var result: Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {

        @JsonProperty("userprofile")
        var userprofile: UserProfile? = null

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class UserProfile : Serializable {

            @JsonProperty("_id")
            var _id: String? = ""

            @JsonProperty("firstName")
            var firstName: String? = ""

            @JsonProperty("lastName")
            var lastName: String? = ""

            @JsonProperty("mobile")
            var mobile: String? = ""

            @JsonProperty("email")
            var email: String? = ""

            @JsonProperty("emailVerified")
            var emailVerified: Boolean? = null

            @JsonProperty("img_url")
            var img_url: String? = ""

            @JsonProperty("role")
            var role: Role? = null

            @JsonProperty("students")
            var students: ArrayList<Students>? = ArrayList()


            @JsonProperty("student_enrollment")
            var studentEnrollment: Boolean? = null

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @JsonIgnoreProperties(ignoreUnknown = true)
            class Role : Serializable {
                @JsonProperty("_id")
                var id: String? = ""

                @JsonProperty("name")
                var name: String? = ""
            }

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @JsonIgnoreProperties(ignoreUnknown = true)
            class Students : Serializable {

                @JsonProperty("_id")
                var _id: String? = ""

                @JsonProperty("current_user")
                var current_user: Boolean = false

                @JsonProperty("user_id")
                var user_id: User? = null

                @JsonInclude(JsonInclude.Include.NON_NULL)
                @JsonIgnoreProperties(ignoreUnknown = true)
                class User : Serializable {

                    @JsonProperty("firstName")
                    var firstName: String? = ""

                    @JsonProperty("lastName")
                    var lastName: String? = ""

                    @JsonProperty("email")
                    var email: String? = ""

                    @JsonProperty("mobile")
                    var mobile: String? = ""

                    @JsonProperty("_id")
                    var _id: String? = ""

                    @JsonProperty("lead_id")
                    var lead_id: String? = ""
                }

            }
        }

    }
}
