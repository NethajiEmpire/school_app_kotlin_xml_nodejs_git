package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import com.lms.sch.response.GetStudentClassTestProgress.Result
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class TeacherSideStudentAssignmentResponse : BaseModel() {
    @JsonProperty("result")
    val result : ArrayList<Result>? = ArrayList()

    class Result : Serializable{

        @JsonProperty("_id")
        var _id : String = ""

        @JsonProperty("program")
        var program : String? = ""

        @JsonProperty("student")
        var  student : Student? = null

        @JsonProperty("scored_marks")
        var scoredMarks: String? = ""

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)

        class Student : Serializable{
            @JsonProperty("_id")
            var _id : String? = ""

            @JsonProperty("firstName")
            var firstName : String? = ""

            @JsonProperty("lastName")
            var lastName : String? = ""

        }
        @JsonProperty("assignment")
        var assignment : Assignment? = null

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)

        class Assignment : Serializable{
            @JsonProperty("_id")
            var _id : String? = ""

            @JsonProperty("title")
            var title : String? = ""

            @JsonProperty("description")
            var description : String? = ""

            @JsonProperty("attachment")
            var attachment : String? = ""
        }
        @JsonProperty("teacher")
        var teacher : Any? = null

        @JsonProperty("board")
        var board : Board? = null

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)

        class Board : Serializable{
            @JsonProperty("_id")
            var _id : String? = ""

            @JsonProperty("name")
            var name : String? = ""
        }
        @JsonProperty("studentClass")
        var studentClass : StudentClass? = null

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)

        class StudentClass :  Serializable{

            @JsonProperty("_id")
            var _id : String? = ""

            @JsonProperty("name")
            var name : String? = ""

        }
        @JsonProperty("section")
        var section : Section? = null

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)

        class Section :  Serializable{

            @JsonProperty("_id")
            var _id : String? = ""

            @JsonProperty("name")
            var name : String? = ""

        }
        @JsonProperty("batch")
        var batch : Any? = null

        @JsonProperty("subject")
        var subject : Subject? = null

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)

        class Subject :  Serializable{

            @JsonProperty("_id")
            var _id : String? = ""

            @JsonProperty("name")
            var name : String? = ""

        }
        @JsonProperty("chapter")
        var  chapter : Chapter? = null

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)

        class Chapter :  Serializable{

            @JsonProperty("_id")
            var _id : String? = ""

            @JsonProperty("name")
            var name : String? = ""

            @JsonProperty("chapterNumber")
            var chapterNumber : String? = ""

        }
        @JsonProperty("lessons")
        var lessons : Lessons? = null

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)

        class Lessons :  Serializable{

            @JsonProperty("_id")
            var _id : String? = ""

            @JsonProperty("name")
            var name : String? = ""

            @JsonProperty("chapterNumber")
            var lessonNumber : String? = ""
        }

        @JsonProperty("dueDate")
        var dueDate : String? = ""

        @JsonProperty("attachment")
        var attachment : ArrayList<String>? = ArrayList()

        @JsonProperty("img_url")
        var img_url : String? = ""

        @JsonProperty("isDeleted")
        var isDeleted : Boolean? = false

        @JsonProperty("createdAt")
        var createdAt : String? = ""

        @JsonProperty("updatedAt")
        var updatedAt : String? = ""

        @JsonProperty("status")
        var status : String? = ""

        @JsonProperty("markStatus")
        var markStatus : String? = ""

        @JsonProperty("submittedOnTime")
        var submittedOnTime : String? = ""

        @JsonProperty("credits")
        var credits : String? = ""

        @JsonProperty("submittedOn")
        var submittedOn : String? = ""

        @JsonProperty("remarks")
        var remarks : String? = ""

        @JsonProperty("verifiedDate")
        var verifiedDate : String? = ""
    }
}