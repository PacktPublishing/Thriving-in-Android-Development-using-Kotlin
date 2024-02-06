package com.packt.newsfeed.data

import com.packt.newsfeed.data.model.PostApiData

class NewsFeedRemoteDataSource(private val api: NewsFeedService) {
    suspend fun getNewsFeed(pageNumber: Int, pageSize: Int): List<PostApiData> {
        return api.getNewsFeed(pageNumber, pageSize)
    }
}