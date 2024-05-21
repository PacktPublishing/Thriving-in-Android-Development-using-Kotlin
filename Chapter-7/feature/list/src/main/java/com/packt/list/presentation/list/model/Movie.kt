package com.packt.list.presentation.list.model

data class Movie(
    val id: Int,
    val title: String,
    val imageUrl: String // For simplicity, using a URL. In a real app, consider handling image loading efficiently.
)

