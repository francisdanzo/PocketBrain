package com.pocketbrain.data.repository

import com.pocketbrain.data.db.dao.TransactionDao
import com.pocketbrain.data.db.entities.TransactionEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepository @Inject constructor(
    private val dao: TransactionDao
) {
    fun getAllTransactions(): Flow<List<TransactionEntity>> = dao.getAllTransactions()

    fun getTransactionsByPocket(pocket: String): Flow<List<TransactionEntity>> =
        dao.getTransactionsByPocket(pocket)

    fun getTransactionsByCategory(category: String): Flow<List<TransactionEntity>> =
        dao.getTransactionsByCategory(category)

    fun getTransactionsByType(type: String): Flow<List<TransactionEntity>> =
        dao.getTransactionsByType(type)

    fun getTransactionsBetween(startMs: Long, endMs: Long): Flow<List<TransactionEntity>> =
        dao.getTransactionsBetween(startMs, endMs)

    fun getExpensesBetween(startMs: Long, endMs: Long): Flow<List<TransactionEntity>> =
        dao.getExpensesBetween(startMs, endMs)

    fun getTotalSpentByPocket(pocket: String): Flow<Double?> =
        dao.getTotalSpentByPocket(pocket)

    fun getAllCategories(): Flow<List<String>> = dao.getAllCategories()

    suspend fun getExpensesByPocketAndPeriod(pocket: String, startMs: Long, endMs: Long): List<TransactionEntity> =
        dao.getExpensesByPocketAndPeriod(pocket, startMs, endMs)

    suspend fun addTransaction(transaction: TransactionEntity): Long =
        dao.insert(transaction)

    suspend fun deleteTransaction(transaction: TransactionEntity) =
        dao.delete(transaction)
}
