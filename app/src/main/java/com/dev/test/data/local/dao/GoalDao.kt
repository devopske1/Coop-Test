package com.dev.test.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dev.test.data.local.entities.GoalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: GoalEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoals(goals: List<GoalEntity>)

    @Update
    suspend fun updateGoal(goal: GoalEntity)

    @Delete
    suspend fun deleteGoal(goal: GoalEntity)

    @Query("SELECT * FROM goals ORDER BY id DESC")
    fun getAllGoals(): Flow<List<GoalEntity>>

    @Query("SELECT * FROM goals WHERE id = :goalId")
    suspend fun getGoalById(goalId: Int): GoalEntity?

    @Query("SELECT * FROM goals WHERE name = :goalName")
    suspend fun getGoalByName(goalName: String): GoalEntity?

    @Query("SELECT * FROM goals WHERE category = :category ORDER BY id DESC")
    fun getGoalsByCategory(category: String): Flow<List<GoalEntity>>

    @Query("SELECT * FROM goals WHERE name LIKE '%' || :searchQuery || '%' ORDER BY id DESC")
    fun searchGoals(searchQuery: String): Flow<List<GoalEntity>>

    @Query("SELECT COUNT(*) FROM goals")
    suspend fun getGoalsCount(): Int

    @Query("DELETE FROM goals WHERE id = :goalId")
    suspend fun deleteGoalById(goalId: Int)

    @Query("DELETE FROM goals")
    suspend fun deleteAllGoals()

    @Query("SELECT * FROM goals WHERE targetDate < :currentDate ORDER BY targetDate ASC")
    fun getOverdueGoals(currentDate: String): Flow<List<GoalEntity>>

    @Query("SELECT * FROM goals WHERE targetDate BETWEEN :startDate AND :endDate ORDER BY targetDate ASC")
    fun getGoalsByDateRange(startDate: String, endDate: String): Flow<List<GoalEntity>>
}