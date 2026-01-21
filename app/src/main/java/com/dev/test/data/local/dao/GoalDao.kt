package com.dev.test.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dev.test.data.local.entities.GoalEntity
import kotlinx.coroutines.flow.Flow


@Dao
    interface GoalDao {

        @Insert
        suspend fun insertGoal(goal: GoalEntity)

        @Query("SELECT * FROM goals")
        fun getAllGoals(): Flow<List<GoalEntity>>
    }
