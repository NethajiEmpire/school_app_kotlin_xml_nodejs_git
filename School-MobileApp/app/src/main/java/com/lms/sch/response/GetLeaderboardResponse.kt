package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class GetLeaderboardResponse : BaseModel() {
    @JsonProperty("data")
    var data: Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {

        @JsonProperty("rows")
        var rows: ArrayList<Rows>? = ArrayList()

        @JsonProperty("pagination")
        var pagination: Pagination? = null

        @JsonProperty("examDetails")
        var examDetails: ExamDetails? = null

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Rows : Serializable {

            @JsonProperty("studentId")
            var studentId: String? = ""

            @JsonProperty("name")
            var name: String? = ""

            @JsonProperty("scoredMark")
            var scoredMark: String? = ""

            @JsonProperty("totalMark")
            var totalMark: String? = ""

            @JsonProperty("status")
            var status: String? = ""

            @JsonProperty("grade")
            var grade: String? = ""

            @JsonProperty("rank")
            var rank: String? = ""

            @JsonProperty("img_url")
            var img_url: String? = ""

        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Pagination : Serializable {
            @JsonProperty("currentPage")
            var currentPage: Int? = 0

            @JsonProperty("pages")
            var pages: Int? = 0

            @JsonProperty("total")
            var total: Int? = 0
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class ExamDetails : Serializable {

            @JsonProperty("examId")
            var examId: String?= ""

            @JsonProperty("examName")
            var examName: String?= ""

            @JsonProperty("startDate")
            var startDate: String?= ""

            @JsonProperty("endDate")
            var endDate: String?= ""
        }
    }
}