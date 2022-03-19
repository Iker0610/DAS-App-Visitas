package das.omegaterapia.visits.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "LOGIN_SETTINGS")

private object PreferencesKeys {
    val LAST_LOGGED_USER = stringPreferencesKey("last_logged_user")
}

interface ILoginSettingsRepository {
    suspend fun getLastLoggedUser(): String?
    suspend fun setLastLoggedUser(value: String)
}

class LoginSettingsRepository @Inject constructor(private val context: Context) : ILoginSettingsRepository {

    override suspend fun getLastLoggedUser(): String? {
        return try {
            val preferences = context.dataStore.data.first()
            preferences[PreferencesKeys.LAST_LOGGED_USER]
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun setLastLoggedUser(value: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_LOGGED_USER] = value
        }
    }

}