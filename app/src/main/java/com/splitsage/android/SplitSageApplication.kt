package com.splitsage.android

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class SplitSageApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        
        // Initialize logging in debug builds
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        
        // Initialize Firebase if configured
        // Firebase.initialize(this)
    }
}