package com.example.translation.ui.webtranslate

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import android.widget.Toast
import com.example.translation.databinding.FragmentTranslateBinding
import kotlinx.android.synthetic.main.fragment_translate.*
import android.webkit.*
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.example.translation.*
import java.lang.Exception


var originalLanguage: String = ""
var targetLanguage: String = ""

class TranslateFragment : Fragment() {

    private var _binding: FragmentTranslateBinding? = null
    private lateinit var webSettings: WebSettings

    private val binding get() = _binding!!

    var mMode: ActionMode? = null

    @SuppressLint("SetJavaScriptEnabled", "DiscouragedPrivateApi")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTranslateBinding.inflate(inflater, container, false)

        (activity as MainActivity).hideFloatingActionButton()

        (activity as MainActivity).supportActionBar?.hide()

        setHasOptionsMenu(true)

        binding.webView.apply {
            webSettings = binding.webView.settings
            webSettings.javaScriptEnabled = true

            setOnLongClickListener {
                /*
                val popupMenu = PopupMenu(requireContext(),it)
                popupMenu.inflate(R.menu.drawerlayout)

                popupMenu.setOnMenuItemClickListener { item ->
                    when(item.itemId){
                        R.id.menuitem1 ->{
                            true
                        }
                        else -> false
                    }
                }

                try{
                    val filedMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
                    filedMPopup.isAccessible = true
                    val mPopup = filedMPopup.get(popupMenu)
                    mPopup.javaClass
                        .getDeclaredMethod("setForceShowIcon",Boolean::class.java)
                        .invoke(mPopup,true)
                }catch (e: Exception){
                    Log.e("Main","Error showing menu icons.",e)
                }finally {
                    popupMenu.show()
                }
                 */

                return@setOnLongClickListener false
            }

            binding.webView.webViewClient = WebViewClient()

            webSettings.setSupportMultipleWindows(true)
            webSettings.javaScriptCanOpenWindowsAutomatically = true
            webSettings.loadWithOverviewMode = true
            webSettings.useWideViewPort = true
            webSettings.setSupportZoom(false)
            webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
            webSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            webSettings.domStorageEnabled = true
            webSettings.safeBrowsingEnabled = true
            webSettings.mediaPlaybackRequiresUserGesture = false
            webSettings.allowContentAccess = true
            webSettings.setGeolocationEnabled(true)
            webSettings.allowUniversalAccessFromFileURLs = true
            webSettings.allowFileAccess = true

            fitsSystemWindows = true
        }
        binding.webView.clearCache(true)
        binding.webView.clearHistory()

        binding.webView.loadUrl("https://en.wikipedia.org/wiki/Main_Page")

        binding.webBack.setOnClickListener {
            if (binding.webView.canGoBack()) {
                binding.webView.goBack()
            } else {
                mMode!!.finish()
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
            if (binding.webView.canGoBack()) {
                binding.webView.goBack()
            } else {
                mMode!!.finish()
                (activity as MainActivity).onBackPressed()
            }
        }

        binding.urlButton.setOnClickListener {
            binding.webView.loadUrl(urlEdit.text.toString())
        }

        return binding.root
    }

    private var mActionCallback: ActionMode.Callback2 = object : ActionMode.Callback2() {
        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false
        }

        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            val inflater: MenuInflater = mode.menuInflater
            inflater.inflate(R.menu.drawerlayout, menu)
            return true
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            mMode = null
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.menuitem1 -> {
                    webView.evaluateJavascript("javascript:(function getSelectedText(){return window.getSelection().toString()})()",
                        ValueCallback<String>() { value ->
                            val str = value.toString().substring(1, value.toString().length - 1)
                            val translateTask = ApiTranslateNmt(str).execute().get()
                            webView.evaluateJavascript(
                                "javascript:(function getSelectedText() {\n" +
                                        "    var sel, range, newTranslateText, range2, container;\n" +
                                        "    if (window.getSelection()) {\n" +
                                        "        sel = window.getSelection();\n" +
                                        "        if (sel.rangeCount) {\n" +
                                        "            range = sel.getRangeAt(0);\n" +
                                        "            range2 = range.cloneRange();\n" +
                                        "            range2.collapse(false);\n" +
                                        //"            range = selection.focusOffset;\n" +
                                        //"            range.deleteContents();\n" +
                                        "            container = document.createElement(\"span\");\n" +
                                        "            container.style.backgroundColor = \"$hexColor\";\n" +
                                        "            container.style.color = \"$hexColor2\";\n" +
                                        "            container.style.border = \"$pref4 $pref5 $hexColor3\";\n" +
                                        "            container.style.borderRadius = \"25px\";\n" +
                                        "            newTranslateText = document.createTextNode('${translateTask}');\n" +
                                        "            container.appendChild(newTranslateText);\n" +
                                        //"            range2.appendChild(newTranslateText);\n" +
                                        "            range2.insertNode(container);\n" +
                                        //"            newTranslateText.appendChild(container);\n" +
                                        "        }\n" +
                                        "    } else if (document.selection && document.selection.createRange) {\n" +
                                        "        range = document.selection.createRange();\n" +
                                        "        range.text = '${translateTask}';\n" +
                                        "    }\n" +
                                        "})()", null
                            )
                            //Toast.makeText(activity, hexColor, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
            return false
        }

        override fun onGetContentRect(mode: ActionMode, view: View, outRect: Rect) {
            outRect.set(0, 0, 0, 0)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (mMode == null) {
            mMode = (activity as MainActivity).startActionMode(mActionCallback)
        }

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).showFloatingActionButton()
        (activity as MainActivity).supportActionBar?.show()
        mMode = null
        _binding = null
    }
}