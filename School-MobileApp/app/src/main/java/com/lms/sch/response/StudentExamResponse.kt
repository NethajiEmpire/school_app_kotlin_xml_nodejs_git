package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import com.lms.sch.response.GetExamResponse.Pagination
import com.lms.sch.response.GetExamResponse.Row
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class StudentExamResponse : BaseModel() {

    @JsonProperty("data")
    var data: Result? = null

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

        @JsonProperty("hallTicket")
        var hallTicket: HallTicket? = null

        @JsonProperty("markSheet")
        var markSheet: MarkSheet? = null

        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("student")
        var student: StudentDetails? = null

        @JsonProperty("majorExam")
        var majorExam: MajorExamDetails? = null

        @JsonProperty("program")
        var program: String? = ""

        @JsonProperty("batch")
        var batch: Batch? = null

        @JsonProperty("createdBy")
        var createdBy: CreatedBy? = null

        @JsonProperty("isDeleted")
        var isDeleted: Boolean? = false

        @JsonProperty("details")
        var details: ArrayList<Any>? = ArrayList() // Adjust type as needed

        @JsonProperty("createdAt")
        var createdAt: String? = ""

        @JsonProperty("updatedAt")
        var updatedAt: String? = ""

        @JsonProperty("hallTicketStatus")
        var hallTicketStatus: String? = ""

        @JsonProperty("markSheetStatus")
        var markSheetStatus: String? = ""
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class HallTicket : Serializable {
        @JsonProperty("url")
        var url: String? = ""

        @JsonProperty("uploadedAt")
        var uploadedAt: String? = ""

        @JsonProperty("uploadedBy")
        var uploadedBy: String? = ""
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class MarkSheet : Serializable {
        @JsonProperty("uploadedAt")
        var uploadedAt: String? = ""

        @JsonProperty("uploadedBy")
        var uploadedBy: String? = ""
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class StudentDetails : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("firstName")
        var firstName: String? = ""

        @JsonProperty("lastName")
        var lastName: String? = ""

        @JsonProperty("img_url")
        var img_url : String? = ""
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class MajorExamDetails : Serializable {
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
    class ExamType : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("name")
        var name: String? = ""
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
    class CreatedBy : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("firstName")
        var firstName: String? = ""

        @JsonProperty("lastName")
        var lastName: String? = ""
    }
}
