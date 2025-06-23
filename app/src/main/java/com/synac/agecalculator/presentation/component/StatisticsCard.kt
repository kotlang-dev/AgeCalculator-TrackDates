package com.synac.agecalculator.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.synac.agecalculator.presentation.calculator.AgeStats
import com.synac.agecalculator.presentation.theme.spacing
import java.text.NumberFormat
import java.util.Locale

@Composable
fun StatisticsCard(
    modifier: Modifier = Modifier,
    ageStats: AgeStats
) {
    ElevatedCard(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
            Text(
                text = "Age Statistics",
                style = MaterialTheme.typography.titleMedium,
                textDecoration = TextDecoration.Underline
            )
            TotalTimeRow(label = "Total Years", value = ageStats.years)
            TotalTimeRow(label = "Total Months", value = ageStats.months)
            TotalTimeRow(label = "Total Weeks", value = ageStats.weeks)
            TotalTimeRow(label = "Total Days", value = ageStats.days)
            TotalTimeRow(label = "Total Hours", value = ageStats.hours)
            TotalTimeRow(label = "Total Minutes", value = ageStats.minutes)
            TotalTimeRow(label = "Total Seconds", value = ageStats.seconds)
        }
    }
}

@Composable
private fun TotalTimeRow(
    modifier: Modifier = Modifier,
    label: String,
    value: Int,
    separator: String = " : "
) {
    val formattedValue = NumberFormat.getNumberInstance(Locale.US).format(value)
    Row(modifier = modifier) {
        Text(
            modifier = Modifier.weight(1f),
            text = formattedValue,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End
        )
        Text(text = separator)
        Text(
            modifier = Modifier.weight(1f),
            text = label
        )
    }
}

@Preview
@Composable
private fun PreviewStatisticsCard() {
    StatisticsCard(
        ageStats = AgeStats(
            years = 10,
            months = 105,
            weeks = 1053,
            days = 46254,
            hours = 156548,
            minutes = 6583325,
            seconds = 65214565
        )
    )
}