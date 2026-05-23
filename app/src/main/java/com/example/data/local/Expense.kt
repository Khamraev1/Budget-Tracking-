package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val merchant: String,
    val category: String,
    val amount: Double,
    val timestamp: Long = System.currentTimeMillis(),
    val isIncome: Boolean = false
)
