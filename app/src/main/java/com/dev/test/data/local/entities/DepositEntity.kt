package com.dev.test.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deposits")
data class DepositEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val goalName: String,
    val destination: String, // "MPESA" or "COOP_ACCOUNT"
    val phoneNumber: String?, // Nullable for COOP_ACCOUNT deposits
    val selectedAccount: String?, // Nullable for MPESA deposits
    val depositAmount: Double,
    val availableBalance: Double,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "SUCCESS" // SUCCESS, PENDING, FAILED
)
@Entity(tableName = "withdrawals")
data class WithdrawalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int =0,
    val goalName: String,
    val destination: String,
    val phoneNumber: String?,
    val selectedAccount: String?,
    val withdrawalAmount: Double,
    val availableBalance: Double,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "SUCCESS"

)