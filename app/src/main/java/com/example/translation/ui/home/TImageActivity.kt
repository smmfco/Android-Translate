package com.example.translation.ui.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.translation.R
import kotlinx.android.synthetic.main.activity_timage.*
import java.io.File
import java.io.InputStream
import java.lang.Exception


class TImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timage)

        val intent = intent

        val doc_dir = intent.getStringExtra("file_dir")
        val file_name = intent.getStringExtra("file_name")
        val file_names = intent.getStringExtra("file_names")

        if(!Python.isStarted()){
            Python.start(AndroidPlatform(this))
        }

        val py : Python = Python.getInstance()
        val pyo : PyObject = py.getModule("test")
        pyo.callAttr("translate",doc_dir,file_name,file_names)

        Toast.makeText(applicationContext,"${Environment.getExternalStorageDirectory()}",Toast.LENGTH_SHORT).show()

        /*
        val trImage = File(Environment.getExternalStorageDirectory(),file_names+"_translate.png").inputStream()

        try{
            val bm : Bitmap = BitmapFactory.decodeStream(trImage)

            trImageView.setImageBitmap(bm)
        }catch (e : Exception){
            e.printStackTrace()
        }

         */
    }
}