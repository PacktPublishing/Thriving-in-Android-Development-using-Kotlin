package com.packt.packtagram

import android.app.Application
import com.packt.newsfeed.di.newsFeedModule
import com.packt.packtagram.di.appModule
import com.packt.stories.di.storyModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class PacktagramApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@PacktagramApplication)
            modules(
                appModule,
                storyModule,
                newsFeedModule
            )
        }
    }
}
