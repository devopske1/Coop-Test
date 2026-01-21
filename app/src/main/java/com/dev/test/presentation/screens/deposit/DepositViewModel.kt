package com.coop.feature_goals.presentation.deposit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DepositViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(DepositState())
    val state: StateFlow<DepositState> = _state.asStateFlow()

    private val _navigation = Channel<DepositNavigation>()
    val navigation = _navigation.receiveAsFlow()

    fun processIntent(intent: DepositIntent) {
        when (intent) {
            is DepositIntent.OnDestinationChanged ->
                _state.update { it.copy(destination = intent.destination) }

            is DepositIntent.OnPhoneNumberChanged ->
                _state.update { it.copy(phoneNumber = intent.value.filter(Char::isDigit)) }

            is DepositIntent.OnAccountSelected ->
                _state.update { it.copy(selectedAccount = intent.account) }

            is DepositIntent.OnAmountChanged ->
                _state.update {
                    it.copy(depositAmount = intent.value.filter { c -> c.isDigit() || c == '.' })
                }

            DepositIntent.OnDepositClicked -> deposit()
            DepositIntent.OnSuccessDismissed -> navigateBack()
        }
    }

    private fun deposit() {
        if (!_state.value.isValid) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            delay(2500)

            _state.update {
                it.copy(
                    isLoading = false,
                    isSuccess = true
                )
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _navigation.send(DepositNavigation.NavigateBack)
        }
    }
}
