package com.petmovie

import android.app.Application
import com.example.petmovie.BuildConfig
import com.petmovie.di.AppComponent
import timber.log.Timber

class PetMovie : Application() {

    override fun onCreate() {
        super.onCreate()

        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    val myComponent: AppComponent by lazy { AppComponent(this) }
}