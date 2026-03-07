package com.pocketbrain.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.pocketbrain.domain.model.*
import com.pocketbrain.ui.components.*
import com.pocketbrain.ui.viewmodel.DashboardViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(vm: DashboardViewModel = hiltViewModel()) {
    val state by vm.uiState.collectAsState()
    val showSheet by vm.showAddSheet.collectAsState()

    Box(Modifier.fillMaxSize()) {
        if (state.isLoading) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                item { DashboardHeader(state.totalIncomeThisMonth, state.totalExpenseThisMonth) }

                // Pocket cards
                items(state.pockets) { pocket ->
                    PocketCard(pocket)
                }

                // Recent transactions
                if (state.recentTransactions.isNotEmpty()) {
                    item {
                        Text(
                            "Transactions récentes",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    items(state.recentTransactions) { tx ->
                        TransactionItem(tx)
                    }
                } else {
                    item {
                        EmptyTransactionsHint()
                    }
                }
            }
        }

        // FAB
        FloatingActionButton(
            onClick = vm::openAddSheet,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Filled.Add, "Ajouter", tint = Color.White)
        }
    }

    // Add transaction bottom sheet
    if (showSheet) {
        AddTransactionSheet(
            onDismiss = vm::closeAddSheet,
            onConfirm = { amount, type, cat, pocket, note ->
                vm.addTransaction(amount, type, cat, pocket, note)
            }
        )
    }
}

@Composable
private fun DashboardHeader(totalIncome: Double, totalExpense: Double) {
    val fmt = NumberFormat.getCurrencyInstance(Locale.getDefault())
    val net = totalIncome - totalExpense
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Ce mois-ci", style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            Text(
                fmt.format(net),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = if (net >= 0) Color(0xFF10B981) else Color(0xFFEF4444)
            )
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                SummaryChip(Icons.Filled.ArrowUpward, fmt.format(totalIncome), Color(0xFF10B981), "Revenus")
                SummaryChip(Icons.Filled.ArrowDownward, fmt.format(totalExpense), Color(0xFFEF4444), "Dépenses")
            }
        }
    }
}

@Composable
private fun SummaryChip(icon: androidx.compose.ui.graphics.vector.ImageVector, amount: String, color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(Modifier.size(28.dp).clip(CircleShape).background(color.copy(alpha = 0.15f)), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = color, modifier = Modifier.size(16.dp))
        }
        Column {
            Text(amount, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = color)
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(0.6f))
        }
    }
}

@Composable
private fun EmptyTransactionsHint() {
    Column(
        Modifier.fillMaxWidth().padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(Icons.Filled.AccountBalanceWallet, null, modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.primary.copy(0.4f))
        Text("Aucune transaction", style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(0.5f))
        Text("Appuyez sur + pour ajouter", style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(0.4f))
    }
}
