package com.packt.domain.di

import com.packt.domain.user.GetUserData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class DomainModule {

    @Provides
    fun providesGetUserData(): GetUserData {
        return GetUserData()
    }
}