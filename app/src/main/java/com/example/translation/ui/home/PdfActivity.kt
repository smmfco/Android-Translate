package com.example.translation.ui.home

import android.annotation.SuppressLint
import android.content.res.AssetManager
import android.os.Bundle
import android.util.Log
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.translation.*
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
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
        val pdfPath = secIntent.getStringExtra("PdfFile")
        if (pdfPath != null) {
            stripPath = pdfPath
        }

        val results = pdfPath?.split("/")
        val textName = results?.get(results.size-1)
        if (textName != null) {
            textNames = textName.substring(0,textName.length-4)
        }

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

        setup()
        stripText()
    }

    private fun setup() {
        PDFBoxResourceLoader.init(applicationContext)

        root = applicationContext.cacheDir
        assetManager = assets
    }

    private fun stripText() {
        var parsedText: String? = null
        var document: PDDocument? = null
        var pages = 0
        try {
            document = PDDocument.load(File(stripPath))
            pages = document.numberOfPages
            Toast.makeText(applicationContext,pages.toString(),Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Log.e("PdfBox-Android-Sample", "문서 로딩 예외 발생", e)
        }

        try {
            val pdfStripper = PDFTextStripper()

            val dir = File(dirpth)
            if(!dir.exists()){
                dir.mkdirs()
            }

            val fos = FileWriter(dirpth+"/"+textNames)
            val writer = BufferedWriter(fos)
            val fos2 = FileWriter(dirpth+"/translate")
            val writer2 = BufferedWriter(fos2)

            for(i in 1..pages){
                pdfStripper.startPage = i
                pdfStripper.endPage = i
                parsedText = pdfStripper.getText(document)
                val trText = ApiTranslateImage(parsedText).execute().get()
                writer2.write(trText)
                writer2.write("\n\n")

                writer.write(parsedText)
                writer.write("\n\n")
            }
            writer.close()
            writer2.close()
        } catch (e: IOException) {
            Log.e("PdfBox-Android-Sample", "추출 예외 발생", e)
        } finally {
            try {
                document?.close()
            } catch (e: IOException) {
                Log.e("PdfBox-Android-Sample", "문서 닫기 예외 발생", e)
            }
        }
        if (parsedText != null) {
            text = parsedText
        }

        val MAX_LEN = 2000
        val len = text.length
        if(len > MAX_LEN){
            var idx = 0
            var nextIdx = 0
            while (idx < len){
                nextIdx += MAX_LEN
                Log.e("PdfBox-Android-Result", text.substring(idx, if (nextIdx > len) len else nextIdx))
                idx = nextIdx
            }
        }
    }
}

