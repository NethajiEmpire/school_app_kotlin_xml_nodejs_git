package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class PointsHistoryResponse : BaseModel() {

    @JsonProperty("data")
    var data : Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)

    class Result : Serializable{

        @JsonProperty("finalList")
        var  finalList : ArrayList<FinalList>? = ArrayList()

        @JsonProperty("totalPoints")
        var totalPoints : Int? = 0

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)

        class FinalList : Serializable{

            @JsonProperty("module")
            var module : String? = ""

            @JsonProperty("_id")
            var _id : String? = ""

            @JsonProperty("name")
            var name : String? =  ""

            @JsonProperty("Date")
            var Date : String? = ""

            @JsonProperty("credits")
            var credits : Int? = 0

        }
    }
}