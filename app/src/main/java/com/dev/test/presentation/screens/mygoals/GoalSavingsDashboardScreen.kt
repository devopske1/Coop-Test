package com.dev.test.presentation.screens.mygoals

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.coop.feature_goals.presentation.GoalSavingsIntent
import com.dev.test.R
import com.dev.test.presentation.navigation.AppRoute
import com.dev.test.presentation.screens.mygoals.GoalSavingsViewModel

/**
 * Goal Savings Dashboard Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalSavingsDashboardScreen(
    navController: NavController,
    viewModel: GoalSavingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    GoalSavingsContent(
        state = state,
        onGoalClick = { goalId ->
            viewModel.processIntent(GoalSavingsIntent.OnGoalClicked(goalId))
        },
        onAddGoalClick = {
            viewModel.processIntent(GoalSavingsIntent.OnAddGoalClicked)
            navController.navigate(AppRoute.CreateGoalScreen.route)
        },
        onDepositClick = { goalId ->
            viewModel.processIntent(GoalSavingsIntent.OnDepositClicked(goalId))
            navController.navigate(AppRoute.DepositScreen.route)
        },
        onWithdrawClick = { goalId ->
            viewModel.processIntent(GoalSavingsIntent.OnWithdrawClicked(goalId))
            navController.navigate(AppRoute.WithdrawScreen.route)
        },
        onTransactionFilterChanged = { filter ->
            viewModel.processIntent(GoalSavingsIntent.OnTransactionFilterChanged(filter))
        },
        onViewAllTransactionsClick = {
            viewModel.processIntent(GoalSavingsIntent.OnViewAllTransactionsClicked)
        },
        onLearnAboutSavingsClick = {
            viewModel.processIntent(GoalSavingsIntent.OnLearnAboutSavingsClicked)
        },
        onRefresh = {
            viewModel.processIntent(GoalSavingsIntent.RefreshGoals)
        }
    )
}

@Composable
private fun GoalSavingsContent(
    state: GoalSavingsState,
    onGoalClick: (String) -> Unit,
    onAddGoalClick: () -> Unit,
    onDepositClick: (String) -> Unit,
    onWithdrawClick: (String) -> Unit,
    onTransactionFilterChanged: (TransactionFilter) -> Unit,
    onViewAllTransactionsClick: () -> Unit,
    onLearnAboutSavingsClick: () -> Unit,
    onRefresh: () -> Unit
) {
    Scaffold(
        topBar = {
            GoalSavingsTopBar(userName = state.userName)
        },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoading -> {
                    LoadingContent()
                }
                state.error != null -> {
                    ErrorContent(
                        message = state.error,
                        onRetry = onRefresh
                    )
                }
                !state.hasGoals -> {
                    EmptyGoalsContent(
                        onAddGoalClick = onAddGoalClick,
                        onLearnAboutSavingsClick = onLearnAboutSavingsClick
                    )
                }
                else -> {
                    GoalsListContent(
                        state = state,
                        onGoalClick = onGoalClick,
                        onAddGoalClick = onAddGoalClick,
                        onDepositClick = onDepositClick,
                        onWithdrawClick = onWithdrawClick,
                        onTransactionFilterChanged = onTransactionFilterChanged,
                        onViewAllTransactionsClick = onViewAllTransactionsClick
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GoalSavingsTopBar(userName: String) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Profile",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Hello $userName!",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "It's a good day to save",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF1B5E4F)
        )
    )
}

@Composable
private fun EmptyGoalsContent(
    onAddGoalClick: () -> Unit,
    onLearnAboutSavingsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Start Saving Towards Your Goals",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
        )

        // Goal Savings Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clickable(onClick = onAddGoalClick),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF2E7D32)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Text(
                        text = "Goal Savings",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Turn your goals into\nsavings!",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }

                Image(
                    painter = painterResource(R.drawable.vault),
                    contentDescription = "Goal Savings",
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.CenterEnd)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // CORRECTION 1: Learn About Savings - Horizontal Scroll with 2 cards
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(2) { index ->
                LearnAboutSavingsCard(
                    onClick = onLearnAboutSavingsClick,
                    index = index
                )
            }
        }
    }
}


@Composable
private fun LearnAboutSavingsCard(
    onClick: () -> Unit,
    index: Int
) {
    val cardColors = listOf(
        Color(0xFF1B5E4F),
        Color(0xFF2E7D32)
    )

    val titles = listOf(
        "Learn about Savings",
        "What is a Goal"
    )

    val descriptions = listOf(
        "Discover the world with our new\nsavings, one step towards your goal",
        "Understand how goal-based\nsavings can help you achieve more"
    )

    Card(
        modifier = Modifier
            .width(280.dp)
            .height(100.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColors[index]
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Text(
                    text = titles[index],
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = descriptions[index],
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }

            Image(
                painter = painterResource(R.drawable.bag),
                contentDescription = "Learn",
                modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.CenterEnd)
            )
        }
    }
}
@Composable
private fun GoalsListContent(
    state: GoalSavingsState,
    onGoalClick: (String) -> Unit,
    onAddGoalClick: () -> Unit,
    onDepositClick: (String) -> Unit,
    onWithdrawClick: (String) -> Unit,
    onTransactionFilterChanged: (TransactionFilter) -> Unit,
    onViewAllTransactionsClick: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { state.goals.size })

    // Sync selected goal with pager
    LaunchedEffect(pagerState.currentPage) {
        if (state.goals.isNotEmpty()) {
            val currentGoal = state.goals[pagerState.currentPage]
            if (currentGoal.id != state.selectedGoal?.id) {
                onGoalClick(currentGoal.id)
            }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // My Goals Header with Add button
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My Goals",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                TextButton(onClick = onAddGoalClick) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color(0xFF2E7D32),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Add a Goal",
                        color = Color(0xFF2E7D32),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Goals Horizontal Pager
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalPager(
                    state = pagerState,
                    pageSpacing = 16.dp,
                    modifier = Modifier.fillMaxWidth(),
                    pageSize = object : PageSize {
                        override fun Density.calculateMainAxisPageSize(
                            availableSpace: Int,
                            pageSpacing: Int
                        ): Int {
                            // If only 1 card, show full width, otherwise show 85% to peek next card
                            return if (state.goals.size == 1) {
                                availableSpace
                            } else {
                                (availableSpace * 0.90f).toInt()
                            }
                        }
                    }
                ) { page ->
                    val goal = state.goals[page]
                    GoalCard(
                        goal = goal,
                        isSelected = goal.id == state.selectedGoal?.id,
                        onClick = { onGoalClick(goal.id) },
                        onDepositClick = { onDepositClick(goal.id) },
                        onWithdrawClick = { onWithdrawClick(goal.id) }
                    )
                }

                // Page Indicators (Dots)
                if (state.goals.size > 1) {
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalPagerIndicator(
                        pagerState = pagerState,
                        pageCount = state.goals.size,
                        activeColor = Color(0xFF2E7D32),
                        inactiveColor = Color.LightGray,
                        indicatorWidth = 8.dp,
                        indicatorHeight = 8.dp,
                        spacing = 8.dp
                    )
                }
            }
        }

        // Transaction History Section
        if (state.selectedGoal != null && state.transactions.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                TransactionHistorySection(
                    transactions = state.filteredTransactions,
                    selectedFilter = state.selectedTransactionFilter,
                    onFilterChanged = onTransactionFilterChanged,
                    onViewAllClick = onViewAllTransactionsClick
                )
            }
        }
    }
}

@Composable
fun HorizontalPagerIndicator(
    pagerState: androidx.compose.foundation.pager.PagerState,
    pageCount: Int,
    modifier: Modifier = Modifier,
    activeColor: Color = Color(0xFF2E7D32),
    inactiveColor: Color = Color.LightGray,
    indicatorWidth: Dp = 8.dp,
    indicatorHeight: Dp = 8.dp,
    spacing: Dp = 8.dp
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            Box(
                modifier = Modifier
                    .size(
                        width = if (index == pagerState.currentPage) indicatorWidth * 1.5f else indicatorWidth,
                        height = indicatorHeight
                    )
                    .clip(CircleShape)
                    .background(
                        if (index == pagerState.currentPage) activeColor else inactiveColor
                    )
            )
        }
    }
}


@Composable
private fun GoalCard(
    goal: SavingsGoal,
    isSelected: Boolean,
    onClick: () -> Unit,
    onDepositClick: () -> Unit,
    onWithdrawClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth() // Fixed width for horizontal scrolling
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = goal.backgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 6.dp else 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = goal.name,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = { },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Current Amount
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${goal.currentAmount.toInt()} KES",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Visibility",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = goal.progressPercentage,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Bar
            LinearProgressIndicator(
                progress = { goal.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = Color.White,
                trackColor = Color.White.copy(alpha = 0.3f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Target Amount
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Target Amount (KES)",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${goal.targetAmount.toInt()}",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onDepositClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8BC34A)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Deposit",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Deposit",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                OutlinedButton(
                    onClick = onWithdrawClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.White
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        width = 1.dp,
                        brush = androidx.compose.ui.graphics.SolidColor(Color.White)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Withdraw",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Withdraw",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

// transactions are sections separate

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun GoalSavingsDashboard_Empty_Preview() {
    GoalSavingsContent(
        state = GoalSavingsSampleData.createEmptyState(),
        onGoalClick = {},
        onAddGoalClick = {},
        onDepositClick = {},
        onWithdrawClick = {},
        onTransactionFilterChanged = {},
        onViewAllTransactionsClick = {},
        onLearnAboutSavingsClick = {},
        onRefresh = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun GoalSavingsDashboard_OneGoal_Preview() {
    GoalSavingsContent(
        state = GoalSavingsSampleData.createStateWithOneGoal(),
        onGoalClick = {},
        onAddGoalClick = {},
        onDepositClick = {},
        onWithdrawClick = {},
        onTransactionFilterChanged = {},
        onViewAllTransactionsClick = {},
        onLearnAboutSavingsClick = {},
        onRefresh = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun GoalSavingsDashboard_MultipleGoals_Preview() {
    GoalSavingsContent(
        state = GoalSavingsSampleData.createStateWithMultipleGoals(),
        onGoalClick = {},
        onAddGoalClick = {},
        onDepositClick = {},
        onWithdrawClick = {},
        onTransactionFilterChanged = {},
        onViewAllTransactionsClick = {},
        onLearnAboutSavingsClick = {},
        onRefresh = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun GoalSavingsDashboard_Loading_Preview() {
    GoalSavingsContent(
        state = GoalSavingsState(isLoading = true),
        onGoalClick = {},
        onAddGoalClick = {},
        onDepositClick = {},
        onWithdrawClick = {},
        onTransactionFilterChanged = {},
        onViewAllTransactionsClick = {},
        onLearnAboutSavingsClick = {},
        onRefresh = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun GoalSavingsDashboard_Error_Preview() {
    GoalSavingsContent(
        state = GoalSavingsState(
            isLoading = false,
            error = "Failed to load goals. Please check your internet connection."
        ),
        onGoalClick = {},
        onAddGoalClick = {},
        onDepositClick = {},
        onWithdrawClick = {},
        onTransactionFilterChanged = {},
        onViewAllTransactionsClick = {},
        onLearnAboutSavingsClick = {},
        onRefresh = {}
    )
}
