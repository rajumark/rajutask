package com.hexflake.rajutaskevince.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.coroutines.CoroutineExceptionHandler
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


/*
* created extesntion function for get click event in cleaner way in
*
* */
infix fun View.click(clickBack: () -> Unit) {
    setOnClickListener {
        clickBack()
    }
}

/**
 * this function will save bitmap to private android storage
 * we can file in public download folder but it
 * will take scrope storage code then need top check every android verison
 *
 * */
fun saveimage(file: File, finalBitmap: Bitmap, saved:(File?)->Unit) {
    val root = file
    val myDir = File("$root")
    myDir.mkdirs()
    val fname = "Image-" + System.currentTimeMillis().toString() + ".jpg"
    val file = File(myDir, fname)
    if (file.exists()) file.delete()
    try {
        val out = FileOutputStream(file)
        finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        out.flush()
        out.close()
        Log.e("filedown","saved")
        saved(file)
    } catch (e: java.lang.Exception) {
        Log.e("filedown",e.message.toString())

        saved(null)
    }
}

/**
 * this function will download code then then return bitmap
 * */
suspend  fun downloadImage(path: String?, bitmap:(Bitmap?)->Unit) {
    var `in`: InputStream? = null
    var bmp: Bitmap? = null
    var responseCode = -1
    try {
        val url = URL(path)
        val con: HttpURLConnection = url.openConnection() as HttpURLConnection
        con.setDoInput(true)
        con.connect()
        responseCode = con.getResponseCode()
        if (responseCode == HttpURLConnection.HTTP_OK) {
            //download
            `in` = con.getInputStream()
            bmp = BitmapFactory.decodeStream(`in`)
            `in`.close()
            bitmap(bmp)
        }else{
            bitmap(null)
        }
    } catch (ex: Exception) {
        bitmap(null)
        Log.e("filedown", ex.toString())
    }
}

/**
* simple toast function for show sort toast
* */
fun Context.toast(message:String?){
    Toast.makeText(
        this,
        message.toString(),
        Toast.LENGTH_SHORT
    ).show()
}


val exceptionCoroutine= CoroutineExceptionHandler { coroutineContext, throwable ->  }


