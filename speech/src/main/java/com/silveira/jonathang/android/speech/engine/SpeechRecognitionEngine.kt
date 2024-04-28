package com.silveira.jonathang.android.speech.engine

import android.content.Context
import com.silveira.jonathang.android.speech.recognizer.SpeechRecognitionListener

interface SpeechRecognitionEngine {
    val isListening: Boolean

    fun startListening(
        context: Context,
        listener: SpeechRecognitionListener? = null
    )

    fun stopListening()

    fun shutdown()

    fun isAvailable(context: Context): Boolean

    fun loadLanguageDetails(context: Context)
}