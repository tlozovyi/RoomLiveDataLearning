package com.levor.roomwordssample

import android.app.Application
import com.facebook.stetho.Stetho

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        Stetho.initializeWithDefaults(this)
    }

    companion object {
        lateinit var instance: App
    }
}