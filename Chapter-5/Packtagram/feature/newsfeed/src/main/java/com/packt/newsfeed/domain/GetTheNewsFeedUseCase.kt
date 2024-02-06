package com.packt.newsfeed.domain

class GetTheNewsFeedUseCase(
    private val repository: NewsFeedRepository
) {
    suspend operator fun invoke(fromTheBeginning: Boolean): List<Post> {
        if (fromTheBeginning) {
            repository.getNewsFeed()
        }
        return repository.getNewsFeed()
    }
}