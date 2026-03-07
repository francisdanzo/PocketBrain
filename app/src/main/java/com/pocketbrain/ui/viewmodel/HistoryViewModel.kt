package com.pocketbrain.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketbrain.data.repository.TransactionRepository
import com.pocketbrain.domain.mapper.toDomain
import com.pocketbrain.domain.model.Transaction
import com.pocketbrain.domain.model.TransactionType
import com.pocketbrain.domain.model.PocketType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HistoryFilter(
    val pocket: PocketType? = null,
    val type: TransactionType? = null,
    val category: String? = null
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repo: TransactionRepository
) : ViewModel() {

    private val _filter = MutableStateFlow(HistoryFilter())
    val filter: StateFlow<HistoryFilter> = _filter.asStateFlow()

    val transactions: StateFlow<List<Transaction>> = combine(
        repo.getAllTransactions(),
        _filter
    ) { entities, filter ->
        entities
            .map { it.toDomain() }
            .filter { tx ->
                (filter.pocket == null || tx.pocket == filter.pocket) &&
                (filter.type == null || tx.type == filter.type) &&
                (filter.category == null || tx.category == filter.category)
            }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categories: StateFlow<List<String>> = repo.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setFilter(filter: HistoryFilter) { _filter.value = filter }
    fun clearFilter() { _filter.value = HistoryFilter() }

    fun deleteTransaction(tx: Transaction) {
        viewModelScope.launch {
            repo.deleteTransaction(
                com.pocketbrain.data.db.entities.TransactionEntity(
                    id        = tx.id,
                    amount    = tx.amount,
                    type      = tx.type.value,
                    category  = tx.category,
                    pocket    = tx.pocket.value,
                    note      = tx.note,
                    timestamp = tx.timestamp
                )
            )
        }
    }
}
