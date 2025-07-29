package com.synac.agecalculator.presentation.list_detail

sealed interface ListDetailEvent {
    data class ShowSnackbar(val message: String) : ListDetailEvent
}