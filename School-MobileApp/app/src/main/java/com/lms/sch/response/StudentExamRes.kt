    package com.lms.sch.response
    import com.fasterxml.jackson.annotation.JsonIgnoreProperties
    import com.fasterxml.jackson.annotation.JsonInclude
    import com.fasterxml.jackson.annotation.JsonProperty
    import com.lms.sch.models.BaseModel
    import java.io.Serializable

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class StudentExamRes : BaseModel() {

        @JsonProperty("result")
        var result: Result? = null

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

                @JsonProperty("majorExam")
                var majorExam: MajorExam? = null

                @JsonProperty("board")
                var board: String? = ""

                @JsonProperty("batch")
                var batch: Batch? = null

                @JsonProperty("hallTicketStatus")
                var hallTicketStatus: String? = ""

                @JsonProperty("markSheetStatus")
                var markSheetStatus: String? = ""

                @JsonProperty("noDueStatus")
                var noDueStatus: String? = ""

                @JsonProperty("createdBy")
                var createdBy: CreatedBy? = null

                @JsonProperty("createdAt")
                var createdAt: String? = ""

                @JsonProperty("updatedAt")
                var updatedAt: String? = ""
            }

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

                @JsonProperty("img_url")
                var imgUrl: String? = ""
            }

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @JsonIgnoreProperties(ignoreUnknown = true)
            class MajorExam : Serializable {

                @JsonProperty("_id")
                var _id: String? = ""

                @JsonProperty("examType")
                var examType: ExamType? = null

                @JsonProperty("startDate")
                var startDate: String? = ""

                @JsonProperty("endDate")
                var endDate: String? = ""
            }

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @JsonIgnoreProperties(ignoreUnknown = true)
            class ExamType : Serializable {

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
            class CreatedBy : Serializable {

                @JsonProperty("_id")
                var _id: String? = ""

                @JsonProperty("firstName")
                var firstName: String? = ""

                @JsonProperty("lastName")
                var lastName: String? = ""
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
