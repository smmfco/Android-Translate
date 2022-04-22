package com.example.translation.ui.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.os.PersistableBundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.translation.R
import com.shockwave.pdfium.PdfiumCore
import kotlinx.android.synthetic.main.activity_timage.*
import java.io.*
import java.lang.Exception


class TImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timage)

        val intent = intent

        val doc_dir = intent.getStringExtra("file_dirs")
        val file_name = intent.getStringExtra("file_name")
        val file_names = intent.getStringExtra("file_names")

        if(!Python.isStarted()){
            Python.start(AndroidPlatform(this))
        }

        val py : Python = Python.getInstance()
        val pyo : PyObject = py.getModule("test")
        val imageStr = pyo.callAttr("translate",doc_dir,file_name,file_names).toString()

        val bytePlainOrg = Base64.decode(imageStr,0)
        val inStream : ByteArrayInputStream = ByteArrayInputStream(bytePlainOrg)
        val bm : Bitmap = BitmapFactory.decodeStream(inStream)
        trImageView.setImageBitmap(bm)
    }

    private fun saveBitmapAsPng(bmp : Bitmap, strFilePath : String, fileName : String){
        val f = File(strFilePath)
        f.mkdirs()
        try{
            f.createNewFile()
            val out : FileOutputStream = FileOutputStream(f.path+"/"+fileName)
            val bufferOutput : BufferedOutputStream = BufferedOutputStream(out)
            bmp.compress(Bitmap.CompressFormat.PNG,100,bufferOutput)
            bufferOutput.flush()
            bufferOutput.close()
            out.close()
        }catch (e : FileNotFoundException){
            e.printStackTrace()
        }catch (e : IOException){
            e.printStackTrace()
        }
    }
}