package com.dev.test.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseMigrations {
    
    val MIGRATION_2_3 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Create the deposits table
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS `deposits` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `goalName` TEXT NOT NULL,
                    `destination` TEXT NOT NULL,
                    `phoneNumber` TEXT,
                    `selectedAccount` TEXT,
                    `depositAmount` REAL NOT NULL,
                    `availableBalance` REAL NOT NULL,
                    `timestamp` INTEGER NOT NULL,
                    `status` TEXT NOT NULL
                )
            """.trimIndent())
            
            // Create the withdrawals table
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS `withdrawals` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `goalName` TEXT NOT NULL,
                    `destination` TEXT NOT NULL,
                    `phoneNumber` TEXT NOT NULL,
                    `selectedAccount` TEXT NOT NULL,
                    `withdrawalAmount` REAL NOT NULL,
                    `availableBalance` REAL NOT NULL,
                    `timestamp` INTEGER NOT NULL,
                    `status` TEXT NOT NULL
                )
            """.trimIndent())
        }
    }
}
