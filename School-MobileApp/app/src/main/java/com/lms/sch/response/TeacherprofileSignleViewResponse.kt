package com.lms.sch.response

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lms.sch.models.BaseModel
import java.io.Serializable


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class TeacherprofileSignleViewResponse: BaseModel() {

    @JsonProperty("result")
    var result: Result? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result : Serializable {

        @JsonProperty("_id")
        var _id: String? = ""

        @JsonProperty("firstName")
        var firstName: String? = ""

        @JsonProperty("lastName")
        var lastName: String? = ""

        @JsonProperty("img_url")
        var img_url: String? = ""

        @JsonProperty("role")
        var role: Role? = null

        @JsonProperty("email")
        var email: String? = ""

        @JsonProperty("mobile")
        var mobile: String? = ""

        @JsonProperty("address")
        var address: String? = ""

        @JsonProperty("gender")
        var gender: String? = ""

        @JsonProperty("student_enrollment")
        var student_enrollment: Boolean? = false

        @JsonProperty("lead_id")
        var lead_id: String? = ""

        @JsonProperty("leadNo")
        var leadNo: Int? = null

        @JsonProperty("lastLogin")
        var lastLogin: String? = ""

        @JsonProperty("createdAt")
        var createdAt: String? = ""

        @JsonProperty("teacherPreference")
        var teacherPreference: TeacherPreference? = null

        @JsonProperty("dob")
        var dob: String? = ""

        @JsonProperty("blood_group")
        var blood_group: String? = ""

        @JsonProperty("nationality")
        var nationality: String? = ""

        @JsonProperty("religion")
        var religion: String? = ""

        @JsonProperty("category")
        var category: String? = ""

        @JsonProperty("pre_school")
        var pre_school: String? = ""

        @JsonProperty("aadhar_number")
        var aadhar_number: String? = ""

        @JsonProperty("pincode")
        var pincode: String? = ""

        @JsonProperty("country")
        var country: String? = ""

        @JsonProperty("state")
        var state: String? = ""

        @JsonProperty("city")
        var city: String? = ""


        @JsonProperty("status")
        var status: String? = ""

        @JsonProperty("isDeleted")
        var isDeleted: Boolean? = null

        @JsonProperty("updatedAt")
        @JsonIgnore
        var updatedAt: String? = ""

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Role : Serializable {
            @JsonProperty("_id")
            var id: String? = ""

            @JsonProperty("name")
            var name: String? = ""
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        class TeacherPreference : Serializable {

            @JsonProperty("_id")
            var _id: String? = ""

            @JsonProperty("teacherType")
            var teacherType: String? = ""

            @JsonProperty("highestQualification")
            var highestQualification: String? = ""

            @JsonProperty("overallExperience")
            var overallExperience: String? = ""

            @JsonProperty("board")
            var board: ArrayList<Board>? = ArrayList()

            @JsonProperty("majorSubjects")
            var majorSubjects: ArrayList<MajorSubjects>? = ArrayList()

            @JsonProperty("myStudentClass")
            var myStudentClass : MyStudentClass? = null

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
            class MajorSubjects : Serializable {

                @JsonProperty("_id")
                var _id: String? = ""

                @JsonProperty("name")
                var name: String? = ""
            }
            @JsonInclude(JsonInclude.Include.NON_NULL)
            @JsonIgnoreProperties(ignoreUnknown = true)
            class MyStudentClass : Serializable{

                @JsonProperty("_id")
                var _id : String? = null

                @JsonProperty("board")
                var board : Board? = null

                @JsonProperty("studentClass")
                var studentClass : StudentClass? = null

                @JsonProperty("section")
                var section : Section? = null

                @JsonInclude(JsonInclude.Include.NON_NULL)
                @JsonIgnoreProperties(ignoreUnknown = true)

                class Board : Serializable{

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
            }
        }
    }
}