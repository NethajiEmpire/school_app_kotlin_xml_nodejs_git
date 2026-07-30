package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class TeacherAssignment2Response : BaseModel() {

    @JsonProperty("result")
    var result: Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {

        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("title")
        var title: String? = ""

        @JsonProperty("description")
        var description: String? = ""

        @JsonProperty("program")
        var program: String? = ""

        @JsonProperty("board")
        var board: Board? = null

        @JsonProperty("studentClass")
        var studentClass: StudentClass? = null

        @JsonProperty("batch")
        var batch: Batch? = null

        @JsonProperty("section")
        var section: Section? = null

        @JsonProperty("subject")
        var subject: Subject? = null

        @JsonProperty("attachment")
        var attachment: String? = ""

        @JsonProperty("dueDate")
        var dueDate: String? = ""

        @JsonProperty("totalMarks")
        var totalMarks: Int? = null

        @JsonProperty("status")
        var status: String? = ""

        @JsonProperty("createdBy")
        var createdBy: CreatedBy? = null

        @JsonProperty("createdAt")
        var createdAt: String? = ""
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Board : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("name")
        var name: String? = ""
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
    class Batch : Serializable {
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
    class Subject : Serializable {
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
