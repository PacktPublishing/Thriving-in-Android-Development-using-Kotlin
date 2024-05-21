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
}

