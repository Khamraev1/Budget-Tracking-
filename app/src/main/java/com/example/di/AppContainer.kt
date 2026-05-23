package com.example.di

import android.content.Context
import androidx.room.Room
import com.example.data.local.AppDatabase
import com.example.data.local.UserPreferences
import com.example.data.local.dataStore
import com.example.data.repository.ExpenseRepository

object AppContainer {
    private var database: AppDatabase? = null
    var expenseRepository: ExpenseRepository? = null
    var userPreferences: UserPreferences? = null

    fun init(context: Context) {
        val db = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "budgetsync_database"
        )
        .fallbackToDestructiveMigration(dropAllTables = true)
        .build()
        database = db
        expenseRepository = ExpenseRepository(db.expenseDao())
        userPreferences = UserPreferences(context.dataStore)
    }
}
