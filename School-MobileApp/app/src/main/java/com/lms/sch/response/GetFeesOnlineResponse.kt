package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
open class GetFeesOnlineResponse : BaseModel() {
    @JsonProperty("data")
    var data: Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {

        @JsonProperty("userDetails")
        var userDetails: User? = null

        @JsonProperty("feesType")
        var feesType: String? = ""

        @JsonProperty("_id")
        var id: String? = ""

        @JsonProperty("title")
        var title: String? = ""

        @JsonProperty("description")
        var description: String? = ""

        @JsonProperty("feesAmount")
        var feesAmount: Int? = 0

        @JsonProperty("dueDate")
        var dueDate: String? = ""

        @JsonProperty("board")
        var board: String? = ""

        @JsonProperty("students")
        var students: ArrayList<Any>? = ArrayList()

        @JsonProperty("paymentMethod")
        var paymentMethod: String? = ""

        @JsonProperty("paymentOptions")
        var paymentOptions: ArrayList<String>? = ArrayList()

        @JsonProperty("customMinAmount")
        var customMinAmount: Int? = 0

        @JsonProperty("status")
        var status: String? = ""

        @JsonProperty("splitInstallment")
        var splitInstallment: ArrayList<SplitInstallment>? = ArrayList()

        @JsonProperty("isDeleted")
        var isDeleted: Boolean? = false

        @JsonProperty("createdAt")
        var createdAt: String? = ""

        @JsonProperty("updatedAt")
        var updatedAt: String? = ""

        @JsonProperty("studentClass")
        var studentClass: String? = ""

        @JsonProperty("payment_status")
        var payment_status: String? = ""

        @JsonProperty("payable_amount")
        var payable_amount: Int? = 0

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class SplitInstallment : Serializable {
            @JsonProperty("splitDate")
            var splitDate: String? = ""

            @JsonProperty("_id")
            var id: String? = ""
        }
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class User : Serializable {
            
            @JsonProperty("firstName")
            var firstName: String? = ""

            @JsonProperty("lastName")
            var lastName: String? = ""

            @JsonProperty("email")
            var email: String? = ""

            @JsonProperty("mobile")
            var mobile: String? = ""

            @JsonProperty("classApplying")
            var classApplying: String? = ""
        }
    }

    /*@JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {
        @JsonProperty("user")
        var user: User? = null

        @JsonProperty("feesDetails")
        var feesDetails: FeesDetails? = null

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class User : Serializable {
            @JsonProperty("name")
            var name: String? = ""

            @JsonProperty("email")
            var email: String? = ""

            @JsonProperty("mobile")
            var mobile: String? = ""

            @JsonProperty("aadharNo")
            var aadharNo: String? = ""

            @JsonProperty("studentType")
            var studentType: String? = ""
        }

    }*/
}