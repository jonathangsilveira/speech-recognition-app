package com.silveira.jonathang.android.speech.recognizer

import android.content.Context
import android.speech.RecognitionListener

interface SpeechRecognizerDelegate {
    fun start(
        context: Context,
        configuration: SpeechRecognitionConfiguration,
        listener: RecognitionListener
    )

    fun stop()

    fun destroy()

    fun isAvailable(context: Context): Boolean
}