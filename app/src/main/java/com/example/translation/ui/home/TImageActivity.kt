package com.example.translation.ui.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.os.PersistableBundle
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

        val pdf_dir = intent.getStringExtra("file_dir")
        val doc_dir = intent.getStringExtra("file_dirs")
        val file_name = intent.getStringExtra("file_name")
        val file_names = intent.getStringExtra("file_names")

        /*
        val images = ArrayList<Bitmap>()
        var pdfiumCore : PdfiumCore = PdfiumCore(applicationContext)
        try{
            var f : File = File(pdf_dir)
            var fd : ParcelFileDescriptor = ParcelFileDescriptor.open(f, ParcelFileDescriptor.MODE_READ_ONLY)
            var pdfDocument : com.shockwave.pdfium.PdfDocument = pdfiumCore.newDocument(fd)
            var pageCount : Int = pdfiumCore.getPageCount(pdfDocument)
            for(i in 0..pageCount){
                pdfiumCore.openPage(pdfDocument,i)
                var width : Int = pdfiumCore.getPageWidthPoint(pdfDocument,i)
                var height : Int = pdfiumCore.getPageHeightPoint(pdfDocument,i)
                var bmp : Bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888)
                pdfiumCore.renderPageBitmap(pdfDocument,bmp,i,0,0,width,height)
                images.add(bmp)
            }
            pdfiumCore.closeDocument(pdfDocument)
        }catch (e : Exception){
            e.printStackTrace()
        }
        trImageView.setImageBitmap(images[0])

        var imPath = applicationContext.getExternalFilesDir("").toString()
        var index = imPath.indexOf("0")
        var imPath2 = imPath.substring(index+1,imPath.length)
        index = imPath2.lastIndexOf("/")
        var imPath3 = imPath2.substring(0,index+1)

        if (file_names != null) {
            saveBitmapAsPng(images[0],"/sdcard$imPath3",file_names)
        }

         */

        if(!Python.isStarted()){
            Python.start(AndroidPlatform(this))
        }

        val py : Python = Python.getInstance()
        val pyo : PyObject = py.getModule("test")
        val imageStr = pyo.callAttr("translate",doc_dir,file_name,file_names).toString()
        Log.d("Test-Message", imageStr)

        //trImageView.setImageURI(Uri.parse("file:///sdcard$imPath3$file_name"))

        /*
        if(!Python.isStarted()){
            Python.start(AndroidPlatform(this))
        }

        val py : Python = Python.getInstance()
        val pyo : PyObject = py.getModule("test")
        pyo.callAttr("translate",doc_dir,file_name,file_names)

        var imPath = applicationContext.getExternalFilesDir("").toString()
        var index = imPath.indexOf("0")
        var imPath2 = imPath.substring(index+1,imPath.length)
        index = imPath2.lastIndexOf("/")
        var imPath3 = imPath2.substring(0,index+1)
        Log.d("Test-Message", imPath3)

        val trImage = File("/sdcard$imPath3",file_names+"_translate.png").inputStream()

        try{
            val bm : Bitmap = BitmapFactory.decodeStream(trImage)

            trImageView.setImageBitmap(bm)
            trImage.close()
        }catch (e : Exception){
            e.printStackTrace()
        }

         */
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