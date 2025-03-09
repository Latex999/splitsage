package com.splitsage.android.di

import com.splitsage.android.data.repository.ExpenseRepository
import com.splitsage.android.data.repository.GroupRepository
import com.splitsage.android.data.repository.InMemoryExpenseRepository
import com.splitsage.android.data.repository.InMemoryGroupRepository
import com.splitsage.android.data.repository.InMemoryUserRepository
import com.splitsage.android.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module that provides repository instances.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideUserRepository(): UserRepository {
        return InMemoryUserRepository()
    }
    
    @Provides
    @Singleton
    fun provideGroupRepository(): GroupRepository {
        return InMemoryGroupRepository()
    }
    
    @Provides
    @Singleton
    fun provideExpenseRepository(): ExpenseRepository {
        return InMemoryExpenseRepository()
    }
}