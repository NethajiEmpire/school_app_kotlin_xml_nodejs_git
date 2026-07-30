package com.lms.sch.network

import android.content.Context
import com.lms.sch.session.SharedHelper
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.IOException

class AddCookiesInterceptor(var context: Context) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        val prefCookies: HashSet<String> = SharedHelper(context).cookies as HashSet<String>
        for (cookie in prefCookies) {
            builder.addHeader("Cookie", cookie)
        }
        return chain.proceed(builder.build())
    }
}