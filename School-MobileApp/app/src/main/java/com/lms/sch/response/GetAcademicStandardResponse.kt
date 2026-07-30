package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class GetAcademicStandardResponse : BaseModel() {

    @JsonProperty("result")
    var result: ArrayList<Result>? = ArrayList()

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("name")
        var name: String? = ""

        @JsonProperty("description")
        var description: String? = ""

        @JsonProperty("board")
        var board: String? = ""

        @JsonProperty("img_url")
        var img_url: String? = ""

        @JsonProperty("status")
        var status: String? = ""

        @JsonProperty("createdAt")
        var createdAt: String? = ""

        @JsonProperty("standardId")
        var standardId: String? = ""

        @JsonProperty("sections")
        var sections: Int? = null

        @JsonProperty("sectionCount")
        var sectionCount: Int? = null

        @JsonProperty("studentCount")
        var studentCount: Int? = null

    }
}





