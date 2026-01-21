package com.dev.test.presentation.screens.withdraw


data class WithdrawState(
    val goalName: String = "Dubai Trip",
    val availableBalance: Double = 900.00,

    val destination: WithdrawDestination = WithdrawDestination.MPESA,

    val phoneNumber: String = "",
    val selectedAccount: String = "",
    val withdrawAmount: String = "",

    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
) {
    val isValid: Boolean
        get() = withdrawAmount.toDoubleOrNull()?.let { it > 0 } == true &&
                when (destination) {
                    WithdrawDestination.MPESA -> phoneNumber.length >= 10
                    WithdrawDestination.COOP_ACCOUNT -> selectedAccount.isNotBlank()
                }
}


data class WithdrawRequest(
    val goalName: String,
    val destination: WithdrawDestination,
    val amount: Double,
    val phoneNumber: String? = null,
    val accountNumber: String? = null
)


enum class WithdrawDestination {
    COOP_ACCOUNT,
    MPESA
}
sealed class WithdrawIntent {
    data class OnDestinationChanged(val destination: WithdrawDestination) : WithdrawIntent()
    data class OnPhoneNumberChanged(val value: String) : WithdrawIntent()
    data class OnAccountSelected(val account: String) : WithdrawIntent()
    data class OnAmountChanged(val value: String) : WithdrawIntent()

    data object OnWithdrawClicked : WithdrawIntent()
    data object OnSuccessDismissed : WithdrawIntent()
}

sealed class WithdrawNavigation {
    data object NavigateBack : WithdrawNavigation()
}