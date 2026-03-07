package com.pocketbrain.ui.screens

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.pocketbrain.ui.viewmodel.ReportsViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(vm: ReportsViewModel = hiltViewModel()) {
    val state by vm.uiState.collectAsState()
    val context = LocalContext.current

    var selectedRange by remember { mutableStateOf("monthly") }
    var customStart by remember { mutableStateOf<Long?>(null) }
    var customEnd by remember { mutableStateOf<Long?>(null) }

    val ranges = listOf("daily" to "Aujourd'hui", "monthly" to "Ce mois", "yearly" to "Cette année", "custom" to "Personnalisé")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text("Rapports PDF", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

        // Range selector
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Période du rapport", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                ranges.forEach { (key, label) ->
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable { selectedRange = key },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = selectedRange == key, onClick = { selectedRange = key })
                            Text(label)
                        }
                    }
                }
            }
        }

        // PDF output info
        Card(Modifier.fillMaxWidth()) {
            Row(
                Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.PictureAsPdf, null, tint = Color(0xFFEF4444), modifier = Modifier.size(32.dp))
                Column {
                    Text("Fichier PDF", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    Text(
                        "PocketBrain_${SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())}.pdf",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(0.5f)
                    )
                    Text("Enregistré dans Téléchargements", style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(0.4f))
                }
            }
        }

        // Generate button
        Button(
            onClick = {
                val (start, end) = getDateRange(selectedRange)
                vm.generateReport(start, end)
            },
            modifier = Modifier.fillMaxWidth().height(54.dp),
            shape = RoundedCornerShape(14.dp),
            enabled = !state.isGenerating
        ) {
            if (state.isGenerating) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("Génération en cours...")
            } else {
                Icon(Icons.Filled.Download, null)
                Spacer(Modifier.width(8.dp))
                Text("Générer le rapport", style = MaterialTheme.typography.labelLarge)
            }
        }

        // Success card
        AnimatedVisibility(visible = state.lastGeneratedFile != null && !state.isGenerating) {
            state.lastGeneratedFile?.let { file ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF10B981).copy(0.12f))
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.CheckCircle, null, tint = Color(0xFF10B981))
                            Text("Rapport généré !", fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
                        }
                        Text(file.absolutePath, style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(0.6f))
                        Button(
                            onClick = {
                                val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    setDataAndType(uri, "application/pdf")
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                }
                                context.startActivity(Intent.createChooser(intent, "Ouvrir PDF"))
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                        ) {
                            Icon(Icons.Filled.OpenInNew, null)
                            Spacer(Modifier.width(6.dp))
                            Text("Ouvrir")
                        }
                    }
                }
            }
        }

        // Error
        state.error?.let { err ->
            Card(
                Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Text(err, Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onErrorContainer)
            }
        }
    }
}

private fun getDateRange(range: String): Pair<Long, Long> {
    val cal = Calendar.getInstance()
    return when (range) {
        "daily"   -> {
            cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0); cal.set(Calendar.SECOND, 0)
            cal.timeInMillis to System.currentTimeMillis()
        }
        "monthly" -> {
            cal.set(Calendar.DAY_OF_MONTH, 1); cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0); cal.set(Calendar.SECOND, 0)
            cal.timeInMillis to System.currentTimeMillis()
        }
        "yearly"  -> {
            cal.set(Calendar.DAY_OF_YEAR, 1); cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0); cal.set(Calendar.SECOND, 0)
            cal.timeInMillis to System.currentTimeMillis()
        }
        else      -> 0L to System.currentTimeMillis()
    }
}
