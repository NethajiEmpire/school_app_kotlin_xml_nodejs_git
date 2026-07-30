package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class GetAcademicSubjectResponse: BaseModel() {

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

        @JsonProperty("img_url")
        var img_url: String? = ""

        @JsonProperty("board")
        var board: String? = ""

        @JsonProperty("studentClass")
        var studentClass: String? = ""

        @JsonProperty("createdBy")
        var createdBy: CreatedBy? = null

        @JsonProperty("isDeleted")
        var isDeleted: String? = ""

        @JsonProperty("status")
        var status: String? = ""

        @JsonProperty("createdAt")
        var createdAt: String? = ""

        @JsonProperty("updatedAt")
        var updatedAt: String? = ""

        @JsonProperty("subjectId")
        var subjectId: String? = ""

        @JsonProperty("subjectNo")
        var subjectNo: Int? = null

        @JsonProperty("teachers")
        var teachers: Int? = null


        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class CreatedBy : Serializable {

            @JsonProperty("_id")
            var _id: String? = ""

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
                var _id: String? = ""

                @JsonProperty("name")
                var name: String? = ""
            }
        }
    }
}





