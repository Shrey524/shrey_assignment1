package com.example.myapplication

import android.app.Application
import com.airbnb.mvrx.Mavericks
import com.airbnb.mvrx.Mavericks.viewModelConfigFactory
import com.airbnb.mvrx.MavericksViewModelConfigFactory

class BaseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        viewModelConfigFactory = MavericksViewModelConfigFactory(applicationContext)
        // Initialize Mavericks
        Mavericks.initialize(applicationContext,viewModelConfigFactory)
    }
}