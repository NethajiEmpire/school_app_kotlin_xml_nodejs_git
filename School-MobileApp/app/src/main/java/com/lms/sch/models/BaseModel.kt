    package com.lms.sch.models

    import androidx.databinding.BaseObservable
    import com.fasterxml.jackson.annotation.JsonIgnore
    import com.fasterxml.jackson.annotation.JsonIgnoreProperties
    import com.fasterxml.jackson.annotation.JsonInclude
    import com.fasterxml.jackson.annotation.JsonProperty

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    open class BaseModel : BaseObservable() {
        @JsonProperty("success")
        @JsonIgnore
        var success: Boolean = false

        @JsonProperty("msg")
        @JsonIgnore
        var msg: String = ""
    }