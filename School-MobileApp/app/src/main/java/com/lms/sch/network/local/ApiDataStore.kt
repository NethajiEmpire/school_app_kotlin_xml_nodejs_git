package com.lms.sch.network.local

import okhttp3.Headers
import org.json.JSONObject

class ApiDataStore(
    var isNew: Int?,
    var url: String,
    var method: String,
    var body: JSONObject,
    var header: Headers?,
    var responseCode: Int,
    var response: String
)