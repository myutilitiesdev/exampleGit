package com.foryou.examplegit.datasource.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users_remote_keys")
data class UserRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val prevPage: Int? = null,
    val nextPage: Int? = null
)