package com.foryou.examplegit.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserRemoteKeysDao {
    @Query("SELECT * FROM users_remote_keys WHERE id =:key")
    suspend fun getRemoteKeys(key: String): UserRemoteKeys

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(remoteKeys: List<UserRemoteKeys>)

    @Query("DELETE FROM users_remote_keys")
    suspend fun deleteAllRemoteKeys()
}