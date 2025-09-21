package com.yuv.seekhoanime.data.remote

import com.yuv.seekhoanime.data.remote.models.AnimeApiModel
import com.yuv.seekhoanime.data.remote.models.AnimeDetailResponse
import com.yuv.seekhoanime.data.remote.models.AnimeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("top/anime")
    suspend fun getTopAnime(): AnimeResponse

    @GET("anime/{id}")
    suspend fun getAnimeDetails(@Path("id") id: Int): AnimeDetailResponse
}