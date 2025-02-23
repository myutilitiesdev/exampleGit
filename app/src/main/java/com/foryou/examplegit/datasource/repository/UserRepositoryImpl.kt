package com.foryou.examplegit.datasource.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.foryou.examplegit.datasource.model.User
import com.foryou.examplegit.datasource.model.UserDetail
import com.foryou.examplegit.datasource.remote.ApiService
import com.foryou.examplegit.datasource.remote.paging.UserPagingSource
import com.foryou.examplegit.utils.DataState
import com.foryou.examplegit.utils.PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : UserRepository {

    override fun getUsers(): Flow<PagingData<User>> {
        return Pager(config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = { UserPagingSource(apiService) }).flow
    }

    override suspend fun getUserDetail(username: String): Flow<DataState<UserDetail>> = flow {
        emit(DataState.Loading)
        try {
            val apiResponse = apiService.getUserDetail(username)
            emit(DataState.Success(apiResponse))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }
}