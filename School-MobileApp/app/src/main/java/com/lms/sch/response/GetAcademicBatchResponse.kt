package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class GetAcademicBatchResponse : BaseModel() {

    @JsonProperty("result")
    var result: Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {

        @JsonProperty("rows")
        var rows: ArrayList<Rows>? = ArrayList()

        @JsonProperty("pagination")
        var pagination: Pagination? = null

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Pagination : Serializable {
            @JsonProperty("currentPage")
            var currentPage: String? = ""

            @JsonProperty("pages")
            var pages: String? = ""

            @JsonProperty("total")
            var total: String? = ""
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Rows : Serializable {

            @JsonProperty("_id")
            var _id: String? = ""

            @JsonProperty("name")
            var name: String? = ""

            @JsonProperty("batchId")
            var batchId: String? = ""

            @JsonProperty("description")
            var description: String? = ""

            @JsonProperty("start_date")
            var start_date: String? = ""

            @JsonProperty("end_date")
            var end_date: String? = ""

            @JsonProperty("status")
            var status: String? = ""

            @JsonProperty("admission_now")
            var admission_now: Boolean? = false

            @JsonProperty("currentBatch")
            var currentBatch: String? = ""

            @JsonProperty("studentCount")
            var studentCount: Int? = null

            @JsonProperty("teacherCount")
            var teacherCount: Int? = null

        }
    }
}

