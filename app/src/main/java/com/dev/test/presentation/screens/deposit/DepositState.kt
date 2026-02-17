//package com.dev.test.presentation.screens.deposit
//
//
//data class DepositState(
//    val goalName: String = "Dubai Trip",
//    val availableBalance: Double = 900.00,
//
//    val destination: DepositDestination = DepositDestination.MPESA,
//
//    val phoneNumber: String = "",
//    val selectedAccount: String = "",
//    val depositAmount: String = "",
//
//    val isLoading: Boolean = false,
//    val isSuccess: Boolean = false,
//    val error: String? = null
//) {
//    val isValid: Boolean
//        get() = depositAmount.toDoubleOrNull()?.let { it > 0 } == true &&
//                when (destination) {
//                    DepositDestination.MPESA -> phoneNumber.length >= 10
//                    DepositDestination.COOP_ACCOUNT -> selectedAccount.isNotBlank()
//                }
//}
//
//
//data class DepositRequest(
//    val goalName: String,
//    val destination: DepositDestination,
//    val amount: Double,
//    val phoneNumber: String? = null,
//    val accountNumber: String? = null
//)
//
//
////
//
//sealed class DepositIntent {
//    data class OnGoalNameChanged(val name: String) : DepositIntent()
//    data class OnPhoneNumberChanged(val phone: String) : DepositIntent()
//    data class OnAmountChanged(val amount: String) : DepositIntent()
//    data class OnDestinationChanged(val destination: DepositDestination) : DepositIntent()
//    data class OnAccountSelected(val account: String) : DepositIntent()
//    object OnDepositClicked : DepositIntent()
//    object OnSuccessDismissed : DepositIntent()
//}
//
//enum class DepositDestination {
//    MPESA,
//    COOP_ACCOUNT
//}
//sealed class DepositNavigation {
//    data object NavigateBack : DepositNavigation()
//}