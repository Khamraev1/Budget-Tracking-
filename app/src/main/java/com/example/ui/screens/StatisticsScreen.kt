package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.BudgetViewModel
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(viewModel: BudgetViewModel) {
    val expenses by viewModel.expenses.collectAsStateWithLifecycle()
    
    val actualExpenses = remember(expenses) { expenses.filter { !it.isIncome } }
    
    val tabs = listOf("24h", "Weekly", "Monthly")
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val filteredExpenses = remember(actualExpenses, selectedTabIndex) {
        val currentTime = System.currentTimeMillis()
        actualExpenses.filter { expense ->
            val timeDiff = currentTime - expense.timestamp
            when (selectedTabIndex) {
                0 -> timeDiff <= 24L * 60 * 60 * 1000L
                1 -> timeDiff <= 7L * 24 * 60 * 60 * 1000L
                2 -> timeDiff <= 30L * 24 * 60 * 60 * 1000L
                else -> true
            }
        }
    }
    
    // Group by category
    val expensesByCategory = remember(filteredExpenses) { filteredExpenses.groupBy { it.category.trim().uppercase() } }
    val categoryTotals = remember(expensesByCategory) { expensesByCategory.mapValues { it.value.sumOf { e -> e.amount } } }
    
    val totalSpent = remember(categoryTotals) { categoryTotals.values.sum() }
    val sortedCategories = remember(categoryTotals) { categoryTotals.entries.sortedByDescending { it.value } }

    val colors = listOf(
        Color(0xFF007AFF), // Blue
        Color(0xFF34C759), // Green
        Color(0xFFFF9500), // Orange
        Color(0xFFFF2D55), // Pink
        Color(0xFFAF52DE), // Purple
        Color(0xFF5856D6), // Indigo
        Color(0xFFFFCC00)  // Yellow
    )
    
    val animatedProgress = remember { Animatable(0f) }
    
    LaunchedEffect(selectedTabIndex, expensesByCategory) {
        animatedProgress.snapTo(0f)
        animatedProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { 
                    Text("Statistics", fontSize = 34.sp, fontWeight = FontWeight.Bold, letterSpacing = (-1.5).sp, color = MaterialTheme.colorScheme.onBackground)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Segmented Control
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ) {
                Row(modifier = Modifier.fillMaxWidth().padding(2.dp)) {
                    tabs.forEachIndexed { index, title ->
                        val isSelected = selectedTabIndex == index
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedTabIndex = index }
                                .padding(2.dp)
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent,
                                    RoundedCornerShape(6.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = title,
                                modifier = Modifier.padding(vertical = 6.dp),
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                                color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }

            if (sortedCategories.isEmpty() || totalSpent == 0.0) {
                Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                    Text("No spending data yet.", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    item {
                        // Pie Chart
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.size(200.dp)) {
                                var startAngle = -90f
                                
                                for ((index, entry) in sortedCategories.withIndex()) {
                                    val percentage = (entry.value / totalSpent).toFloat()
                                    val sweepAngle = percentage * 360f * animatedProgress.value
                                    
                                    val color = colors[index % colors.size]
                                    
                                    if (sweepAngle > 0f) {
                                        drawArc(
                                            color = color,
                                            startAngle = startAngle,
                                            sweepAngle = sweepAngle,
                                            useCenter = false,
                                            style = Stroke(width = 40.dp.toPx(), cap = StrokeCap.Butt),
                                            size = Size(size.width, size.height)
                                        )
                                    }
                                    startAngle += sweepAngle
                                }
                            }
                            
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Total", fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                                Text(String.format("₹%.0f", totalSpent), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Top Categories", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    
                    items(sortedCategories.size) { index ->
                        val entry = sortedCategories[index]
                        val color = colors[index % colors.size]
                        val percentage = (entry.value / totalSpent) * 100
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = color,
                                modifier = Modifier.size(16.dp)
                            ) {}
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(entry.key, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onBackground)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(String.format("₹%.2f", entry.value), fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground)
                                Text(String.format("%.1f%%", percentage), fontSize = 12.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                            }
                        }
                    }
                }
            }
        }
    }
}

