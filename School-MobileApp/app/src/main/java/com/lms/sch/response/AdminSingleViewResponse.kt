package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class AdminSingleViewResponse : BaseModel(){
    @JsonProperty("result")
    var result: Result? = null
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

            @JsonProperty("feesType")
            var feesType: String? = ""

            @JsonProperty("admissionFee")
            var admissionFee: Int? = 0

            @JsonProperty("feesAmount")
            var feesAmount: Int? = 0

            @JsonProperty("terms")
            var terms: ArrayList<Terms>? = ArrayList()

            @JsonProperty("discount")
            var discount: List<Discount>? = null

            @JsonProperty("paymentMethod")
            var paymentMethod: List<String>? = null

            @JsonProperty("paymentOptions")
            var paymentOptions: List<String>? = null

            @JsonProperty("status")
            var status: String? = ""

            @JsonProperty("createdAt")
            var createdAt: String? = ""

            @JsonProperty("updatedAt")
            var updatedAt: String? = ""

            @JsonProperty("createdBy")
            var createdBy: CreatedBy? = null

            @JsonProperty("studentCount")
            var studentCount: Int? = null

            @JsonProperty("collectedAmount")
            var collectedAmount: Int? = null

            @JsonProperty("pendingAmount")
            var pendingAmount: Int? = null

            @JsonProperty("overdueAmount")
            var overdueAmount: Int? = null

            @JsonProperty("total")
            var total: Int? = null

            @JsonProperty("totalPer")
            var totalPer: Int? = null

            @JsonProperty("collectedPer")
            var collectedPer: Int? = null

            @JsonProperty("pendingPer")
            var pendingPer: Int? = null

            @JsonProperty("overduePer")
            var overduePer: Int? = null

            @JsonProperty("classTotals")
            var classTotals : ClassTotals? = null

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @JsonIgnoreProperties(ignoreUnknown = true)
            class ClassTotals : Serializable {

                @JsonProperty("total")
                var total : Int? = 0

                @JsonProperty("collected")
                var collected : Int? = 0

                @JsonProperty("pending")
                var pending : Int? = 0

                @JsonProperty("overdue")
                var overdue : Int? = 0

                @JsonProperty("totalPer")
                var totalPer : Int? = 0

                @JsonProperty("collectedPer")
                var collectedPer : Int? = 0

                @JsonProperty("pendingPer")
                var pendingPer : Int? = 0

                @JsonProperty("overduePer")
                var overduePer : Int? = 0
            }
            @JsonInclude(JsonInclude.Include.NON_NULL)
            @JsonIgnoreProperties(ignoreUnknown = true)
            class StudentClass : Serializable {
                @JsonProperty("_id")
                var _id: String? = ""

                @JsonProperty("name")
                var name: String? = null
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
            class Terms : Serializable {

                @JsonProperty("name")
                var name: String? = ""

                @JsonProperty("dueDate")
                var dueDate: String? = ""

                @JsonProperty("types")
                var types: ArrayList<Types>? = ArrayList()

                @JsonProperty("totalAmount")
                var totalAmount: Int? = 0

                @JsonProperty("_id")
                var _id: String? = ""

                @JsonInclude(JsonInclude.Include.NON_NULL)
                @JsonIgnoreProperties(ignoreUnknown = true)
                class Types : Serializable {
                    @JsonProperty("name")
                    var name: Name? = null

                    @JsonProperty("_id")
                    var _id: String? = ""

                    @JsonProperty("amount")
                    var amount: Int? = 0

                    @JsonInclude(JsonInclude.Include.NON_NULL)
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    class Name : Serializable {
                        @JsonProperty("name")
                        var name: String? = ""

                    }
                }
            }

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @JsonIgnoreProperties(ignoreUnknown = true)
            class Discount : Serializable {
                @JsonProperty("discountAmount")
                var discountAmount: Int? = 0

                @JsonProperty("discountType")
                var discountType: String? = ""

                @JsonProperty("type")
                var type: String? = ""

                @JsonProperty("_id")
                var _id: String? = ""
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
}