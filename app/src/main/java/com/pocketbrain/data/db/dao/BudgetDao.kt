package com.pocketbrain.data.db.dao

import androidx.room.*
import com.pocketbrain.data.db.entities.BudgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(budget: BudgetEntity)

    @Query("SELECT * FROM budget WHERE id = 1")
    fun getBudget(): Flow<BudgetEntity?>

    @Query("SELECT * FROM budget WHERE id = 1")
    suspend fun getBudgetOnce(): BudgetEntity?
}
