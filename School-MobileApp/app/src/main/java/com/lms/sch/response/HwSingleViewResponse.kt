    package com.lms.sch.response
    import com.fasterxml.jackson.annotation.JsonIgnoreProperties
    import com.fasterxml.jackson.annotation.JsonInclude
    import com.fasterxml.jackson.annotation.JsonProperty
    import com.lms.sch.models.BaseModel
    import java.io.Serializable

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class HwSingleViewResponse : BaseModel() {

        @JsonProperty("result")
        var result: ArrayList<Result>? = ArrayList()

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Result : Serializable {

            @JsonProperty("_id")
            var _id: String? = ""

            @JsonProperty("program")
            var program: String? = ""

            @JsonProperty("student")
            var student: Student? = null

            @JsonProperty("homework")
            var homework: Homework? = null

            @JsonProperty("subject")
            var subject: Subject? = null

            @JsonProperty("dueDate")
            var dueDate: String? = ""

            @JsonProperty("attachment")
            var attachment: ArrayList<String>? = ArrayList()

            @JsonProperty("status")
            var status: String? = ""

            @JsonProperty("markStatus")
            var markStatus: String? = ""

            @JsonProperty("isDeleted")
            var isDeleted: Boolean = false

            @JsonProperty("createdAt")
            var createdAt: String? = ""

            @JsonProperty("updatedAt")
            var updatedAt: String? = ""

            @JsonProperty("submittedOn")
            var submittedOn: String? = ""

            @JsonProperty("credits")
            var credits : String? =  ""

            @JsonProperty("remarks")
            var remarks : String? = ""

            @JsonProperty("verifiedDate")
            var verifiedDate : String? = ""

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @JsonIgnoreProperties(ignoreUnknown = true)
            class Student : Serializable {
                @JsonProperty("_id")
                var _id: String? = ""

                @JsonProperty("firstName")
                var firstName: String? = ""

                @JsonProperty("lastName")
                var lastName: String? = ""
            }

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @JsonIgnoreProperties(ignoreUnknown = true)
            class Homework : Serializable {
                @JsonProperty("_id")
                var _id: String? = ""

                @JsonProperty("title")
                var title: String? = ""

                @JsonProperty("description")
                var description: String? = ""

                @JsonProperty("studentClass")
                var studentClass: StudentClass? = null

                @JsonProperty("attachment")
                var attachment: String? = ""

                @JsonProperty("createdBy")
                var createdBy: CreatedBy? = null

                @JsonInclude(JsonInclude.Include.NON_NULL)
                @JsonIgnoreProperties(ignoreUnknown = true)
                class StudentClass : Serializable {
                    @JsonProperty("_id")
                    var _id: String? = ""

                    @JsonProperty("name")
                    var name: Int? = null // It's numeric in the sample JSON
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
            }

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @JsonIgnoreProperties(ignoreUnknown = true)
            class Subject : Serializable {
                @JsonProperty("_id")
                var _id: String? = ""

                @JsonProperty("name")
                var name: String? = ""
            }
        }
    }
