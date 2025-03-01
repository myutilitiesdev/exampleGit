package com.foryou.examplegit.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GithubUser::class, UserRemoteKeys::class, CacheInfo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun githubUserDao(): GithubUserDao
    abstract fun userRemoteKeyDao(): UserRemoteKeysDao
    abstract fun cacheInfoDao(): CacheInfoDao
}