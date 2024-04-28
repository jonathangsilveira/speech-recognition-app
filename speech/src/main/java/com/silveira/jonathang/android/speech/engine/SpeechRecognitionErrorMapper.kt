package com.silveira.jonathang.android.speech.engine

import android.speech.SpeechRecognizer
import com.silveira.jonathang.android.speech.recognizer.SpeechRecognitionError

object SpeechRecognitionErrorMapper {

    fun map(code: Int) : SpeechRecognitionError {
        return when (code) {

            SpeechRecognizer.ERROR_AUDIO ->
                SpeechRecognitionError.AUDIO

            SpeechRecognizer.ERROR_CLIENT ->
                SpeechRecognitionError.CLIENT

            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS ->
                SpeechRecognitionError.INSUFFICIENT_PERMISSIONS

            SpeechRecognizer.ERROR_LANGUAGE_NOT_SUPPORTED ->
                SpeechRecognitionError.LANGUAGE_NOT_SUPPORTED

            SpeechRecognizer.ERROR_LANGUAGE_UNAVAILABLE ->
                SpeechRecognitionError.LANGUAGE_UNAVAILABLE

            SpeechRecognizer.ERROR_NETWORK ->
                SpeechRecognitionError.NETWORK

            SpeechRecognizer.ERROR_NETWORK_TIMEOUT ->
                SpeechRecognitionError.NETWORK_TIMEOUT

            SpeechRecognizer.ERROR_NO_MATCH ->
                SpeechRecognitionError.NO_MATCH

            SpeechRecognizer.ERROR_RECOGNIZER_BUSY ->
                SpeechRecognitionError.RECOGNIZER_BUSY

            SpeechRecognizer.ERROR_SERVER ->
                SpeechRecognitionError.SERVER

            SpeechRecognizer.ERROR_SERVER_DISCONNECTED ->
                SpeechRecognitionError.SERVER_DISCONNECTED

            SpeechRecognizer.ERROR_SPEECH_TIMEOUT ->
                SpeechRecognitionError.SPEECH_TIMEOUT

            else -> SpeechRecognitionError.OTHER
        }
    }
}