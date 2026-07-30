package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import com.lms.sch.response.GetAttendanceResponse.Result.Student
import com.lms.sch.response.StudentFeeResponse.Term
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class GetTransactionResponse : BaseModel() {

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

            @JsonProperty("feesMaster")
            var feesMaster: FeesMaster? = null

            @JsonProperty("student")
            var student: Student? = null

            @JsonProperty("transaction_id")
            var transactionId: String? = ""

            @JsonProperty("amount")
            var amount: Int? = null

            @JsonProperty("status")
            var status: String? = ""

            @JsonProperty("paymentOptions")
            var paymentOptions: String? = ""

            @JsonProperty("admissionFee")
            var admissionFee: Boolean? = false

            @JsonProperty("terms")
            var terms: ArrayList<Term>? = ArrayList()

            @JsonProperty("latePayment")
            var latePayment: Int? = null

            @JsonProperty("createdAt")
            var createdAt: String? = ""

            @JsonProperty("studentPreferenceDetails")
            var studentPreferenceDetails: StudentPreferenceDetails? = null

            @JsonProperty("bank_transaction_id")
            var bankTransactionId: String? = ""

            @JsonProperty("paidOn")
            var paidOn: String? = ""

            @JsonProperty("payment_info")
            var paymentInfo: String? = ""

            @JsonProperty("payment_type")
            var paymentType: String? = ""

            @JsonProperty("typeOfTransfer")
            var typeOfTransfer: String? = ""

            @JsonProperty("uniqueNumber")
            var uniqueNumber: String? = ""

            @JsonProperty("reciptUrl")
            var reciptUrl: String? = ""

            @JsonProperty("attachment")
            var attachment: String? = ""

            @JsonProperty("collectedBy")
            var collectedBy: String? = ""

            @JsonProperty("createdBy")
            var createdBy: String? = ""
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class FeesMaster : Serializable {
            @JsonProperty("_id")
            var _id: String? = ""

            @JsonProperty("title")
            var title: String? = ""

            @JsonProperty("description")
            var description: String? = ""
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
            var img_url: String? = ""
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class StudentPreferenceDetails : Serializable {

            @JsonProperty("studentClass")
            var studentClass: Int? = null

            @JsonProperty("section")
            var section: String? = ""

            @JsonProperty("board")
            var board: String? = ""
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Pagination : Serializable {
            @JsonProperty("currPage")
            var currPage: Int? = null

            @JsonProperty("pages")
            var pages: Int? = null

            @JsonProperty("total")
            var total: Int? = null
        }
    }
}
