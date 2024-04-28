package com.silveira.jonathang.android.speech.engine

import android.content.Context
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import com.silveira.jonathang.android.speech.languages.registerForLanguageDetails
import com.silveira.jonathang.android.speech.recognizer.DefaultSpeechRecognizerDelegate
import com.silveira.jonathang.android.speech.recognizer.SpeechRecognitionConfiguration
import com.silveira.jonathang.android.speech.recognizer.SpeechRecognitionListener
import com.silveira.jonathang.android.speech.recognizer.SpeechRecognizerDelegate
import java.util.Locale

class NativeSpeechRecognitionEngine(
    private val recognizerDelegate: SpeechRecognizerDelegate =
        DefaultSpeechRecognizerDelegate()
) : SpeechRecognitionEngine, RecognitionListener {

    override val isListening: Boolean
        get() = _isListening

    private var _isListening = false

    private var recognitionListener: SpeechRecognitionListener? = null

    private var recognitionConfiguration = SpeechRecognitionConfiguration(
        maxResultsCount = 1,
        allowPartialResults = true,
        languagePreference = Locale.getDefault().toString(),
        supportedLanguages = listOf()
    )

    override fun startListening(
        context: Context,
        listener: SpeechRecognitionListener?
    ) {
        if (!isAvailable(context)) {
            error("Speech recognition is not available on your device!")
        }
        if (isListening) {
            stopListening()
        }
        recognitionListener = listener
        recognizerDelegate.start(context, recognitionConfiguration, this)
        _isListening = true
    }

    override fun stopListening() {
        if (!isListening) return
        recognizerDelegate.stop()
        _isListening = false
    }

    override fun shutdown() {
        stopListening()
        recognizerDelegate.destroy()
    }

    override fun isAvailable(
        context: Context
    ): Boolean = recognizerDelegate.isAvailable(context)

    override fun loadLanguageDetails(context: Context) {
        context.registerForLanguageDetails { preferredLanguage, supportedLanguages ->
            updateConfiguration(preferredLanguage, supportedLanguages)
        }
    }

    override fun onReadyForSpeech(params: Bundle?) {
        recognitionListener?.onSpeechReady()
    }

    override fun onBeginningOfSpeech() {
        recognitionListener?.onSpeechStarted()
    }

    override fun onEndOfSpeech() {
        recognitionListener?.onSpeechEnded()
    }

    override fun onError(error: Int) {
        val cause = SpeechRecognitionErrorMapper.map(error)
        recognitionListener?.onSpeechError(cause)
    }

    override fun onResults(results: Bundle?) {
        val recognitionResults = results
            ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        val bestResult = recognitionResults?.firstOrNull()
        recognitionListener?.onSpeechResult(bestResult)
    }

    override fun onPartialResults(results: Bundle?) {
        val recognitionResults = results
            ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        val bestResult = recognitionResults?.firstOrNull()
        recognitionListener?.onSpeechPartialResults(bestResult)
    }

    override fun onRmsChanged(rmsdB: Float) = Unit

    override fun onBufferReceived(buffer: ByteArray?) = Unit

    override fun onEvent(eventType: Int, params: Bundle?) = Unit

    private fun updateConfiguration(
        preferredLanguage: String,
        supportedLanguages: List<String>
    ) {
        recognitionConfiguration = recognitionConfiguration.copy(
            languageModel = preferredLanguage,
            supportedLanguages = supportedLanguages
        )
    }
}