package com.lms.sch.response

import com.lms.sch.models.BaseModel
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
open class GetDocumentsUploadResponse : BaseModel() {
    @JsonProperty("result")
    @JsonIgnore
    var result: Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {
        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("user_id")
        var user_id: String? = ""

        @JsonProperty("docDetails")
        var docDetails: ArrayList<DocDetails>? = ArrayList()

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class DocDetails : Serializable {
            @JsonProperty("_id")
            var _id: String? = ""

            @JsonProperty("DOC_CODE")
            var DOC_CODE: String? = ""

            @JsonProperty("file_name")
            var file_name: String? = ""

            @JsonProperty("DOC_TYPE")
            var DOC_TYPE: String? = ""

            @JsonProperty("DOC_URL")
            var DOC_URL: String? = ""

            @JsonProperty("DOC_STATUS")
            var DOC_STATUS: String? = ""

            @JsonProperty("required")
            var required: Boolean? = false

            @JsonProperty("Remarks")
            var Remarks: Any? = null

        }
    }
}
