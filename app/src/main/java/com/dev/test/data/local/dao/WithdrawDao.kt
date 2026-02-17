package com.dev.test.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dev.test.data.local.entities.WithdrawalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WithdrawalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWithdrawal(withdrawal: WithdrawalEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWithdrawals(withdrawals: List<WithdrawalEntity>)

    @Update
    suspend fun updateWithdrawal(withdrawal: WithdrawalEntity)

    @Delete
    suspend fun deleteWithdrawal(withdrawal: WithdrawalEntity)

    @Query("SELECT * FROM withdrawals ORDER BY timestamp DESC")
    fun getAllWithdrawals(): Flow<List<WithdrawalEntity>>

    @Query("SELECT * FROM withdrawals WHERE id = :withdrawalId")
    suspend fun getWithdrawalById(withdrawalId: Int): WithdrawalEntity?

    @Query("SELECT * FROM withdrawals WHERE goalName = :goalName ORDER BY timestamp DESC")
    fun getWithdrawalsByGoalName(goalName: String): Flow<List<WithdrawalEntity>>

    @Query("SELECT * FROM withdrawals WHERE destination = :destination ORDER BY timestamp DESC")
    fun getWithdrawalsByDestination(destination: String): Flow<List<WithdrawalEntity>>

    @Query("SELECT * FROM withdrawals WHERE status = :status ORDER BY timestamp DESC")
    fun getWithdrawalsByStatus(status: String): Flow<List<WithdrawalEntity>>

    @Query("SELECT SUM(withdrawalAmount) FROM withdrawals WHERE goalName = :goalName AND status = 'SUCCESS'")
    suspend fun getTotalWithdrawalsByGoalName(goalName: String): Double?

    @Query("SELECT SUM(withdrawalAmount) FROM withdrawals WHERE status = 'SUCCESS'")
    suspend fun getTotalWithdrawals(): Double?

    @Query("SELECT COUNT(*) FROM withdrawals WHERE status = 'SUCCESS'")
    suspend fun getSuccessfulWithdrawalsCount(): Int

    @Query("SELECT COUNT(*) FROM withdrawals WHERE status = 'PENDING'")
    suspend fun getPendingWithdrawalsCount(): Int

    @Query("SELECT COUNT(*) FROM withdrawals WHERE status = 'FAILED'")
    suspend fun getFailedWithdrawalsCount(): Int

    @Query("DELETE FROM withdrawals WHERE id = :withdrawalId")
    suspend fun deleteWithdrawalById(withdrawalId: Int)

    @Query("DELETE FROM withdrawals")
    suspend fun deleteAllWithdrawals()

    @Query("SELECT * FROM withdrawals WHERE timestamp BETWEEN :startDate AND :endDate ORDER BY timestamp DESC")
    fun getWithdrawalsByDateRange(startDate: Long, endDate: Long): Flow<List<WithdrawalEntity>>

    @Query("SELECT * FROM withdrawals WHERE goalName = :goalName AND timestamp BETWEEN :startDate AND :endDate ORDER BY timestamp DESC")
    fun getWithdrawalsByGoalAndDateRange(goalName: String, startDate: Long, endDate: Long): Flow<List<WithdrawalEntity>>

    @Query("UPDATE withdrawals SET status = :newStatus WHERE id = :withdrawalId")
    suspend fun updateWithdrawalStatus(withdrawalId: Int, newStatus: String)

    @Query("SELECT * FROM withdrawals WHERE destination = 'MPESA' ORDER BY timestamp DESC")
    fun getMpesaWithdrawals(): Flow<List<WithdrawalEntity>>

    @Query("SELECT * FROM withdrawals WHERE destination = 'COOP_ACCOUNT' ORDER BY timestamp DESC")
    fun getCoopAccountWithdrawals(): Flow<List<WithdrawalEntity>>

    @Query("SELECT AVG(withdrawalAmount) FROM withdrawals WHERE status = 'SUCCESS'")
    suspend fun getAverageWithdrawalAmount(): Double?

    @Query("SELECT * FROM withdrawals WHERE withdrawalAmount > :amount ORDER BY timestamp DESC")
    fun getWithdrawalsAboveAmount(amount: Double): Flow<List<WithdrawalEntity>>

    @Query("SELECT * FROM withdrawals WHERE withdrawalAmount BETWEEN :minAmount AND :maxAmount ORDER BY timestamp DESC")
    fun getWithdrawalsByAmountRange(minAmount: Double, maxAmount: Double): Flow<List<WithdrawalEntity>>
}