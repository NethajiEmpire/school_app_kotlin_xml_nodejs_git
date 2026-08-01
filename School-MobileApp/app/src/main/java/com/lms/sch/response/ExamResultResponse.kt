package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class ExamResultResponse : BaseModel() {

    @JsonProperty("data")
    var data : Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable{
        @JsonProperty("rows")
        var rows : ArrayList<Rows>? = ArrayList()

        @JsonProperty("pagination")
        var pagination : Pagination? = null
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Rows : Serializable{

        @JsonProperty("answerSheet")
        var answerSheet : AnswerSheet? =  null

        @JsonProperty("_id")
        var _id : String? = ""

        @JsonProperty("student")
        var student : Student? = null

        @JsonProperty("majorExam")
        var majorExam : MajorExam? = null

        @JsonProperty("subject")
        var subject : Subject? = null

        @JsonProperty("exam_subject")
        var exam_subject : Exam_subject? = null

        @JsonProperty("attendance")
        var attendance : String? = ""

        @JsonProperty("totalMark")
        var totalMark : String? = ""

        @JsonProperty("scoredPracticalMark")
        var scoredPracticalMark : String? = ""

        @JsonProperty("scoredMark")
        var scoredMark : String? = ""

        @JsonProperty("totalScore")
        var totalScore : String? = ""

        @JsonProperty("status")
        var status : String? = ""

        @JsonProperty("isDeleted")
        var isDeleted : Boolean? = false

        @JsonProperty("createdAt")
        var createdAt : String? = ""

        @JsonProperty("updatedAt")
        var updatedAt : String? = ""

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)

        class Student : Serializable{

            @JsonProperty("_id")
            var _id : String? = ""

            @JsonProperty("mobile")
            var mobile : String? = ""

            @JsonProperty("email")
            var email: String? = ""

            @JsonProperty("firstName")
            var firstName : String? = ""

            @JsonProperty("lastName")
            var lastName : String? = ""

            @JsonProperty("img_url")
            var img_url : String? = ""
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)

        class MajorExam : Serializable{
            @JsonProperty("_id")
            var _id : String? = ""

            @JsonProperty("examType")
            var examType : ExamType? = null

            @JsonProperty("startDate")
            var startDate : String? = ""

            @JsonProperty("endDate")
            var endDate : String? = ""
        }
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class ExamType : Serializable{
            @JsonProperty("_id")
            var _id : String? = ""

            @JsonProperty("name")
            var name : String? = ""
        }
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)

        class Subject : Serializable{
            @JsonProperty("_id")
            var _id : String? = ""

            @JsonProperty("name")
            var name : String? = ""

        }
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)

        class Exam_subject : Serializable{

            @JsonProperty("_id")
            var _id : String? = ""

            @JsonProperty("date")
            var date : String? = ""

            @JsonProperty("practicalMark")
            var practicalMark : String? = ""

        }
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class AnswerSheet : Serializable{
        @JsonProperty("url")
        var url : String? = ""

        @JsonProperty("date")
        var date : String? = ""
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Pagination : Serializable{
        @JsonProperty("currentPage")
        var currentPage : String? = ""

        @JsonProperty("pages")
        var pages : String? = ""

        @JsonProperty("total")
        var total : String? = ""
    }
}