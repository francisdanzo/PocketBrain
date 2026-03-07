package com.pocketbrain.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budget")
data class BudgetEntity(
    @PrimaryKey
    val id: Int = 1,
    val monthlyIncome: Double = 0.0,
    val dailyLifePct: Float = 50f,
    val savingsPct: Float = 20f,
    val investmentPct: Float = 20f,
    val funPct: Float = 10f
)
