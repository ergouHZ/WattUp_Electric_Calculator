package com.example.wattup.utils

import android.util.Base64
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.IOException

object ImageTo64 {
    private val TAG = "Image to 64 test"
    fun encodeImageToBase64(imagePath: String): String? {
        val file = File(imagePath)
        return try {
            val inputStream = FileInputStream(file)
            val bytes = inputStream.readBytes()
            inputStream.close()

            val result = Base64.encodeToString(bytes, Base64.NO_WRAP)
            Log.d(TAG, "encodeImageToBase64: $result ")
            result

        } catch (e: IOException) {
            e.printStackTrace()
            Log.e(TAG, "encodeImageToBase64: $e", )
            null
        }
    }



}