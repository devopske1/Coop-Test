package com.dev.test.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dev.test.data.local.dao.GoalDao
import com.dev.test.data.local.entities.GoalEntity

@Database(entities = [
    GoalEntity::class
                     ], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun goalDao(): GoalDao
}