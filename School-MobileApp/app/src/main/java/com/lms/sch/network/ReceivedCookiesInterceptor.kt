package com.lms.sch.network

import android.content.Context
import com.lms.sch.session.SharedHelper
import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException

class ReceivedCookiesInterceptor(var context: Context) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse: Response = chain.proceed(chain.request())
        if (originalResponse.headers("Set-Cookie").isNotEmpty()) {
            val cookies: HashSet<String> = HashSet()
            for (header in originalResponse.headers("Set-Cookie")) {
                cookies.add(header)
            }
           // preferenceProvider.putStringSet("PREF_COOKIES", cookies)
            SharedHelper(context).cookies = cookies
            // Prefrence Provider In My SharedPrefrence Object
        }
        return originalResponse
    }
}