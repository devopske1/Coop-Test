package com.dev.test.data.repository

import android.util.Log
import com.dev.test.data.local.dao.GoalDao
import com.dev.test.data.local.entities.GoalEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoalRepository @Inject constructor(
    private val goalDao: GoalDao
) {

    companion object {
        private const val TAG = "GoalRepository"
    }

    suspend fun insertGoal(goal: GoalEntity): Long {
        Log.d(TAG, "insertGoal called with: $goal")
        return try {
            val id = goalDao.insertGoal(goal)
            Log.d(TAG, "✅ Goal inserted successfully with ID: $id")
            id
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error inserting goal", e)
            throw e
        }
    }

    suspend fun updateGoal(goal: GoalEntity) {
        Log.d(TAG, "updateGoal called with: $goal")
        goalDao.updateGoal(goal)
    }

    suspend fun deleteGoal(goal: GoalEntity) {
        Log.d(TAG, "deleteGoal called with: $goal")
        goalDao.deleteGoal(goal)
    }

    fun getAllGoals(): Flow<List<GoalEntity>> {
        Log.d(TAG, "getAllGoals called")
        return goalDao.getAllGoals()
    }

    suspend fun getGoalById(goalId: Int): GoalEntity? {
        Log.d(TAG, "getGoalById called with ID: $goalId")
        return goalDao.getGoalById(goalId)
    }

    suspend fun getGoalByName(goalName: String): GoalEntity? {
        Log.d(TAG, "getGoalByName called with: $goalName")
        return goalDao.getGoalByName(goalName)
    }

    fun getGoalsByCategory(category: String): Flow<List<GoalEntity>> {
        Log.d(TAG, "getGoalsByCategory called with: $category")
        return goalDao.getGoalsByCategory(category)
    }

    fun searchGoals(query: String): Flow<List<GoalEntity>> {
        Log.d(TAG, "searchGoals called with: $query")
        return goalDao.searchGoals(query)
    }

    suspend fun getGoalsCount(): Int {
        Log.d(TAG, "getGoalsCount called")
        return goalDao.getGoalsCount()
    }

    suspend fun deleteGoalById(goalId: Int) {
        Log.d(TAG, "deleteGoalById called with ID: $goalId")
        goalDao.deleteGoalById(goalId)
    }

    suspend fun deleteAllGoals() {
        Log.d(TAG, "deleteAllGoals called")
        goalDao.deleteAllGoals()
    }

    fun getOverdueGoals(currentDate: String): Flow<List<GoalEntity>> {
        Log.d(TAG, "getOverdueGoals called with date: $currentDate")
        return goalDao.getOverdueGoals(currentDate)
    }

    fun getGoalsByDateRange(startDate: String, endDate: String): Flow<List<GoalEntity>> {
        Log.d(TAG, "getGoalsByDateRange called - start: $startDate, end: $endDate")
        return goalDao.getGoalsByDateRange(startDate, endDate)
    }
}