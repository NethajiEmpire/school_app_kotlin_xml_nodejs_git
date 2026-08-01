package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class GetStudentProjectResponse  : BaseModel() {

    @JsonProperty("data")
    var data: ArrayList<Result>? = ArrayList()

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {

        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("student")
        var student: Student? = null

        @JsonProperty("homework")
        var homework: Homework? = null

        @JsonProperty("createdBy")
        var createdBy: CreatedBy? = null

        @JsonProperty("subject")
        var subject: Subject? = null

        @JsonProperty("attachment")
        var attachment: ArrayList<String>? = ArrayList()

        @JsonProperty("img_url")
        var img_url: String? = ""

        @JsonProperty("isDeleted")
        var isDeleted: Boolean? = false

        @JsonProperty("dueDate")
        var dueDate: String? = ""

        @JsonProperty("status")
        var status: String? = ""

        @JsonProperty("submittedOn")
        var submittedOn: String? = ""

        @JsonProperty("credits")
        var credits: String? = ""

        @JsonProperty("remarks")
        var remarks: String? = ""

        @JsonProperty("createdAt")
        var createdAt: String? = ""

        @JsonProperty("markStatus")
        var markStatus: String? = ""

        @JsonProperty("submittedOnTime")
        var submittedOnTime: Boolean = false

        @JsonProperty("updatedAt")
        var updatedAt: String? = ""

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Student : Serializable {
            @JsonProperty("_id")
            var id: String? = ""

            @JsonProperty("firstName")
            var firstName: String? = ""

            @JsonProperty("lastName")
            var lastName: String? = ""
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Homework : Serializable {
            @JsonProperty("_id")
            var _id: String? = ""

            @JsonProperty("title")
            var title: String? = ""

            @JsonProperty("description")
            var description: String? = ""
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class CreatedBy : Serializable {
            @JsonProperty("_id")
            var id: String? = ""

            @JsonProperty("firstName")
            var firstName: String? = ""

            @JsonProperty("lastName")
            var lastName: String? = ""

            @JsonProperty("role")
            var role: Role? = null

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @JsonIgnoreProperties(ignoreUnknown = true)
            class Role : Serializable {
                @JsonProperty("_id")
                var id: String? = ""

                @JsonProperty("name")
                var name: String? = ""
            }
        }

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
        class StudentClass : Serializable {
            @JsonProperty("_id")
            var id: String? = ""

            @JsonProperty("name")
            var name: Any? = null
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
            var chapterNumber: Int? = 0
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Lessons : Serializable {
            @JsonProperty("_id")
            var id: String? = ""

            @JsonProperty("name")
            var name: String? = ""

            @JsonProperty("lessonNumber")
            var lessonNumber: Int? = 0
        }
    }
}
