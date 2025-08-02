package com.synac.agecalculator.presentation.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.synac.agecalculator.data.local.EmojiDataSource
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmojiPickerBottomSheet(
    modifier: Modifier = Modifier,
    isOpen: Boolean,
    onDismissRequest: () -> Unit,
    onEmojiSelected: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    if (isOpen) {
        ModalBottomSheet(
            modifier = modifier,
            onDismissRequest = onDismissRequest,
            sheetState = sheetState
        ) {
            EmojiPickerContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f),
                onEmojiSelected = {
                    scope.launch { sheetState.hide() }
                    onEmojiSelected(it)
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EmojiPickerContent(
    onEmojiSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = remember { EmojiDataSource.allCategories }
    val pagerState = rememberPagerState(pageCount = { categories.size })
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
    ) {
        TabRow(selectedTabIndex = pagerState.currentPage) {
            categories.forEachIndexed { index, category ->
                Tab(
                    selected = pagerState.currentPage == index,
                    icon = {
                        Icon(imageVector = category.icon, contentDescription = category.name)
                    },
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { pageIndex ->
            val category = categories[pageIndex]
            EmojiGrid(
                emojis = category.emojis,
                onEmojiSelected = onEmojiSelected
            )
        }
    }
}

@Composable
private fun EmojiGrid(
    emojis: List<String>,
    onEmojiSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(minSize = 48.dp),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(emojis) { emoji ->
            Text(
                text = emoji,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .clickable { onEmojiSelected(emoji) }
                    .padding(4.dp)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewEmojiPickerBottomSheet() {
    EmojiPickerBottomSheet(
        isOpen = true,
        onEmojiSelected = {},
        onDismissRequest = {}
    )
}