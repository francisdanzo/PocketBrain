package com.pocketbrain.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketbrain.data.repository.BudgetRepository
import com.pocketbrain.data.repository.TransactionRepository
import com.pocketbrain.domain.mapper.toDomain
import com.pocketbrain.domain.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.util.Calendar
import javax.inject.Inject

data class ChartDataPoint(val label: String, val value: Float, val colorHex: Int)

data class ChartsUiState(
    val pocketPieData: List<ChartDataPoint> = emptyList(),
    val categoryBarData: List<ChartDataPoint> = emptyList(),
    val monthlyBarData: List<ChartDataPoint> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class ChartsViewModel @Inject constructor(
    private val transactionRepo: TransactionRepository,
    private val budgetRepo: BudgetRepository
) : ViewModel() {

    val uiState: StateFlow<ChartsUiState> = combine(
        transactionRepo.getAllTransactions(),
        budgetRepo.getBudget()
    ) { entities, budgetEntity ->
        val transactions = entities.map { it.toDomain() }
        val budget = budgetEntity?.toDomain() ?: Budget()

        // Pocket pie: spent per pocket this month
        val monthStart = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1); set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0)
        }.timeInMillis
        val monthExpenses = transactions.filter { it.type == TransactionType.EXPENSE && it.timestamp >= monthStart }

        val pocketColors = mapOf(
            PocketType.DAILY      to android.graphics.Color.parseColor("#3B82F6"),
            PocketType.SAVINGS    to android.graphics.Color.parseColor("#10B981"),
            PocketType.INVESTMENT to android.graphics.Color.parseColor("#8B5CF6"),
            PocketType.FUN        to android.graphics.Color.parseColor("#F59E0B")
        )

        val pocketPie = PocketType.entries.map { p ->
            val spent = monthExpenses.filter { it.pocket == p }.sumOf { it.amount }.toFloat()
            ChartDataPoint(p.labelFr, spent, pocketColors[p] ?: android.graphics.Color.GRAY)
        }.filter { it.value > 0f }

        // Category bar
        val categoryMap = monthExpenses.groupBy { it.category }
            .mapValues { (_, txs) -> txs.sumOf { it.amount }.toFloat() }
            .entries.sortedByDescending { it.value }.take(8)
        val categoryBar = categoryMap.map { (cat, amt) ->
            ChartDataPoint(cat, amt, android.graphics.Color.parseColor("#7C3AED"))
        }

        // Monthly bar: last 6 months
        val monthlyBar = (0..5).map { monthsAgo ->
            val cal = Calendar.getInstance().apply { add(Calendar.MONTH, -monthsAgo) }
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val label = android.text.format.DateFormat.format("MMM yy", cal.time).toString()
            val start = Calendar.getInstance().apply {
                set(Calendar.YEAR, year); set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, 1); set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
            }.timeInMillis
            val end = Calendar.getInstance().apply {
                set(Calendar.YEAR, year); set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
                set(Calendar.HOUR_OF_DAY, 23); set(Calendar.MINUTE, 59)
            }.timeInMillis
            val total = transactions
                .filter { it.type == TransactionType.EXPENSE && it.timestamp in start..end }
                .sumOf { it.amount }.toFloat()
            ChartDataPoint(label, total, android.graphics.Color.parseColor("#4F46E5"))
        }.reversed()

        ChartsUiState(
            pocketPieData = pocketPie,
            categoryBarData = categoryBar,
            monthlyBarData = monthlyBar,
            isLoading = false
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ChartsUiState())
}
