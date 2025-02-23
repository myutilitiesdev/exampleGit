package com.foryou.examplegit.datasource.remote

import com.foryou.examplegit.BuildConfig
import com.foryou.examplegit.datasource.model.User
import com.foryou.examplegit.datasource.model.UserDetail
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    suspend fun getUsers(
        @Query("per_page") perPage: Int,
        @Query("since") since: Int,
        @Header("Authorization") auth: String? = BuildConfig.GITHUB_TOKEN
    ): List<User>

    @GET("users/{username}")
    suspend fun getUserDetail(
        @Path("username") username: String,
        @Header("Authorization") auth: String? = BuildConfig.GITHUB_TOKEN
    ): UserDetail
}