package com.example.translation.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.translation.R
import org.w3c.dom.Text

class DocsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_docs)

        if(!Python.isStarted()){
            Python.start(AndroidPlatform(this))
        }

        val py : Python = Python.getInstance()
        val pyo : PyObject = py.getModule("docs")
        val text = pyo.callAttr("docsTranslate").toString()
        Log.d("docs-Text",text)
        //커밋 테스트
    }
}