package com.yuv.seekhoanime.data.remote.models

data class AnimeApiModel(
    val mal_id: Int,
    val title: String,
    val episodes: Int?,
    val score: Double?,
    val images: ImageData,
    val synopsis: String?,
    val genres: List<Genre>?
)

data class ImageData(
    val jpg: JpgImage
)

data class JpgImage(
    val image_url: String
)

data class Genre(
    val name: String
)