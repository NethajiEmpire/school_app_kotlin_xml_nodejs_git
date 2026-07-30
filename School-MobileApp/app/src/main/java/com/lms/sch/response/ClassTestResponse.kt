package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class ClassTestResponse : BaseModel() {

    @JsonProperty("result")
    var result: ArrayList<Result>? = ArrayList()

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {

        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("classTest")
        var classTest: ClassTestDetails? = null

        @JsonProperty("student")
        var student: StudentDetails? = null

        @JsonProperty("subject")
        var subject: SubjectDetails? = null

        @JsonProperty("attachment")
        var attachment: ArrayList<String>? = ArrayList()

        @JsonProperty("isAbsent")
        var isAbsent: Boolean? = false

        @JsonProperty("isDeleted")
        var isDeleted: Boolean? = false

        @JsonProperty("createdAt")
        var createdAt: String? = ""

        @JsonProperty("updatedAt")
        var updatedAt: String? = ""

        @JsonProperty("status")
        var status: String? = ""

        @JsonProperty("submittedOn")
        var submittedOn: String? = ""

        @JsonProperty("markStatus")
        var markStatus: String? = ""

        @JsonProperty("remarks")
        var remarks: String? = ""

        @JsonProperty("scored_marks")
        var scored_marks: String? = ""

        @JsonProperty("credits")
        var credits: String? = ""

        @JsonProperty("scheduledOn")
        var scheduledOn: String? = ""
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class ClassTestDetails : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("title")
        var title: String? = ""

        @JsonProperty("description")
        var description: String? = ""

        @JsonProperty("totalMarks")
        var totalMarks: Int? = null

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
    class StudentDetails : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("firstName")
        var firstName: String? = ""

        @JsonProperty("lastName")
        var lastName: String? = ""
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class SubjectDetails : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("name")
        var name: String? = ""
    }
}
