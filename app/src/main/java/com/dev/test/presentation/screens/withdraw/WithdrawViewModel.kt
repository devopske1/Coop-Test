package com.dev.test.presentation.screens.withdraw

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.test.data.local.entities.WithdrawalEntity
import com.dev.test.data.repository.WithdrawalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WithdrawViewModel @Inject constructor(
    private val withdrawalRepository: WithdrawalRepository,
    savedStateHandle: SavedStateHandle     // Reads goalName passed via navigation
) : ViewModel() {

    companion object {
        private const val TAG = "WithdrawViewModel"
    }

    private val _state = MutableStateFlow(WithdrawState())
    val state: StateFlow<WithdrawState> = _state.asStateFlow()

    private val _navigation = Channel<WithdrawNavigation>()
    val navigation = _navigation.receiveAsFlow()

    init {
        // Populate goalName from navigation argument automatically
        val goalName: String = savedStateHandle.get<String>("goalName") ?: ""
        Log.d(TAG, "Received goalName from nav: $goalName")
        _state.update { it.copy(goalName = goalName) }
    }

    fun processIntent(intent: WithdrawIntent) {
        when (intent) {
            is WithdrawIntent.OnGoalNameChanged ->
                _state.update { it.copy(goalName = intent.name) }

            is WithdrawIntent.OnDestinationChanged ->
                _state.update {
                    it.copy(
                        destination = intent.destination,
                        phoneNumber = "",
                        selectedAccount = ""
                    )
                }

            is WithdrawIntent.OnPhoneNumberChanged ->
                _state.update { it.copy(phoneNumber = intent.value.filter(Char::isDigit)) }

            is WithdrawIntent.OnAccountSelected ->
                _state.update { it.copy(selectedAccount = intent.account) }

            is WithdrawIntent.OnAmountChanged ->
                _state.update {
                    it.copy(withdrawAmount = intent.value.filter { c -> c.isDigit() || c == '.' })
                }

            WithdrawIntent.OnWithdrawClicked -> {
                Log.d(TAG, "Withdraw button clicked!")
                Log.d(TAG, "Current state: ${_state.value}")
                performWithdraw()
            }

            WithdrawIntent.OnSuccessDismissed -> navigateBack()
        }
    }

    private fun performWithdraw() {
        if (!_state.value.isValid) return

        viewModelScope.launch {
            try {
                Log.d(TAG, "=== STARTING WITHDRAWAL PROCESS ===")
                _state.update { it.copy(isLoading = true) }

                val s = _state.value
                Log.d(TAG, "Goal Name: ${s.goalName}")
                Log.d(TAG, "Destination: ${s.destination}")
                Log.d(TAG, "Phone Number: ${s.phoneNumber}")
                Log.d(TAG, "Selected Account: ${s.selectedAccount}")
                Log.d(TAG, "Amount: ${s.withdrawAmount}")
                Log.d(TAG, "Available Balance: ${s.availableBalance}")

                val withdrawal = WithdrawalEntity(
                    goalName = s.goalName,
                    destination = s.destination.name,
                    phoneNumber = if (s.destination == WithdrawDestination.MPESA)
                        s.phoneNumber else "",
                    selectedAccount = if (s.destination == WithdrawDestination.COOP_ACCOUNT)
                        s.selectedAccount else "",
                    withdrawalAmount = s.withdrawAmount.toDouble(),
                    availableBalance = s.availableBalance,
                    status = "SUCCESS"
                )

                Log.d(TAG, "Created WithdrawalEntity: $withdrawal")
                Log.d(TAG, "Attempting to save to database...")
                val insertedId = withdrawalRepository.insertWithdrawal(withdrawal)
                Log.d(TAG, "✅ SUCCESSFULLY SAVED TO DATABASE! ID: $insertedId")

                kotlinx.coroutines.delay(1500)
                _state.update { it.copy(isLoading = false, isSuccess = true) }
                Log.d(TAG, "=== WITHDRAWAL PROCESS COMPLETED ===")

            } catch (e: Exception) {
                Log.e(TAG, "❌ ERROR SAVING WITHDRAWAL", e)
                _state.update { it.copy(isLoading = false, isSuccess = false) }
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _navigation.send(WithdrawNavigation.NavigateBack)
        }
    }
}