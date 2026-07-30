package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class GetAttendanceResponse : BaseModel() {

    @JsonProperty("result")
    var result: Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {

        @JsonProperty("rows")
        var rows: ArrayList<Row>? = ArrayList()

        @JsonProperty("pagination")
        var pagination: Pagination? = null

        @JsonProperty("stats")
        var stats :Stats?= null

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Row : Serializable {

            @JsonProperty("student")
            var student: Student? = null

            @JsonProperty("date")
            var date: String? = ""

            @JsonProperty("status")
            var status: String? = ""

            @JsonProperty("rollNo")
            var rollNo: String? = ""

            @JsonProperty("standard")
            var standard: String? = ""

            @JsonProperty("section")
            var section: String? = ""
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
        class Pagination : Serializable {
            @JsonProperty("currentPage")
            var currentPage: Int? = null

            @JsonProperty("pages")
            var pages: Int? = null

            @JsonProperty("total")
            var total: Int? = null
        }
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
     class Stats : Serializable{
         @JsonProperty("total")
         var total : String? = ""

        @JsonProperty("present")
        var present : String? = ""

        @JsonProperty("absent")
        var absent : String? = ""
     }
}
