package com.pocketbrain.data.repository

import com.pocketbrain.data.db.dao.BudgetDao
import com.pocketbrain.data.db.entities.BudgetEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BudgetRepository @Inject constructor(
    private val dao: BudgetDao
) {
    fun getBudget(): Flow<BudgetEntity?> = dao.getBudget()

    suspend fun getBudgetOnce(): BudgetEntity? = dao.getBudgetOnce()

    suspend fun saveBudget(budget: BudgetEntity) = dao.insertOrUpdate(budget)
}
