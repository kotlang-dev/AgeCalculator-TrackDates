package com.synac.agecalculator.presentation.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun StylizedAgeText(
    modifier: Modifier = Modifier,
    years: Int,
    months: Int,
    days: Int,
) {
    val styledText: AnnotatedString = buildAnnotatedString {
        val highlightStyle = SpanStyle(
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        withStyle(style = highlightStyle) {
            append("$years ")
        }
        append("Years ")
        withStyle(style = highlightStyle) {
            append("$months ")
        }
        append("Months ")
        withStyle(style = highlightStyle) {
            append("$days ")
        }
        append("Days")
    }

    Text(
        modifier = modifier,
        text = styledText,
        style = MaterialTheme.typography.bodyLarge
    )
}


@Preview(showBackground = true)
@Composable
private fun PreviewStylizedAgeText() {
    StylizedAgeText(
        years = 25,
        months = 8,
        days = 14
    )
}