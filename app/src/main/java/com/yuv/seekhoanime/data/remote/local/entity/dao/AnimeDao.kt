package com.yuv.seekhoanime.data.remote.local.entity.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yuv.seekhoanime.data.remote.local.entity.AnimeEntity

@Dao
interface AnimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(animeList: List<AnimeEntity>)

    @Query("SELECT * FROM anime_table ORDER BY score DESC")
    fun getAllAnime(): LiveData<List<AnimeEntity>>
}

