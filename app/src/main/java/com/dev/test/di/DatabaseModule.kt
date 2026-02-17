package com.dev.test.di

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.dev.test.data.local.AppDatabase
import com.dev.test.data.local.DatabaseMigrations
import com.dev.test.data.local.dao.DepositDao
import com.dev.test.data.local.dao.GoalDao
import com.dev.test.data.local.dao.WithdrawalDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        Log.d(TAG, "Creating AppDatabase instance")
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        )
            .addMigrations(DatabaseMigrations.MIGRATION_2_3)
            .fallbackToDestructiveMigration()
            .build()
            .also {
                Log.d(TAG, "✅ AppDatabase created successfully with version 2")
            }
    }

    @Provides
    fun provideGoalDao(database: AppDatabase): GoalDao {
        return database.goalDao()
    }

    @Provides
    fun provideDepositDao(database: AppDatabase): DepositDao {
        return database.depositDao()
    }

    @Provides
    fun provideWithdrawDao(database: AppDatabase): WithdrawalDao {
        return database.withdrawalDao()
    }
}