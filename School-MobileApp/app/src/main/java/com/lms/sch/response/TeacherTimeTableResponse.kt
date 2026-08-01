package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class TeacherTimeTableResponse  : BaseModel(){

    @JsonProperty("data")
    var data: Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)

    class Result : Serializable{

        @JsonProperty("day")
        var day : String? = ""

        @JsonProperty("teacher")
        var teacher : Teacher? = null

        @JsonProperty("periods")
        var periods : ArrayList<Periods>? = ArrayList()

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)

        class Teacher : Serializable{

            @JsonProperty("_id")
            var _id : String? = ""

            @JsonProperty("firstName")
            var firstName : String? = ""

            @JsonProperty("lastName")
            var lastName : String? = ""

            @JsonProperty("email")
            var email : String? = ""
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)

        class Periods : Serializable{

            @JsonProperty("_id")
            var _id : String? = ""

            @JsonProperty("startTime")
            var startTime :  String? = ""

            @JsonProperty("endTime")
            var endTime :  String? = ""

            @JsonProperty("type")
            var type :  String? = ""

            @JsonProperty("studentClass")
            var studentClass :  String? = ""

            @JsonProperty("board")
            var board :  String? = ""

            @JsonProperty("section")
            var section :  String? = ""

            @JsonProperty("subject")
            var subject :  Subject? = null
        }
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)

        class Subject : Serializable{

            @JsonProperty("_id")
            var _id : String? = ""

            @JsonProperty("name")
            var name : String? = ""

        }

    }

}