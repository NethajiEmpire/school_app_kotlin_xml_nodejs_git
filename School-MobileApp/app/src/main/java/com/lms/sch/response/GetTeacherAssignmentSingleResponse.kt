package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class GetTeacherAssignmentSingleResponse : BaseModel()  {

    @JsonProperty("data")
    var data: Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable{

        @JsonProperty("markStatus")
        var markStatus : String? = ""

        @JsonProperty("_id")
        var _id : String? = ""

        @JsonProperty("title")
        var title : String? = ""

        @JsonProperty("description")
        var description : String? = ""

        @JsonProperty("teacher")
        var  teacher : Any? = null

        @JsonProperty("board")
        var  board : Board? = null

        @JsonProperty("studentClass")
        var studentClass : StudentClass? = null

        @JsonProperty("batch")
        var batch : Any? = null

        @JsonProperty("section")
        var section : Section? = null

        @JsonProperty("subject")
        var  subject : Subject? = null

        @JsonProperty("chapter")
        var chapter : String? = null

        @JsonProperty("lessons")
        var lessons : String? = null

        @JsonProperty("studentIds")
        var  studentIds : Any? = null

        @JsonProperty("attachment")
        var attachment : String? = ""

        @JsonProperty("dueDate")
        var dueDate : String? = ""

        @JsonProperty("img_url")
        var img_url : String? = ""

        @JsonProperty("status")
        var status : String? = ""

        @JsonProperty("createdBy")
        var createdBy : CreatedBy? = null

        @JsonProperty("isDeleted")
        var isDeleted : Boolean? = false

        @JsonProperty("createdAt")
        var createdAt : String? = ""

        @JsonProperty("updatedAt")
        var updatedAt : String? = ""

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)

        class  Board : Serializable{

            @JsonProperty("_id")
            var _id : String? = ""

            @JsonProperty("name")
            var name : String? = ""
        }
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)

        class StudentClass : Serializable{
            @JsonProperty("_id")
            var _id : String? = ""

            @JsonProperty("name")
            var name : String? = ""
        }
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)

        class Section : Serializable{

            @JsonProperty("_id")
            var _id : String? = ""

            @JsonProperty("name")
            var name : String? = ""
        }
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)

        class Subject : Serializable{
            @JsonProperty("_id")
            var _id : String? = ""

            @JsonProperty("name")
            var name: String? = ""
        }
        /*@JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)

        class Chapter : Serializable{
            @JsonProperty("_id")
            var _id : String? = ""

            @JsonProperty("name")
            var name: String? = ""

            @JsonProperty("chapterNumber")
            var chapterNumber : String? = ""
        }*/
        /*@JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)

        class Lessons : Serializable{

            @JsonProperty("_id")
            var _id : String? = ""

            @JsonProperty("name")
            var name: String? = ""

            @JsonProperty("lessonNumber")
            var lessonNumber : String? = ""
        }*/
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class StudentIds : Serializable{

        }
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class CreatedBy : Serializable{

            @JsonProperty("_id")
            var _id : String? = ""

            @JsonProperty("firstName")
            var firstName : String? = ""

            @JsonProperty("lastName")
            var lastName : String? = ""

            @JsonProperty("role")
            var role : Role? = null

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @JsonIgnoreProperties(ignoreUnknown = true)

            class Role : Serializable{

                @JsonProperty("_id")
                var _id : String? = ""

                @JsonProperty("name")
                var name : String? = ""

            }
        }

    }

}