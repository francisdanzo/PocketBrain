package com.pocketbrain.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketbrain.data.db.entities.BudgetEntity
import com.pocketbrain.data.preferences.PreferencesManager
import com.pocketbrain.data.repository.BudgetRepository
import com.pocketbrain.domain.mapper.toDomain
import com.pocketbrain.domain.model.Budget
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val budgetRepo: BudgetRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    val budget: StateFlow<Budget> = budgetRepo.getBudget()
        .map { it?.toDomain() ?: Budget() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Budget())

    val isDarkTheme: StateFlow<Boolean> = preferencesManager.isDarkTheme
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun saveBudget(income: Double, dailyPct: Float, savingsPct: Float, investPct: Float, funPct: Float) {
        viewModelScope.launch {
            budgetRepo.saveBudget(
                BudgetEntity(
                    monthlyIncome = income,
                    dailyLifePct  = dailyPct,
                    savingsPct    = savingsPct,
                    investmentPct = investPct,
                    funPct        = funPct
                )
            )
        }
    }

    fun toggleTheme(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setDarkTheme(enabled) }
    }
}
