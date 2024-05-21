package com.packt.list.presentation.list

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PacktflixBottomBar() {
    NavigationBar (
        containerColor = Color.Black,
        contentColor = Color.White,
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            selected = false,
            onClick = { /* Handle Home navigation */ }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
            selected = false,
            onClick = { /* Handle Search navigation */ }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.ArrowDropDown, contentDescription = "Downloads") },
            selected = false,
            onClick = { /* Handle Downloads navigation */ }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.MoreVert, contentDescription = "More") },
            selected = false,
            onClick = { /* Handle More navigation */ }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun AppBottomNavigationPreview() {
    PacktflixBottomBar()
}
