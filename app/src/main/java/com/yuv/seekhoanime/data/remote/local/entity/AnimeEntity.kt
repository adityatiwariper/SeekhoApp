package com.yuv.seekhoanime.data.remote.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anime_table")
data class AnimeEntity(
    @PrimaryKey val malId: Int,
    val title: String,
    val episodes: Int?,
    val score: Double?,
    val posterUrl: String,
    val synopsis: String?,
    val genres: String?, // CSV of genre names
    val lastUpdated: Long
)
