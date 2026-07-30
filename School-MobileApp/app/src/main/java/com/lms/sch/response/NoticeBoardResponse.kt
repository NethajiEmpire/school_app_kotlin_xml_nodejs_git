package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import com.lms.sch.response.StudentFeeResponse.CreatedBy
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
open class NoticeBoardResponse : BaseModel(){

    @JsonProperty("result")
    val result : ArrayList<Result> = ArrayList()

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable{
        @JsonProperty("_id")
        var _id:String?=""

        @JsonProperty("title")
         var title: String?=""

        @JsonProperty("type")
        var type: Type? = null

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Type : Serializable{
            @JsonProperty("_id")
            var _id : String? = ""

            @JsonProperty("name")
            var  name : String? = ""
        }
        @JsonProperty("description")
        var description: String?=""

        @JsonProperty("viewType")
        var viewType: String?=""

        @JsonProperty("startDate")
        var startDate: String?=""

        @JsonProperty("endDate")
        var endDate: String?=""

        @JsonProperty("from")
        var from: String?=""

        @JsonProperty("to")
        var to: String?=""

        @JsonProperty("createdBy")
        var createdBy: CreatedBy? = null

        @JsonProperty("attachment")
        var attachment: ArrayList<String>? = ArrayList()

        @JsonProperty("isDeleted")
        var isDeleted: Boolean? = false

        @JsonProperty("createdAt")
        var createdAt: String?=""

        @JsonProperty("updatedAt")
        var updatedAt: String?=""


    }

}