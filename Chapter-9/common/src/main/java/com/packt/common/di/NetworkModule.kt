package com.packt.common.di

import android.content.Context
import com.packt.common.data.AuthInterceptor
import com.packt.common.domain.AuthTokenProvider
import com.packt.common.domain.LoginRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun authTokenProvider(@ApplicationContext context: Context): AuthTokenProvider {
        return AuthTokenProvider(context)
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(authTokenProvider: AuthTokenProvider): AuthInterceptor {
        return AuthInterceptor(authTokenProvider)
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        moshi: Moshi,
        authInterceptor: AuthInterceptor
    ): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://your.api.url/") // Replace with your actual base URL
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
    }

}