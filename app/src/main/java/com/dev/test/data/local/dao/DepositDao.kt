package com.dev.test.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dev.test.data.local.entities.DepositEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertDeposit(deposit: DepositEntity): Long

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertDeposits(deposits: List<DepositEntity>)

    @Update
    suspend fun updateDeposit(deposit: DepositEntity)

    @Delete
    suspend fun deleteDeposit(deposit: DepositEntity)

    @Query("SELECT * FROM deposits ORDER BY timestamp DESC")
    fun getAllDeposits(): Flow<List<DepositEntity>>

    @Query("SELECT * FROM deposits WHERE id = :depositId")
    suspend fun getDepositById(depositId: Int): DepositEntity?

    @Query("SELECT * FROM deposits WHERE goalName = :goalName ORDER BY timestamp DESC")
    fun getDepositsByGoalName(goalName: String): Flow<List<DepositEntity>>

    @Query("SELECT * FROM deposits WHERE destination = :destination ORDER BY timestamp DESC")
    fun getDepositsByDestination(destination: String): Flow<List<DepositEntity>>

    @Query("SELECT * FROM deposits WHERE status = :status ORDER BY timestamp DESC")
    fun getDepositsByStatus(status: String): Flow<List<DepositEntity>>

    @Query("SELECT SUM(depositAmount) FROM deposits WHERE goalName = :goalName AND status = 'SUCCESS'")
    suspend fun getTotalDepositsByGoalName(goalName: String): Double?

    @Query("SELECT SUM(depositAmount) FROM deposits WHERE status = 'SUCCESS'")
    suspend fun getTotalDeposits(): Double?

    @Query("DELETE FROM deposits WHERE id = :depositId")
    suspend fun deleteDepositById(depositId: Int)

    @Query("DELETE FROM deposits")
    suspend fun deleteAllDeposits()

    @Query("SELECT * FROM deposits WHERE timestamp BETWEEN :startDate AND :endDate ORDER BY timestamp DESC")
    fun getDepositsByDateRange(startDate: Long, endDate: Long): Flow<List<DepositEntity>>
}