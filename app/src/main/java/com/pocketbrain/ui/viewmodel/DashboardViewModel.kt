package com.pocketbrain.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketbrain.data.repository.BudgetRepository
import com.pocketbrain.data.repository.TransactionRepository
import com.pocketbrain.data.db.entities.TransactionEntity
import com.pocketbrain.domain.mapper.toDomain
import com.pocketbrain.domain.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val budget: Budget = Budget(),
    val pockets: List<PocketState> = emptyList(),
    val recentTransactions: List<Transaction> = emptyList(),
    val totalExpenseThisMonth: Double = 0.0,
    val totalIncomeThisMonth: Double = 0.0,
    val isLoading: Boolean = true
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val transactionRepo: TransactionRepository,
    private val budgetRepo: BudgetRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private val _showAddSheet = MutableStateFlow(false)
    val showAddSheet: StateFlow<Boolean> = _showAddSheet.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            combine(
                budgetRepo.getBudget(),
                transactionRepo.getAllTransactions()
            ) { budgetEntity, txEntities ->
                val budget = budgetEntity?.toDomain() ?: Budget()
                val transactions = txEntities.map { it.toDomain() }

                // This month range
                val now = System.currentTimeMillis()
                val cal = java.util.Calendar.getInstance().apply {
                    timeInMillis = now
                    set(java.util.Calendar.DAY_OF_MONTH, 1)
                    set(java.util.Calendar.HOUR_OF_DAY, 0)
                    set(java.util.Calendar.MINUTE, 0)
                    set(java.util.Calendar.SECOND, 0)
                }
                val monthStart = cal.timeInMillis

                val thisMonthTx = transactions.filter { it.timestamp >= monthStart }
                val monthExpenses = thisMonthTx.filter { it.type == TransactionType.EXPENSE }
                val monthIncomes  = thisMonthTx.filter { it.type == TransactionType.INCOME }

                // Calculate pocket spending
                val pockets = PocketType.entries.map { pocket ->
                    val spent = monthExpenses.filter { it.pocket == pocket }.sumOf { it.amount }
                    val allocated = when (pocket) {
                        PocketType.DAILY      -> budget.dailyLifeAmount()
                        PocketType.SAVINGS    -> budget.savingsAmount()
                        PocketType.INVESTMENT -> budget.investmentAmount()
                        PocketType.FUN        -> budget.funAmount()
                    }
                    PocketState(pocketType = pocket, allocated = allocated, spent = spent)
                }

                DashboardUiState(
                    budget = budget,
                    pockets = pockets,
                    recentTransactions = transactions.take(5),
                    totalExpenseThisMonth = monthExpenses.sumOf { it.amount },
                    totalIncomeThisMonth  = monthIncomes.sumOf { it.amount },
                    isLoading = false
                )
            }.collect { state -> _uiState.value = state }
        }
    }

    fun openAddSheet()  { _showAddSheet.value = true }
    fun closeAddSheet() { _showAddSheet.value = false }

    fun addTransaction(
        amount: Double,
        type: TransactionType,
        category: String,
        pocket: PocketType,
        note: String
    ) {
        viewModelScope.launch {
            transactionRepo.addTransaction(
                TransactionEntity(
                    amount    = amount,
                    type      = type.value,
                    category  = category,
                    pocket    = pocket.value,
                    note      = note,
                    timestamp = System.currentTimeMillis()
                )
            )
            _showAddSheet.value = false
        }
    }
}
