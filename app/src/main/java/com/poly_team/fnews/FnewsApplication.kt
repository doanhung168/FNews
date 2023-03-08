package com.poly_team.fnews

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FnewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()

    }
}