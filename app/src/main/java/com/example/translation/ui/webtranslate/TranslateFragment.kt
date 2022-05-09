package com.example.translation.ui.webtranslate

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import android.widget.Toast
import com.example.translation.databinding.FragmentTranslateBinding
import kotlinx.android.synthetic.main.fragment_translate.*
import android.webkit.*
import com.example.translation.*


var originalLanguage: String = ""
var targetLanguage: String = ""

class TranslateFragment : Fragment() {

    private var _binding: FragmentTranslateBinding? = null
    private lateinit var webSettings: WebSettings

    private val binding get() = _binding!!

    @SuppressLint("SetJavaScriptEnabled", "DiscouragedPrivateApi")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTranslateBinding.inflate(inflater, container, false)

        (activity as MainActivity).supportActionBar?.hide()

        setHasOptionsMenu(true)

        binding.webView.apply {
            webSettings = binding.webView.settings
            webSettings.javaScriptEnabled = true
            webSettings.domStorageEnabled = true

            binding.webView.webViewClient = WebViewClient()

            if(Build.VERSION.SDK_INT >= 19){
                binding.webView.setLayerType(View.LAYER_TYPE_HARDWARE,null)
            }else{
                binding.webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE,null)
            }
            (activity as MainActivity).window.setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
            )

            webSettings.setSupportMultipleWindows(true)
            webSettings.javaScriptCanOpenWindowsAutomatically = true
            webSettings.loadWithOverviewMode = true
            webSettings.useWideViewPort = true
            webSettings.setSupportZoom(false)
            webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
            webSettings.domStorageEnabled = true
            webSettings.safeBrowsingEnabled = true
            webSettings.mediaPlaybackRequiresUserGesture = false
            webSettings.allowContentAccess = true
            webSettings.setGeolocationEnabled(true)
            webSettings.allowUniversalAccessFromFileURLs = true
            webSettings.allowFileAccess = true
            webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH)
            webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
            webSettings.setEnableSmoothTransition(true)

            fitsSystemWindows = true

        }

        binding.webView.clearCache(true)
        binding.webView.clearHistory()

        binding.webView.loadUrl("https://www.google.com")

        binding.webBack.setOnClickListener {
            if (binding.webView.canGoBack()) {
                binding.webView.goBack()
            } else {
                (activity as MainActivity).onBackPressed()
            }
        }

        binding.webForward.setOnClickListener {
            if (binding.webView.canGoForward()) {
                binding.webView.goForward()
            }
        }

        binding.dicAdd.setOnClickListener {
            Toast.makeText(activity, originalLanguage, Toast.LENGTH_SHORT).show()
        }

        binding.webClose.setOnClickListener {
            binding.webView.clearCache(true)
            binding.webView.clearHistory()
            binding.webView.onPause()
            binding.webView.removeAllViews()
            binding.webView.destroyDrawingCache()
            binding.webView.pauseTimers()
            (activity as MainActivity).onBackPressed()
        }

        binding.urlButton.setOnClickListener {
            binding.webView.loadUrl(urlEdit.text.toString())
        }



        return binding.root
    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("SetJavaScriptEnabled")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val items = (activity as MainActivity).resources.getStringArray(R.array.my_array)
        val myAdapter = ArrayAdapter(
            context as MainActivity,
            android.R.layout.simple_spinner_dropdown_item,
            items
        )

        orginalSpinner.adapter = myAdapter
        orginalSpinner.setSelection(0)
        orginalSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (p2) {
                    0 -> {
                        originalLanguage = "en".trim()
                    }
                    1 -> {
                        originalLanguage = "ko".trim()
                    }
                    2 -> {
                        originalLanguage = "ja".trim()
                    }
                    3 -> {
                        originalLanguage = "zh".trim()
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        targetSpinner.adapter = myAdapter
        targetSpinner.setSelection(1)
        targetSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (p2) {
                    0 -> {
                        targetLanguage = "en".trim()
                    }
                    1 -> {
                        targetLanguage = "ko".trim()
                    }
                    2 -> {
                        targetLanguage = "ja".trim()
                    }
                    3 -> {
                        targetLanguage = "zh-CN".trim()
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).supportActionBar?.show()
        _binding = null
    }

}