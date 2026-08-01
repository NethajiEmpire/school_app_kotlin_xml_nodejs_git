package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
open class GuestFeesResponse : BaseModel() {
    @JsonProperty("data")
    var data: Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {

        @JsonProperty("_id")
        var id: String? = ""

        @JsonProperty("title")
        var title: String? = ""

        @JsonProperty("description")
        var description: String? = ""

        @JsonProperty("batch")
        var batch: String? = ""

        @JsonProperty("board")
        var board: String? = ""

        @JsonProperty("students")
        var students: ArrayList<String>? = ArrayList()

        @JsonProperty("annualPayment")
        var annualPayment: AnnualPayment? = null

        @JsonProperty("paymentMethod")
        var paymentMethod: ArrayList<String>? = ArrayList()

        @JsonProperty("terms")
        var terms: ArrayList<Terms>? = ArrayList()

        @JsonProperty("userDetails")
        var userDetails: User? = null

        @JsonProperty("feesType")
        var feesType: String? = ""

        @JsonProperty("admissionFee")
        var admissionFee: String? = ""

        @JsonProperty("feesAmount")
        var feesAmount: String? = ""

        @JsonProperty("paymentOptions")
        var paymentOptions: ArrayList<String>? = ArrayList()

        @JsonProperty("customMinAmount")
        var customMinAmount: Int? = 0

        @JsonProperty("status")
        var status: String? = ""

        @JsonProperty("discount")
        var discount: ArrayList<Discount>? = ArrayList()

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
        var payable_amount: String? = ""

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Discount : Serializable {
            @JsonProperty("discountAmount")
            var discountAmount: String? = ""

            @JsonProperty("type")
            var type: String? = ""

            @JsonProperty("_id")
            var _id: String? = ""

            @JsonProperty("discountType")
            var discountType: String? = ""
        }


        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Terms : Serializable {
            @JsonProperty("name")
            var name: String? = ""

            @JsonProperty("dueDate")
            var dueDate: String? = ""

            @JsonProperty("_id")
            var _id: String? = ""

            @JsonProperty("types")
            var types: ArrayList<Types>? = ArrayList()

            @JsonProperty("totalAmount")
            var totalAmount: String? = ""

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @JsonIgnoreProperties(ignoreUnknown = true)
            class Types : Serializable {

                @JsonProperty("_id")
                var _id: String? = ""

                @JsonProperty("name")
                var name: Name? = null

                @JsonProperty("amount")
                var amount: String? = ""

                @JsonInclude(JsonInclude.Include.NON_NULL)
                @JsonIgnoreProperties(ignoreUnknown = true)
                class Name : Serializable {
                    @JsonProperty("_id")
                    var _id: String? = ""

                    @JsonProperty("name")
                    var name: String? = ""

                }
            }
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class AnnualPayment : Serializable {

            @JsonProperty("feesName")
            var feesName: String? = ""

            @JsonProperty("totalAmount")
            var totalAmount: String? = ""

            @JsonProperty("discount")
            var discount: String? = ""

            @JsonProperty("discountType")
            var discountType: String? = ""

            @JsonProperty("payable_amount")
            var payable_amount: String? = ""
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
}