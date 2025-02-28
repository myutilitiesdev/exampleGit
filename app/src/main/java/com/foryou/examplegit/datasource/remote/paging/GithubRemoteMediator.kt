package com.foryou.examplegit.datasource.remote.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.foryou.examplegit.BuildConfig
import com.foryou.examplegit.datasource.local.AppDatabase
import com.foryou.examplegit.datasource.local.GithubUser
import com.foryou.examplegit.datasource.local.UserRemoteKeys
import com.foryou.examplegit.datasource.local.toEntity
import com.foryou.examplegit.datasource.remote.ApiService
import com.foryou.examplegit.utils.PAGE_SIZE
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class GithubRemoteMediator(
    private val api: ApiService,
    private val database: AppDatabase
) : RemoteMediator<Int, GithubUser>() {

    private val userDao = database.githubUserDao()
    private val userRemoteKeys = database.userRemoteKeyDao()

    private val authHeader = "token ${BuildConfig.GITHUB_TOKEN}"

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, GithubUser>
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }

                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    prevPage
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage
                }
            }

            Timber.e("MediatorResult currentPage = $currentPage")

            val response = api.getUsers(PAGE_SIZE, currentPage * PAGE_SIZE, authHeader)
            val endOfPaginationReached = response.isEmpty()

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    userDao.clearAll()
                    userRemoteKeys.deleteAllRemoteKeys()
                }
                val keys = response.map { unsplashImage ->
                    UserRemoteKeys(
                        id = unsplashImage.id.toString(),
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }
                userRemoteKeys.addAllRemoteKeys(remoteKeys = keys)
                userDao.insertAll(response.map { it.toEntity() })
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, GithubUser>
    ): UserRemoteKeys? {
        val userDao = database.userRemoteKeyDao()
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                userDao.getRemoteKeys(key = id.toString())
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, GithubUser>
    ): UserRemoteKeys? {
        val userDao = database.userRemoteKeyDao()
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { unsplashImage ->
                userDao.getRemoteKeys(key = unsplashImage.id.toString())
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, GithubUser>
    ): UserRemoteKeys? {
        val userDao = database.userRemoteKeyDao()
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { unsplashImage ->
                userDao.getRemoteKeys(key = unsplashImage.id.toString())
            }
    }
}