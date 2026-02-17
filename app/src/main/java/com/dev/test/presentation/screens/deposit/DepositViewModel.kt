package com.dev.test.presentation.screens.deposit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.test.data.local.entities.DepositEntity
import com.dev.test.data.repository.DepositRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DepositViewModel @Inject constructor(
    private val depositRepository: DepositRepository
) : ViewModel() {

    companion object {
        private const val TAG = "DepositViewModel"
    }

    private val _state = MutableStateFlow(DepositState())
    val state = _state.asStateFlow()

    private val _navigation = MutableSharedFlow<Unit>()
    val navigation = _navigation.asSharedFlow()

    fun processIntent(intent: DepositIntent) {
        Log.d(TAG, "processIntent: $intent")
        when (intent) {
            is DepositIntent.OnGoalNameChanged -> {
                Log.d(TAG, "Goal Name changed to: ${intent.name}")
                _state.update { it.copy(goalName = intent.name) }
                validateForm()
            }
            is DepositIntent.OnPhoneNumberChanged -> {
                Log.d(TAG, "Phone Number changed to: ${intent.phone}")
                _state.update { it.copy(phoneNumber = intent.phone) }
                validateForm()
            }
            is DepositIntent.OnAmountChanged -> {
                Log.d(TAG, "Amount changed to: ${intent.amount}")
                _state.update { it.copy(depositAmount = intent.amount) }
                validateForm()
            }
            is DepositIntent.OnDestinationChanged -> {
                Log.d(TAG, "Destination changed to: ${intent.destination}")
                _state.update {
                    it.copy(
                        destination = intent.destination,
                        phoneNumber = "",
                        selectedAccount = ""
                    )
                }
                validateForm()
            }
            is DepositIntent.OnAccountSelected -> {
                Log.d(TAG, "Account selected: ${intent.account}")
                _state.update { it.copy(selectedAccount = intent.account) }
                validateForm()
            }
            DepositIntent.OnDepositClicked -> {
                Log.d(TAG, "Deposit button clicked!")
                Log.d(TAG, "Current state: ${_state.value}")
                performDeposit()
            }
            DepositIntent.OnSuccessDismissed -> {
                Log.d(TAG, "Success dialog dismissed")
                _state.update { it.copy(isSuccess = false) }
                viewModelScope.launch {
                    _navigation.emit(Unit)
                }
            }
        }
    }

    private fun validateForm() {
        val currentState = _state.value
        val isValid = when {
            currentState.goalName.isBlank() -> {
                Log.d(TAG, "Validation failed: Goal name is blank")
                false
            }
            currentState.depositAmount.isBlank() -> {
                Log.d(TAG, "Validation failed: Amount is blank")
                false
            }
            currentState.depositAmount.toDoubleOrNull() == null -> {
                Log.d(TAG, "Validation failed: Amount is not a valid number")
                false
            }
            currentState.depositAmount.toDoubleOrNull()!! <= 0 -> {
                Log.d(TAG, "Validation failed: Amount is <= 0")
                false
            }
            currentState.destination == DepositDestination.MPESA &&
                    currentState.phoneNumber.isBlank() -> {
                Log.d(TAG, "Validation failed: Phone number is blank for M-PESA")
                false
            }
            currentState.destination == DepositDestination.COOP_ACCOUNT &&
                    currentState.selectedAccount.isBlank() -> {
                Log.d(TAG, "Validation failed: Account not selected for Coop")
                false
            }
            else -> {
                Log.d(TAG, "Validation passed!")
                true
            }
        }
        _state.update { it.copy(isValid = isValid) }
        Log.d(TAG, "Form is valid: $isValid")
    }

    private fun performDeposit() {
        viewModelScope.launch {
            try {
                Log.d(TAG, "=== STARTING DEPOSIT PROCESS ===")
                _state.update { it.copy(isLoading = true) }
                Log.d(TAG, "Loading state set to true")

                // Log the current state
                val currentState = _state.value
                Log.d(TAG, "Goal Name: ${currentState.goalName}")
                Log.d(TAG, "Destination: ${currentState.destination}")
                Log.d(TAG, "Phone Number: ${currentState.phoneNumber}")
                Log.d(TAG, "Selected Account: ${currentState.selectedAccount}")
                Log.d(TAG, "Amount: ${currentState.depositAmount}")
                Log.d(TAG, "Available Balance: ${currentState.availableBalance}")

                // Create deposit entity
                val deposit = DepositEntity(
                    goalName = currentState.goalName,
                    destination = currentState.destination.name,
                    phoneNumber = if (currentState.destination == DepositDestination.MPESA)
                        currentState.phoneNumber else null,
                    selectedAccount = if (currentState.destination == DepositDestination.COOP_ACCOUNT)
                        currentState.selectedAccount else null,
                    depositAmount = currentState.depositAmount.toDouble(),
                    availableBalance = currentState.availableBalance,
                    status = "SUCCESS"
                )

                Log.d(TAG, "Created DepositEntity: $deposit")

                // Save to database
                Log.d(TAG, "Attempting to save to database...")
                val insertedId = depositRepository.insertDeposit(deposit)
                Log.d(TAG, "✅ SUCCESSFULLY SAVED TO DATABASE! ID: $insertedId")

                // Simulate network delay
                kotlinx.coroutines.delay(1500)

                _state.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                }
                Log.d(TAG, "=== DEPOSIT PROCESS COMPLETED ===")

            } catch (e: Exception) {
                Log.e(TAG, "❌ ERROR SAVING DEPOSIT", e)
                Log.e(TAG, "Error message: ${e.message}")
                Log.e(TAG, "Error stacktrace: ${e.stackTraceToString()}")
                _state.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = false
                    )
                }
            }
        }
    }
}

// State data class
data class DepositState(
    val goalName: String = "",
    val phoneNumber: String = "",
    val selectedAccount: String = "",
    val depositAmount: String = "",
    val availableBalance: Double = 5000.00,
    val destination: DepositDestination = DepositDestination.COOP_ACCOUNT,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isValid: Boolean = false
)

// Intent sealed class
sealed class DepositIntent {
    data class OnGoalNameChanged(val name: String) : DepositIntent()
    data class OnPhoneNumberChanged(val phone: String) : DepositIntent()
    data class OnAmountChanged(val amount: String) : DepositIntent()
    data class OnDestinationChanged(val destination: DepositDestination) : DepositIntent()
    data class OnAccountSelected(val account: String) : DepositIntent()
    object OnDepositClicked : DepositIntent()
    object OnSuccessDismissed : DepositIntent()
}

// Enum for deposit destinations
enum class DepositDestination {
    MPESA,
    COOP_ACCOUNT
}