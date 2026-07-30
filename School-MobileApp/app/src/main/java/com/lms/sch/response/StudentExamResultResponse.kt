package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class StudentExamResultResponse : BaseModel() {

    @JsonProperty("result")
    var result: Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {

        @JsonProperty("rows")
        var rows: ArrayList<Rows>? = ArrayList()

        @JsonProperty("pagination")
        var pagination: Pagination? = null
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Rows : Serializable {

        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("answerSheet")
        var answerSheet : AnswerSheet? = null

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)

        class AnswerSheet : Serializable{

            @JsonProperty("updatedAt")
            var updatedAt : String? = ""

            @JsonProperty("url")
            var url : String?  = ""

        }

        @JsonProperty("student")
        var student: StudentDetails? = null

        @JsonProperty("majorExam")
        var majorExam: MajorExamDetails? = null

        @JsonProperty("board")
        var board : String? = ""

        @JsonProperty("batch")
        var  batch : Batch?  = null

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Batch :  Serializable{

            @JsonProperty("_id")
            var  _id : String? = ""

            @JsonProperty("name")
            var name : String? = ""

        }

        @JsonProperty("subject")
        var subject: SubjectDetails? = null

        @JsonProperty("exam_subject")
        var examSubject: ExamSubjectDetails? = null

        @JsonProperty("attendance")
        var attendance: String? = ""

        @JsonProperty("totalMark")
        var totalMark: String? = ""

        @JsonProperty("scoredPracticalMark")
        var scoredPracticalMark: String? = ""

        @JsonProperty("scoredMark")
        var scoredMark: String? = ""

        @JsonProperty("grade")
        var grade: Grade? = null

        @JsonProperty("totalScore")
        var totalScore: String? = ""

        @JsonProperty("status")
        var status: String? = ""

        @JsonProperty("isDeleted")
        var isDeleted: Boolean? = false

        @JsonProperty("createdAt")
        var createdAt: String? = ""

        @JsonProperty("updatedAt")
        var updatedAt: String? = ""
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

        @JsonProperty("mobile")
        var mobile: String? = ""

        @JsonProperty("email")
        var email: String? = ""

        @JsonProperty("img_Url")
        var img_Url: String? = ""
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
    class Grade : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("name")
        var name: String? = ""
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class SubjectDetails : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("name")
        var name: String? = ""
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class ExamSubjectDetails : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("date")
        var date: String? = ""

        @JsonProperty("practicalMark")
        var practicalMark: String? = ""
    }

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
}
