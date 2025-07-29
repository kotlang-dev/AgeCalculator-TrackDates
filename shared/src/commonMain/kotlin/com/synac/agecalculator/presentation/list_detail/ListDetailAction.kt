package com.synac.agecalculator.presentation.list_detail

import com.synac.agecalculator.presentation.calculator.DateField

sealed interface ListDetailAction {
    data class OccasionSelected(val occasionId: Int?) : ListDetailAction
    data object AddNewOccasionClicked : ListDetailAction
    data object ShowEmojiPicker : ListDetailAction
    data object DismissEmojiPicker : ListDetailAction
    data class EmojiSelected(val emoji: String) : ListDetailAction
    data class ShowDatePicker(val dateField: DateField) : ListDetailAction
    data object DismissDatePicker : ListDetailAction
    data class DateSelected(val millis: Long?) : ListDetailAction
    data class SetTitle(val title: String) : ListDetailAction
    data object SaveOccasionChanges : ListDetailAction
    data object DeleteOccasion : ListDetailAction
    data object NavigateToSettingsScreen : ListDetailAction
    data object NavigateUp : ListDetailAction
}