package com.pocketbrain.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pocketbrain.domain.model.*
import com.pocketbrain.ui.components.TransactionItem
import com.pocketbrain.ui.viewmodel.HistoryFilter
import com.pocketbrain.ui.viewmodel.HistoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(vm: HistoryViewModel = hiltViewModel()) {
    val transactions by vm.transactions.collectAsState()
    val filter by vm.filter.collectAsState()
    val categories by vm.categories.collectAsState()

    var showFilterSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historique", fontWeight = FontWeight.Bold) },
                actions = {
                    BadgedBox(
                        badge = {
                            if (filter != HistoryFilter())
                                Badge { Text("!") }
                        }
                    ) {
                        IconButton(onClick = { showFilterSheet = true }) {
                            Icon(Icons.Filled.FilterList, "Filtrer")
                        }
                    }
                    if (filter != HistoryFilter()) {
                        IconButton(onClick = vm::clearFilter) {
                            Icon(Icons.Filled.FilterListOff, "Effacer")
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (transactions.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Filled.SearchOff, null, modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(0.3f))
                    Text("Aucune transaction", color = MaterialTheme.colorScheme.onSurface.copy(0.5f))
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        "${transactions.size} transaction(s)",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(0.5f)
                    )
                }
                items(transactions, key = { it.id }) { tx ->
                    AnimatedVisibility(visible = true, enter = fadeIn()) {
                        TransactionItem(tx, onDelete = { vm.deleteTransaction(tx) })
                    }
                }
            }
        }
    }

    if (showFilterSheet) {
        FilterSheet(
            currentFilter = filter,
            categories = categories,
            onDismiss = { showFilterSheet = false },
            onApply = { vm.setFilter(it); showFilterSheet = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterSheet(
    currentFilter: HistoryFilter,
    categories: List<String>,
    onDismiss: () -> Unit,
    onApply: (HistoryFilter) -> Unit
) {
    var type by remember { mutableStateOf(currentFilter.type) }
    var pocket by remember { mutableStateOf(currentFilter.pocket) }
    var category by remember { mutableStateOf(currentFilter.category) }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            Modifier.fillMaxWidth().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Filtrer", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

            Text("Type", style = MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = type == null, onClick = { type = null }, label = { Text("Tous") })
                FilterChip(selected = type == TransactionType.EXPENSE, onClick = { type = TransactionType.EXPENSE }, label = { Text("Dépenses") })
                FilterChip(selected = type == TransactionType.INCOME, onClick = { type = TransactionType.INCOME }, label = { Text("Revenus") })
            }

            Text("Poche", style = MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.horizontalScroll(rememberScrollState())) {
                FilterChip(selected = pocket == null, onClick = { pocket = null }, label = { Text("Toutes") })
                PocketType.entries.forEach { p ->
                    FilterChip(selected = pocket == p, onClick = { pocket = p }, label = { Text(p.labelFr) })
                }
            }

            Button(
                onClick = { onApply(HistoryFilter(pocket, type, category)) },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) { Text("Appliquer") }
        }
    }
}


