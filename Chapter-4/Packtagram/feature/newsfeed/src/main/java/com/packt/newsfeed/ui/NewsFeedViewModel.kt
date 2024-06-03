package com.packt.newsfeed.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.packt.newsfeed.domain.GetTheNewsFeedUseCase
import com.packt.newsfeed.domain.Post
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class NewsFeedViewModel(
    private val getTheNewsFeedUseCase: GetTheNewsFeedUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> get() = _posts

    init {
        loadInitialPosts()
    }

    private fun loadInitialPosts() {
        viewModelScope.launch {
            val newPosts = withContext(dispatcher) {
                getTheNewsFeedUseCase(fromTheBeginning = true)
            }
            _posts.value = newPosts
        }
    }

    fun loadMorePosts() {
        viewModelScope.launch {
            val newPosts = withContext(dispatcher) {
                getTheNewsFeedUseCase(fromTheBeginning = false)
            }
            val updatedPosts = (_posts.value + newPosts).takeLast(60)
            _posts.value = updatedPosts
        }
    }

    private fun generateFakePosts(): List<Post> {
        val users = listOf(
            Post.UserData(
                id = "user1",
                name = "Jane Doe",
                imageUrl = "https://ui-avatars.com/api/?name=Jane+Doe?rounded=true"
            ),
            Post.UserData(
                id = "user2",
                name = "John Smith",
                imageUrl = "https://ui-avatars.com/api/?name=John+Smith?rounded=true"
            ),
            Post.UserData(
                id = "user3",
                name = "Alice Johnson",
                imageUrl = "https://ui-avatars.com/api/?name=Alice+Johnson?rounded=true"
            ),
        )

        return listOf(
            Post(
                id = "post1",
                user = users[0],
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6a/Kitesurfer_at_sunset%2C_Workum%2C_may_2017.jpg/640px-Kitesurfer_at_sunset%2C_Workum%2C_may_2017.jpg",
                caption = "A beautiful sunset at the beach.",
                likesCount = Random.nextInt(100, 1000),
                commentsCount = Random.nextInt(10, 100),
                timeStamp = System.currentTimeMillis() - Random.nextLong(1000000, 10000000)
            ),
            Post(
                id = "post2",
                user = users[1],
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f7/Trekking_trail_of_Netravati_peak_from_the_summit_%28Zoomed_in%29.jpg/640px-Trekking_trail_of_Netravati_peak_from_the_summit_%28Zoomed_in%29.jpg",
                caption = "Enjoying the great outdoors with friends.",
                likesCount = Random.nextInt(100, 1000),
                commentsCount = Random.nextInt(10, 100),
                timeStamp = System.currentTimeMillis() - Random.nextLong(1000000, 10000000)
            ),
            Post(
                id = "post3",
                user = users[2],
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/9/91/Pizza-3007395.jpg/640px-Pizza-3007395.jpg",
                caption = "New recipe success! #homemadepizza",
                likesCount = Random.nextInt(100, 1000),
                commentsCount = Random.nextInt(10, 100),
                timeStamp = System.currentTimeMillis() - Random.nextLong(1000000, 10000000)
            ),
        )
    }
}
