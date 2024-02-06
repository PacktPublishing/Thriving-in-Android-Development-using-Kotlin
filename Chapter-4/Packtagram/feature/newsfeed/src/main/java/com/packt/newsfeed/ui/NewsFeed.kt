package com.packt.newsfeed.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel

@Composable
fun NewsFeed(
    modifier: Modifier = Modifier,
    viewModel: NewsFeedViewModel = koinViewModel()
) {
    val posts = viewModel.posts.collectAsState()
    val listState = rememberLazyListState()
    LazyColumn{
        itemsIndexed(posts.value){ _, post ->
            PostItem(post = post)
        }
    }

    listState.OnBottomReached {
        viewModel.loadMorePosts()
    }
}

@Composable
fun LazyListState.OnBottomReached(
    loadMore : () -> Unit
){
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastItemInView = layoutInfo.visibleItemsInfo.lastOrNull()
                ?:
                return@derivedStateOf true

            lastItemInView.index == layoutInfo.totalItemsCount - 1
        }
    }
    LaunchedEffect(shouldLoadMore){
        snapshotFlow { shouldLoadMore.value }
            .collect {
                if (it) loadMore()
            }
    }

}