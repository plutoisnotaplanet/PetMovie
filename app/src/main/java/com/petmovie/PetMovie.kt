package com.petmovie

import android.app.Application
import com.petmovie.di.AppComponent

class PetMovie : Application() {

    val myComponent: AppComponent by lazy { AppComponent(this) }
}