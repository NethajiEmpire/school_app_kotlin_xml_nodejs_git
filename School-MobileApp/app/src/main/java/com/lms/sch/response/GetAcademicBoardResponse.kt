package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class GetAcademicBoardResponse : BaseModel() {

    @JsonProperty("data")
    var data: ArrayList<Result>? = ArrayList()

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("name")
        var name: String? = ""

        @JsonProperty("description")
        var description: String? = ""

        @JsonProperty("img_url")
        var img_url: String? = ""

        @JsonProperty("status")
        var status: String? = ""

        @JsonProperty("isDeleted")
        var isDeleted: Boolean? = false

        @JsonProperty("createdAt")
        var createdAt: String? = ""

        @JsonProperty("updatedAt")
        var updatedAt: String? = ""

        @JsonProperty("boardId")
        var boardId: String? = ""

        @JsonProperty("classCount")
        var classCount: Int? = null

        @JsonProperty("studentCount")
        var studentCount: Int? = null

        @JsonProperty("boardNo")
        var boardNo: Int? = null

    }
}





