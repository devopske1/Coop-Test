package com.dev.test.presentation.screens.dashboard


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coop.feature_goals.presentation.GoalSavingsIntent
import com.coop.feature_goals.presentation.GoalSavingsNavigation
import com.coop.feature_goals.presentation.GoalSavingsSampleData
import com.coop.feature_goals.presentation.GoalSavingsState
import com.coop.feature_goals.presentation.TransactionFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Goal Savings Dashboard following MVI architecture
 */
@HiltViewModel
class GoalSavingsViewModel @Inject constructor(
    // private val getGoalsUseCase: GetGoalsUseCase // For integration
) : ViewModel() {

    private val _state = MutableStateFlow(GoalSavingsState())
    val state: StateFlow<GoalSavingsState> = _state.asStateFlow()

    private val _navigationEvents = Channel<GoalSavingsNavigation>()
    val navigationEvents = _navigationEvents.receiveAsFlow()

    init {
        processIntent(GoalSavingsIntent.LoadGoals)
    }

    /**
     * Process user intents
     */
    fun processIntent(intent: GoalSavingsIntent) {
        when (intent) {
            is GoalSavingsIntent.OnViewGoals -> handleViewAllGoalsGoalClicked()
            is GoalSavingsIntent.LoadGoals -> loadGoals()
            is GoalSavingsIntent.RefreshGoals -> refreshGoals()
            is GoalSavingsIntent.OnGoalClicked -> handleGoalClicked(intent.goalId)
            is GoalSavingsIntent.OnAddGoalClicked -> handleAddGoalClicked()
            is GoalSavingsIntent.OnDepositClicked -> handleDepositClicked(intent.goalId)
            is GoalSavingsIntent.OnWithdrawClicked -> handleWithdrawClicked(intent.goalId)
            is GoalSavingsIntent.OnTransactionFilterChanged -> handleTransactionFilterChanged(intent.filter)
            is GoalSavingsIntent.OnViewAllTransactionsClicked -> handleViewAllTransactionsClicked()
            is GoalSavingsIntent.OnLearnAboutSavingsClicked -> handleLearnAboutSavingsClicked()
            is GoalSavingsIntent.OnErrorDismissed -> dismissError()
        }
    }

    private fun loadGoals() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            // Simulation - replace with actual implementation
            delay(2000L)

            // Simulate loading goals - you can change this to test different states
            val goals = GoalSavingsSampleData.sampleGoals
            val selectedGoal = goals.firstOrNull()
            val transactions = if (selectedGoal != null) {
                GoalSavingsSampleData.sampleTransactions
            } else {
                emptyList()
            }

            _state.update {
                it.copy(
                    isLoading = false,
                    goals = goals,
                    selectedGoal = selectedGoal,
                    transactions = transactions,
                    error = null
                )
            }

            /* For integration:
            getGoalsUseCase().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        val goals = result.data ?: emptyList()
                        _state.update {
                            it.copy(
                                isLoading = false,
                                goals = goals,
                                selectedGoal = goals.firstOrNull(),
                                error = null
                            )
                        }
                        // Load transactions for selected goal
                        goals.firstOrNull()?.let { goal ->
                            loadTransactions(goal.id)
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = result.message ?: "Failed to load goals"
                            )
                        }
                    }
                }
            }
            */
        }
    }

    private fun refreshGoals() {
        loadGoals()
    }

    private fun handleViewAllGoalsGoalClicked() {
        viewModelScope.launch {
            _navigationEvents.send(GoalSavingsNavigation.NavigateToMyGoalsScreen)
        }
    }
    private fun handleGoalClicked(goalId: String) {
        val goal = _state.value.goals.find { it.id == goalId }
        _state.update { it.copy(selectedGoal = goal) }

        viewModelScope.launch {
            _navigationEvents.send(GoalSavingsNavigation.NavigateToGoalDetails(goalId))
        }
    }

    private fun handleAddGoalClicked() {
        viewModelScope.launch {
            _navigationEvents.send(GoalSavingsNavigation.NavigateToCreateGoal)
        }
    }

    private fun handleDepositClicked(goalId: String) {
        viewModelScope.launch {
            _navigationEvents.send(GoalSavingsNavigation.NavigateToDeposit(goalId))
        }
    }

    private fun handleWithdrawClicked(goalId: String) {
        viewModelScope.launch {
            _navigationEvents.send(GoalSavingsNavigation.NavigateToWithdraw(goalId))
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

    private fun dismissError() {
        _state.update { it.copy(error = null) }
    }

    fun setUserName(name: String) {
        _state.update { it.copy(userName = name) }
    }

    /**
     * Load transactions for a specific goal
     */
    private fun loadTransactions(goalId: String) {
        viewModelScope.launch {
            // Simulate loading transactions
            delay(500L)
            val transactions = GoalSavingsSampleData.sampleTransactions
                .filter { it.goalId == goalId }

            _state.update { it.copy(transactions = transactions) }
        }
    }
}

/**
 * For integration:
 * Use case for getting goals
 */
/*
class GetGoalsUseCase @Inject constructor(
    private val repository: GoalSavingsRepository
) {
    operator fun invoke(): Flow<Resource<List<SavingsGoal>>> {
        return repository.getGoals()
    }
}
*/

/**
 * Repository interface
 */
/*
interface GoalSavingsRepository {
    fun getGoals(): Flow<Resource<List<SavingsGoal>>>
    suspend fun createGoal(goal: SavingsGoal): Resource<Unit>
    suspend fun depositToGoal(goalId: String, amount: Double): Resource<Unit>
    suspend fun withdrawFromGoal(goalId: String, amount: Double): Resource<Unit>
    suspend fun deleteGoal(goalId: String): Resource<Unit>
    fun getTransactions(goalId: String): Flow<Resource<List<GoalTransaction>>>
}
*/