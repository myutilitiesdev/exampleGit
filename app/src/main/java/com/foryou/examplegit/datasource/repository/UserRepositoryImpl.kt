package com.foryou.examplegit.datasource.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.foryou.examplegit.BuildConfig
import com.foryou.examplegit.datasource.local.AppDatabase
import com.foryou.examplegit.datasource.local.GithubUser
import com.foryou.examplegit.datasource.model.User
import com.foryou.examplegit.datasource.model.UserDetail
import com.foryou.examplegit.datasource.remote.ApiService
import com.foryou.examplegit.datasource.remote.paging.GithubRemoteMediator
import com.foryou.examplegit.datasource.remote.paging.UserPagingSource
import com.foryou.examplegit.utils.DataState
import com.foryou.examplegit.utils.PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val database: AppDatabase
) : UserRepository {

    override fun getUsers(): Flow<PagingData<User>> {
        return Pager(config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = { UserPagingSource(apiService) }).flow
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getUsersMediator(): Flow<PagingData<GithubUser>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            remoteMediator = GithubRemoteMediator(apiService, database),
            pagingSourceFactory = { database.githubUserDao().getUsers() }
        ).flow
    }

    override suspend fun getUserDetail(username: String): Flow<DataState<UserDetail>> = flow {
        emit(DataState.Loading)
        try {
            val authHeader = "token ${BuildConfig.GITHUB_TOKEN}"
            val apiResponse = apiService.getUserDetail(username, authHeader)
            emit(DataState.Success(apiResponse))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }
}