package com.poly_team.fnews.utility

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import java.util.*
import kotlin.collections.ArrayList


const val TAG = "MyTextToSpeech"

class MyTextToSpeech {

    private var mTTS: TextToSpeech? = null
    private var mTextToSpeechList = emptyList<String>()
    private var index = -1
    private var doneIndex = 0

    private lateinit var mOnSpeakDone: () -> Unit

    private var isPrepared = false

    fun addOnSpeakDoneListener(onSpeakDone: () -> Unit) {
        mOnSpeakDone = onSpeakDone
    }

    fun speakText(context: Context, textToSpeak: String) {
        mTTS?.stop()
        if(isPrepared) {
            play(textToSpeak)
        } else {
            mTTS = TextToSpeech(context) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    Log.i(TAG, "initializeTTS: success")
                    val result = mTTS!!.setLanguage(Locale.forLanguageTag("vi-VN"))
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                        result == TextToSpeech.LANG_NOT_SUPPORTED
                    ) {
                        Log.e(TAG, "Vietnamese language not supported")
                    }
                    mTTS?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {

                        override fun onStart(p0: String?) {
                            Log.i(TAG, "onStart: $p0")
                        }

                        override fun onDone(p0: String?) {
                            Log.i(TAG, "onDone: $p0")
                            doneIndex = p0?.toInt()!!
                            if(doneIndex == mTextToSpeechList.size - 1) {
                                mOnSpeakDone()
                            }
                        }

                        override fun onError(p0: String?) {
                            Log.e(TAG, "onError: $p0")
                        }

                    })
                    play(textToSpeak)
                    isPrepared = true
                } else {
                    Log.e(TAG, "Initialization failed")
                }
            }
        }
    }

    private fun play(textToSpeak: String) {
        index = 0
        mTextToSpeechList = textToSpeak.split(delimiters = arrayOf(".", ","))
        Log.i(TAG, "speakText: $mTextToSpeechList")

        while (true) {
            val data = mTextToSpeechList[index]
            Log.i(TAG, "add: $data")
            mTTS?.speak(data, TextToSpeech.QUEUE_ADD, null, index.toString())
            if(index == mTextToSpeechList.size - 1) {
                break
            }
            index++
        }
    }

    fun pause() {
        mTTS?.stop()
    }

    fun resume() {
        if(doneIndex == -1) {
            Log.i(TAG, "speak done all or not yet start")
        }
        val nextIndex = doneIndex + 1
        index = nextIndex
        while (true) {
            val data = mTextToSpeechList[index]
            Log.i(TAG, "add: $data")
            mTTS?.speak(data, TextToSpeech.QUEUE_ADD, null, index.toString())
            if(index == mTextToSpeechList.size - 1) {
                break
            }
            index++
        }
    }

    fun destroyTTS() {
        mTTS?.stop()
        mTTS?.shutdown()
        isPrepared = false
    }
}