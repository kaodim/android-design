package com.kaodim.myapplication

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize ThreeTenABP library
        AndroidThreeTen.init(this)
    }
}