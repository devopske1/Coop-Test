package com.dev.test.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dev.test.data.local.dao.DepositDao
import com.dev.test.data.local.dao.GoalDao
import com.dev.test.data.local.dao.WithdrawalDao
import com.dev.test.data.local.entities.DepositEntity
import com.dev.test.data.local.entities.GoalEntity
import com.dev.test.data.local.entities.WithdrawalEntity

@Database(
    entities = [
        GoalEntity::class,
        DepositEntity::class,
        WithdrawalEntity::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)  // Add this for GoalCategory enum conversion
abstract class AppDatabase : RoomDatabase() {
    abstract fun goalDao(): GoalDao
    abstract fun depositDao(): DepositDao
    abstract fun withdrawalDao(): WithdrawalDao
}