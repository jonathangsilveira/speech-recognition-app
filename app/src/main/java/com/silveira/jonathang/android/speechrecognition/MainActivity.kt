package com.silveira.jonathang.android.speechrecognition

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.silveira.jonathang.android.speechrecognition.databinding.ActivityMainBinding
import com.silveira.jonathang.android.speechrecognition.recognizer.NativeSpeechRecognizer
import com.silveira.jonathang.android.speechrecognition.recognizer.SpeechRecognition

class MainActivity : AppCompatActivity(), RecognitionListener {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var speechRecognition: SpeechRecognition? = null

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
    }

    override fun onStart() {
        super.onStart()
        speechRecognition = NativeSpeechRecognizer()
    }

    override fun onStop() {
        super.onStop()
        stopSpeaking()
        speechRecognition = null
    }

    override fun onDestroy() {
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

    private fun checkRecordAudioPermission() {
        recordAudioPermissionLauncher.launch(
            Manifest.permission.RECORD_AUDIO
        )
    }

    private fun speak() {
        binding.speechMicButton.setImageResource(
            R.drawable.baseline_mic_24
        )
        speechRecognition?.startListening(this, this)
    }

    private fun stopSpeaking() {
        binding.speechMicButton.setImageResource(
            R.drawable.baseline_mic_none_24
        )
        speechRecognition?.stopListening()
    }

    private fun onSpeakButtonClicked() {
        val isSpeaking = speechRecognition?.isListening == true
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

    override fun onReadyForSpeech(params: Bundle?) {
        println("SPEECH_RECOGNITION -> onReadyForSpeech")
        updateLog("Ready for speeching")
    }

    override fun onBeginningOfSpeech() {
        println("SPEECH_RECOGNITION -> onBeginningOfSpeech")
        updateLog("Speeching")
    }

    override fun onRmsChanged(rmsdB: Float) {
        println("SPEECH_RECOGNITION -> onRmsChanged($rmsdB)")
    }

    override fun onBufferReceived(buffer: ByteArray?) {
        println("SPEECH_RECOGNITION -> onBufferReceived(${buffer})")
    }

    override fun onEndOfSpeech() {
        println("SPEECH_RECOGNITION -> onEndOfSpeech")
        updateLog("End of speech")
    }

    override fun onError(error: Int) {
        println("SPEECH_RECOGNITION -> onError(${error})")
        updateLog("Error $error")
        stopSpeaking()
    }

    override fun onResults(results: Bundle?) {
        val speechAsString = results
            ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            ?.joinToString(separator = " ")
            .orEmpty()
        println("SPEECH_RECOGNITION -> onResults(${speechAsString})")
        binding.speechEditText.setText(speechAsString)
    }

    override fun onPartialResults(partialResults: Bundle?) {
        println("SPEECH_RECOGNITION -> onPartialResults(${partialResults})")
        onResults(partialResults)
    }

    override fun onEvent(eventType: Int, params: Bundle?) {
        println("SPEECH_RECOGNITION -> onEvent(${eventType}, ${params})")
    }
}