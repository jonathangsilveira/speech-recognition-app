package com.silveira.jonathang.android.speechrecognition.recognizer

import android.content.Context
import android.content.Intent
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import java.util.Locale

class NativeSpeechRecognizer : SpeechRecognition {

    override val isListening: Boolean
        get() = _isListening

    private var _isListening = false

    private var speechRecognizer: SpeechRecognizer? = null

    private val defaultLocale = Locale("pt", "BR")

    private val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        .putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        .putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        .putExtra(RecognizerIntent.EXTRA_LANGUAGE, defaultLocale)
        .putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)

    override fun startListening(context: Context, listener: RecognitionListener?) {
        if (!isRecognitionAvailable(context)) {
            error("Speech recognition is not available on your device!")
        }
        speechRecognizer = SpeechRecognizer
            .createSpeechRecognizer(context)
            .also { recognizer ->
                recognizer.setRecognitionListener(listener)
                recognizer.startListening(recognizerIntent)
            }
        _isListening = true
    }

    override fun stopListening() {
        speechRecognizer?.run {
            stopListening()
            cancel()
            destroy()
        }
        _isListening = false
    }

    override fun isRecognitionAvailable(
        context: Context
    ): Boolean = SpeechRecognizer.isRecognitionAvailable(context)
}