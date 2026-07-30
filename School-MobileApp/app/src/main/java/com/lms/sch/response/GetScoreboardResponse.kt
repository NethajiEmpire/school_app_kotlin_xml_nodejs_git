package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class GetScoreboardResponse : BaseModel() {

    @JsonProperty("result")
    var result: ArrayList<Result>? = ArrayList()

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {

        @JsonProperty("firstName")
        var firstName: String? = ""

        @JsonProperty("lastName")
        var lastName: String? = ""

        @JsonProperty("email")
        var email: String? = ""

        @JsonProperty("mobile")
        var mobile: String? = ""

        @JsonProperty("points")
        var points: Int? = null

        @JsonProperty("me")
        var me: Boolean? = false

        @JsonProperty("rank")
        var rank: Int? = null

        @JsonProperty("img_url")
        var img_url: String? = ""

        @JsonProperty("gender")
        var gender: String? = ""
    }
}
