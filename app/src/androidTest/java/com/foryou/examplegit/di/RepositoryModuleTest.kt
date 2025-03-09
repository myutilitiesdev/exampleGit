package com.foryou.examplegit.di

import com.foryou.examplegit.datasource.repository.UserRepository
import com.foryou.examplegit.datasource.repository.UserRepositoryImplTest
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

// Hey Hilt!! Forget about the AppModule - use me instead
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
@Module
class RepositoryModuleTest {
    @Provides
    @Singleton
    fun provideFakeUserRepository(): UserRepository {
        return UserRepositoryImplTest() // Replace with a mock or fake implementation
    }
}