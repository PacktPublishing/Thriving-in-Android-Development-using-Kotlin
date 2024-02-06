package com.packt.newsfeed.data

import com.packt.newsfeed.data.model.PostApiData
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsFeedService {
    @GET("feed")
    suspend fun getNewsFeed(
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int
    ): List<PostApiData>
}