    package com.lms.sch.response
    import com.fasterxml.jackson.annotation.JsonIgnoreProperties
    import com.fasterxml.jackson.annotation.JsonInclude
    import com.fasterxml.jackson.annotation.JsonProperty
    import com.lms.sch.models.BaseModel
    import java.io.Serializable

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
     class AssSglViewResponse : BaseModel() {

        @JsonProperty("data")
        var data: ArrayList<Result>? = ArrayList()

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Result : Serializable {

            @JsonProperty("_id")
            var _id: String? = ""

            @JsonProperty("program")
            var program: String? = ""

            @JsonProperty("student")
            var student: Student? = null

            @JsonProperty("assignment")
            var assignment: Assignment? = null

            @JsonProperty("subject")
            var subject: Subject? = null

            @JsonProperty("dueDate")
            var dueDate: String? = ""

            @JsonProperty("attachment")
            var attachment: ArrayList<String>? = ArrayList()

            @JsonProperty("img_url")
            var imgUrl: String? = ""

            @JsonProperty("status")
            var status: String? = ""

            @JsonProperty("markStatus")
            var markStatus: String? = ""

            @JsonProperty("isDeleted")
            var isDeleted: Boolean? = false

            @JsonProperty("createdAt")
            var createdAt: String? = ""

            @JsonProperty("updatedAt")
            var updatedAt: String? = ""

            @JsonProperty("credits")
            var credits: String? = ""

            @JsonProperty("remarks")
            var remarks : String? = ""

            @JsonProperty("submittedOn")
            var submittedOn: String? = ""

            @JsonProperty("submittedOnTime")
            var submittedOnTime: Boolean = false

            @JsonProperty("scored_marks")
            var scored_marks : String? = ""

            @JsonProperty("verifiedDate")
            var verifiedDate : String? = ""

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
                var img_url: String? = ""
            }

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @JsonIgnoreProperties(ignoreUnknown = true)
            class Assignment : Serializable {
                @JsonProperty("_id")
                var _id: String? = ""

                @JsonProperty("title")
                var title: String? = ""

                @JsonProperty("description")
                var description: String? = ""

                @JsonProperty("attachment")
                var attachment: String? = ""

                @JsonProperty("totalMarks")
                var totalMarks: String? = ""

                @JsonProperty("createdBy")
                var createdBy: CreatedBy? = null

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