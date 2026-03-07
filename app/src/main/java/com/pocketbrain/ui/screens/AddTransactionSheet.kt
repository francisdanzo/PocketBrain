package com.pocketbrain.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.pocketbrain.domain.model.*

val expenseCategories = listOf(
    "Alimentation", "Transport", "Logement", "Santé", "Vêtements",
    "Loisirs", "Restaurant", "Abonnements", "Électronique", "Sport", "Autre"
)
val incomeCategories  = listOf("Salaire", "Freelance", "Investissement", "Cadeau", "Remboursement", "Autre")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionSheet(
    onDismiss: () -> Unit,
    onConfirm: (Double, TransactionType, String, PocketType, String) -> Unit
) {
    var amountText by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(TransactionType.EXPENSE) }
    var selectedCategory by remember { mutableStateOf("Alimentation") }
    var selectedPocket by remember { mutableStateOf(PocketType.DAILY) }
    var note by remember { mutableStateOf("") }
    var categoryExpanded by remember { mutableStateOf(false) }
    var pocketExpanded by remember { mutableStateOf(false) }

    val categories = if (selectedType == TransactionType.EXPENSE) expenseCategories else incomeCategories

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Nouvelle transaction",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            // Type toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TransactionType.entries.forEach { type ->
                    FilterChip(
                        selected = selectedType == type,
                        onClick = {
                            selectedType = type
                            selectedCategory = if (type == TransactionType.EXPENSE) "Alimentation" else "Salaire"
                        },
                        label = { Text(if (type == TransactionType.EXPENSE) "Dépense" else "Revenu") },
                        modifier = Modifier.weight(1f),
                        leadingIcon = {
                            Icon(
                                if (type == TransactionType.EXPENSE) Icons.Filled.ArrowDownward else Icons.Filled.ArrowUpward,
                                null, modifier = Modifier.size(16.dp)
                            )
                        }
                    )
                }
            }

            // Amount
            OutlinedTextField(
                value = amountText,
                onValueChange = { amountText = it.filter { c -> c.isDigit() || c == '.' } },
                label = { Text("Montant") },
                leadingIcon = { Icon(Icons.Filled.Euro, null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Category dropdown
            ExposedDropdownMenuBox(
                expanded = categoryExpanded,
                onExpandedChange = { categoryExpanded = it }
            ) {
                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Catégorie") },
                    leadingIcon = { Icon(Icons.Filled.Category, null) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(expanded = categoryExpanded, onDismissRequest = { categoryExpanded = false }) {
                    categories.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat) },
                            onClick = { selectedCategory = cat; categoryExpanded = false }
                        )
                    }
                }
            }

            // Pocket dropdown (only for expenses)
            if (selectedType == TransactionType.EXPENSE) {
                ExposedDropdownMenuBox(
                    expanded = pocketExpanded,
                    onExpandedChange = { pocketExpanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedPocket.labelFr,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Poche") },
                        leadingIcon = { Icon(Icons.Filled.AccountBalanceWallet, null) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = pocketExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(expanded = pocketExpanded, onDismissRequest = { pocketExpanded = false }) {
                        PocketType.entries.forEach { pocket ->
                            DropdownMenuItem(
                                text = { Text(pocket.labelFr) },
                                onClick = { selectedPocket = pocket; pocketExpanded = false }
                            )
                        }
                    }
                }
            }

            // Note
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Note (optionnel)") },
                leadingIcon = { Icon(Icons.Filled.Notes, null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Confirm button
            Button(
                onClick = {
                    val amount = amountText.toDoubleOrNull() ?: return@Button
                    val pocket = if (selectedType == TransactionType.INCOME) PocketType.DAILY else selectedPocket
                    onConfirm(amount, selectedType, selectedCategory, pocket, note)
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                enabled = amountText.toDoubleOrNull() != null && amountText.isNotEmpty()
            ) {
                Icon(Icons.Filled.Check, null)
                Spacer(Modifier.width(8.dp))
                Text("Confirmer", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}
