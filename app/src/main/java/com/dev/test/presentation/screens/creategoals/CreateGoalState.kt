package com.dev.test.presentation.dashboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email

import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ThumbUp

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Enum for goal categories
 */
enum class GoalCategory(val displayName: String,  val icon: ImageVector) {
    TRAVELLING("Travelling", Icons.Default.ThumbUp),
    EDUCATION("Education", Icons.Default.Email),
    INVESTMENT("Investment", Icons.Default.Email),
    EMERGENCY("Emergency Fund", Icons.Default.Email),
    WEDDING("Wedding", Icons.Default.Email),
    HOUSE("House", Icons.Default.Email),
    CAR("Car", Icons.Default.Email),
    GADGETS("Gadgets", Icons.Default.Email),
    OTHER("Other", Icons.Default.MoreVert)
}

/**
 * Data class for creating a new goal
 */
data class CreateGoalRequest(
    val name: String,
    val category: GoalCategory,
    val targetAmount: Double,
    val targetDate: String
)

/**
 * UI State for Create Goal screen
 */
data class CreateGoalState(
    val goalName: String = "",
    val selectedCategory: GoalCategory? = null,
    val targetAmount: String = "",
    val targetDate: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val createdGoalName: String = "",
    val showCategoryDropdown: Boolean = false,
    val showDatePicker: Boolean = false
) {
    val isValid: Boolean
        get() = goalName.isNotBlank() &&
                selectedCategory != null &&
                targetAmount.isNotBlank() &&
                targetAmount.toDoubleOrNull() != null &&
                targetAmount.toDouble() > 0 &&
                targetDate.isNotBlank()

    val formattedAmount: String
        get() = targetAmount.toDoubleOrNull()?.let {
            String.format("%,.2f", it)
        } ?: targetAmount
}

/**
 * User intents for Create Goal screen
 */
sealed class CreateGoalIntent {
    data class OnGoalNameChanged(val name: String) : CreateGoalIntent()
    data class OnCategorySelected(val category: GoalCategory) : CreateGoalIntent()
    data class OnTargetAmountChanged(val amount: String) : CreateGoalIntent()
    data class OnTargetDateChanged(val date: String) : CreateGoalIntent()
    data object OnCategoryDropdownToggled : CreateGoalIntent()
    data object OnDatePickerToggled : CreateGoalIntent()
    data object OnCreateGoalClicked : CreateGoalIntent()
    data object OnSuccessDialogDismissed : CreateGoalIntent()
    data object OnErrorDismissed : CreateGoalIntent()
}

/**
 * Navigation events
 */
sealed class CreateGoalNavigation {
    data object NavigateBack : CreateGoalNavigation()
    data object NavigateToGoalsList : CreateGoalNavigation()
}