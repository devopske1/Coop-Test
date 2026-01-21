package com.dev.test.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dev.test.presentation.dashboard.GoalCategory

@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val category: GoalCategory,
    val targetAmount: String,
    val targetDate: String
)