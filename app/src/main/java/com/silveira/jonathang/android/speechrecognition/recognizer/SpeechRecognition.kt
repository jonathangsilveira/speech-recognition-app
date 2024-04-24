package com.silveira.jonathang.android.speechrecognition.recognizer

import android.content.Context
import android.speech.RecognitionListener

interface SpeechRecognition {
    val isListening: Boolean

    fun startListening(
        context: Context,
        listener: RecognitionListener? = null
    )

    fun stopListening()

    fun isRecognitionAvailable(context: Context): Boolean
}