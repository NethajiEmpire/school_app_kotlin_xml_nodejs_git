package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import com.lms.sch.response.GetGuestInfoResponse.Result.Row.Batch
import com.lms.sch.response.GetGuestInfoResponse.Result.Row.Section
import com.lms.sch.response.GetHomeworkResponse.Result.StudentClass
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class GetTimeTableResponse : BaseModel() {

    @JsonProperty("result")
    var result: ArrayList<Result>? = ArrayList()

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {

        @JsonProperty("_id")
        var id: String? = ""

        @JsonProperty("studentClass")
        var studentClass: StudentClass? = null

        @JsonProperty("section")
        var section: Section? = null

        @JsonProperty("batchId")
        var batchId: Batch? = null

        @JsonProperty("studentBoard")
        var studentBoard: StudentBoardResponse.Result? = null

        @JsonProperty("day")
        var day: String? = ""

        @JsonProperty("periods")
        var periods: ArrayList<Period>? = ArrayList()

        @JsonProperty("isDeleted")
        var isDeleted: Boolean? = false

        @JsonProperty("createdBy")
        var createdBy: CreatedBy? = null

        @JsonProperty("createdAt")
        var createdAt: String? = ""

        @JsonProperty("updatedAt")
        var updatedAt: String? = ""

        @JsonProperty("program")
        var program: String? = ""

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Period : Serializable {

            @JsonProperty("Session")
            var Session: String? = ""

            @JsonProperty("startTime")
            var startTime: String? = ""

            @JsonProperty("endTime")
            var endTime: String? = ""

            @JsonProperty("type")
            var type: String? = ""

            @JsonProperty("_id")
            var id: String? = ""

            @JsonProperty("subject")
            var subject: Subject? = null

            @JsonProperty("teacher")
            var teacher: Teacher? = null

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
            class Teacher : Serializable {
                @JsonProperty("_id")
                var id: String? = ""

                @JsonProperty("firstName")
                var firstName: String? = ""

                @JsonProperty("lastName")
                var lastName: String? = ""
            }
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
        }
    }
}
