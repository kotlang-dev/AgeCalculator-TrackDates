package com.synac.agecalculator.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "occasions")
data class OccasionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val dateMillis: Long?,
    val emoji: String
)