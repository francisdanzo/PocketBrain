package com.pocketbrain.domain.mapper

import com.pocketbrain.data.db.entities.BudgetEntity
import com.pocketbrain.data.db.entities.TransactionEntity
import com.pocketbrain.domain.model.*

fun TransactionEntity.toDomain() = Transaction(
    id        = id,
    amount    = amount,
    type      = TransactionType.fromString(type),
    category  = category,
    pocket    = PocketType.fromString(pocket),
    note      = note,
    timestamp = timestamp
)

fun Transaction.toEntity() = TransactionEntity(
    id        = id,
    amount    = amount,
    type      = type.value,
    category  = category,
    pocket    = pocket.value,
    note      = note,
    timestamp = timestamp
)

fun BudgetEntity.toDomain() = Budget(
    monthlyIncome  = monthlyIncome,
    dailyLifePct   = dailyLifePct,
    savingsPct     = savingsPct,
    investmentPct  = investmentPct,
    funPct         = funPct
)

fun Budget.toEntity() = BudgetEntity(
    monthlyIncome  = monthlyIncome,
    dailyLifePct   = dailyLifePct,
    savingsPct     = savingsPct,
    investmentPct  = investmentPct,
    funPct         = funPct
)
