package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class GetTeacherNewEventResponse : BaseModel() {

    @JsonProperty("data")
    var data: Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {

        @JsonProperty("rows")
        var rows: ArrayList<Rows>? = ArrayList()

        @JsonProperty("pagination")
        var pagination: GetStaffResponse.Result.Pagination? = null

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Rows : Serializable {

            @JsonProperty("_id")
            var _id: String? = ""

            @JsonProperty("title")
            var title: String? = ""

            @JsonProperty("description")
            var description: String? = ""

            @JsonProperty("type")
            var type: String? = ""

            @JsonProperty("from")
            var from: String? = ""

            @JsonProperty("to")
            var to: String? = ""

            @JsonProperty("isDeleted")
            var isDeleted: Boolean? = false

            @JsonProperty("createdAt")
            var createdAt: String? = ""

            @JsonProperty("updatedAt")
            var updatedAt: String? = ""

            @JsonProperty("attachment")
            var attachment: ArrayList<Any>? = ArrayList()
        }

    }

}