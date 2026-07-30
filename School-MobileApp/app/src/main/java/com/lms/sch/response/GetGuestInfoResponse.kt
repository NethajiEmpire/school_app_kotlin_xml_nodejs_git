package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class GetGuestInfoResponse : BaseModel() {

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

            @JsonProperty("img_url")
            var img_url : String? = ""

            @JsonProperty("_id")
            var _id: String? = ""

            @JsonProperty("mobile")
            var mobile: String? = ""

            @JsonProperty("currentStep")
            var currentStep: String? = ""

            @JsonProperty("totalStep")
            var totalStep: String? = ""

            @JsonProperty("email")
            var email: String? = ""

            @JsonProperty("firstName")
            var firstName: String? = ""

            @JsonProperty("lastName")
            var lastName: String? = ""

            @JsonProperty("pre_school")
            var pre_school: String? = ""

            @JsonProperty("activeStatus")
            var activeStatus: String? = ""

            @JsonProperty("status")
            var status: String? = ""

            @JsonProperty("lead_id")
            var lead_id: String? = ""

            @JsonProperty("lastLogin")
            var lastLogin: String? = ""

            @JsonProperty("city")
            var city: String? = ""

            @JsonProperty("student_enrollment")
            var student_enrollment: Boolean? = false

            @JsonProperty("registrationFee")
            var registrationFee: Boolean? = false

            @JsonProperty("grade_level")
            var grade_level: GradeLevel? = null

            @JsonProperty("createdAt")
            var createdAt: String? = ""

            @JsonProperty("updatedAt")
            var updatedAt: String? = ""

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
            class GradeLevel : Serializable {
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
