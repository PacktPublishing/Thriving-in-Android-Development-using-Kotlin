package com.packt.newsfeed.domain

interface NewsFeedRepository {
    suspend fun getNewsFeed():List<Post>
    fun resetPagination()
}