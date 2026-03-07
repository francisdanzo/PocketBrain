package com.pocketbrain.domain.model

data class Transaction(
    val id: Long = 0,
    val amount: Double,
    val type: TransactionType,
    val category: String,
    val pocket: PocketType,
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

enum class TransactionType(val value: String) {
    INCOME("income"),
    EXPENSE("expense");

    companion object {
        fun fromString(v: String) = entries.firstOrNull { it.value == v } ?: EXPENSE
    }
}

enum class PocketType(val value: String, val labelFr: String) {
    DAILY("daily", "Vie quotidienne"),
    SAVINGS("savings", "Épargne"),
    INVESTMENT("investment", "Investissement"),
    FUN("fun", "Plaisir");

    companion object {
        fun fromString(v: String) = entries.firstOrNull { it.value == v } ?: DAILY
    }
}
