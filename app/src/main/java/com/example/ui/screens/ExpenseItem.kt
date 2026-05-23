package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.Expense
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ExpenseItem(expense: Expense, onDelete: (() -> Unit)? = null) {
    val formatter = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault())
    val dateStr = formatter.format(Date(expense.timestamp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onDelete?.invoke() }
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val (bg, iconColor, icon) = when {
            expense.isIncome -> Triple(Color(0xFFE5F9E6), Color(0xFF34C759), Icons.Default.Add)
            expense.category.lowercase() in listOf("food & dining", "food") -> Triple(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.onPrimaryContainer, Icons.Default.Restaurant)
            expense.category.lowercase() in listOf("transportation", "transit", "transport") -> Triple(MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.onTertiaryContainer, Icons.Default.DirectionsCar)
            expense.category.lowercase() == "shopping" -> Triple(MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.onSecondaryContainer, Icons.Default.ShoppingBag)
            expense.category.lowercase() == "utilities" -> Triple(MaterialTheme.colorScheme.errorContainer, MaterialTheme.colorScheme.onErrorContainer, Icons.Default.Lightbulb)
            else -> Triple(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.onSurfaceVariant, Icons.Default.Receipt)
        }
        
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(bg, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = expense.category, tint = iconColor)
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = expense.merchant, 
                fontSize = 14.sp, 
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "${expense.category} • $dateStr", 
                fontSize = 12.sp, 
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
        }
        
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = if (expense.isIncome) String.format("+₹%.2f", expense.amount) else String.format("-₹%.2f", expense.amount), 
                fontSize = 15.sp, 
                fontWeight = FontWeight.Bold,
                color = if (expense.isIncome) Color(0xFF34C759) else MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
