package com.silveira.jonathang.android.speech.recognizer

enum class SpeechRecognitionError {
    NETWORK_TIMEOUT,
    NETWORK,
    AUDIO,
    SERVER,
    CLIENT,
    SPEECH_TIMEOUT,
    NO_MATCH,
    RECOGNIZER_BUSY,
    INSUFFICIENT_PERMISSIONS,
    SERVER_DISCONNECTED,
    LANGUAGE_NOT_SUPPORTED,
    LANGUAGE_UNAVAILABLE,
    OTHER
}