package com.packt.newsfeed.data

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://packtagram.com/"

    fun getNewsFeedApi(): NewsFeedService = run {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(NewsFeedService::class.java)
    }
}
