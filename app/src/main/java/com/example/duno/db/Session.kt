package com.example.duno.db

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class Session @Inject constructor(private val dataStore: DataStore<Preferences>) {
    companion object {
        const val DATA = "Data"
        private const val IsLogin = "IsLogin"
        private const val NAME = "Name"
        private const val PASSWORD = "Password"
        val name = stringPreferencesKey(NAME)
        val isLogin = booleanPreferencesKey(IsLogin)
        val password = stringPreferencesKey(PASSWORD)
    }

    fun isUserLoggedIn(): Flow<Boolean> {
        return dataStore.data.catch {
            emit(emptyPreferences())
        }.map { preference ->
            preference[isLogin] ?: false
        }
    }

    suspend fun setUserLoggedIn(isUserLoggedIn: Boolean) {
        dataStore.edit { preference ->
            preference[isLogin] = isUserLoggedIn
        }
    }

    fun getUserName(): Flow<String> {
        return dataStore.data.catch {
            Timber.e(it,it.message)
            emit(emptyPreferences())
        }.map { preferences ->
            preferences[name] ?: ""
        }
    }

    suspend fun setUserName(userName: String) {
        dataStore.edit { preference ->
            preference[name] = userName
        }
    }

    fun getPassword(): Flow<String> {
        return dataStore.data.catch {
            emit(emptyPreferences())
        }.map { value: Preferences ->
            value[password] ?: ""
        }
    }

    suspend fun setPassword(userPassWord: String) {
        dataStore.edit { preference ->
            preference[password] = userPassWord
        }
    }
}