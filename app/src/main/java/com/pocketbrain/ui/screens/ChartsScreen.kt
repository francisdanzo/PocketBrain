package com.pocketbrain.ui.screens

import android.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.pocketbrain.ui.viewmodel.ChartDataPoint
import com.pocketbrain.ui.viewmodel.ChartsViewModel

@Composable
fun ChartsScreen(vm: ChartsViewModel = hiltViewModel()) {
    val state by vm.uiState.collectAsState()

    if (state.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            "Graphiques",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        // Pie chart
        if (state.pocketPieData.isNotEmpty()) {
            ChartCard(title = "Répartition des poches (ce mois)") {
                InteractivePieChart(data = state.pocketPieData)
            }
        } else {
            Card(Modifier.fillMaxWidth()) {
                Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text("Aucune dépense ce mois", color = MaterialTheme.colorScheme.onSurface.copy(0.5f))
                }
            }
        }

        // Category bar chart
        if (state.categoryBarData.isNotEmpty()) {
            ChartCard(title = "Dépenses par catégorie") {
                InteractiveBarChart(
                    data = state.categoryBarData,
                    barColor = Color.parseColor("#7C3AED")
                )
            }
        }

        // Monthly bar chart
        if (state.monthlyBarData.isNotEmpty()) {
            ChartCard(title = "Évolution mensuelle (6 mois)") {
                InteractiveBarChart(
                    data = state.monthlyBarData,
                    barColor = Color.parseColor("#4F46E5")
                )
            }
        }
    }
}

@Composable
private fun ChartCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            content()
        }
    }
}

@Composable
private fun InteractivePieChart(data: List<ChartDataPoint>) {
    AndroidView(
        factory = { ctx ->
            PieChart(ctx).apply {
                description.isEnabled = false
                isDrawHoleEnabled = true
                holeRadius = 50f
                transparentCircleRadius = 55f
                setHoleColor(Color.TRANSPARENT)
                setDrawEntryLabels(true)
                setEntryLabelTextSize(11f)
                setEntryLabelColor(Color.WHITE)
                isRotationEnabled = true
                legend.apply {
                    isEnabled = true
                    orientation = Legend.LegendOrientation.HORIZONTAL
                    horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                    setDrawInside(false)
                }
                animateY(1200)
            }
        },
        update = { chart ->
            val entries = data.map { PieEntry(it.value, it.label) }
            val colors = data.map { it.colorHex }
            val ds = PieDataSet(entries, "").apply {
                this.colors = colors
                sliceSpace = 2f
                valueTextSize = 12f
                valueTextColor = Color.WHITE
            }
            chart.data = PieData(ds)
            chart.invalidate()
        },
        modifier = Modifier.fillMaxWidth().height(280.dp)
    )
}

@Composable
private fun InteractiveBarChart(data: List<ChartDataPoint>, barColor: Int) {
    AndroidView(
        factory = { ctx ->
            BarChart(ctx).apply {
                description.isEnabled = false
                setFitBars(true)
                xAxis.apply {
                    setDrawGridLines(false)
                    granularity = 1f
                }
                axisRight.isEnabled = false
                axisLeft.apply {
                    setDrawGridLines(true)
                    gridColor = Color.LTGRAY
                    axisMinimum = 0f
                }
                legend.isEnabled = false
                isDoubleTapToZoomEnabled = false
                animateY(1000)
            }
        },
        update = { chart ->
            val entries = data.mapIndexed { i, d -> BarEntry(i.toFloat(), d.value) }
            val labels = data.map { it.label }
            chart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            val ds = BarDataSet(entries, "").apply {
                color = barColor
                valueTextSize = 10f
            }
            chart.data = BarData(ds).apply { barWidth = 0.7f }
            chart.invalidate()
        },
        modifier = Modifier.fillMaxWidth().height(240.dp)
    )
}
