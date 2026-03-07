package com.pocketbrain.data.db.dao

import androidx.room.*
import com.pocketbrain.data.db.entities.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionEntity): Long

    @Delete
    suspend fun delete(transaction: TransactionEntity)

    @Update
    suspend fun update(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE pocket = :pocket ORDER BY timestamp DESC")
    fun getTransactionsByPocket(pocket: String): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE category = :category ORDER BY timestamp DESC")
    fun getTransactionsByCategory(category: String): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY timestamp DESC")
    fun getTransactionsByType(type: String): Flow<List<TransactionEntity>>

    @Query("""
        SELECT * FROM transactions 
        WHERE timestamp BETWEEN :startMs AND :endMs 
        ORDER BY timestamp DESC
    """)
    fun getTransactionsBetween(startMs: Long, endMs: Long): Flow<List<TransactionEntity>>

    @Query("""
        SELECT * FROM transactions 
        WHERE timestamp BETWEEN :startMs AND :endMs AND type = 'expense'
        ORDER BY timestamp DESC
    """)
    fun getExpensesBetween(startMs: Long, endMs: Long): Flow<List<TransactionEntity>>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'expense' AND pocket = :pocket")
    fun getTotalSpentByPocket(pocket: String): Flow<Double?>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'expense' AND category = :category")
    fun getTotalSpentByCategory(category: String): Flow<Double?>

    @Query("SELECT DISTINCT category FROM transactions ORDER BY category")
    fun getAllCategories(): Flow<List<String>>

    @Query("""
        SELECT * FROM transactions 
        WHERE pocket = :pocket AND type = 'expense' AND timestamp BETWEEN :startMs AND :endMs
        ORDER BY timestamp DESC
    """)
    suspend fun getExpensesByPocketAndPeriod(pocket: String, startMs: Long, endMs: Long): List<TransactionEntity>
}
