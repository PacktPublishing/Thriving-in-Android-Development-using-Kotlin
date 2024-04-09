package com.packt.list.presentation.detail.model

data class ItemDetail(
    val type: Type,
    val title: String,
    val imageUrl: String,
    val rating: String,
    val isHD: Boolean,
    val year: String,
    val duration: String,
    val cast: List<String>,
    val description: String,
    val creators: List<String>,
    val episodes: List<Episode>,
    val movieUrl: String
) {
    enum class Type {
        MOVIE, SERIES
    }
}

data class Episode(
    val title: String,
    val imageUrl: String,
    val duration: String,
    val episodeUrl: String
)