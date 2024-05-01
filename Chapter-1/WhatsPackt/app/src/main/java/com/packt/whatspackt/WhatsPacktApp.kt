package com.packt.whatspackt

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WhatsPacktApp: Application() {

    override fun onCreate() {
        super.onCreate()

    }
}