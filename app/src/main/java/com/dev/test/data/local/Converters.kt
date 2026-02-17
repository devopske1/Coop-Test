package com.dev.test.data.local

import androidx.room.TypeConverter
import com.dev.test.presentation.screens.mygoals.GoalCategory

/**
 * Type converters for Room database
 * Converts complex types to/from primitive types that Room can store
 */
class Converters {
    
    @TypeConverter
    fun fromGoalCategory(category: GoalCategory): String {
        return category.name
    }
    
    @TypeConverter
    fun toGoalCategory(categoryName: String): GoalCategory {
        return try {
            GoalCategory.valueOf(categoryName)
        } catch (e: IllegalArgumentException) {
            GoalCategory.OTHER // Default fallback
        }
    }
}
