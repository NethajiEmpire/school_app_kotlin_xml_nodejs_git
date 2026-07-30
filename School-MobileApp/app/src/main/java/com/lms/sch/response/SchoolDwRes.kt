package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class SchoolDwRes : BaseModel(){

    @JsonProperty("result")
    var result : ArrayList<Result>? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)

    class Result : Serializable{

        @JsonProperty("label")
        var label : String? = ""

        @JsonProperty("value")
        var value : String? = ""

        @JsonProperty("email")
        var email : String? = ""

        @JsonProperty("mobile")
        var mobile : String? = ""

        @JsonProperty("city")
        var city : String? = ""

        @JsonProperty("state")
        var state : String? = ""

        @JsonProperty("country")
        var country : String? = ""

        @JsonProperty("pincode")
        var pincode : String? = ""



    }
}