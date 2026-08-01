package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import com.lms.sch.response.GetStudentAssignmentResponse.Result
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

open class StudentProjectResponse : BaseModel(){
    @JsonProperty("data")
    @JsonIgnore
    var data: ArrayList<Result> = ArrayList()
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable{
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("project")
        var project : Project? = null

        @JsonProperty("student_id")
        var student_id: StudentId? = null

        @JsonProperty("attachment")
        var attachment: ArrayList<String>? = null

        @JsonProperty("subject")
        var subject: Subject? = null

        @JsonProperty("credits")
        var credits : String? = ""

        @JsonProperty("dueDate")
        var dueDate : String? = ""

        @JsonProperty("submittedOn")
        var submittedOn: String? = ""

        @JsonProperty("submittedOnTime")
        var submittedOnTime: Boolean = false

        @JsonProperty("remarks")
        var remarks : String? = ""

        @JsonProperty("markStatus")
        var markStatus: String? = ""

        @JsonProperty("scored_marks")
        var scored_marks: String? = null

        @JsonProperty("status")
        var status: String? = ""

        @JsonProperty("isDeleted")
        var isDeleted: Boolean? = false

        @JsonProperty("createdAt")
        var createdAt: String? = ""

        @JsonProperty("updatedAt")
        var updatedAt: String? = ""

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Board : Serializable {
            @JsonProperty("_id")
            var id: String? = ""

            @JsonProperty("name")
            var name: String? = ""
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Subject : Serializable {
            @JsonProperty("_id")
            var id: String? = ""

            @JsonProperty("name")
            var name: String? = ""
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Chapter : Serializable {
            @JsonProperty("_id")
            var id: String? = ""

            @JsonProperty("name")
            var name: String? = ""

            @JsonProperty("chapterNumber")
            var chapterNumber: Int? = null
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class CreatedBy : Serializable {
            @JsonProperty("_id")
            var id: String? = ""

            @JsonProperty("firstName")
            var firstName : String? = ""

            @JsonProperty("lastName")
            var lastName : String? = ""
        }
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class StudentId : Serializable{
            @JsonProperty("_id")
            var _id : String? = ""

            @JsonProperty("firstName")
            var firstName : String? = ""

            @JsonProperty("lastName")
            var  lastName :String? = ""

            @JsonProperty("img_url")
            var  img_url :String? = ""
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Project : Serializable{
            @JsonProperty("_id")
            var _id : String? = ""

            @JsonProperty("title")
            var title : String? = ""

            @JsonProperty("description")
            var description : String? = ""

            @JsonProperty("dueDate")
            var dueDate : String? = ""

            @JsonProperty("createdBy")
            var createdBy: CreatedBy? = null

            @JsonProperty("totalMarks")
            var totalMarks: String? = ""

            @JsonProperty("attachment")
            var attachment: String? = ""
        }
    }
}
