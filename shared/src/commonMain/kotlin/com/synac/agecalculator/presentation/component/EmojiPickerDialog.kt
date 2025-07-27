package com.synac.agecalculator.presentation.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.synac.agecalculator.presentation.theme.spacing

@Composable
fun EmojiPickerDialog(
    modifier: Modifier = Modifier,
    isOpen: Boolean,
    onDismissRequest: () -> Unit,
    onEmojiSelected: (String) -> Unit
) {
    if (isOpen) {
        AlertDialog(
            modifier = modifier,
            onDismissRequest = onDismissRequest,
            confirmButton = {},
            title = {
                Text(text = "Pick an Emoji")
            },
            text = {
                EmojiGrid(onEmojiSelected = onEmojiSelected)
            }
        )
    }
}

@Composable
private fun EmojiGrid(
    modifier: Modifier = Modifier,
    onEmojiSelected: (String) -> Unit
) {
    val emojis = listOf(
        "🎂", "🎉", "🎁", "❤️", "🎓", "📅", "🧁", "🍰",
        "👶", "👨‍👩‍👧‍👦", "💍", "🎊", "🎵", "📌", "🎈", "🌟",
        "🕯️", "🥳", "🍔", "🍕", "🍦", "🍾", "📖", "🎮", "🏆"
    )
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(5)
    ) {
        items(emojis) { emoji ->
            IconButton(
                onClick = { onEmojiSelected(emoji) },
                modifier = Modifier.padding(MaterialTheme.spacing.extraSmall)
            ) {
                Text(
                    text = emoji,
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        }
    }
}

//@Preview
@Composable
private fun PreviewEmojiPickerDialog() {
    EmojiPickerDialog(
        isOpen = true,
        onEmojiSelected = {},
        onDismissRequest = {}
    )
}