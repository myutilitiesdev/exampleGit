package com.foryou.examplegit.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CacheInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCacheInfo(cacheInfo: CacheInfo)

    @Query("SELECT lastUpdated FROM cache_info WHERE id = 1")
    suspend fun getLastUpdated(): Long?
}