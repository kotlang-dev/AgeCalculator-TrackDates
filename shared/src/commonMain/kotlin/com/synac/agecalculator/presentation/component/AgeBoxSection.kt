package com.synac.agecalculator.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.synac.agecalculator.presentation.theme.greenTextColor
import com.synac.agecalculator.presentation.theme.spacing

@Composable
fun AgeBoxSection(
    title: String,
    values: List<Pair<String, Int>>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
        ElevatedCard{
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.medium),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                values.forEach { (label, value) ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = value.toString(),
                            style = MaterialTheme.typography.headlineLarge.copy(
                                color = MaterialTheme.colorScheme.greenTextColor,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }
    }
}

//@Preview(showBackground = true)
@Composable
private fun PreviewAgeBoxSection() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        AgeBoxSection(
            title = "Time Passed",
            values = listOf(
                "YEARS" to 25,
                "MONTHS" to 1,
                "DAYS" to 16
            )
        )
        Spacer(modifier = Modifier.height(24.dp))
        AgeBoxSection(
            title = "Upcoming",
            values = listOf(
                "MONTHS" to 8,
                "DAYS" to 12
            )
        )
    }

}