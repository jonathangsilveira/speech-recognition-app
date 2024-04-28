package com.silveira.jonathang.android.speech.recognizer

interface SpeechRecognitionListener {
    fun onSpeechReady()
    fun onSpeechStarted()
    fun onSpeechEnded()
    fun onSpeechError(cause: SpeechRecognitionError)
    fun onSpeechResult(resultAsText: String?)
    fun onSpeechPartialResults(text: String?)
}