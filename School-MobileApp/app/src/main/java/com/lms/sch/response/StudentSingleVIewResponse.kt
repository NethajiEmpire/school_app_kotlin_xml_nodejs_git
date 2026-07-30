package com.lms.sch.response

    import com.fasterxml.jackson.annotation.JsonIgnoreProperties
    import com.fasterxml.jackson.annotation.JsonInclude
    import com.fasterxml.jackson.annotation.JsonProperty
    import com.lms.sch.models.BaseModel
    import java.io.Serializable

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class StudentSingleVIewResponse : BaseModel() {

        @JsonProperty("result")
        var result: Result? = null

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Result : Serializable {

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

            @JsonProperty("program")
            var program: String? = ""

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
                var leadId: String? = ""
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
                var name: Int? = 0
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
    }

