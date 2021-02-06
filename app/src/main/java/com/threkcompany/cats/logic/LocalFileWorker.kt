package com.threkcompany.cats.logic

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class LocalFileWorker {

    fun saveImageToDir(path: String, name: String, bitmap: Bitmap, context: Context): Uri {

        val file = File(path, "$name.jpg")
        file.createNewFile()

        try {
            val stream: OutputStream = FileOutputStream(file)

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

            stream.flush()

            stream.close()
            MediaStore.Images.Media.insertImage(
                context.contentResolver,
                file.absolutePath,
                file.name,
                file.name
            )
        } catch (e: IOException) { // Catch the exception
            Log.e("Save error", e.toString())
        }

        Log.d("Save image", file.absolutePath)
        return Uri.parse(file.absolutePath)
    }
}