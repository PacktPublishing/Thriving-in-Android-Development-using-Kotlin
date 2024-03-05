package com.packt.list.presentation.list

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.packt.list.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PacktflixTopBar() {
    TopAppBar(
        title = {
            Text(
                text = "PACKTFLIX",
                color = Color.Red,
                fontSize = 48.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )
        },
        actions = {
            IconButton(onClick = { /* Handle profile action */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_profile),
                    contentDescription = "Profile"
                )
            }
            IconButton(onClick = { /* Handle more action */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_more),
                    contentDescription = "More"
                )
            }
        },

    )
}
