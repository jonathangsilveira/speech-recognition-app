package com.silveira.jonathang.android.speech.languages

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.core.os.bundleOf
import java.util.Locale

private const val PREFERRED_LANGUAGE_KEY = RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE

private const val SUPPORTED_LANGUAGES_KEY = RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES

typealias OnLanguageDetailsReceived = (
    preferredLanguage: String,
    supportedLanguages: List<String>
) -> Unit

internal class GetLanguageDetailsBroadcastReceiver(
    private val onReceived: OnLanguageDetailsReceived
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val extras = getResultExtras(true)
        val preferredLanguage = extras.getString(PREFERRED_LANGUAGE_KEY)
        val supportedLanguages = extras
            .getStringArrayList(SUPPORTED_LANGUAGES_KEY)
            ?: listOf<String>()
        if (preferredLanguage.isNullOrEmpty()) return
        onReceived(
            preferredLanguage,
            supportedLanguages
        )
    }
}