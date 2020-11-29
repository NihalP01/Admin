package com.example.admin.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

enum class Logged {
    IN, OUT
}

class UserPrefManager(context: Context) {

    companion object {
        val LOGGED_IN = preferencesKey<Boolean>("logged_in")
    }

    private val dataStore = context.createDataStore("user_pref")

    val loggedInflow: Flow<Logged> = dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preference ->
            when (preference[LOGGED_IN] ?: false) {
                true -> Logged.IN
                false -> Logged.OUT
            }
        }

    suspend fun setLogged(logged: Logged) {
        dataStore.edit { preferences ->
            preferences[LOGGED_IN] = when (logged) {
                Logged.OUT -> false
                Logged.IN -> true
            }
        }
    }
}