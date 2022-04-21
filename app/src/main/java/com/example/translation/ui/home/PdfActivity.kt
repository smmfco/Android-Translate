package com.example.translation.ui.home

import android.annotation.SuppressLint
import android.content.res.AssetManager
import android.os.Bundle
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.example.translation.*
import java.io.*

class PdfActivity : AppCompatActivity() {
    lateinit var root: File
    lateinit var assetManager: AssetManager
    var text: String = ""
    var stripPath : String = ""
    var dirpth = ""
    var textNames = ""
    @SuppressLint("SdCardPath", "SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf)

        dirpth = "${filesDir.path}/tmp"

        val pdfWebView = findViewById<WebView>(R.id.pdfWebView)

        val secIntent = intent
        val htmlPath = secIntent.getStringExtra("File")

        pdfWebView.webViewClient = WebViewClient()
        pdfWebView.webChromeClient = WebChromeClient()
        val webSetting = pdfWebView.settings
        webSetting.javaScriptEnabled = true
        webSetting.setSupportMultipleWindows(false)
        webSetting.javaScriptCanOpenWindowsAutomatically = false
        webSetting.loadWithOverviewMode = true
        webSetting.useWideViewPort = true
        webSetting.setSupportZoom(true)
        webSetting.builtInZoomControls=true
        webSetting.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        webSetting.cacheMode=WebSettings.LOAD_NO_CACHE
        webSetting.domStorageEnabled=true

        pdfWebView.loadUrl("file://$htmlPath")
    }
}

