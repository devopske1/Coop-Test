package com.dev.test.presentation.screens.withdraw


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WithdrawViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(WithdrawState())
    val state: StateFlow<WithdrawState> = _state.asStateFlow()

    private val _navigation = Channel<WithdrawNavigation>()
    val navigation = _navigation.receiveAsFlow()

    fun processIntent(intent: WithdrawIntent) {
        when (intent) {
            is WithdrawIntent.OnDestinationChanged ->
                _state.update { it.copy(destination = intent.destination) }

            is WithdrawIntent.OnPhoneNumberChanged ->
                _state.update { it.copy(phoneNumber = intent.value.filter(Char::isDigit)) }

            is WithdrawIntent.OnAccountSelected ->
                _state.update { it.copy(selectedAccount = intent.account) }

            is WithdrawIntent.OnAmountChanged ->
                _state.update {
                    it.copy(withdrawAmount = intent.value.filter { c -> c.isDigit() || c == '.' })
                }

            WithdrawIntent.OnWithdrawClicked -> withdraw()
            WithdrawIntent.OnSuccessDismissed -> navigateBack()
        }
    }

    private fun withdraw() {
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
            _navigation.send(WithdrawNavigation.NavigateBack)
        }
    }
}
