package com.synac.agecalculator.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.synac.agecalculator.domain.model.PrefsKey
import com.synac.agecalculator.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferenceRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : PreferenceRepository {

    override suspend fun <T> savePreference(key: PrefsKey<T>, value: T) {
        dataStore.edit { settings ->
            val dataStoreKey = key.toDatastoreKey()
            settings[dataStoreKey] = value
        }
    }

    override fun <T> getPreference(key: PrefsKey<T>, defaultValue: T): Flow<T> {
        return dataStore.data.map { preferences ->
            val dataStoreKey = key.toDatastoreKey()
            preferences[dataStoreKey] ?: defaultValue
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> PrefsKey<T>.toDatastoreKey(): Preferences.Key<T> {
        return when (this) {
            PrefsKey.AppTheme -> intPreferencesKey(name)
        } as Preferences.Key<T>
    }
}