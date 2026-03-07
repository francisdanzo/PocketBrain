package com.pocketbrain.domain.model

data class Budget(
    val monthlyIncome: Double = 0.0,
    val dailyLifePct: Float = 50f,
    val savingsPct: Float = 20f,
    val investmentPct: Float = 20f,
    val funPct: Float = 10f
) {
    fun dailyLifeAmount() = monthlyIncome * dailyLifePct / 100.0
    fun savingsAmount()   = monthlyIncome * savingsPct / 100.0
    fun investmentAmount()= monthlyIncome * investmentPct / 100.0
    fun funAmount()       = monthlyIncome * funPct / 100.0
}
