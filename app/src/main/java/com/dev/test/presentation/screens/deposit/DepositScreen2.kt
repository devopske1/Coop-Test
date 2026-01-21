package com.dev.test.presentation.screens.deposit


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DepositScreen2(
    navController: NavController,
    onNavigateBack: () -> Unit = {},
    onDeposit: () -> Unit = {}
) {
    var selectedGoal by remember { mutableStateOf("Dubai Trip") }
    var selectedDepositTo by remember { mutableStateOf("Coop Account") }
    var selectedAccount by remember { mutableStateOf("Salary Account\n01109014524202") }
    var DepositAmount by remember { mutableStateOf("100.00") }
    var expandedGoal by remember { mutableStateOf(false) }
    var expandedAccount by remember { mutableStateOf(false) }

    val goals = listOf("Dubai Trip", "Egypt Trip", "Savings")
    val accounts = listOf(
        "Salary Account\n011090145241207",
        "Savings Account\n01109014524208"
    )

    var showSuccessDialog by remember { mutableStateOf(false) }

//    val Gilroy = FontFamily(
//        Font(R.font.gilroy_medium, FontWeight.Medium)
//    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Deposit",
                        color = Color(0xFFFDFDFD),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1B4332)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Form Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(24.dp)
            ) {
                // Goal Name Dropdown
                Text(
                    text = "Goal Name",
                    fontSize = 12.sp,
                    color = Color(0xFF707070),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                ExposedDropdownMenuBox(
                    expanded = expandedGoal,
                    onExpandedChange = { expandedGoal = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    OutlinedTextField(
                        value = selectedGoal,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Dropdown",
                                tint = Color.Gray
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.LightGray,
                            unfocusedBorderColor = Color.LightGray,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        singleLine = true
                    )

                    ExposedDropdownMenu(
                        expanded = expandedGoal,
                        onDismissRequest = { expandedGoal = false }
                    ) {
                        goals.forEach { goal ->
                            DropdownMenuItem(
                                text = { Text(goal) },
                                onClick = {
                                    selectedGoal = goal
                                    expandedGoal = false
                                }
                            )
                        }
                    }
                }

                // Available Balance
                Row(
                    modifier = Modifier.padding(bottom = 20.dp)
                ) {
                    Text(
                        text = "Available balance: ",
                        fontSize = 12.sp,
                        color = Color(0xFF707070) // gray
                    )

                    Text(
                        text = "900.00 KES",
                        fontSize = 12.sp,
                        color = Color(0xFF7CB342) // green
                    )
                }

                // Deposit to Section
                Text(
                    text = "Deposit to:",
                    fontSize = 12.sp,
                    color = Color(0xFF707070),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Radio Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedDepositTo == "Coop Account",
                            onClick = { selectedDepositTo = "Coop Account" },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFF7CB342)
                            )
                        )
                        Text(
                            text = "Coop Account",
                            fontSize = 16.sp,
                            color = Color(0xFF7AC143),
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }


                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedDepositTo == "M-PESA",
                            onClick = { selectedDepositTo = "M-PESA" },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFF7CB342)
                            )
                        )
                        Text(
                            text = "M-PESA",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }

                // Credit Account Dropdown
                Text(
                    text = "Credit Account",
                    fontSize = 12.sp,
                    color = Color(0xFF707070),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                ExposedDropdownMenuBox(
                    expanded = expandedAccount,
                    onExpandedChange = { expandedAccount = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    OutlinedTextField(
                        value = selectedAccount,
                        onValueChange = {},
                        readOnly = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Account",
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Dropdown",
                                tint = Color.Gray
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.LightGray,
                            unfocusedBorderColor = Color.LightGray,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        maxLines = 2
                    )

                    ExposedDropdownMenu(
                        expanded = expandedAccount,
                        onDismissRequest = { expandedAccount = false }
                    ) {
                        accounts.forEach { account ->
                            DropdownMenuItem(
                                text = { Text(account) },
                                onClick = {
                                    selectedAccount = account
                                    expandedAccount = false
                                }
                            )
                        }
                    }
                }

                // Available Balance for Account
                Row(
                    modifier = Modifier.padding(bottom = 20.dp)
                ) {
                    Text(
                        text = "Available balance: ",
                        fontSize = 12.sp,
                        color = Color(0xFF565555) // gray
                    )

                    Text(
                        text = "19000.00 KES",
                        fontSize = 12.sp,
                        color = Color(0xFF7CB342) // green
                    )
                }

                // Amount to Deposit
                Text(
                    text = "Amount to Deposit",
                    fontSize = 12.sp,
                    color = Color(0xFF707070),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = DepositAmount,
                    onValueChange = { DepositAmount = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    leadingIcon = {
                        Text(
                            text = "KES",
                            fontSize = 14.sp,
                            color = Color(0xFF272626),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.LightGray,
                        unfocusedBorderColor = Color.LightGray,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Deposit Button
            Button(
                onClick = {
                    onDeposit()
                    showSuccessDialog = true   // 🔥 show dialog
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7CB342)
                ),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = "Deposit",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            if (showSuccessDialog) {
                DepositSuccessDialog(
                    amount = "100.00 KES",
                    onDismiss = { showSuccessDialog = false },
                    onGoToMyGoals = {
                        showSuccessDialog = false
                        // navigate to My Goals here
                    }
                )
            }
        }
    }}

//@Preview(showBackground = true, heightDp = 800, widthDp = 400)
//@Composable
//fun PreviewDepositScreen() {
//    MaterialTheme {
//        DepositScreen2()
//    }
//}