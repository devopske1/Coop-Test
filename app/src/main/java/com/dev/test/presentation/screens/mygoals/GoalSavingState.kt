package com.coop.feature_goals.presentation

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.dev.test.R

/**
 * Enum representing goal categories
 */
enum class GoalCategory(val displayName: String) {
    TRAVELLING("Travelling"),
    EDUCATION("Education"),
    WEDDING("Wedding"),
    EMERGENCY("Emergency Fund"),
    HOUSE("House"),
    CAR("Car"),
    BUSINESS("Business"),
    OTHER("Other")
}

/**
 * Enum representing transaction types
 */
enum class TransactionType {
    DEPOSIT,
    WITHDRAWAL
}

/**
 * Data class representing a goal transaction
 */
data class GoalTransaction(
    val id: String,
    val type: TransactionType,
    val amount: Double,
    val reference: String,
    val date: String,
    val goalId: String
)

/**
 * Data class representing a savings goal
 */
data class SavingsGoal(
    val id: String,
    val name: String,
    val category: GoalCategory,
    val targetAmount: Double,
    val currentAmount: Double,
    val targetDate: String,
    val createdDate: String,
    @DrawableRes val iconRes: Int = R.drawable.bag,
    val backgroundColor: Color = Color(0xFF2E7D32)
) {
    val progress: Float
        get() = if (targetAmount > 0) (currentAmount / targetAmount).toFloat() else 0f

    val progressPercentage: String
        get() = "${(progress * 100).toInt()}%"

    val remainingAmount: Double
        get() = (targetAmount - currentAmount).coerceAtLeast(0.0)

    val isCompleted: Boolean
        get() = currentAmount >= targetAmount
}

/**
 * UI State for Goal Savings Dashboard
 */
data class GoalSavingsState(
    val isLoading: Boolean = false,
    val goals: List<SavingsGoal> = emptyList(),
    val selectedGoal: SavingsGoal? = null,
    val transactions: List<GoalTransaction> = emptyList(),
    val selectedTransactionFilter: TransactionFilter = TransactionFilter.ALL,
    val error: String? = null,
    val userName: String = "There" // Default greeting
) {
    val hasGoals: Boolean
        get() = goals.isNotEmpty()

    val activeGoals: List<SavingsGoal>
        get() = goals.filter { !it.isCompleted }

    val completedGoals: List<SavingsGoal>
        get() = goals.filter { it.isCompleted }

    val filteredTransactions: List<GoalTransaction>
        get() = when (selectedTransactionFilter) {
            TransactionFilter.ALL -> transactions
            TransactionFilter.DEPOSITS -> transactions.filter { it.type == TransactionType.DEPOSIT }
            TransactionFilter.WITHDRAWALS -> transactions.filter { it.type == TransactionType.WITHDRAWAL }
        }
}

/**
 * Enum for transaction filtering
 */
enum class TransactionFilter(val displayName: String) {
    ALL("All"),
    DEPOSITS("Deposits"),
    WITHDRAWALS("Withdrawals")
}

/**
 * Sealed class representing user intents/actions
 */
sealed class GoalSavingsIntent {
    data object OnViewGoals: GoalSavingsIntent()

    data object LoadGoals : GoalSavingsIntent()
    data object RefreshGoals : GoalSavingsIntent()
    data class OnGoalClicked(val goalId: String) : GoalSavingsIntent()
    data object OnAddGoalClicked : GoalSavingsIntent()
    data class OnDepositClicked(val goalId: String) : GoalSavingsIntent()
    data class OnWithdrawClicked(val goalId: String) : GoalSavingsIntent()
    data class OnTransactionFilterChanged(val filter: TransactionFilter) : GoalSavingsIntent()
    data object OnViewAllTransactionsClicked : GoalSavingsIntent()
    data object OnLearnAboutSavingsClicked : GoalSavingsIntent()
    data object OnErrorDismissed : GoalSavingsIntent()
}

/**
 * Sealed class for navigation events
 */
sealed class GoalSavingsNavigation {
    data object NavigateToMyGoalsScreen: GoalSavingsNavigation()
    data class NavigateToGoalDetails(val goalId: String) : GoalSavingsNavigation()
    data object NavigateToCreateGoal : GoalSavingsNavigation()
    data class NavigateToDeposit(val goalId: String) : GoalSavingsNavigation()
    data class NavigateToWithdraw(val goalId: String) : GoalSavingsNavigation()
    data object NavigateToAllTransactions : GoalSavingsNavigation()
    data object NavigateToLearnMore : GoalSavingsNavigation()
}

/**
 * Sample data for preview and testing
 */
object GoalSavingsSampleData {
    val sampleTransactions = listOf(
        GoalTransaction(
            id = "1",
            type = TransactionType.DEPOSIT,
            amount = 600.00,
            reference = "MPESA 071245678",
            date = "24 Sep 2025",
            goalId = "1"
        ),
        GoalTransaction(
            id = "2",
            type = TransactionType.WITHDRAWAL,
            amount = 200.00,
            reference = "MPESA 071245678",
            date = "12 Oct 2025",
            goalId = "1"
        ),
        GoalTransaction(
            id = "3",
            type = TransactionType.DEPOSIT,
            amount = 500.00,
            reference = "AC 012345678901612",
            date = "24 Sep 2025",
            goalId = "1"
        )
    )

    val sampleGoals = listOf(
        SavingsGoal(
            id = "1",
            name = "Dubai Trip",
            category = GoalCategory.TRAVELLING,
            targetAmount = 10000.00,
            currentAmount = 900.00,
            targetDate = "24/08/2026",
            createdDate = "01/01/2025",
            backgroundColor = Color(0xFF2E7D32)
        ),
        SavingsGoal(
            id = "2",
            name = "New Laptop",
            category = GoalCategory.OTHER,
            targetAmount = 5000.00,
            currentAmount = 2500.00,
            targetDate = "31/12/2025",
            createdDate = "15/06/2025",
            backgroundColor = Color(0xFF1976D2)
        )
    )

    fun createEmptyState() = GoalSavingsState(
        goals = emptyList(),
        userName = "There"
    )

    fun createStateWithOneGoal() = GoalSavingsState(
        goals = listOf(sampleGoals[0]),
        selectedGoal = sampleGoals[0],
        transactions = sampleTransactions,
        userName = "There"
    )

    fun createStateWithMultipleGoals() = GoalSavingsState(
        goals = sampleGoals,
        selectedGoal = sampleGoals[0],
        transactions = sampleTransactions,
        userName = "There"
    )
}