package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable
import java.util.ArrayList


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
open class PincodeResponse : BaseModel() {

    @JsonProperty("result")
    @JsonIgnore
    var result: ArrayList<Result> = ArrayList()

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {

        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("circleName")
        var circleName: String? = ""

        @JsonProperty("regionName")
        var regionName: String? = ""

        @JsonProperty("divisionName")
        var divisionName: String? = ""

        @JsonProperty("officeName")
        var officeName: String? = ""

        @JsonProperty("pincode")
        var pincode: String? = ""

        @JsonProperty("officeType")
        var officeType: String? = ""

        @JsonProperty("delivery")
        var delivery: String? = ""

        @JsonProperty("district")
        var district: String? = ""

        @JsonProperty("stateName")
        var stateName: String? = ""

        @JsonProperty("latitude")
        var latitude: String? = ""

        @JsonProperty("longitude")
        var longitude: String? = ""

    }
}