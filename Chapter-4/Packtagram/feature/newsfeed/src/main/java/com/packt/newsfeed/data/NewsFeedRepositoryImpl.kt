package com.packt.newsfeed.data

import com.packt.newsfeed.domain.NewsFeedRepository
import com.packt.newsfeed.domain.Post

class NewsFeedRepositoryImpl(
    private val remoteDataSource: NewsFeedRemoteDataSource
): NewsFeedRepository {

    private var currentPage = 0
    private val pageSize = 20  // Or whatever page size we prefer

    override suspend fun getNewsFeed(): List<Post> {
        return remoteDataSource
            .getNewsFeed(currentPage, pageSize)
            .map { it.toDomain() }
            .also { currentPage++ }
    }

    override fun resetPagination() {
        currentPage = 0
    }
}