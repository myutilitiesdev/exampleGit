package com.foryou.examplegit.datasource.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GithubUserDao {
    @Query("SELECT * FROM github_users ORDER BY id ASC")
    fun getUsers(): PagingSource<Int, GithubUser>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<GithubUser>)

    @Query("SELECT COUNT(*) FROM github_users")
    suspend fun getUserCount(): Int

    @Query("DELETE FROM github_users")
    suspend fun clearAll()
}