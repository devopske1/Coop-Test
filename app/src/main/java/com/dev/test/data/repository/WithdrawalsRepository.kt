package com.dev.test.data.repository

import android.util.Log
import com.dev.test.data.local.dao.WithdrawalDao
import com.dev.test.data.local.entities.WithdrawalEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WithdrawalRepository @Inject constructor(
    private val withdrawalDao: WithdrawalDao
) {

    companion object {
        private const val TAG = "WithdrawalRepository"
    }

    suspend fun insertWithdrawal(withdrawal: WithdrawalEntity): Long {
        Log.d(TAG, "insertWithdrawal called with: $withdrawal")
        return try {
            val id = withdrawalDao.insertWithdrawal(withdrawal)
            Log.d(TAG, "✅ Withdrawal inserted successfully with ID: $id")
            id
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error inserting withdrawal", e)
            throw e
        }
    }

    suspend fun updateWithdrawal(withdrawal: WithdrawalEntity) {
        Log.d(TAG, "updateWithdrawal called with: $withdrawal")
        withdrawalDao.updateWithdrawal(withdrawal)
    }

    suspend fun deleteWithdrawal(withdrawal: WithdrawalEntity) {
        Log.d(TAG, "deleteWithdrawal called with: $withdrawal")
        withdrawalDao.deleteWithdrawal(withdrawal)
    }

    fun getAllWithdrawals(): Flow<List<WithdrawalEntity>> {
        Log.d(TAG, "getAllWithdrawals called")
        return withdrawalDao.getAllWithdrawals()
    }

    suspend fun getWithdrawalById(withdrawalId: Int): WithdrawalEntity? {
        Log.d(TAG, "getWithdrawalById called with ID: $withdrawalId")
        return withdrawalDao.getWithdrawalById(withdrawalId)
    }

    fun getWithdrawalsByGoalName(goalName: String): Flow<List<WithdrawalEntity>> {
        Log.d(TAG, "getWithdrawalsByGoalName called with: $goalName")
        return withdrawalDao.getWithdrawalsByGoalName(goalName)
    }

    fun getWithdrawalsByDestination(destination: String): Flow<List<WithdrawalEntity>> {
        Log.d(TAG, "getWithdrawalsByDestination called with: $destination")
        return withdrawalDao.getWithdrawalsByDestination(destination)
    }

    fun getWithdrawalsByStatus(status: String): Flow<List<WithdrawalEntity>> {
        Log.d(TAG, "getWithdrawalsByStatus called with: $status")
        return withdrawalDao.getWithdrawalsByStatus(status)
    }

    suspend fun getTotalWithdrawalsByGoalName(goalName: String): Double {
        Log.d(TAG, "getTotalWithdrawalsByGoalName called with: $goalName")
        return withdrawalDao.getTotalWithdrawalsByGoalName(goalName) ?: 0.0
    }

    suspend fun getTotalWithdrawals(): Double {
        Log.d(TAG, "getTotalWithdrawals called")
        return withdrawalDao.getTotalWithdrawals() ?: 0.0
    }

    suspend fun deleteWithdrawalById(withdrawalId: Int) {
        Log.d(TAG, "deleteWithdrawalById called with ID: $withdrawalId")
        withdrawalDao.deleteWithdrawalById(withdrawalId)
    }

    suspend fun deleteAllWithdrawals() {
        Log.d(TAG, "deleteAllWithdrawals called")
        withdrawalDao.deleteAllWithdrawals()
    }

    fun getWithdrawalsByDateRange(startDate: Long, endDate: Long): Flow<List<WithdrawalEntity>> {
        Log.d(TAG, "getWithdrawalsByDateRange called with start: $startDate, end: $endDate")
        return withdrawalDao.getWithdrawalsByDateRange(startDate, endDate)
    }
}