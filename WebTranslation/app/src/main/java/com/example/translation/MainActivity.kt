package com.example.translation

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.StrictMode
import android.view.*
import android.webkit.ValueCallback
import android.webkit.WebView
import android.widget.PopupMenu
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.example.translation.databinding.ActivityMainBinding
import com.example.translation.ui.webtranslate.ApiTranslateNmt
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.translate.Translate
import com.google.cloud.translate.TranslateOptions
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_translate.*
import java.io.*


var pref: Int = 0
var pref2: Int = 0
var pref3: Int = 0
var pref4: String = ""
var pref5: String = ""
var hexColor: String = ""
var hexColor2: String = ""
var hexColor3: String = ""

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    var translate: Translate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_manage, R.id.login
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        pref = PreferenceManager.getDefaultSharedPreferences(this)
            .getInt("background_color", ContextCompat.getColor(baseContext, R.color.background))
        pref2 = PreferenceManager.getDefaultSharedPreferences(this)
            .getInt(
                "word_color",
                ContextCompat.getColor(baseContext, R.color.background)
            )
        pref3 = PreferenceManager.getDefaultSharedPreferences(this)
            .getInt(
                "border_color",
                ContextCompat.getColor(baseContext, R.color.background)
            )
        pref4 = PreferenceManager.getDefaultSharedPreferences(this)
            .getString("border_thick", "").toString()
        pref5 = PreferenceManager.getDefaultSharedPreferences(this)
            .getString("border_style", "").toString()
        hexColor = String.format("#%06X", 0xFFFFFF and pref)
        hexColor2 = String.format("#%06X", 0xFFFFFF and pref2)
        hexColor3 = String.format("#%06X", 0xFFFFFF and pref3)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_activity2, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun getTranslateService() {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        try {
            resources.openRawResource(R.raw.credential2).use { `is` ->
                val myCredentials = GoogleCredentials.fromStream(`is`)
                val translateOptions =
                    TranslateOptions.newBuilder().setCredentials(myCredentials).build()
                translate = translateOptions.service
            }
        } catch (ioe: IOException) {
            ioe.printStackTrace()
        }
    }

    fun getFloatingActionButton(): FloatingActionButton {
        return fab
    }

    fun showFloatingActionButton() {
        fab.show()
    }

    fun hideFloatingActionButton() {
        fab.hide()
    }

    /*
    @SuppressLint("ResourceType")
    override fun onActionModeStarted(mode: ActionMode) {
        super.onActionModeStarted(mode)

        mode.menu.clear()
        mode.menuInflater.inflate(R.menu.drawerlayout,mode.menu)
        mode.menu.getItem(0).setOnMenuItemClickListener {
            webView.evaluateJavascript("javascript:(function getSelectedText(){return window.getSelection().toString()})()",
                ValueCallback<String>(){ value ->
                    val str = value.toString().substring(1, value.toString().length-1)
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
                                //"            newTranslateText = document.createTextNode('${"환영합니다"}');\n" +
                                "            container.appendChild(newTranslateText);\n" +
                                //"            range2.appendChild(newTranslateText);\n" +
                                "            range2.insertNode(container);\n" +
                                //"            newTranslateText.appendChild(container);\n" +
                                "        }\n" +
                                "    } else if (document.selection && document.selection.createRange) {\n" +
                                "        range = document.selection.createRange();\n" +
                                "        range.text = '${"환영합니다"}';\n" +
                                "    }\n" +
                                "})()", null
                    )
                    Toast.makeText(applicationContext, value.toString(), Toast.LENGTH_SHORT).show()
                }
            )
            return@setOnMenuItemClickListener true
        }
        mode.menu.getItem(1).setOnMenuItemClickListener {
            return@setOnMenuItemClickListener true
        }
        mode.menu.getItem(2).setOnMenuItemClickListener {
            return@setOnMenuItemClickListener true
        }
    }
     */



    override fun onActionModeFinished(mode: ActionMode?) {
        super.onActionModeFinished(mode)
    }
}