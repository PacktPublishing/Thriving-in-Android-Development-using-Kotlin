package com.packt.newsfeed.domain

class GetTheNewsFeedUseCase(
    private val repository: NewsFeedRepository
) {
    suspend operator fun invoke(fromTheBeginning: Boolean): List<Post> {
        return try {
            if (fromTheBeginning) {
                repository.resetPagination()
            }
            repository.getNewsFeed()
        } catch (throwable: Throwable) {
            emptyList<Post>()
        }
    }
}
