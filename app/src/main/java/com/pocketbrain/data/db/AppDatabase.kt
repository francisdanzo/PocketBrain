package com.pocketbrain.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pocketbrain.data.db.dao.BudgetDao
import com.pocketbrain.data.db.dao.TransactionDao
import com.pocketbrain.data.db.entities.BudgetEntity
import com.pocketbrain.data.db.entities.TransactionEntity

@Database(
    entities = [TransactionEntity::class, BudgetEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao
}
