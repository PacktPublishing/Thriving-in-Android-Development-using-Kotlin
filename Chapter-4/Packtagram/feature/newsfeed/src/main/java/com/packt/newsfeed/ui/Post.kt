package com.packt.newsfeed.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.packt.newsfeed.R
import com.packt.newsfeed.domain.Post

@Composable
fun PostItem(
    post: Post
){
    Column{
        Spacer(modifier = Modifier.height(2.dp))
        TitleBar(post = post)
        MediaContent(post = post)
        ActionsBar()
        LikesCount(post = post)
        Caption(post = post)
        Spacer(modifier = Modifier.height(2.dp))
        CommentsCount(post = post)
        Spacer(modifier = Modifier.height(4.dp))
        TimeStamp(post = post)
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun TitleBar(
    modifier: Modifier = Modifier,
    post: Post
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = modifier.width(5.dp))
        AsyncImage(
            modifier = modifier
                .size(40.dp)
                .weight(1f),
            model = post.user.imageUrl,
            contentDescription = "User ${post.user.name} avatar"
        )
        Text(
            text = post.user.name,
            modifier = modifier
                .weight(8f)
                .padding(start = 10.dp),
            fontWeight = FontWeight.Bold
        )
        IconButton(onClick = { }) {
            Icon(
                Icons.Outlined.MoreVert,
                "More"
            )
        }
    }

}

@Composable
fun ActionsBar(
    modifier: Modifier = Modifier,
){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = modifier
                    .fillMaxHeight()
                    .weight(1f)
                ,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Outlined.Favorite,
                        contentDescription = "like",
                        modifier = modifier
                    )
                }
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "comment",
                        modifier = modifier
                    )
                }
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = "share",
                        modifier = modifier
                    )
                }
                Row(
                    modifier = modifier
                        .fillMaxHeight()
                        .weight(1f)
                ) {

                }
                Row(
                    modifier = modifier
                        .fillMaxHeight()
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Outlined.Star,
                            contentDescription = "bookmark",
                            )
                    }
                }

            }
        }
    }
}

@Composable
fun MediaContent (
    modifier: Modifier = Modifier,
    post: Post
){
    Box(
        modifier = modifier
            .fillMaxHeight()
            .height(300.dp),
        contentAlignment = Alignment.Center,

        ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize(),
            model = post.imageUrl,
            contentDescription = null
        )
    }
}

@Composable
fun LikesCount(
    modifier: Modifier = Modifier,
    post: Post
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(30.dp)
            .padding(horizontal = 10.dp)
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = post.likesCount.toString().plus(" likes"),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
fun Caption(
    modifier: Modifier = Modifier,
    post: Post
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 10.dp)
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = buildAnnotatedString {
                val boldStyle = SpanStyle(
                    fontWeight = Bold,
                    fontSize = 14.sp
                )
                val normalStyle = SpanStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                )
                pushStyle(boldStyle)
                append(post.user.name)
                append(" ")
                if (post.caption.isNotEmpty()){
                    pushStyle(normalStyle)
                    append(post.caption)
                }
            }
        )
    }
}

@Composable
fun CommentsCount(
    modifier: Modifier = Modifier,
    post: Post
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.comment_count, post.commentsCount),
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        )
    }

}

@Composable
fun TimeStamp(
    modifier: Modifier = Modifier,
    post: Post
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${post.timeStamp} hours ago ",
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Light
        )
    }

}




@Preview(backgroundColor = 0xFFFFFFF)
@Composable
fun postTest(){

}



