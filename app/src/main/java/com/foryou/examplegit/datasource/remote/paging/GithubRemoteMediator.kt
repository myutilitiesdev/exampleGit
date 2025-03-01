package com.foryou.examplegit.datasource.remote.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.foryou.examplegit.BuildConfig
import com.foryou.examplegit.datasource.local.AppDatabase
import com.foryou.examplegit.datasource.local.CacheInfo
import com.foryou.examplegit.datasource.local.GithubUser
import com.foryou.examplegit.datasource.local.UserRemoteKeys
import com.foryou.examplegit.datasource.local.toEntity
import com.foryou.examplegit.datasource.remote.ApiService
import com.foryou.examplegit.utils.PAGE_SIZE
import timber.log.Timber
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class GithubRemoteMediator(
    private val api: ApiService, private val database: AppDatabase
) : RemoteMediator<Int, GithubUser>() {

    private val userDao = database.githubUserDao()
    private val userRemoteKeys = database.userRemoteKeyDao()

    private val authHeader = "token ${BuildConfig.GITHUB_TOKEN}"

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(5, TimeUnit.SECONDS)
        val lastUpdated = database.cacheInfoDao().getLastUpdated()

        return if (lastUpdated != null && System.currentTimeMillis() - lastUpdated <= cacheTimeout) {
            Timber.e("Cached data is up-to-date, no need to fetch from network")
            // Cached data is up-to-date, no need to fetch from network.
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            Timber.e("Need to refresh the cached data from network.")
            // Need to refresh the cached data from network.
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, GithubUser>
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }

                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    prevPage
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage ?: return MediatorResult.Success(
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
                        id = unsplashImage.id.toString(), prevPage = prevPage, nextPage = nextPage
                    )
                }
                userRemoteKeys.addAllRemoteKeys(remoteKeys = keys)
                userDao.insertAll(response.map { it.toEntity() })
            }

            // Update the lastUpdated timestamp after successful fetch
            val currentTime = System.currentTimeMillis()
            database.cacheInfoDao().insertCacheInfo(CacheInfo(lastUpdated = currentTime))

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