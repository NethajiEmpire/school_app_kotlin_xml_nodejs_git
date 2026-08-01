package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class GetStudentResponse : BaseModel() {

    @JsonProperty("data")
    var data: Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {

        @JsonProperty("rows")
        var rows: ArrayList<Row>? = ArrayList()

        @JsonProperty("pagination")
        var pagination: Pagination? = null

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Row : Serializable {

            @JsonProperty("_id")
            var _id: String? = ""

            @JsonProperty("student")
            var student: Student? = null

            @JsonProperty("board")
            var board: Board? = null

            @JsonProperty("studentClass")
            var studentClass: StudentClass? = null

            @JsonProperty("section")
            var section: Section? = null

            @JsonProperty("batch")
            var batch: Batch? = null

            @JsonProperty("createdAt")
            var createdAt: String? = ""

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @JsonIgnoreProperties(ignoreUnknown = true)
            class Student : Serializable {
                @JsonProperty("_id")
                var _id: String? = ""

                @JsonProperty("mobile")
                var mobile: String? = ""

                @JsonProperty("email")
                var email: String? = ""

                @JsonProperty("firstName")
                var firstName: String? = ""

                @JsonProperty("lastName")
                var lastName: String? = ""

                @JsonProperty("lead_id")
                var lead_id: String? = ""

                @JsonProperty("dob")
                var dob : String? = ""

                @JsonProperty("gender")
                var gender : String? = ""

                @JsonProperty("activeStatus")
                var activeStatus : String? = ""

                @JsonProperty("img_url")
                var img_url : String? = ""

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
            class Section : Serializable {
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
}
