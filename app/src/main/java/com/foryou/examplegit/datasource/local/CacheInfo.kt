package com.foryou.examplegit.datasource.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cache_info")
data class CacheInfo(
    @PrimaryKey val id: Int = 1, // You can have a single row for cache info.
    val lastUpdated: Long
)