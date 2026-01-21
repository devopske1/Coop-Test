package com.dev.test.presentation.screens.creategoals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.test.presentation.dashboard.CreateGoalIntent
import com.dev.test.presentation.dashboard.CreateGoalNavigation
import com.dev.test.presentation.dashboard.CreateGoalRequest
import com.dev.test.presentation.dashboard.CreateGoalState
import com.dev.test.presentation.dashboard.GoalCategory
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
 * ViewModel for Create Goal screen following MVI architecture
 */
@HiltViewModel
class CreateGoalViewModel @Inject constructor(
//    private val goalsRepository: GoalsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CreateGoalState())
    val state: StateFlow<CreateGoalState> = _state.asStateFlow()

    private val _navigationEvents = Channel<CreateGoalNavigation>()
    val navigationEvents = _navigationEvents.receiveAsFlow()

    /**
     * Process user intents
     */
    fun processIntent(intent: CreateGoalIntent) {
        when (intent) {
            is CreateGoalIntent.OnGoalNameChanged -> handleGoalNameChanged(intent.name)
            is CreateGoalIntent.OnCategorySelected -> handleCategorySelected(intent.category)
            is CreateGoalIntent.OnTargetAmountChanged -> handleTargetAmountChanged(intent.amount)
            is CreateGoalIntent.OnTargetDateChanged -> handleTargetDateChanged(intent.date)
            is CreateGoalIntent.OnCategoryDropdownToggled -> toggleCategoryDropdown()
            is CreateGoalIntent.OnDatePickerToggled -> toggleDatePicker()
            is CreateGoalIntent.OnCreateGoalClicked -> createGoal()
            is CreateGoalIntent.OnSuccessDialogDismissed -> handleSuccessDialogDismissed()
            is CreateGoalIntent.OnErrorDismissed -> dismissError()
        }
    }

    private fun handleGoalNameChanged(name: String) {
        _state.update { it.copy(goalName = name) }
    }

    private fun handleCategorySelected(category: GoalCategory) {
        _state.update {
            it.copy(
                selectedCategory = category,
                showCategoryDropdown = false
            )
        }
    }

    private fun handleTargetAmountChanged(amount: String) {
        // Filter to allow only numbers and one decimal point
        val filtered = amount.filter { it.isDigit() || it == '.' }
            .let { str ->
                // Ensure only one decimal point
                val parts = str.split(".")
                if (parts.size > 2) {
                    parts[0] + "." + parts.drop(1).joinToString("")
                } else {
                    str
                }
            }

        _state.update { it.copy(targetAmount = filtered) }
    }

    private fun handleTargetDateChanged(date: String) {
        _state.update {
            it.copy(
                targetDate = date,
                showDatePicker = false
            )
        }
    }

    private fun toggleCategoryDropdown() {
        _state.update { it.copy(showCategoryDropdown = !it.showCategoryDropdown) }
    }

    private fun toggleDatePicker() {
        _state.update { it.copy(showDatePicker = !it.showDatePicker) }
    }

    private fun createGoal() {
        val currentState = _state.value

        // Validate form
        if (!currentState.isValid) {
            _state.update { it.copy(error = "Please fill in all fields correctly") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val request = CreateGoalRequest(
                name = currentState.goalName.trim(),
                category = currentState.selectedCategory!!,
                targetAmount = currentState.targetAmount.toDouble(),
                targetDate = currentState.targetDate
            )
            delay(3000L)
            _state.update {
                it.copy(
                    isLoading = false,
                    isSuccess = true,
                    createdGoalName = currentState.goalName
                )
            }

           /* val result = goalsRepository.createGoal(request)

            when (result) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true,
                            createdGoalName = currentState.goalName
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.message ?: "Failed to create goal"
                        )
                    }
                }
                is Resource.Loading -> {
                    // Already handled
                }
            }*/
        }
    }

    private fun handleSuccessDialogDismissed() {
        viewModelScope.launch {
            _navigationEvents.send(CreateGoalNavigation.NavigateToGoalsList)
        }
    }

    private fun dismissError() {
        _state.update { it.copy(error = null) }
    }
}