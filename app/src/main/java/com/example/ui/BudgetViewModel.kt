package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.Expense
import com.example.di.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BudgetViewModel : ViewModel() {
    private val expenseRepo = AppContainer.expenseRepository!!
    private val userPrefs = AppContainer.userPreferences!!

    val expenses: StateFlow<List<Expense>> = expenseRepo.allExpenses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalExpenses: StateFlow<Double> = expenseRepo.totalExpenses
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalIncome: StateFlow<Double> = expenseRepo.totalIncome
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    private val _errorMsg = MutableStateFlow<String?>(null)
    val errorMsg: StateFlow<String?> = _errorMsg

    fun manualAddExpense(merchant: String, amount: Double, category: String, isIncome: Boolean = false) {
        viewModelScope.launch {
            expenseRepo.insert(Expense(merchant = merchant, amount = amount, category = category, isIncome = isIncome))
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            expenseRepo.delete(expense)
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            expenseRepo.deleteAll()
        }
    }

    fun clearError() {
        _errorMsg.value = null
    }

    var quickAddPrefill: Pair<String, String>? = null

    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return BudgetViewModel() as T
        }
    }
}
