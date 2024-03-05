package com.packt.packtagram.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.MailOutline
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.packt.newsfeed.ui.NewsFeed
import com.packt.packtagram.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
){
    val tabs = generateTabs()
    val selectedIndex = remember { mutableStateOf(0) }
    val pagerState = rememberPagerState(initialPage = 0)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.app_name))
                },
                actions = {
                    IconButton(onClick = { /* Menu action */ }) {
                        Icon(Icons.Rounded.MailOutline, contentDescription = "Messages")
                    }
                }
            )
        },
        bottomBar = {
            TabRow(selectedTabIndex = selectedIndex.value) {
                tabs.forEachIndexed { index, _ ->
                    Tab(
                        icon = { Icon(tabs[index].icon, contentDescription = null) },
                        selected = index == selectedIndex.value,
                        onClick = {
                            selectedIndex.value = index
                        }
                    )
                }
            }
        },
        content = { innerPadding ->
            HorizontalPager(
                modifier = Modifier.padding(innerPadding),
                state = pagerState,
                pageCount = tabs.size
            ) { index ->
                when (index) {
                    0 -> {
                        NewsFeed()
                    }
                    1 -> {
                        //Search
                    }
                    2-> {
                        // New publication
                    }
                    3-> {
                        // Reels
                    }
                    4-> {
                        // Profile
                    }
                }
            }
            LaunchedEffect(selectedIndex.value) {
                pagerState.animateScrollToPage(selectedIndex.value)
            }
        },
    )

}

data class TabItem(
    val icon: ImageVector
)

fun generateTabs(): List<TabItem> {
    return listOf(
        TabItem(
            icon = Icons.Outlined.Home
        ),
        TabItem(
            icon = Icons.Outlined.Search
        ),
        TabItem(
            icon = Icons.Outlined.AddCircle
        ),
        TabItem(
            icon = Icons.Outlined.PlayArrow
        ),
        TabItem(
            icon = Icons.Outlined.Person
        )
    )
}