package com.yuv.seekhoanime.data.remote.mappers

import com.yuv.seekhoanime.data.remote.local.entity.AnimeEntity
import com.yuv.seekhoanime.data.remote.models.AnimeApiModel

fun AnimeApiModel.toEntity(): AnimeEntity {
    return AnimeEntity(
        malId = mal_id,
        title = title,
        episodes = episodes,
        score = score,
        posterUrl = images.jpg.image_url,
        synopsis = synopsis,
        genres = genres?.joinToString(",") { it.name },
        lastUpdated = System.currentTimeMillis()
    )
}