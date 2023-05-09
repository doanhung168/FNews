package com.poly_team.fnews.view.home


import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent.*
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.poly_team.fnews.CHANNEL_ID
import com.poly_team.fnews.R
import com.poly_team.fnews.data.local.AppDatabase
import com.poly_team.fnews.data.model.News
import com.poly_team.fnews.utility.MyTextToSpeech
import dagger.hilt.android.AndroidEntryPoint
import org.jsoup.Jsoup
import javax.inject.Inject

@AndroidEntryPoint
class ReadNewsService() : Service() {

    companion object {
        const val START_READ_NEWS_SERVICE = "com.poly_team.fnews.reading_news_service"
        const val NOTIFICATION_ID = 1

        const val PLAY_ACTION = "com.poly_team.fnews.player_reading_news_action"
        const val LOOP_ACTION = "com.poly_team.fnews.loop_reading_news_action"
        const val STOP_ACTION = "com.poly_team.fnews.stop_reading_news_action"
        const val NEXT_ACTION = "com.poly_team.fnews.next_reading_news_action"
        const val PREVIOUS_ACTION = "com.poly_team.fnews.previous_reading_news_action"
        const val UPDATE_STATE = "com.poly_team.fnews.update_state"
        const val REQUEST_UPDATE_STATE = "com.poly_team.fnews.request_update_state"

        const val TITLE_KEY = "title"
        const val LOOP_KEY = "loop"
        const val PLAY_KEY = "play"
        const val SER_RUNNING_KEY = "service running"

        const val CURRENT_ITEM = "current_item"
    }

    private val TAG = "ReadNewsService"

    @Inject
    lateinit var mAppDatabase: AppDatabase

    private lateinit var mNewsList: ArrayList<News>
    private var mCurrentPos = -1
    private var mCurrentNewsId = ""

    private lateinit var notificationManager: NotificationManager
    private lateinit var controlPlayIntent: Intent
    private lateinit var stopIntent: Intent
    private lateinit var loopIntent: Intent
    private lateinit var nextIntent: Intent
    private lateinit var previousIntent: Intent

    private var mTextToSpeech: MyTextToSpeech? = null
    private var mTextToSpeechData: String? = null

    private var mIsPlaying = false
    private var mIsLoop = false

    private lateinit var mThread: Thread
    private lateinit var mHandler: Handler

    private lateinit var mReadNewsServiceReceiver: ReadNewsServiceReceiver


    override fun onCreate() {
        Log.i(TAG, "onCreate: ")
        super.onCreate()
        mNewsList = ArrayList()
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        controlPlayIntent = Intent(PLAY_ACTION)
        stopIntent = Intent(STOP_ACTION)
        loopIntent = Intent(LOOP_ACTION)
        nextIntent = Intent(NEXT_ACTION)
        previousIntent = Intent(PREVIOUS_ACTION)

        mTextToSpeech = MyTextToSpeech()
        mTextToSpeech?.addOnSpeakDoneListener {
            if (mIsLoop) {
                Log.i(TAG, "onCreate: start speaking news again")
                startSpeech(mNewsList[mCurrentPos])
            } else {
                Log.i(TAG, "onCreate: speech next news")
                handleNextAction()
            }
        }
        mHandler = Handler(Looper.getMainLooper())

        mReadNewsServiceReceiver = ReadNewsServiceReceiver()
        val intentFilter = IntentFilter().apply {
            addAction(STOP_ACTION)
            addAction(LOOP_ACTION)
            addAction(NEXT_ACTION)
            addAction(PREVIOUS_ACTION)
            addAction(PLAY_ACTION)
        }
        registerReceiver(mReadNewsServiceReceiver, intentFilter)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    @SuppressLint("RemoteViewLayout")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mThread = Thread {
            when (intent?.action) {
                START_READ_NEWS_SERVICE -> {
                    mTextToSpeech?.destroyTTS()
                    Log.i(TAG, "onStartCommand: start news with $intent")
                    val newsId = intent.getStringExtra(CURRENT_ITEM)
                    newsId?.let {
                        mCurrentNewsId = it
                        val newsList = mAppDatabase.newsDao().getAll()
                        mNewsList = newsList as ArrayList<News>
                        mCurrentPos = mNewsList.indexOf(mNewsList.first {
                            it.id == mCurrentNewsId
                        })
                        val currentNews = mNewsList[mCurrentPos]
                        Log.i(TAG, "onStartCommand: stat with news: ${currentNews}}")
                        startSpeech(currentNews)
                        mIsPlaying = true
                        mIsLoop = false
                        updateState(true)
                        postNotification()
                    }
                }

                PLAY_ACTION -> {
                    if (mIsPlaying) {
                        mTextToSpeech?.pause()
                        mIsPlaying = false
                        Log.i(TAG, "onStartCommand: paused speech")
                    } else {
                        mTextToSpeech?.resume()
                        mIsPlaying = true
                        Log.i(TAG, "onStartCommand: resumed speech")
                    }
                    updateState(true)
                    postNotification()
                }

                STOP_ACTION -> {
                    Log.i(TAG, "onStartCommand: stop speech")
                    mTextToSpeech?.destroyTTS()
                    mIsPlaying = false
                    mIsLoop = false
                    updateState(false)
                    stopSelf()
                }

                LOOP_ACTION -> {
                    if (mIsLoop) {
                        mIsLoop = false
                        Log.i(TAG, "onStartCommand: unLoop")
                    } else {
                        mIsLoop = true
                        Log.i(TAG, "onStartCommand: loop")
                    }
                    updateState(true)
                    postNotification()
                }

                NEXT_ACTION -> {
                    handleNextAction()
                }

                PREVIOUS_ACTION -> {
                    Log.i(TAG, "onStartCommand: previous action")
                    mIsPlaying = true
                    if (mCurrentPos > 0) {
                        mCurrentPos--
                    } else {
                        mCurrentPos = mNewsList.size - 1
                    }
                    startSpeech(mNewsList[mCurrentPos])
                    updateState(true)
                    postNotification()
                }

                REQUEST_UPDATE_STATE -> {
                    Log.i(TAG, "onStartCommand: update state")
                    updateState(true)
                }
            }
        }
        mThread.start()
        return START_STICKY
    }

    private fun postNotification() {
        val remoteViews = RemoteViews(packageName, R.layout.reading_news_notification)
        remoteViews.setTextViewText(R.id.tvTitle, mNewsList[mCurrentPos].title)

        if (mIsPlaying) {
            remoteViews.setImageViewResource(R.id.imvPlay, R.drawable.ic_pause_circle_outline)
        } else {
            remoteViews.setImageViewResource(R.id.imvPlay, R.drawable.ic_play_circle_outline)
        }

        if (mIsLoop) {
            remoteViews.setImageViewResource(R.id.imvLoop, R.drawable.ic_repeat_one)
        } else {
            remoteViews.setImageViewResource(R.id.imvLoop, R.drawable.ic_repeat_more)
        }

        val playIntent = getBroadcast(this, 0, Intent(PLAY_ACTION), FLAG_MUTABLE)
        val stopIntent = getBroadcast(this, 1, stopIntent, FLAG_MUTABLE)
        val loopIntent = getBroadcast(this, 2, loopIntent, FLAG_MUTABLE)
        val nextIntent = getBroadcast(this, 3, nextIntent, FLAG_MUTABLE)
        val previousIntent = getBroadcast(this, 4, previousIntent, FLAG_MUTABLE)

        remoteViews.setOnClickPendingIntent(R.id.imvPlay, playIntent)
        remoteViews.setOnClickPendingIntent(R.id.imvLoop, loopIntent)
        remoteViews.setOnClickPendingIntent(R.id.imvStop, stopIntent)
        remoteViews.setOnClickPendingIntent(R.id.imvNext, nextIntent)
        remoteViews.setOnClickPendingIntent(R.id.imvPrevious, previousIntent)

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background).setContent(remoteViews)

        val notification: Notification = builder.build()

        mHandler.postDelayed({
            startForeground(NOTIFICATION_ID, notification)
        }, 0)
    }

    private fun startSpeech(news: News) {
        val content = getSpeechData(news)
        mTextToSpeech?.speakText(this, content)
    }

    private fun getSpeechData(news: News): String {
        val content = Jsoup.parse("<h4>${news.title!!}.</h4>" + news.content, "UTF-8").text()
        mTextToSpeechData = content
        Log.i(
            TAG,
            "setCurrentNews: textSpeechData: $content"
        )
        return content
    }

    private fun updateState(running: Boolean) {
        val intent = Intent(UPDATE_STATE)
        val bundle = Bundle().apply {
            putString(TITLE_KEY, mNewsList[mCurrentPos].title)
            putBoolean(PLAY_KEY, mIsPlaying)
            putBoolean(LOOP_KEY, mIsLoop)
            putBoolean(SER_RUNNING_KEY, running)
        }
        intent.putExtras(bundle)
        sendBroadcast(intent)
    }

    private fun handleNextAction() {
        Log.i(TAG, "onStartCommand: next action")
        mIsPlaying = true
        if (mCurrentPos >= mNewsList.size - 1) {
            mCurrentPos = 0
        } else {
            mCurrentPos++
        }
        startSpeech(mNewsList[mCurrentPos])
        updateState(true)
        postNotification()
    }


    override fun onDestroy() {
        Log.i(TAG, "onDestroy: ")
        super.onDestroy()
        mTextToSpeech?.destroyTTS()
        mTextToSpeech = null
        notificationManager.cancel(NOTIFICATION_ID)
        unregisterReceiver(mReadNewsServiceReceiver)
    }
}

class ReadNewsServiceReceiver : BroadcastReceiver() {

    private val TAG = "ReadNewsService"

    override fun onReceive(context: Context, intent: Intent) {
        Log.i(TAG, "onReceive: action: ${intent.action}")
        val newIntent = Intent(context, ReadNewsService::class.java)
        newIntent.action = intent.action
        newIntent.setPackage(context.packageName)
        context.startService(newIntent)
    }
}