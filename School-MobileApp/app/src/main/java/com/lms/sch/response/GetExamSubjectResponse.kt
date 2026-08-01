package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import com.lms.sch.response.GetExamResponse.ExamType
import com.lms.sch.response.GetExamResponse.Incharge
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class GetExamSubjectResponse : BaseModel() {

    @JsonProperty("data")
    var data: Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {

        @JsonProperty("rows")
        var rows: ArrayList<Row>? = ArrayList()

        @JsonProperty("pagination")
        var pagination: Pagination? = null

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Row : Serializable {

            @JsonProperty("_id")
            var _id: String? = ""

            @JsonProperty("majorExam")
            var majorExam: MajorExam? = null

            @JsonProperty("subject")
            var subject: Subject? = null

            @JsonProperty("incharge")
            var incharge: Incharge? = null

            @JsonProperty("date")
            var date: String? = ""

            @JsonProperty("day")
            var day: String? = ""

            @JsonProperty("fromTime")
            var fromTime: String? = ""

            @JsonProperty("toTime")
            var toTime: String? = ""

            @JsonProperty("duration")
            var duration: Int? = null

            @JsonProperty("practicalMark")
            var practicalMark: Int? = null

            @JsonProperty("totalMark")
            var totalMark: Int? = null

            @JsonProperty("passMark")
            var passMark: Int? = null

            @JsonProperty("session")
            var session: String? = ""

            @JsonProperty("completeStatus")
            var completeStatus: String? = ""

            @JsonProperty("createdBy")
            var createdBy: String? = ""

            @JsonProperty("isDeleted")
            var isDeleted: Boolean? = false

            @JsonProperty("createdAt")
            var createdAt: String? = ""

            @JsonProperty("updatedAt")
            var updatedAt: String? = ""
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class MajorExam : Serializable {
            @JsonProperty("_id")
            var _id: String? = ""

            @JsonProperty("examType")
            var examType: ExamType? = null

            @JsonProperty("startDate")
            var startDate: String? = ""

            @JsonProperty("endDate")
            var endDate: String? = ""
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Subject : Serializable {
            @JsonProperty("_id")
            var _id: String? = ""

            @JsonProperty("name")
            var name: String? = ""
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
}
