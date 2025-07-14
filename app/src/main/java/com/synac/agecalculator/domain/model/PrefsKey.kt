package com.synac.agecalculator.domain.model

sealed class PrefsKey<T>(val name: String) {
    data object AppTheme : PrefsKey<Int>(name = "app_theme")
    data object LastUpdatePrompt : PrefsKey<Long>(name = "last_update_prompt_timestamp")
}