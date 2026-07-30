package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
open class GetStudentAssignmentResponse : BaseModel() {

    @JsonProperty("result")
    var result: ArrayList<Result>? = ArrayList()

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {

        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("student")
        var student: Student? = null

        @JsonProperty("assignment")
        var assignment: Assignment? = null

        @JsonProperty("board")
        var board: String? = ""

        @JsonProperty("teacher")
        var teacher: String? = ""

        @JsonProperty("studentClass")
        var studentClass: String? = ""

        @JsonProperty("section")
        var section: String? = ""

        @JsonProperty("batch")
        var batch: Batch? = null

        @JsonProperty("subject")
        var subject: Subject? = null

        @JsonProperty("chapter")
        var chapter: Chapter? = null

        @JsonProperty("lessons")
        var lessons: Lessons? = null

        @JsonProperty("dueDate")
        var dueDate: String? = ""

        @JsonProperty("attachment")
        var attachment: ArrayList<String>? = null

        @JsonProperty("img_url")
        var imgUrl: String? = ""

        @JsonProperty("isDeleted")
        var isDeleted: Boolean? = false

        @JsonProperty("createdAt")
        var createdAt: String? = ""

        @JsonProperty("updatedAt")
        var updatedAt: String? = ""

        @JsonProperty("status")
        var status: String? = ""

        @JsonProperty("credits")
        var credits: String? = ""

        @JsonProperty("remarks")
        var remarks: String? = ""

        @JsonProperty("scored_marks")
        var scored_marks: String? = ""

        @JsonProperty("verifiedDate")
        var verifiedDate: String? = ""

        @JsonProperty("markStatus")
        var markStatus: String? = ""

        @JsonProperty("submittedOnTime")
        var submittedOnTime: Boolean = false

        @JsonProperty("submittedOn")
        var submittedOn: String? = ""

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Student : Serializable {
            @JsonProperty("_id")
            var _id: String? = ""

            @JsonProperty("firstName")
            var firstName: String? = ""

            @JsonProperty("lastName")
            var lastName: String? = ""

            @JsonProperty("img_url")
            var img_url: String? = ""
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Assignment : Serializable {
            @JsonProperty("_id")
            var _id: String? = ""

            @JsonProperty("title")
            var title: String? = ""

            @JsonProperty("description")
            var description: String? = ""

            @JsonProperty("totalMarks")
            var totalMarks: String? = ""

            @JsonProperty("attachment")
            var attachment: String? = ""

            @JsonProperty("createdBy")
            var createdBy: CreatedBy? = null

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

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class StudentClass : Serializable {
            @JsonProperty("_id")
            var _id: String? = ""

            @JsonProperty("name")
            var name: String? = ""
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Section : Serializable {
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
        class Chapter : Serializable {
            @JsonProperty("_id")
            var _id: String? = ""

            @JsonProperty("name")
            var name: String? = ""

            @JsonProperty("chapterNumber")
            var chapterNumber: Int? = null
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Lessons : Serializable {
            @JsonProperty("_id")
            var _id: String? = ""

            @JsonProperty("name")
            var name: String? = ""

            @JsonProperty("lessonNumber")
            var lessonNumber: Int? = null
        }
    }
}
