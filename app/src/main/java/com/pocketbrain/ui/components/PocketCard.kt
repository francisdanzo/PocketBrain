package com.pocketbrain.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pocketbrain.domain.model.*
import com.pocketbrain.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

fun pocketGradient(pocketType: PocketType): List<Color> = when (pocketType) {
    PocketType.DAILY      -> PocketDailyGradient
    PocketType.SAVINGS    -> PocketSavingsGradient
    PocketType.INVESTMENT -> PocketInvestmentGradient
    PocketType.FUN        -> PocketFunGradient
}

fun pocketIcon(pocketType: PocketType): String = when (pocketType) {
    PocketType.DAILY      -> "🏠"
    PocketType.SAVINGS    -> "🏦"
    PocketType.INVESTMENT -> "📈"
    PocketType.FUN        -> "🎉"
}

@Composable
fun PocketCard(pocket: PocketState) {
    val fmt = NumberFormat.getCurrencyInstance(Locale.getDefault())
    val gradientColors = pocketGradient(pocket.pocketType)

    // Animated progress
    var progTarget by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(pocket.progressFraction) { progTarget = pocket.progressFraction }
    val animatedProgress by animateFloatAsState(
        targetValue = progTarget,
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        label = "progress"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        listOf(
                            gradientColors[0].copy(alpha = 0.12f),
                            gradientColors[1].copy(alpha = 0.04f)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                )
                .padding(18.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Top row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    Brush.linearGradient(gradientColors)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(pocketIcon(pocket.pocketType), style = MaterialTheme.typography.titleMedium)
                        }
                        Column {
                            Text(
                                pocket.pocketType.labelFr,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            if (pocket.isOverBudget) {
                                Text(
                                    "Dépassement!",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ErrorRed
                                )
                            }
                        }
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            fmt.format(pocket.remaining),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (pocket.isOverBudget) ErrorRed else gradientColors[0]
                        )
                        Text(
                            "sur ${fmt.format(pocket.allocated)}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(0.5f)
                        )
                    }
                }

                // Animated progress bar
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = if (pocket.isOverBudget) ErrorRed else gradientColors[0],
                    trackColor = gradientColors[0].copy(alpha = 0.15f)
                )

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        "Dépensé: ${fmt.format(pocket.spent)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
                    )
                    Text(
                        "${(animatedProgress * 100).toInt()}%",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        color = if (pocket.isOverBudget) ErrorRed else gradientColors[0]
                    )
                }
            }
        }
    }
}
