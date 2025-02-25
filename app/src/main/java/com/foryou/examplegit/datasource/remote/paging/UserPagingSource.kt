package com.foryou.examplegit.datasource.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.foryou.examplegit.BuildConfig
import com.foryou.examplegit.datasource.model.User
import com.foryou.examplegit.datasource.remote.ApiService
import com.foryou.examplegit.utils.PAGE_SIZE

class UserPagingSource(
    private val apiService: ApiService
) : PagingSource<Int, User>() {

    private val authHeader = "token ${BuildConfig.GITHUB_TOKEN}"

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        return try {
            val page = params.key ?: 0
            val users = apiService.getUsers(PAGE_SIZE, page * PAGE_SIZE, authHeader)
            LoadResult.Page(
                data = users.map { it },
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (users.isNotEmpty()) page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1) ?: state.closestPageToPosition(
                position
            )?.nextKey?.minus(1)
        }
    }
}