package com.dev.test.data.repository

import android.util.Log
import com.dev.test.data.local.dao.DepositDao
import com.dev.test.data.local.entities.DepositEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DepositRepository @Inject constructor(
    private val depositDao: DepositDao
) {

    companion object {
        private const val TAG = "DepositRepository"
    }

    suspend fun insertDeposit(deposit: DepositEntity): Long {
        Log.d(TAG, "insertDeposit called with: $deposit")
        return try {
            val id = depositDao.insertDeposit(deposit)
            Log.d(TAG, "✅ Deposit inserted successfully with ID: $id")
            id
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error inserting deposit", e)
            throw e
        }
    }

    suspend fun updateDeposit(deposit: DepositEntity) {
        Log.d(TAG, "updateDeposit called with: $deposit")
        depositDao.updateDeposit(deposit)
    }

    suspend fun deleteDeposit(deposit: DepositEntity) {
        Log.d(TAG, "deleteDeposit called with: $deposit")
        depositDao.deleteDeposit(deposit)
    }

    fun getAllDeposits(): Flow<List<DepositEntity>> {
        Log.d(TAG, "getAllDeposits called")
        return depositDao.getAllDeposits()
    }

    suspend fun getDepositById(depositId: Int): DepositEntity? {
        Log.d(TAG, "getDepositById called with ID: $depositId")
        return depositDao.getDepositById(depositId)
    }

    fun getDepositsByGoalName(goalName: String): Flow<List<DepositEntity>> {
        Log.d(TAG, "getDepositsByGoalName called with: $goalName")
        return depositDao.getDepositsByGoalName(goalName)
    }

    fun getDepositsByDestination(destination: String): Flow<List<DepositEntity>> {
        Log.d(TAG, "getDepositsByDestination called with: $destination")
        return depositDao.getDepositsByDestination(destination)
    }

    fun getDepositsByStatus(status: String): Flow<List<DepositEntity>> {
        Log.d(TAG, "getDepositsByStatus called with: $status")
        return depositDao.getDepositsByStatus(status)
    }

    suspend fun getTotalDepositsByGoalName(goalName: String): Double {
        Log.d(TAG, "getTotalDepositsByGoalName called with: $goalName")
        return depositDao.getTotalDepositsByGoalName(goalName) ?: 0.0
    }

    suspend fun getTotalDeposits(): Double {
        Log.d(TAG, "getTotalDeposits called")
        return depositDao.getTotalDeposits() ?: 0.0
    }

    suspend fun deleteDepositById(depositId: Int) {
        Log.d(TAG, "deleteDepositById called with ID: $depositId")
        depositDao.deleteDepositById(depositId)
    }

    suspend fun deleteAllDeposits() {
        Log.d(TAG, "deleteAllDeposits called")
        depositDao.deleteAllDeposits()
    }

    fun getDepositsByDateRange(startDate: Long, endDate: Long): Flow<List<DepositEntity>> {
        Log.d(TAG, "getDepositsByDateRange called with start: $startDate, end: $endDate")
        return depositDao.getDepositsByDateRange(startDate, endDate)
    }
}