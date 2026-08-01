package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class GetTeacherStdClsTestResponse : BaseModel() {

    @JsonProperty("data")
    var data: ArrayList<Result>? = ArrayList()

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("program")
        var program: String? = ""

        @JsonProperty("classTest")
        var classTest: ClassTest? = null

        @JsonProperty("student")
        var student: Student? = null

        @JsonProperty("subject")
        var subject: Subject? = null

        @JsonProperty("scored_marks")
        var scored_marks: String? = ""

        @JsonProperty("attachment")
        var attachment: ArrayList<String>? = ArrayList()

        @JsonProperty("scheduledOn")
        var scheduledOn: String? = ""

        @JsonProperty("isAbsent")
        var isAbsent: Boolean? = false

        @JsonProperty("status")
        var status: String? = ""

        @JsonProperty("markStatus")
        var markStatus: String? = ""

        @JsonProperty("isDeleted")
        var isDeleted: Boolean? = false

        @JsonProperty("createdAt")
        var createdAt: String? = ""

        @JsonProperty("updatedAt")
        var updatedAt: String? = ""

        @JsonProperty("submittedOn")
        var submittedOn: String? = ""

        @JsonProperty("createdBy")
        var credits: Int? = null

        @JsonProperty("remarks")
        var remarks: String? = ""

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class ClassTest : Serializable {

            @JsonProperty("_id")
            var _id: String? = ""

            @JsonProperty("title")
            var title: String? = ""

            @JsonProperty("description")
            var description: String? = ""

            @JsonProperty("attachment")
            var attachment: String? = ""

            @JsonProperty("totalMarks")
            var totalMarks: Int? = null
        }

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
        class Subject : Serializable {

            @JsonProperty("_id")
            var _id: String? = ""

            @JsonProperty("Name")
            var Name: String? = ""
        }
    }
}
