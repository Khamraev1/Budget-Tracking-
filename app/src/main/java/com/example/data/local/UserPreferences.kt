package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserPreferences(private val dataStore: DataStore<Preferences>) {
    companion object {
        val MONTHLY_BUDGET_GOAL = doublePreferencesKey("monthly_budget_goal")
    }

    val monthlyBudgetGoal: Flow<Double> = dataStore.data
        .map { preferences ->
            preferences[MONTHLY_BUDGET_GOAL] ?: 0.0
        }

    suspend fun saveMonthlyBudgetGoal(goal: Double) {
        dataStore.edit { preferences ->
            preferences[MONTHLY_BUDGET_GOAL] = goal
        }
    }
}
