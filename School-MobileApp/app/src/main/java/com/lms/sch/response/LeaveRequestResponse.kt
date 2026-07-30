package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import com.lms.sch.response.AdminFeesResponse.Result.Rows.Board
import com.lms.sch.response.GetComplaintResponse.Result.Rows.StudentDetails.Standard
import com.lms.sch.response.GetGuestInfoResponse.Result.Row.Section
import com.lms.sch.response.ParentProfileResponse.Result.UserProfile.Role
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class LeaveRequestResponse : BaseModel() {

    @JsonProperty("result")
    var result: Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {

        @JsonProperty("rows")
        var rows: ArrayList<Rows>? = ArrayList()

        @JsonProperty("pagination")
        var pagination: Pagination? = null
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Rows : Serializable {

        @JsonProperty("attachment")
        var attachment: ArrayList<String>? = ArrayList()

        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("title")
        var title: String? = ""

        @JsonProperty("description")
        var description: String? = ""

        @JsonProperty("startDate")
        var startDate: String? = ""

        @JsonProperty("endDate")
        var endDate: String? = ""

        @JsonProperty("numberOfDays")
        var numberOfDays: String? = ""

        @JsonProperty("requestId")
        var requestId: String? = ""

        @JsonProperty("rejectReason")
        var rejectReason: String? = ""

        @JsonProperty("type")
        var type: LeaveType? = null

        @JsonProperty("status")
        var status: String? = ""

        @JsonProperty("createdBy")
        var createdBy: CreatedByDetails? = null

        @JsonProperty("studentDetails")
        var studentDetails: StudentDetails? = null

        @JsonProperty("createdAt")
        var createdAt: String? = ""

        @JsonProperty("updatedAt")
        var updatedAt: String? = ""

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class LeaveType : Serializable {
            @JsonProperty("_id")
            var _id: String? = ""

            @JsonProperty("name")
            var name: String? = ""
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class CreatedByDetails : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("firstName")
        var firstName: String? = ""

        @JsonProperty("lastName")
        var lastName: String? = ""

        @JsonProperty("email")
        var email: String? = ""

        @JsonProperty("mobile")
        var mobile: String? = ""

        @JsonProperty("img_url")
        var img_url: String? = ""

        @JsonProperty("role")
        var role: Role? = null
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class StudentDetails : Serializable {

        @JsonProperty("board")
        var board: Board? = null

        @JsonProperty("standard")
        var standard: Standard? = null

        @JsonProperty("section")
        var section: Section? = null

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Pagination : Serializable {
        @JsonProperty("currentPage")
        var currentPage: String? = ""

        @JsonProperty("pages")
        var pages: String? = ""

        @JsonProperty("total")
        var total: String? = ""
    }
}
