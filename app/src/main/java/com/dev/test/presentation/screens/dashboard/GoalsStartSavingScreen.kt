package com.dev.test.presentation.screens.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.coop.feature_goals.presentation.GoalSavingsIntent
import com.coop.feature_goals.presentation.GoalSavingsSampleData
import com.coop.feature_goals.presentation.GoalSavingsState
import com.dev.test.R
import com.dev.test.presentation.navigation.AppRoute
import com.dev.test.presentation.screens.mygoals.GoalSavingsViewModel

@Composable
 fun StartSavingScreen(
    navController: NavController,
    viewModel: GoalSavingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    StartSavingContent(
        state = state,
        onViewAllGoals = {
            viewModel.processIntent(GoalSavingsIntent.OnViewGoals)
            navController.navigate(AppRoute.GoalSavingsDashboardScreen.route)

        },

        onLearnAboutSavingsClick = {
            viewModel.processIntent(GoalSavingsIntent.OnLearnAboutSavingsClicked)
        },

    )
}
@Composable
 fun StartSavingContent(
    state: GoalSavingsState,
    onViewAllGoals: () -> Unit,
    onLearnAboutSavingsClick: () -> Unit
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
                        .clickable(onClick = onViewAllGoals),
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun StartSavingsDashboard_Empty_Preview() {
    StartSavingContent(
        state = GoalSavingsSampleData.createEmptyState(),
        onViewAllGoals = {},
        onLearnAboutSavingsClick = {},
    )
}