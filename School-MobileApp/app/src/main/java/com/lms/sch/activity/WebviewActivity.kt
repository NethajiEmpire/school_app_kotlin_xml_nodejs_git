package com.lms.sch.activity

import android.os.Bundle
import android.util.Log
import android.view.Window
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lms.sch.R
import com.lms.sch.databinding.ActivityWebviewBinding
import com.lms.sch.session.TempSingleton


class WebviewActivity : AppCompatActivity() {
    private lateinit var binding : ActivityWebviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        TempSingleton.getInstance().isPaymentSuccess = false
        val webViewClient: WebViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.e("ccv",""+url)
               // val url = "https://mercury-uat.phonepe.com/transact/simulator?token=2XK78zCpszKErsEv5nYvN625pznewXunCoPYsMWHYPmBt#"
                val url1 = "https://appschool.aimwindow.in/"
                val url2 = "https://appschool.aimwindow.in/student-registeration"
                if (url != null && url.contains(url1) || url!!.contains(url2) ){
                    TempSingleton.getInstance().isPaymentSuccess = true
                    finish()
                }
            }
        }

        val webChromeClient: WebChromeClient = object : WebChromeClient() {
            override fun onCloseWindow(window: WebView?) {
                super.onCloseWindow(window)
                // Log.d("vfdv2",""+window)
                finish()
            }
        }

        // if you want to enable zoom feature
        binding.webView.settings.setSupportZoom(true)
        binding.webView.clearCache(true)
        binding.webView.clearHistory()
        // this will enable the javascript settings, it can also allow xss vulnerabilities
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.javaScriptCanOpenWindowsAutomatically = true
        binding.webView.settings.domStorageEnabled = true
        // WebViewClient allows you to handle
        // onPageFinished and override Url loading.
        binding.webView.webViewClient = webViewClient
        binding.webView.webChromeClient = webChromeClient
        // this will load the url of the website
        binding.webView.loadUrl(TempSingleton.getInstance().webUrl)
    }

    // if you press Back button this code will work
    override fun onBackPressed() {
        // if your webview can go back it will go back
        if (binding.webView.canGoBack())
            binding.webView.goBack()
        // if your webview cannot go back
        // it will exit the application
        else
            super.onBackPressed()
    }
}