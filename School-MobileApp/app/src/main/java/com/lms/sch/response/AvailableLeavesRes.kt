package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ser.Serializers
import com.lms.sch.models.BaseModel
import java.io.Serializable


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class AvailableLeavesRes : BaseModel(){

    @JsonProperty("result")
    var result : ArrayList<Result>? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)

    class Result : Serializable {

        @JsonProperty("_id")
        var _id : String? = ""

        @JsonProperty("role")
        var role : String? = ""

        @JsonProperty("numberOfDays")
        var numberOfDays : String? = ""

        @JsonProperty("status")
        var status : String? = ""

        @JsonProperty("isDeleted")
        var isDeleted : String? = ""

        @JsonProperty("createdBy")
        var createdBy : String? = ""

        @JsonProperty("remaining")
        var remaining : String? = ""

        @JsonProperty("taken")
        var taken : String? = ""

        @JsonProperty("leaveType")
        var leaveType : LeaveType? = null

        class LeaveType : Serializable{

            @JsonProperty("_id")
            var _id : String? = ""

            @JsonProperty("name")
            var name : String? = ""
        }
    }
}