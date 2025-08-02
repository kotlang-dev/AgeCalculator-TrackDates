package com.synac.agecalculator.domain.usecase

import com.synac.agecalculator.domain.model.Occasion
import com.synac.agecalculator.domain.repository.OccasionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SaveOccasionUseCase(
    private val repository: OccasionRepository
) {
    private var saveJob: Job? = null

    operator fun invoke(
        scope: CoroutineScope,
        occasion: Occasion,
        onSaveComplete: (Int) -> Unit = {}
    ) {
        saveJob?.cancel()
        saveJob = scope.launch {
            delay(500)
            val savedId = repository.insertOccasion(occasion)
            onSaveComplete(savedId)
        }
    }
}