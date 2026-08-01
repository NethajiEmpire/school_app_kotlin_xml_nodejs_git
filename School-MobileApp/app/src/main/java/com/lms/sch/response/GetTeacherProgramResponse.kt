package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class GetTeacherProgramResponse : BaseModel() {

    @JsonProperty("data")
    var data: ArrayList<Result>? = ArrayList()

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {

        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("board")
        var board: String? = ""

        @JsonProperty("standard")
        var standard: Int? = null

        @JsonProperty("section")
        var section: String? = ""

        @JsonProperty("myClass")
        var myClass: Boolean? = false

        @JsonProperty("subject")
        var subject: Subject? = null
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Subject : Serializable {

        @JsonProperty("subject_id")
        var subjectId: SubjectId? = null

        @JsonProperty("teacher")
        var teacher: String? = ""

        @JsonProperty("_id")
        var _id: String? = ""
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class SubjectId : Serializable {

        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("name")
        var name: String? = ""

        @JsonProperty("img_url")
        var imgUrl: String? = ""
    }
}
