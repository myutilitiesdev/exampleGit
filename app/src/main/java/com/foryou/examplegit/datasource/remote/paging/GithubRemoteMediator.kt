package com.foryou.examplegit.datasource.remote.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.foryou.examplegit.BuildConfig
import com.foryou.examplegit.datasource.local.AppDatabase
import com.foryou.examplegit.datasource.local.GithubUser
import com.foryou.examplegit.datasource.local.toEntity
import com.foryou.examplegit.datasource.remote.ApiService
import com.foryou.examplegit.utils.PAGE_SIZE

@OptIn(ExperimentalPagingApi::class)
class GithubRemoteMediator(
    private val api: ApiService,
    private val database: AppDatabase
) : RemoteMediator<Int, GithubUser>() {

    private val userDao = database.githubUserDao()

    private val authHeader = "token ${BuildConfig.GITHUB_TOKEN}"

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, GithubUser>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val hasData = userDao.getUserCount() > 0
                    if (hasData) {
                        return MediatorResult.Success(endOfPaginationReached = false)
                    }
                    0
                }

                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> state.lastItemOrNull()?.id ?: 0
            }

            val response = api.getUsers(PAGE_SIZE, page * PAGE_SIZE, authHeader)
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    userDao.clearAll()
                }
                userDao.insertAll(response.map { it.toEntity() })
            }

            MediatorResult.Success(endOfPaginationReached = response.isEmpty())
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}