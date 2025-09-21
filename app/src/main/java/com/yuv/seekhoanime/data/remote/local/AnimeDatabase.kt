package com.yuv.seekhoanime.data.remote.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yuv.seekhoanime.data.remote.local.entity.AnimeEntity
import com.yuv.seekhoanime.data.remote.local.entity.dao.AnimeDao

@Database(entities = [AnimeEntity::class], version = 1)
abstract class AnimeDatabase : RoomDatabase() {
    abstract fun animeDao(): AnimeDao

    companion object {
        @Volatile private var INSTANCE: AnimeDatabase? = null
        fun getInstance(context: Context): AnimeDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AnimeDatabase::class.java,
                    "anime_db"
                ).build().also { INSTANCE = it }
            }
    }
}
