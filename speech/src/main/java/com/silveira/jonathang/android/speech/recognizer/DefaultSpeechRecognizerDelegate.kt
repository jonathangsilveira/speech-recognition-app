package com.silveira.jonathang.android.speech.recognizer

import android.content.Context
import android.content.Intent
import android.speech.RecognitionListener
import android.speech.RecognizerIntent.ACTION_RECOGNIZE_SPEECH
import android.speech.RecognizerIntent.EXTRA_LANGUAGE
import android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL
import android.speech.RecognizerIntent.EXTRA_MAX_RESULTS
import android.speech.RecognizerIntent.EXTRA_PARTIAL_RESULTS
import android.speech.RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES
import android.speech.SpeechRecognizer

class DefaultSpeechRecognizerDelegate: SpeechRecognizerDelegate {

    private var recognizer: SpeechRecognizer? = null

    override fun start(
        context: Context,
        configuration: SpeechRecognitionConfiguration,
        listener: RecognitionListener
    ) {
        val newRecognizer = SpeechRecognizer
            .createSpeechRecognizer(context)
        newRecognizer.run {
            setRecognitionListener(listener)
            startListening(
                createIntent(configuration)
            )
        }
        recognizer = newRecognizer
    }

    override fun stop() {
        recognizer?.stopListening()
    }

    override fun destroy() {
        recognizer?.destroy()
        recognizer = null
    }

    override fun isAvailable(
        context: Context
    ): Boolean = SpeechRecognizer.isRecognitionAvailable(context)

    private fun createIntent(
        configuration: SpeechRecognitionConfiguration
    ): Intent =
        Intent(ACTION_RECOGNIZE_SPEECH)
            .putExtra(EXTRA_MAX_RESULTS, configuration.maxResultsCount)
            .putExtra(EXTRA_PARTIAL_RESULTS, configuration.allowPartialResults)
            .putExtra(EXTRA_LANGUAGE, configuration.languagePreference)
            .putExtra(EXTRA_SUPPORTED_LANGUAGES, arrayOf(configuration.supportedLanguages))
            .putExtra(EXTRA_LANGUAGE_MODEL, configuration.languageModel)

}