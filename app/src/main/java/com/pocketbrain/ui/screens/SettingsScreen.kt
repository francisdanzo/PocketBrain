package com.pocketbrain.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pocketbrain.ui.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(vm: SettingsViewModel = hiltViewModel()) {
    val budget by vm.budget.collectAsState()
    val isDark by vm.isDarkTheme.collectAsState()

    var income by remember(budget.monthlyIncome) { mutableStateOf(budget.monthlyIncome.toString()) }
    var dailyPct by remember(budget.dailyLifePct) { mutableStateOf(budget.dailyLifePct) }
    var savingsPct by remember(budget.savingsPct) { mutableStateOf(budget.savingsPct) }
    var investPct by remember(budget.investmentPct) { mutableStateOf(budget.investmentPct) }
    var funPct by remember(budget.funPct) { mutableStateOf(budget.funPct) }

    val total = dailyPct + savingsPct + investPct + funPct
    val isValid = total == 100f && (income.toDoubleOrNull() ?: 0.0) > 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text("Réglages", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

        // Theme toggle
        Card(Modifier.fillMaxWidth()) {
            Row(
                Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(if (isDark) Icons.Filled.DarkMode else Icons.Filled.LightMode, null)
                    Column {
                        Text("Thème", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text(if (isDark) "Sombre" else "Clair", style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(0.6f))
                    }
                }
                Switch(checked = isDark, onCheckedChange = vm::toggleTheme)
            }
        }

        // Budget setup
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text("Budget mensuel", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)

                OutlinedTextField(
                    value = income,
                    onValueChange = { income = it.filter { c -> c.isDigit() || c == '.' } },
                    label = { Text("Revenu mensuel (€)") },
                    leadingIcon = { Icon(Icons.Filled.Euro, null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Text("Répartition des poches (total: ${total.toInt()}%)",
                    style = MaterialTheme.typography.labelLarge,
                    color = if (total != 100f) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary)

                PocketSlider("🏠 Vie quotidienne", dailyPct) { dailyPct = it }
                PocketSlider("🏦 Épargne", savingsPct) { savingsPct = it }
                PocketSlider("📈 Investissement", investPct) { investPct = it }
                PocketSlider("🎉 Plaisir", funPct) { funPct = it }

                if (total != 100f) {
                    Text("⚠️ La somme doit être égale à 100%", color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall)
                }

                Button(
                    onClick = {
                        vm.saveBudget(
                            income.toDoubleOrNull() ?: 0.0,
                            dailyPct, savingsPct, investPct, funPct
                        )
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    enabled = isValid
                ) {
                    Icon(Icons.Filled.Save, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Enregistrer", style = MaterialTheme.typography.labelLarge)
                }
            }
        }

        // App info
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("À propos", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text("PocketBrain v1.0.0", style = MaterialTheme.typography.bodyMedium)
                Text("100% hors ligne • Données stockées localement",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(0.5f))
            }
        }
    }
}

@Composable
private fun PocketSlider(label: String, value: Float, onValueChange: (Float) -> Unit) {
    Column {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.bodyMedium)
            Text("${value.toInt()}%", style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
        Slider(
            value = value,
            onValueChange = { onValueChange(it.toInt().toFloat()) },
            valueRange = 0f..100f,
            steps = 99,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


