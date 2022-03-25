package das.omegaterapia.visits.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "PREFERENCES_SETTINGS")

private object PreferencesKeys {
    val LAST_LOGGED_USER = stringPreferencesKey("last_logged_user")
}


@Singleton
class PreferencesRepository @Inject constructor(private val context: Context) : ILoginSettings, IUserPreferences {

    override suspend fun getLastLoggedUser(): String? = context.dataStore.data.first()[PreferencesKeys.LAST_LOGGED_USER]

    override suspend fun setLastLoggedUser(value: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_LOGGED_USER] = value
        }
    }
}