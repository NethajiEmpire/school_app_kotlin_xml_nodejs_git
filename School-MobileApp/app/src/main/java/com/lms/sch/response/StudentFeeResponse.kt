package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class StudentFeeResponse : BaseModel() {

    @JsonProperty("data")
    var data: Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {

        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("title")
        var title: String? = ""

        @JsonProperty("description")
        var description: String? = ""

        @JsonProperty("studentClass")
        var studentClass: StudentClass? = null

        @JsonProperty("batch")
        var batch: Batch? = null

        @JsonProperty("board")
        var board: Board? = null

        @JsonProperty("students")
        var students: ArrayList<String>? = ArrayList()

        @JsonProperty("feesType")
        var feesType: String? = ""

        @JsonProperty("admissionFee")
        var admissionFee: Int? = null

        @JsonProperty("feesAmount")
        var feesAmount: Int? = null

        @JsonProperty("terms")
        var terms: ArrayList<Term>? = ArrayList()

        @JsonProperty("discount")
        var discount: ArrayList<Discount>? = ArrayList()

        @JsonProperty("paymentMethod")
        var paymentMethod: ArrayList<String>? = ArrayList()

        @JsonProperty("paymentOptions")
        var paymentOptions: ArrayList<String>? = ArrayList()

        @JsonProperty("status")
        var status: String? = ""

        @JsonProperty("createdAt")
        var createdAt: String? = ""

        @JsonProperty("updatedAt")
        var updatedAt: String? = ""

        @JsonProperty("createdBy")
        var createdBy: CreatedBy? = null

        @JsonProperty("admissionFeesStatus")
        var admissionFeesStatus: String? = ""

        @JsonProperty("paidAmount")
        var paidAmount: Int? = null

        @JsonProperty("pendingAmount")
        var pendingAmount: Int? = null

        @JsonProperty("overdueAmount")
        var overdueAmount: Int? = null

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class StudentClass : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("name")
        var name: Int? = null
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
    class Board : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("name")
        var name: String? = ""
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Term : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("name")
        var name: String? = ""

        @JsonProperty("dueDate")
        var dueDate: String? = ""

        @JsonProperty("paidOn")
        var paidOn: String? = ""

        @JsonProperty("status")
        var status: String? = ""

        @JsonProperty("types")
        var types: ArrayList<TermType>? = ArrayList()

        @JsonProperty("totalAmount")
        var totalAmount: String? = ""
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class TermType : Serializable {
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Discount : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("discountAmount")
        var discountAmount: Int? = null

        @JsonProperty("discountType")
        var discountType: String? = ""

        @JsonProperty("type")
        var type: String? = ""
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
