package com.lms.sch.network

import android.content.Context
import android.util.Log
import com.lms.sch.BuildConfig
import com.lms.sch.session.Constants.ApplicationConstants.API_AUTH_TYPE
import com.lms.sch.session.Constants.ApplicationConstants.BASE_URL
import com.lms.sch.utils.BaseUtils.getMd5String
import com.lms.sch.network.local.ApiDataDialog
import com.lms.sch.session.Constants
import com.lms.sch.session.SharedHelper
import com.lms.sch.session.TempSingleton
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.CookieManager
import java.net.CookiePolicy
import java.net.URLDecoder
import java.nio.charset.Charset
import java.security.cert.CertificateException
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.*


class ApiClient {

    companion object {

        private const val CONNECT_TIMEOUT_MULTIPLIER = 1
        private const val DEFAULT_CONNECT_TIMEOUT_IN_SEC = 60 * CONNECT_TIMEOUT_MULTIPLIER
        private const val DEFAULT_WRITE_TIMEOUT_IN_SEC = 60 * CONNECT_TIMEOUT_MULTIPLIER
        private const val DEFAULT_READ_TIMEOUT_IN_SEC = 60 * CONNECT_TIMEOUT_MULTIPLIER

        private const val NO_OF_LOG_CHAR = 1000

        private var sRetrofitClient: Retrofit? = null
        private val sDispatcher: Dispatcher? = null

        fun getClient(context: Context): Retrofit? {
            if (sRetrofitClient == null) {
                sRetrofitClient = Retrofit.Builder()
                        .baseUrl("$BASE_URL")
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(JacksonConverterFactory.create())
                        .client(getOkHttpClientBuilder(context).build())
//                        .client(getUnsafeOkHttpClientBuilder().build())
                        .build()
            }
            return sRetrofitClient
        }

        private fun getOkHttpClientBuilder(context: Context): OkHttpClient.Builder {
            /*OkHttp client builder*/
            val oktHttpClientBuilder = OkHttpClient.Builder()
                    .connectTimeout((CONNECT_TIMEOUT_MULTIPLIER * DEFAULT_CONNECT_TIMEOUT_IN_SEC).toLong(), TimeUnit.SECONDS)
                    .writeTimeout((CONNECT_TIMEOUT_MULTIPLIER * DEFAULT_WRITE_TIMEOUT_IN_SEC).toLong(), TimeUnit.SECONDS)
                    .readTimeout((CONNECT_TIMEOUT_MULTIPLIER * DEFAULT_READ_TIMEOUT_IN_SEC).toLong(), TimeUnit.SECONDS)
                    .cookieJar(JavaNetCookieJar(getCookieManager())) /* Using okhttp3 cookie instead of java net cookie*/

            oktHttpClientBuilder.dispatcher(getDispatcher())
            oktHttpClientBuilder.addInterceptor { chain ->
                val request = chain.request()
                val builder = request.newBuilder()
                    .addHeader("X-School-Code", Constants.ApplicationConstants.SCHOOL_CODE)

                Log.d("cvbnm",""+request.body+".."+SharedHelper(context).token)
                
                if(request.body != null){
                    Log.d("cvbnm",""+request.body!!.contentType().toString())
                    if(TempSingleton.getInstance().isWhatsApp){
                        TempSingleton.getInstance().isWhatsApp = false
                        builder.addHeader("Content-Type", "application/json")
                            .addHeader("clientId", "nagheff6")
                            .addHeader("clientSecret", "gkmmuhre8cgjbxsa")
                    }
                    else if(request.body!!.contentType().toString() == "image/png"){
                        builder.addHeader("Content-Type", "image/png")
                    }
                    else{
                        builder.addHeader("Content-Type", "application/json")
                            .addHeader("Authorization", Constants.ApiKeys.BEARER+SharedHelper(context).token)
                    }
                }
                else{
                    builder.addHeader("Authorization", Constants.ApiKeys.BEARER+SharedHelper(context).token)
                }
                
                chain.proceed(builder.build())
            }
          //  oktHttpClientBuilder.addInterceptor(AddCookiesInterceptor(context))
          //  oktHttpClientBuilder.addInterceptor(ReceivedCookiesInterceptor(context))
            oktHttpClientBuilder.addInterceptor(getHttpLoggingInterceptor())
            oktHttpClientBuilder.addInterceptor { chain ->
                var request = chain.request()
                var response = chain.proceed(request)
                if(response.code == 403 || response.code == 401 || response.code == 500){
                    TempSingleton.getInstance().apiUrl = request.url.toString()
                }
                if(BuildConfig.DEBUG){
                    printPostmanFormattedLog(request,response,context)
                }
                val token = response.header("token")
                Log.d("Response Code = ",""+response.code)
                response
            }

            return oktHttpClientBuilder
        }

        private fun getUnsafeOkHttpClientBuilder(context: Context): OkHttpClient.Builder {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
                }

                override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate?> {
                    return arrayOfNulls(0)
                }
            })

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory

            val oktHttpClientBuilder = OkHttpClient.Builder()
                    .connectTimeout((CONNECT_TIMEOUT_MULTIPLIER * DEFAULT_CONNECT_TIMEOUT_IN_SEC).toLong(), TimeUnit.SECONDS)
                    .writeTimeout((CONNECT_TIMEOUT_MULTIPLIER * DEFAULT_WRITE_TIMEOUT_IN_SEC).toLong(), TimeUnit.SECONDS)
                    .readTimeout((CONNECT_TIMEOUT_MULTIPLIER * DEFAULT_READ_TIMEOUT_IN_SEC).toLong(), TimeUnit.SECONDS)
                    .cookieJar(JavaNetCookieJar(getCookieManager())) /* Using okhttp3 cookie instead of java net cookie*/
            oktHttpClientBuilder.dispatcher(getDispatcher())

            oktHttpClientBuilder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
//            HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory)
            oktHttpClientBuilder.hostnameVerifier(object : HostnameVerifier {
                override fun verify(hostname: String, session: SSLSession): Boolean {
                    return true
                }
            })

            oktHttpClientBuilder.addInterceptor { chain ->
                val builder = chain.request().newBuilder()
                        .addHeader("Content-Type", "text/html")
                        .addHeader("X-School-Code", Constants.ApplicationConstants.SCHOOL_CODE)
                        .addHeader("apiAuthType", API_AUTH_TYPE.lowercase(Locale.getDefault()))
                chain.proceed(builder.build())
            }

            oktHttpClientBuilder.addInterceptor(getHttpLoggingInterceptor())
            oktHttpClientBuilder.addInterceptor { chain ->
                var request = chain.request()
                var response = chain.proceed(request)
                Log.d("Response Code :", "intercept: " + response.code)
                val token = response.header("token")
                if (response.code == 401 && token != null && token.isNotEmpty()) {
                    val usernamePasswordMd5 = getMd5String(Constants.ApplicationConstants.API_USER_NAME + ":" + Constants.ApplicationConstants.API_PASSWORD)
                    Log.d("Token :", "$token")
                    response.close()
                    request = request.newBuilder().build()
                    response = chain.proceed(request)
                }
                printPostmanFormattedLog(request, response,context)
                response
            }

            return oktHttpClientBuilder
        }

        private fun printPostmanFormattedLog(request: Request, response: Response,context: Context) {
            try {
                val allParams: String = if (request.method == "GET" || request.method == "DELETE") {
                    request.url.toString().substring(request.url.toString().indexOf("?") + 1, request.url.toString().length)
                } else {
                    val buffer = Buffer()
                    request.body!!.writeTo(buffer)
                    buffer.readString(Charset.forName("UTF-8"))
                }
                val params = allParams.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val paramsString = StringBuilder("\n")
                for (param in params) {
                    paramsString.append(decode(param.replace("=", ":")))
                    paramsString.append("\n")
                }
               // Log.d("Request Body : ", paramsString.toString())

                var requestBody = JSONObject()
               // var responseBody = response.body.string()
                val bCount = Long.MAX_VALUE
                var responseBody = response.peekBody(2048).string()

               // val responseBody = Gson().fromJson(response.body.string(), Object::class.java).toString()
               // val responseBody = response.body
                if(request.body != null){
                    requestBody = JSONObject(bodyToString(request.body!!).toString())
                }
                ApiDataDialog(context).load(request.url.toString(),request.method,requestBody,request.headers,response.code,responseBody)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private fun bodyToString(request: RequestBody): String? {
            return try {
                val buffer = Buffer()
                request.writeTo(buffer)
                buffer.readUtf8()
            } catch (e: IOException) {
                "did not work"
            }
        }

        private fun getHttpLoggingInterceptor(): Interceptor {
            val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
                if (message.length > NO_OF_LOG_CHAR) {
                    for (noOfLogs in 0..message.length / NO_OF_LOG_CHAR) {
                        if (noOfLogs * NO_OF_LOG_CHAR + NO_OF_LOG_CHAR < message.length) {
                            Log.d("Char Log1 :", message.substring(noOfLogs * NO_OF_LOG_CHAR, noOfLogs * NO_OF_LOG_CHAR + NO_OF_LOG_CHAR))
                        } else {
                            Log.d("Char Log2 :", message.substring(noOfLogs * NO_OF_LOG_CHAR, message.length))
                        }
                    }
                }
                else {
                    Log.d("Char Log3 :", message)
                }
            })
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            return loggingInterceptor
        }

        private fun getCookieManager(): CookieManager {
            val cookieManager = CookieManager()
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
            return cookieManager
        }

        fun getDispatcher(): Dispatcher {
            return sDispatcher ?: Dispatcher()
        }

        private fun decode(url: String): String {
            return try {
                var prevURL = ""
                var decodeURL = url
                while (prevURL != decodeURL) {
                    prevURL = decodeURL
                    decodeURL = URLDecoder.decode(decodeURL, "UTF-8")
                }
                decodeURL
            } catch (e: UnsupportedEncodingException) {
                "Issue while decoding" + e.message
            }
        }
    }
}
