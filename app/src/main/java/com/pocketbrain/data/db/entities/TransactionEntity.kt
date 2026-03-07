package com.pocketbrain.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: Double,
    val type: String, // "income" | "expense"
    val category: String,
    val pocket: String, // "daily" | "savings" | "investment" | "fun"
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
