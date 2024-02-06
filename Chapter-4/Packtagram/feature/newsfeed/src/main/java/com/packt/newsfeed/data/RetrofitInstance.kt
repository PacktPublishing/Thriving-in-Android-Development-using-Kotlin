package com.packt.newsfeed.data

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://packtagram.com/"

    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    fun getNewsFeedApi(): NewsFeedService = run {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(NewsFeedService::class.java)
    }
}
