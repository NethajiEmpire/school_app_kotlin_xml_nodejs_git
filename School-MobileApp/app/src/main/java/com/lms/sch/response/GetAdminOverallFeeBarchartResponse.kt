package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class GetAdminOverallFeeBarchartResponse : BaseModel() {

    @JsonProperty("result")
    val result: Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable{

        @JsonProperty("result")
        val result: ArrayList<Result> = ArrayList()

        @JsonProperty("summary")
        val summary: Summary? = null

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Result : Serializable {

            @JsonProperty("classId")
            var classId : String? = ""

            @JsonProperty("className")
            var className : String? = ""

            @JsonProperty("studentCount")
            var studentCount : String? = ""

            @JsonProperty("admissionFee")
            var admissionFee : String? = ""

            @JsonProperty("totalFees")
            var totalFees : String? = ""

            @JsonProperty("collected")
            var collected : String? = ""

            @JsonProperty("pending")
            var pending : String? = ""

            @JsonProperty("overdue")
            var overdue : String? = ""

            @JsonProperty("collectedPer")
            var collectedPer : String? = ""

            @JsonProperty("overduePer")
            var overduePer : String? = ""

            @JsonProperty("pendingPer")
            var pendingPer : String? = ""
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Summary : Serializable{

            @JsonProperty("totalFees")
            val totalFees: String = ""

            @JsonProperty("collected")
            val collected: String = ""

            @JsonProperty("pending")
            val pending: String = ""

            @JsonProperty("overdue")
            val overdue: String = ""
        }
    }
}