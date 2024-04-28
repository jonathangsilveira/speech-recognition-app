package com.silveira.jonathang.android.speech.recognizer

import android.content.Context
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import android.speech.SpeechRecognizer.createSpeechRecognizer
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.verify
import org.junit.Test

class DefaultSpeechRecognitionDelegateTest {

    private val context = mockk<Context>()

    private val recognizer = mockk<SpeechRecognizer>()

    private val listener = mockk<RecognitionListener>()

    private val delegate = DefaultSpeechRecognizerDelegate()

    private val config = SpeechRecognitionConfiguration(
        supportedLanguages = listOf(),
        allowPartialResults = true,
        maxResultsCount = 1,
        languagePreference = ""
    )

    @Test
    fun `start Should do something When something happens`() {
        // Given
        mockkStatic(SpeechRecognizer::class) {
            every { createSpeechRecognizer(context) } returns recognizer
        }
        every { recognizer.setRecognitionListener(listener) } just runs
        every { recognizer.startListening(any()) } just runs

        // When
        delegate.start(context, config, listener)

        // then
        verify {
            recognizer.startListening(any())
        }
    }
}