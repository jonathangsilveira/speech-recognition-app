package com.silveira.jonathang.android.speech.languages

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent

fun Context.registerForLanguageDetails(
    onReceived: OnLanguageDetailsReceived
) {
    val receiver = GetLanguageDetailsBroadcastReceiver(onReceived)
    val intent = Intent(RecognizerIntent.ACTION_GET_LANGUAGE_DETAILS)
    sendOrderedBroadcast(
        intent,
        null,
        receiver,
        null,
        Activity.RESULT_OK,
        null,
        null
    )
}
