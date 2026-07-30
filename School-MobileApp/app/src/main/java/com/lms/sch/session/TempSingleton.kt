package com.lms.sch.session

import com.lms.sch.response.GetDocumentMaster
import com.lms.sch.response.GetDocumentsUploadResponse

class TempSingleton private constructor() {
    companion object {
        var isCalledForm: Boolean = false
        private var instane: TempSingleton? = null
        fun getInstance(): TempSingleton {
            if (instane == null) {
                instane = TempSingleton()
            }
            return instane!!
        }

        fun clearAllValues() {
            instane = TempSingleton()
        }
    }
    var apiUrl: String = ""

    var name: String = ""
    var isMultiple: Boolean = false
    var isVideo: Boolean = false
    var isImage: Boolean = true
    var isFile: Boolean = false
    var isPaymentSuccess: Boolean = false
    var isFormComplete: Boolean = false
    var files: String = ""

//    var fileList:ArrayList<FileFacer> = ArrayList()
    var docDetails:ArrayList<GetDocumentsUploadResponse.Result.DocDetails> = ArrayList()

    var isCommentUpdate: Boolean = false
    var isUserPost: Boolean = false

    var isWhatsApp: Boolean = false

    var feesPos = ""
    var videoSec = 0
    var webUrl:String = ""

}