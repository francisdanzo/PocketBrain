package com.pocketbrain.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketbrain.data.repository.BudgetRepository
import com.pocketbrain.data.repository.TransactionRepository
import com.pocketbrain.domain.mapper.toDomain
import com.pocketbrain.domain.model.*
import com.pocketbrain.pdf.PdfReportGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class ReportUiState(
    val isGenerating: Boolean = false,
    val lastGeneratedFile: File? = null,
    val error: String? = null
)

@HiltViewModel
class ReportsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val transactionRepo: TransactionRepository,
    private val budgetRepo: BudgetRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportUiState())
    val uiState: StateFlow<ReportUiState> = _uiState.asStateFlow()

    fun generateReport(startMs: Long, endMs: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isGenerating = true, error = null)
            try {
                val transactions = transactionRepo.getTransactionsBetween(startMs, endMs)
                    .first().map { it.toDomain() }
                val budget = budgetRepo.getBudgetOnce()?.toDomain() ?: Budget()
                val file = PdfReportGenerator.generate(context, transactions, budget, startMs, endMs)
                _uiState.value = _uiState.value.copy(isGenerating = false, lastGeneratedFile = file)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isGenerating = false, error = e.message)
            }
        }
    }
}
