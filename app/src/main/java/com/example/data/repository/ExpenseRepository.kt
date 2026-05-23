package com.example.data.repository

import com.example.data.local.Expense
import com.example.data.local.ExpenseDao
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(
    private val expenseDao: ExpenseDao
) {
    val allExpenses: Flow<List<Expense>> = expenseDao.getAllExpenses()
    val totalExpenses: Flow<Double?> = expenseDao.getTotalExpenses()
    val totalIncome: Flow<Double?> = expenseDao.getTotalIncome()

    suspend fun insert(expense: Expense) = expenseDao.insertExpense(expense)
    suspend fun delete(expense: Expense) = expenseDao.deleteExpenseById(expense.id)
    suspend fun deleteAll() = expenseDao.deleteAllExpenses()
}
