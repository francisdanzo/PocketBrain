package com.pocketbrain.domain.model

data class PocketState(
    val pocketType: PocketType,
    val allocated: Double,
    val spent: Double
) {
    val remaining: Double get() = allocated - spent
    val progressFraction: Float get() = if (allocated == 0.0) 0f else (spent / allocated).toFloat().coerceIn(0f, 1f)
    val isOverBudget: Boolean get() = spent > allocated
}
