package com.dev.test.presentation.screens.withdraw

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dev.test.presentation.components.AmountField
import com.dev.test.presentation.components.SuccessDialog
import kotlinx.coroutines.flow.collectLatest



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WithdrawScreen(
    navController: NavController,
    viewModel: WithdrawViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    val coopAccounts = listOf(
        "Salary Account •••• 2341",
        "Savings Account •••• 9823"
    )

    LaunchedEffect(Unit) {
        viewModel.navigation.collectLatest {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Withdraw",  color = Color(0xFFFDFDFD),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Close, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(Color(0xFF1B4332))
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Text("Goal Name", fontSize = 12.sp, color = Color.Gray)

            OutlinedTextField(
                value = state.goalName,
                onValueChange = {
                    viewModel.processIntent(
                        WithdrawIntent.OnPhoneNumberChanged(it)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("0712345678") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF7CB342),
                    unfocusedBorderColor = Color.LightGray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp)
            )

            Row(modifier = Modifier.wrapContentSize()) {
                Text(
                    "Available balance: ",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
                Text(
                    "${state.availableBalance} KES",
                    color = Color(0xFF8BC34A),
                    fontSize = 12.sp
                )
            }

            WithdrawDestinationSelector(
                state.destination,
                onSelect = { viewModel.processIntent(WithdrawIntent.OnDestinationChanged(it)) }
            )

            if (state.destination == WithdrawDestination.MPESA) {

                Column {
                    Text(
                        text = "Phone Number",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = state.phoneNumber,
                        onValueChange = {
                            viewModel.processIntent(
                                WithdrawIntent.OnPhoneNumberChanged(it)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("0712345678") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF7CB342),
                            unfocusedBorderColor = Color.LightGray,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }
            else {
                var expanded by remember { mutableStateOf(false) }

                Column {
                    Text(
                        text = "Credit Account",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = state.selectedAccount,
                            onValueChange = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            placeholder = { Text("Select account") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            readOnly = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF7CB342),
                                unfocusedBorderColor = Color.LightGray,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            coopAccounts.forEach { account ->
                                DropdownMenuItem(
                                    text = { Text(account) },
                                    onClick = {
                                        expanded = false
                                        viewModel.processIntent(
                                            WithdrawIntent.OnAccountSelected(account)
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }



            AmountField(
                "Amount to withdraw",
                state.withdrawAmount
            ) {
                viewModel.processIntent(WithdrawIntent.OnAmountChanged(it))
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = { viewModel.processIntent(WithdrawIntent.OnWithdrawClicked) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = state.isValid && !state.isLoading,
                colors = ButtonDefaults.buttonColors(Color(0xFF7CB342)),
                shape = RoundedCornerShape(10.dp)
            ) {
                if (state.isLoading)
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                else
                    Text("Withdraw", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        if (state.isSuccess) {
            SuccessDialog(
                title = "100.00 KES",
                subtitle = "Withdraw Successful",
                buttonText = "Go to My Goals",
                onDismiss = {
                    viewModel.processIntent(WithdrawIntent.OnSuccessDismissed)
                },
                onButtonClick = {
                    viewModel.processIntent(WithdrawIntent.OnSuccessDismissed)
                }
            )
        }
        }
    }


@Composable
fun WithdrawDestinationSelector(
    selected: WithdrawDestination,
    onSelect: (WithdrawDestination) -> Unit
) {
    Column {
        Text(
            text = "Withdraw to:",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            DestinationOption(
                label = "Coop Account",
                selected = selected == WithdrawDestination.COOP_ACCOUNT,
                onClick = { onSelect(WithdrawDestination.COOP_ACCOUNT) }
            )

            DestinationOption(
                label = "M-PESA",
                selected = selected == WithdrawDestination.MPESA,
                onClick = { onSelect(WithdrawDestination.MPESA) }
            )
        }
    }
}

@Composable
private fun DestinationOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFF8BC34A),
                unselectedColor = Color.LightGray
            )
        )
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal,
            color = if (selected) Color(0xFF8BC34A) else Color.Black
        )
    }
}

@Preview(showBackground = true, heightDp = 800, widthDp = 400)
@Composable
fun Preview_WithdrawScreen() {
    MaterialTheme {
        WithdrawScreen(rememberNavController())


    }
}