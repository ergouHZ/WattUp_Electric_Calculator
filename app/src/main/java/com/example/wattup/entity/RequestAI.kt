package com.example.wattup.entity

import androidx.annotation.AnyRes

data class ChatCompletionRequest(
    val model: String,
    val messages: List<Message>,
    val max_tokens: Int,
    val temperature: Float = 1.21f
)

data class Message(
    val role: String,
    val content: Any
)

data class Content(
    val type: String,
    val text: String? = null,
    val image_url: ImageUrl? = null
)

data class ImageUrl(
    val url: String
)

data class ChatCompletionResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: Message
)
