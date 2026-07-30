package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class TeacherSideStudentProjectResponse : BaseModel() {

    @JsonProperty("result")
    var result : ArrayList<Result>? = ArrayList()

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)

    class Result : Serializable{
        @JsonProperty("_id")
        var _id : String? = ""

        @JsonProperty("program")
        var  program : String? = ""

        @JsonProperty("project")
        var project : Project? = null

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)

        class Project : Serializable{
            @JsonProperty("_id")
            var  _id : String? = ""

            @JsonProperty("title")
            var title : String? = ""

            @JsonProperty("description")
            var description : String? = ""

            @JsonProperty("attachment")
            var attachment : String? = ""

            @JsonProperty("createdBy")
            var createdBy : Createdby? = null

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @JsonIgnoreProperties(ignoreUnknown = true)

            class Createdby : Serializable{

                @JsonProperty("_id")
                var _id : String? = ""

                @JsonProperty("firstName")
                var firstName : String? = ""

                @JsonProperty("lastName")
                var lastName : String? = ""
            }
            @JsonProperty("totalMarks")
            var totalMarks : String? = ""
        }
           @JsonProperty("student")
           var student : Student? = null

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)

           class Student : Serializable{

               @JsonProperty("_id")
               var _id : String? = ""

               @JsonProperty("firstName")
               var firstName : String? = ""

               @JsonProperty("lastName")
               var lastName : String? = ""

               @JsonProperty("img_url")
               var img_url : String? =""

                @JsonProperty("mobile")
                var mobile : String? = ""

                @JsonProperty("email")
                var email : String? =""
           }

        @JsonProperty("subject")
        var subject : Subject? = null

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)

        class Subject : Serializable{

            @JsonProperty("_id")
            var _id : String? = ""

            @JsonProperty("Name")
            var Name : String? = ""
        }

        @JsonProperty("dueDate")
        var  dueDate: String? = ""

        @JsonProperty("attachment")
         var attachment: ArrayList<String>? = ArrayList()

        @JsonProperty("status")
        var status : String? = ""

        @JsonProperty("markStatus")
        var markStatus : String? = ""

        @JsonProperty("createdAt")
        var createdAt : String? = ""

        @JsonProperty("updatedAt")
        var updatedAt : String? = ""

        @JsonProperty("remarks")
        var remarks: String? = ""

        @JsonProperty("scored_marks")
        var scored_marks : String? = ""
    }

}