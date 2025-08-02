package com.synac.agecalculator.presentation.list_detail

sealed interface ListDetailEvent {
    data class OccasionDeleted(val wasSinglePane: Boolean) : ListDetailEvent
}