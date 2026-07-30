package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import com.lms.sch.response.GetExamResponse.Pagination
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class StudentListAnalyticsResponse : BaseModel() {

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
        }
    }
}
