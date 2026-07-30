package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class GetExamResponse : BaseModel() {

    @JsonProperty("result")
    var result: Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {

        @JsonProperty("rows")
        var rows: ArrayList<Row>? = ArrayList()

        @JsonProperty("pagination")
        var pagination: Pagination? = null
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Row : Serializable {

        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("examType")
        var examType: ExamType? = null

        @JsonProperty("description")
        var description: String? = ""

        @JsonProperty("board")
        var board: Board? = null

        @JsonProperty("standard")
        var standard: Standard? = null

        @JsonProperty("batch")
        var batch: Batch? = null

        @JsonProperty("incharge")
        var incharge: Incharge? = null

        @JsonProperty("startDate")
        var startDate: String? = ""

        @JsonProperty("endDate")
        var endDate: String? = ""

        @JsonProperty("students")
        var students: ArrayList<String>? = ArrayList()

        @JsonProperty("completeStatus")
        var completeStatus: String? = ""

        @JsonProperty("status")
        var status: String? = ""

        @JsonProperty("noDueForm")
        var noDueForm: String? = ""

        @JsonProperty("createdBy")
        var createdBy: String? = ""

        @JsonProperty("isDeleted")
        var isDeleted: Boolean? = false

        @JsonProperty("createdAt")
        var createdAt: String? = ""

        @JsonProperty("updatedAt")
        var updatedAt: String? = ""

        @JsonProperty("subjectCount")
        var subjectCount: String? = ""
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class ExamType : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("name")
        var name: String? = ""
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Board : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("name")
        var name: String? = ""
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Standard : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("name")
        var name: Int? = null
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Batch : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("name")
        var name: String? = ""
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Incharge : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("firstName")
        var firstName: String? = ""

        @JsonProperty("lastName")
        var lastName: String? = ""

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Pagination : Serializable {
        @JsonProperty("currentPage")
        var currentPage: Int? = null

        @JsonProperty("pages")
        var pages: Int? = null

        @JsonProperty("total")
        var total: Int? = null
    }
}
