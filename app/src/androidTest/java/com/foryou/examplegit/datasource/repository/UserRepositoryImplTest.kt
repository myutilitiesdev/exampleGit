package com.foryou.examplegit.datasource.repository

import androidx.paging.PagingData
import com.foryou.examplegit.datasource.local.GithubUser
import com.foryou.examplegit.datasource.model.User
import com.foryou.examplegit.datasource.model.UserDetail
import com.foryou.examplegit.utils.DataState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepositoryImplTest : UserRepository {
    override fun getUsers(): Flow<PagingData<User>> = flow {
        // Create a fake list of users.
        val users = listOf(
            User(1, "Alice", "", ""),
            User(2, "Bob", "", ""),
            User(3, "Charlie", "", "")
        )
        // Create PagingData from the list of users.
        val pagingData = PagingData.from(users)
        emit(pagingData)
    }

    override fun getUsersMediator(): Flow<PagingData<GithubUser>> = flow {
        // Create a fake list of GitHub users.
        val githubUsers = listOf(
            GithubUser(1, "aliceGH", "", ""),
            GithubUser(2, "bobGH", "", ""),
            GithubUser(3, "charlieGH", "", "")
        )
        // Create PagingData from the list of GitHub users.
        val pagingData = PagingData.from(githubUsers)
        emit(pagingData)
    }

    override suspend fun getUserDetail(username: String): Flow<DataState<UserDetail>> = flow {
        emit(DataState.Loading)
        // Simulate network or processing delay.
        delay(500)
        // Return a fake user detail if the username is not empty.
        if (username.isNotEmpty()) {
            val userDetail = UserDetail(1, username, "Fake details for $username", "", "", 9, 9)
            emit(DataState.Success(userDetail))
        } else {
            emit(DataState.Error(Exception("User not found")))
        }
    }
}