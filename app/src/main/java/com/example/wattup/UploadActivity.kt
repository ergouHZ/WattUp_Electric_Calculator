package com.example.wattup

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.wattup.entity.ChatCompletionRequest
import com.example.wattup.entity.ChatCompletionResponse
import com.example.wattup.entity.Message
import com.example.wattup.utils.ChatService
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class UploadActivity : AppCompatActivity() {

    private val TAG = "UploadActivity test"

    private val REQUEST_IMAGE_PICK = 100
    private val PERMISSION_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkAndRequestPermissions()

        openGallery()
    }

    // 打开相册选择图片
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            val selectedImage: Uri? = data.data
            selectedImage?.let { uploadImage(it) }
        }
    }

    private fun convertUriToBase64(uri: Uri): String? {
        return try {
            // 读取文件为字节数组
            val inputStream = contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes()
            inputStream?.close()

            // 编码为 Base64 字符串
            bytes?.let {
                Base64.encodeToString(it, Base64.NO_WRAP)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun uploadImage(imageUri: Uri) {
        // get the real path
        val realPath = getRealPathFromURI(imageUri)

        //
        if (realPath != null) {
            val baseCode = convertUriToBase64(imageUri)

            try {
                // 创建 ImageUploadService 实例并上传图片
                if (baseCode != null) {
                    sendChatRequest(baseCode)
                }


            } catch (e: Exception) {
                Log.e(TAG, "on uploadImage: error $e")
            } finally {


            }
        }
    }


    private fun checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {


            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                PERMISSION_REQUEST_CODE
            )
        } else {
            // 权限已获得，可以执行相关操作
            // 比如打开图库或执行上传操作
        }
    }

    private fun getRealPathFromURI(contentUri: Uri): String? {
        var cursor: Cursor? = null
        return try {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            cursor = contentResolver.query(contentUri, projection, null, null, null)
            val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor?.moveToFirst()
            Log.d(TAG, "getRealPathFromURI: ${cursor?.getString(columnIndex ?: 0)}")
            cursor?.getString(columnIndex ?: 0) // return the real path
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            cursor?.close()
        }
    }


    private fun sendChatRequest(image64code: String) {
        val service = ChatService()

        val request = ChatCompletionRequest(
            model = "gpt-4o-mini",
            messages = listOf(
                Message(
                    role = "system",
                    content = arrayOf(
                        mapOf(
                            "type" to "text",
                            "text" to " Obtain the electrical appliance in the picture, and tell me: 1.the name of the appliance; 2. The accurate assessment monthly electricity consumption; 3. The power load of the appliance; If it is not an appliance, just return the string \"Please obtain a clearer photo of the appliance.\""
                        )
                    )
                ),
                Message(
                    role = "user",
                    content = arrayOf(
                        mapOf(
                            "type" to "image_url",
                            "image_url" to mapOf("url" to "data:image/jpeg;base64,$image64code")
                        )
                    )
                )
            ),
            max_tokens = 180
        )
        service.createChatCompletion(request) { response, error ->
            if (error != null) {
                // 处理错误
                Log.e(TAG, "send Request error: $error")
                return@createChatCompletion // 直接返回，避免继续执行


            } else {
                var result:String? = null

                println("Response: $response")
                Log.d(TAG, "Response: $response")

                try {
                    val chatCompletionResponse: ChatCompletionResponse? = response
                    val choices = chatCompletionResponse!!.choices

                    if (choices.isNotEmpty()) {
                        val firstChoice = choices[0]
                        val message = firstChoice.message
                        val content = message.content

                        result = content.toString() // get the response content from the http response
                        Log.d(TAG, "Content: $content")
                    }
                } catch (e: Exception) {
                    Log.w(TAG, "on parse response: $e")
                }

                //back to main activity
                try {
                    val intent = Intent(this@UploadActivity, MainActivity::class.java)
                    intent.putExtra("result", result)
                    setResult(RESULT_OK, intent)
                    startActivity(intent)

                } catch (e: java.lang.Exception) {
                    Log.e(TAG, "onClick: $e")
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }
}


//    response = client.chat.completions.create(
//    model="gpt-4o-mini",
//    messages=[
//    {
//        "role": "user",
//        "content": [
//        {
//            "type": "text",
//            "text": "what is this image"
//        }
//        ]
//    },
//    {
//        "role": "user",
//        "content": [
//        {
//            "type": "image_url",
//            "image_url": {
//            "url": "data:image/jpeg;base64,{BASE64CODE}"
//        }
//        }
//        ]
//    }
//    ],
//    temperature=1.09,
//    max_tokens=2280,
//    top_p=1,
//    frequency_penalty=0,
//    presence_penalty=0
//    )
