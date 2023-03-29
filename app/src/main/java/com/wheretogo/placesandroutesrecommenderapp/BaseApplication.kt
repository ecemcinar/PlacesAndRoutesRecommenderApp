package com.wheretogo.placesandroutesrecommenderapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// dagger is compile time injection
@HiltAndroidApp
class BaseApplication: Application()  {

    override fun onCreate() {
        super.onCreate()
    }
}