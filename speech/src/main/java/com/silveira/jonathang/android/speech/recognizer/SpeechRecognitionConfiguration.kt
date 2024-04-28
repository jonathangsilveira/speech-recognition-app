package com.silveira.jonathang.android.speech.recognizer

import android.speech.RecognizerIntent

data class SpeechRecognitionConfiguration(
    val languagePreference: String,
    val supportedLanguages: List<String>,
    val maxResultsCount: Int,
    val allowPartialResults: Boolean,
    val languageModel: String = RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
)
