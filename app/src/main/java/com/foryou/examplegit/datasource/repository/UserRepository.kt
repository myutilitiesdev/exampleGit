package com.foryou.examplegit.datasource.repository

import androidx.paging.PagingData
import com.foryou.examplegit.datasource.model.User
import com.foryou.examplegit.datasource.model.UserDetail
import com.foryou.examplegit.utils.DataState
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUsers(): Flow<PagingData<User>>
    suspend fun getUserDetail(username: String): Flow<DataState<UserDetail>>
}