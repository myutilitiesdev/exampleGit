package com.foryou.examplegit.di

import com.foryou.examplegit.datasource.local.AppDatabase
import com.foryou.examplegit.datasource.remote.ApiService
import com.foryou.examplegit.datasource.repository.UserRepository
import com.foryou.examplegit.datasource.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideUserRepository(
        apiService: ApiService, database: AppDatabase
    ): UserRepository {
        return UserRepositoryImpl(
            apiService, database
        )
    }
}