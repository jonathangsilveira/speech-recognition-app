package com.silveira.jonathang.android.speechrecognition

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.silveira.jonathang.android.speech.engine.NativeSpeechRecognitionEngine
import com.silveira.jonathang.android.speech.engine.SpeechRecognitionEngine
import com.silveira.jonathang.android.speech.recognizer.SpeechRecognitionError
import com.silveira.jonathang.android.speech.recognizer.SpeechRecognitionListener
import com.silveira.jonathang.android.speechrecognition.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), SpeechRecognitionListener {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val speechRecognitionEngine: SpeechRecognitionEngine by lazy {
        NativeSpeechRecognitionEngine()
    }

    private val recordAudioPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            speak()
        } else {
            requestPermissionToRecordAudio()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.speechMicButton.setOnClickListener {
            onSpeakButtonClicked()
        }
        speechRecognitionEngine.loadLanguageDetails(this)
    }

    override fun onStop() {
        super.onStop()
        stopSpeaking()
    }

    override fun onDestroy() {
        runCatching {
            speechRecognitionEngine.shutdown()
        }.onFailure {
            log(message = "Error on shutdown engine!", cause = it)
        }
        recordAudioPermissionLauncher.unregister()
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if ((grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED)
            ) {
                speak()
            }
        }
    }

    override fun onSpeechReady() {
        updateLog("Ready to speech...")
    }

    override fun onSpeechStarted() {
        updateLog("User started to speech...")
    }

    override fun onSpeechEnded() {
        updateLog("User has ended to speech!")
        stopSpeaking()
    }

    override fun onSpeechError(cause: SpeechRecognitionError) {
        updateLog("Error on speech: ${cause.name}")
        stopSpeaking()
    }

    override fun onSpeechResult(resultAsText: String?) {
        if (resultAsText.isNullOrEmpty()) return
        with(binding.speechEditText) {
            append(" $resultAsText")
            val lenght = text?.toString().orEmpty().length
            setSelection(lenght)
        }
        stopSpeaking()
    }

    override fun onSpeechPartialResults(text: String?) = Unit

    private fun checkRecordAudioPermission() {
        recordAudioPermissionLauncher.launch(
            Manifest.permission.RECORD_AUDIO
        )
    }

    private fun speak() {
        binding.speechMicButton.setImageResource(
            R.drawable.baseline_mic_24
        )
        runCatching {
            speechRecognitionEngine.startListening(this, this)
        }.onFailure {
            log(message = "Error on start listening!", cause = it)
        }
    }

    private fun stopSpeaking() {
        binding.speechMicButton.setImageResource(
            R.drawable.baseline_mic_none_24
        )
        runCatching {
            speechRecognitionEngine.stopListening()
        }.onFailure {
            log(message = "Error on stop listening!", cause = it)
        }
    }

    private fun onSpeakButtonClicked() {
        val isSpeaking = speechRecognitionEngine.isListening
        if (isSpeaking) {
            stopSpeaking()
        } else {
            checkRecordAudioPermission()
        }
    }

    private fun requestPermissionToRecordAudio() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(Manifest.permission.RECORD_AUDIO),
                100
            )
        }
    }

    private fun updateLog(message: String?) {
        binding.speechLogText.text = message
    }

    private fun log(message: String, cause: Throwable? = null) {
        Log.d("SPEECH_RECOGNITION", message, cause)
    }
}