package com.packt.newsfeed.domain

data class Post(
    val id: String,
    val user: UserData,
    val imageUrl: String,
    val caption: String,
    val likesCount: Int,
    val commentsCount: Int,
    val timeStamp: Long
) {
    data class UserData(
        val id: String,
        val name: String,
        val imageUrl: String
    )
}