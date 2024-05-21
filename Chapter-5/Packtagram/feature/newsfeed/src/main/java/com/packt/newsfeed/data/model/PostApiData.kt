package com.packt.newsfeed.data.model

import com.packt.newsfeed.domain.Post
import com.squareup.moshi.Json


data class PostApiData(
    @Json(name = "id")
    val id: String,

    @Json(name = "author")
    val user: UserApiData,

    @Json(name = "image_url")
    val imageUrl: String,

    @Json(name = "caption")
    val caption: String,

    @Json(name = "likes_count")
    val likesCount: Int,

    @Json(name = "comments_count")
    val commentsCount: Int,

    @Json(name = "timestamp")
    val timeStamp: Long
) {
    data class UserApiData(

        @Json(name = "id")
        val id: String,

        @Json(name = "name")
        val name: String,

        @Json(name = "image_url")
        val imageUrl: String
    ) {
        fun toDomain(): Post.UserData {
            return Post.UserData(
                id = id,
                name = name,
                imageUrl = imageUrl
            )
        }
    }

    fun toDomain(): Post {
        return Post(
            id = id,
            user = user.toDomain(),
            imageUrl = imageUrl,
            caption = caption,
            likesCount = likesCount,
            commentsCount = commentsCount,
            timeStamp = timeStamp
        )
    }
}
