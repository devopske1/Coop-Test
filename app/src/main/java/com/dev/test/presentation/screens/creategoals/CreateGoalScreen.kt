package com.dev.test.presentation.screens.creategoals


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dev.test.presentation.dashboard.CreateGoalIntent
import com.dev.test.presentation.dashboard.CreateGoalNavigation
import com.dev.test.presentation.dashboard.GoalCategory
import com.dev.test.presentation.components.SuccessDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGoalScreen(
    navController: NavController,
    viewModel: CreateGoalViewModel
) {
    val state by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()

    val categories = listOf("Travelling", "Education", "Shopping", "Emergency", "Other")

    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                is CreateGoalNavigation.NavigateToGoalsList -> {
                    navController.popBackStack()
                }

                CreateGoalNavigation.NavigateBack -> TODO()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Create a Goal",
                        color = Color(0xFFFDFDFD),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Close, "Close", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1B5E4F))
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .background(Color(0xFFF5F5F5))
        ) {
            // Form Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(24.dp)
            ) {

                // Goal Name - Use ViewModel state
                Text(
                    "Goal Name",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = state.goalName,
                    onValueChange = { viewModel.processIntent(CreateGoalIntent.OnGoalNameChanged(it)) },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                    singleLine = true
                )

                // Category - Use ViewModel state
                Text(
                    "Goal Category",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                ExposedDropdownMenuBox(
                    expanded = state.showCategoryDropdown,
                    onExpandedChange = { viewModel.processIntent(CreateGoalIntent.OnCategoryDropdownToggled) },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
                ) {
                    OutlinedTextField(
                        value = state.selectedCategory?.name ?: "Select Category",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = state.showCategoryDropdown,
                        onDismissRequest = { viewModel.processIntent(CreateGoalIntent.OnCategoryDropdownToggled) }
                    ) {
                        GoalCategory.values().forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = {
                                    viewModel.processIntent(
                                        CreateGoalIntent.OnCategorySelected(
                                            category
                                        )
                                    )
                                }
                            )
                        }
                    }
                }

                // Target Amount - Use ViewModel state
                Text(
                    "Target Amount",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = state.targetAmount,
                    onValueChange = {
                        viewModel.processIntent(
                            CreateGoalIntent.OnTargetAmountChanged(
                                it
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                    leadingIcon = { Text("KES", modifier = Modifier.padding(start = 12.dp)) },
                    singleLine = true
                )

                // Target Date - Use ViewModel state
                Text(
                    "Savings Target Date",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = state.targetDate,
                    onValueChange = {
                        viewModel.processIntent(
                            CreateGoalIntent.OnTargetDateChanged(
                                it
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                    trailingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Create Goal Button
            Button(
                onClick = {
                    viewModel.processIntent(CreateGoalIntent.OnCreateGoalClicked)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7CB342)
                ),
                shape = MaterialTheme.shapes.small
            ){
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        "Create a Goal",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }

    // Show error Snackbar
    state.error?.let { errorMsg ->
        LaunchedEffect(errorMsg) {
            // Show snackbar or toast
            viewModel.processIntent(CreateGoalIntent.OnErrorDismissed)
        }
    }

    // Show success dialog

    if (state.isSuccess) {
        SuccessDialog(
            title = state.createdGoalName ?: "Your Goal",
            subtitle = "Goal Created Successfully",
            message = "You are one step closer to\nreaching your target",
            buttonText = "Go to My Goals",
            onDismiss = {
                viewModel.processIntent(CreateGoalIntent.OnSuccessDialogDismissed)
            },
            onButtonClick = {
                viewModel.processIntent(CreateGoalIntent.OnSuccessDialogDismissed)
            }
        )
    }
}