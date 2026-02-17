package com.dev.test.presentation.screens.mygoals

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.test.data.repository.DepositRepository
import com.dev.test.data.repository.GoalRepository
import com.dev.test.data.repository.WithdrawalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.dev.test.R

@HiltViewModel
class GoalSavingsViewModel @Inject constructor(
    private val goalRepository: GoalRepository,
    private val depositRepository: DepositRepository,
    private val withdrawalRepository: WithdrawalRepository
) : ViewModel() {

    companion object {
        private const val TAG = "GoalSavingsViewModel"
    }

    private val _state = MutableStateFlow(GoalSavingsState())
    val state: StateFlow<GoalSavingsState> = _state.asStateFlow()

    private val _navigationEvents = Channel<GoalSavingsNavigation>()
    val navigationEvents = _navigationEvents.receiveAsFlow()

    init {
        processIntent(GoalSavingsIntent.LoadGoals)
    }

    fun processIntent(intent: GoalSavingsIntent) {
        Log.d(TAG, "Processing intent: $intent")
        when (intent) {
            is GoalSavingsIntent.OnViewGoals -> handleViewAllGoalsClicked()
            is GoalSavingsIntent.LoadGoals -> loadGoals()
            is GoalSavingsIntent.RefreshGoals -> loadGoals()
            is GoalSavingsIntent.OnGoalClicked -> handleGoalClicked(intent.goalId)
            is GoalSavingsIntent.OnAddGoalClicked -> handleAddGoalClicked()
            is GoalSavingsIntent.OnDepositClicked -> handleDepositClicked(intent.goalId)
            is GoalSavingsIntent.OnWithdrawClicked -> handleWithdrawClicked(intent.goalId)
            is GoalSavingsIntent.OnTransactionFilterChanged -> handleTransactionFilterChanged(intent.filter)
            is GoalSavingsIntent.OnViewAllTransactionsClicked -> handleViewAllTransactionsClicked()
            is GoalSavingsIntent.OnLearnAboutSavingsClicked -> handleLearnAboutSavingsClicked()
            is GoalSavingsIntent.OnErrorDismissed -> _state.update { it.copy(error = null) }
        }
    }

    private fun loadGoals() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            Log.d(TAG, "Loading goals from database...")

            goalRepository.getAllGoals()
                .catch { e ->
                    Log.e(TAG, "Error loading goals", e)
                    _state.update {
                        it.copy(isLoading = false, error = "Failed to load goals: ${e.message}")
                    }
                }
                .collect { goalEntities ->
                    Log.d(TAG, "Loaded ${goalEntities.size} goals from database")

                    val savingsGoals = goalEntities.map { entity ->
                        val totalDeposits = depositRepository.getTotalDepositsByGoalName(entity.name)
                        val totalWithdrawals = withdrawalRepository.getTotalWithdrawalsByGoalName(entity.name)
                        val currentAmount = totalDeposits - totalWithdrawals

                        Log.d(TAG, "Goal: ${entity.name} | Deposits: $totalDeposits | Withdrawals: $totalWithdrawals | Current: $currentAmount")

                        SavingsGoal(
                            id = entity.id.toString(),
                            name = entity.name,
                            category = entity.category,
                            targetAmount = entity.targetAmount.toDoubleOrNull() ?: 0.0,
                            currentAmount = currentAmount,
                            targetDate = entity.targetDate,
                            createdDate = "",
                            iconRes = R.drawable.bag,
                            backgroundColor = getCategoryColor(entity.category)
                        )
                    }

                    val selectedGoal = savingsGoals.firstOrNull()

                    _state.update {
                        it.copy(
                            isLoading = false,
                            goals = savingsGoals,
                            selectedGoal = selectedGoal,
                            error = null
                        )
                    }

                    selectedGoal?.let { loadTransactions(it.id) }
                }
        }
    }

    private fun loadTransactions(goalId: String) {
        viewModelScope.launch {
            Log.d(TAG, "Loading transactions for goalId: $goalId")

            val goalName = _state.value.goals.find { it.id == goalId }?.name ?: run {
                Log.w(TAG, "Goal not found for id: $goalId")
                return@launch
            }

            Log.d(TAG, "Loading transactions for goal name: $goalName")

            combine(
                depositRepository.getDepositsByGoalName(goalName),
                withdrawalRepository.getWithdrawalsByGoalName(goalName)
            ) { deposits, withdrawals ->

                val transactions = mutableListOf<GoalTransaction>()

                deposits.forEach { deposit ->
                    transactions.add(
                        GoalTransaction(
                            id = "D${deposit.id}",
                            type = TransactionType.DEPOSIT,
                            amount = deposit.depositAmount,
                            reference = deposit.phoneNumber ?: deposit.selectedAccount ?: "N/A",
                            date = formatTimestamp(deposit.timestamp),
                            goalId = goalId
                        )
                    )
                }

                withdrawals.forEach { withdrawal ->
                    transactions.add(
                        GoalTransaction(
                            id = "W${withdrawal.id}",
                            type = TransactionType.WITHDRAWAL,
                            amount = withdrawal.withdrawalAmount,
                            reference = withdrawal.phoneNumber?.ifEmpty { withdrawal.selectedAccount },
                            date = formatTimestamp(withdrawal.timestamp),
                            goalId = goalId
                        )
                    )
                }

                transactions.sortedByDescending { it.date }
            }
                .catch { e -> Log.e(TAG, "Error loading transactions", e) }
                .collect { transactions ->
                    Log.d(TAG, "Loaded ${transactions.size} transactions for $goalName")
                    _state.update { it.copy(transactions = transactions) }
                }
        }
    }

    private fun handleGoalClicked(goalId: String) {
        Log.d(TAG, "Goal clicked: $goalId")
        val goal = _state.value.goals.find { it.id == goalId }
        _state.update { it.copy(selectedGoal = goal) }
        loadTransactions(goalId)
        viewModelScope.launch {
            _navigationEvents.send(GoalSavingsNavigation.NavigateToGoalDetails(goalId))
        }
    }

    private fun handleViewAllGoalsClicked() {
        viewModelScope.launch {
            _navigationEvents.send(GoalSavingsNavigation.NavigateToMyGoalsScreen)
        }
    }

    private fun handleAddGoalClicked() {
        viewModelScope.launch {
            _navigationEvents.send(GoalSavingsNavigation.NavigateToCreateGoal)
        }
    }

    private fun handleDepositClicked(goalId: String) {
        viewModelScope.launch {
            // ✅ Pass goalName so DepositScreen can pre-fill it
            val goalName = _state.value.goals.find { it.id == goalId }?.name ?: ""
            Log.d(TAG, "Navigate to deposit for goal: $goalId name: $goalName")
            _navigationEvents.send(GoalSavingsNavigation.NavigateToDeposit(goalId, goalName))
        }
    }

    private fun handleWithdrawClicked(goalId: String) {
        viewModelScope.launch {
            // ✅ Pass goalName so WithdrawScreen can pre-fill it
            val goalName = _state.value.goals.find { it.id == goalId }?.name ?: ""
            Log.d(TAG, "Navigate to withdraw for goal: $goalId name: $goalName")
            _navigationEvents.send(GoalSavingsNavigation.NavigateToWithdraw(goalId, goalName))
        }
    }

    private fun handleTransactionFilterChanged(filter: TransactionFilter) {
        _state.update { it.copy(selectedTransactionFilter = filter) }
    }

    private fun handleViewAllTransactionsClicked() {
        viewModelScope.launch {
            _navigationEvents.send(GoalSavingsNavigation.NavigateToAllTransactions)
        }
    }

    private fun handleLearnAboutSavingsClicked() {
        viewModelScope.launch {
            _navigationEvents.send(GoalSavingsNavigation.NavigateToLearnMore)
        }
    }

    fun setUserName(name: String) {
        _state.update { it.copy(userName = name) }
    }

    private fun formatTimestamp(timestamp: Long): String {
        val sdf = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault())
        return sdf.format(java.util.Date(timestamp))
    }

    private fun getCategoryColor(category: GoalCategory): androidx.compose.ui.graphics.Color {
        return when (category) {
            GoalCategory.TRAVELLING -> androidx.compose.ui.graphics.Color(0xFF2E7D32)
            GoalCategory.EDUCATION  -> androidx.compose.ui.graphics.Color(0xFF1976D2)
            GoalCategory.WEDDING    -> androidx.compose.ui.graphics.Color(0xFF880E4F)
            GoalCategory.EMERGENCY  -> androidx.compose.ui.graphics.Color(0xFFE65100)
            GoalCategory.HOUSE      -> androidx.compose.ui.graphics.Color(0xFF4527A0)
            GoalCategory.CAR        -> androidx.compose.ui.graphics.Color(0xFF00695C)
            GoalCategory.BUSINESS   -> androidx.compose.ui.graphics.Color(0xFF37474F)
            GoalCategory.OTHER      -> androidx.compose.ui.graphics.Color(0xFF558B2F)
        }
    }
}