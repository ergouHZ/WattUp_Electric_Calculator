package com.example.wattup.utils

import android.util.Log
import com.example.wattup.entity.ChatCompletionRequest
import com.example.wattup.entity.ChatCompletionResponse
import com.example.wattup.entity.Content
import com.example.wattup.entity.ImageUrl
import com.example.wattup.entity.Message
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ChatApiService {
    @Headers("Content-Type: application/json")
    @POST("chat/completions")
    fun createChatCompletion(
        @Header("Authorization") apiKey: String, // 添加API Key的Header
        @Body request: ChatCompletionRequest
    ): Call<ChatCompletionResponse>
}

class ChatService {
    private val TAG = "ChatService"

    private val retrofit: Retrofit
    private val chatApiService: ChatApiService


    init {
        // 设置OkHttpClient
        val client = OkHttpClient.Builder()
            .build()

        // 初始化Retrofit
        retrofit = Retrofit.Builder()
            .baseUrl("https://api.openai.com/v1/") // 根据实际API替换
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        chatApiService = retrofit.create(ChatApiService::class.java)
    }


    fun createChatCompletion(request: ChatCompletionRequest, callback: (ChatCompletionResponse?, Throwable?) -> Unit) {
        val openAiKeyInThis = "REPLACE TO YOUR KEY"
        val apikey = "Bearer $openAiKeyInThis"
        Log.d(TAG, "request: $request")

        chatApiService.createChatCompletion(apikey,request).enqueue(object : retrofit2.Callback<ChatCompletionResponse> {

            override fun onResponse(call: Call<ChatCompletionResponse>, response: retrofit2.Response<ChatCompletionResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Exception("Error: ${response.errorBody()?.string()}"))
                }
            }

            override fun onFailure(call: Call<ChatCompletionResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }




}