package com.coop.feature_goals.presentation


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TransactionHistorySection(
    transactions: List<GoalTransaction>,
    selectedFilter: TransactionFilter,
    onFilterChanged: (TransactionFilter) -> Unit,
    onViewAllClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Transaction History",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            TextButton(onClick = onViewAllClick) {
                Text(
                    text = "View all",
                    color = Color(0xFF2E7D32),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(TransactionFilter.values().size) { index ->
                val filter = TransactionFilter.values()[index]
                FilterChip(
                    selected = filter == selectedFilter,
                    onClick = { onFilterChanged(filter) },
                    label = {
                        Text(
                            text = filter.displayName,
                            fontSize = 14.sp
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF2E7D32),
                        selectedLabelColor = Color.White,
                        containerColor = Color.White,
                        labelColor = Color.Gray
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Transactions List
        transactions.take(3).forEach { transaction ->
            TransactionItem(transaction = transaction)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun TransactionItem(transaction: GoalTransaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (transaction.type == TransactionType.DEPOSIT) {
                            Color(0xFFE8F5E9)
                        } else {
                            Color(0xFFFFEBEE)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (transaction.type == TransactionType.DEPOSIT) {
                        Icons.Default.KeyboardArrowDown
                    } else {
                        Icons.Default.KeyboardArrowUp
                    },
                    contentDescription = transaction.type.name,
                    tint = if (transaction.type == TransactionType.DEPOSIT) {
                        Color(0xFF2E7D32)
                    } else {
                        Color(0xFFD32F2F)
                    },
                    modifier = Modifier.size(20.dp)
                )
            }

            Column {
                Text(
                    text = transaction.type.name.lowercase().replaceFirstChar { it.uppercase() },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Text(
                    text = transaction.reference,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = if (transaction.type == TransactionType.DEPOSIT) {
                    "KES ${transaction.amount.toInt()}"
                } else {
                    "KES ${transaction.amount.toInt()}"
                },
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (transaction.type == TransactionType.DEPOSIT) {
                    Color(0xFF2E7D32)
                } else {
                    Color(0xFFD32F2F)
                }
            )
            Text(
                text = transaction.date,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = Color(0xFF2E7D32)
        )
    }
}

@Composable
fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = message,
                color = Color.Red,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2E7D32)
                )
            ) {
                Text("Retry")
            }
        }
    }
}