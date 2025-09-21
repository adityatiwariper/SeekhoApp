package com.yuv.seekhoanime.data.remote.repository

import androidx.lifecycle.LiveData
import com.yuv.seekhoanime.data.remote.RetrofitInstance
import com.yuv.seekhoanime.data.remote.local.entity.AnimeEntity
import com.yuv.seekhoanime.data.remote.local.entity.dao.AnimeDao
import com.yuv.seekhoanime.data.remote.mappers.toEntity
import com.yuv.seekhoanime.data.remote.models.AnimeDetailResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AnimeRepository(private val animeDao: AnimeDao) {

    val allAnime: LiveData<List<AnimeEntity>> = animeDao.getAllAnime()

    suspend fun fetchAndSaveTopAnime() {
        val response = RetrofitInstance.api.getTopAnime()
        val entities = response.data.map { it.toEntity() }
        animeDao.insertAll(entities)
    }

    suspend fun getAnimeDetails(id: Int): AnimeDetailResponse {
        return RetrofitInstance.api.getAnimeDetails(id)
    }
}
