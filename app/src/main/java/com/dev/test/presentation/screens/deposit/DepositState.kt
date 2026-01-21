package com.coop.feature_goals.presentation.deposit


data class DepositState(
    val goalName: String = "Dubai Trip",
    val availableBalance: Double = 900.00,

    val destination: DepositDestination = DepositDestination.MPESA,

    val phoneNumber: String = "",
    val selectedAccount: String = "",
    val depositAmount: String = "",

    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
) {
    val isValid: Boolean
        get() = depositAmount.toDoubleOrNull()?.let { it > 0 } == true &&
                when (destination) {
                    DepositDestination.MPESA -> phoneNumber.length >= 10
                    DepositDestination.COOP_ACCOUNT -> selectedAccount.isNotBlank()
                }
}


data class DepositRequest(
    val goalName: String,
    val destination: DepositDestination,
    val amount: Double,
    val phoneNumber: String? = null,
    val accountNumber: String? = null
)


enum class DepositDestination {
    COOP_ACCOUNT,
    MPESA
}
sealed class DepositIntent {
    data class OnDestinationChanged(val destination: DepositDestination) : DepositIntent()
    data class OnPhoneNumberChanged(val value: String) : DepositIntent()
    data class OnAccountSelected(val account: String) : DepositIntent()
    data class OnAmountChanged(val value: String) : DepositIntent()

    data object OnDepositClicked : DepositIntent()
    data object OnSuccessDismissed : DepositIntent()
}

sealed class DepositNavigation {
    data object NavigateBack : DepositNavigation()
}