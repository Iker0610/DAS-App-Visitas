package das.omegaterapia.visits.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "PREFERENCES_SETTINGS")

@Suppress("FunctionName")
private object PreferencesKeys {
    val LAST_LOGGED_USER = stringPreferencesKey("last_logged_user")

    fun USER_LANG(user: String) = stringPreferencesKey("${user}_lang")
}


@Singleton
class PreferencesRepository @Inject constructor(private val context: Context) : ILoginSettings, IUserPreferences {

    override suspend fun getLastLoggedUser(): String? = context.dataStore.data.first()[PreferencesKeys.LAST_LOGGED_USER]

    override suspend fun setLastLoggedUser(value: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_LOGGED_USER] = value
        }
    }

    override fun userLanguage(user: String): Flow<String> =
        context.dataStore.data.map { it[PreferencesKeys.USER_LANG(user)] ?: Locale.getDefault().language }

    override suspend fun setUserLanguage(user: String, langCode: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_LANG(user)] = langCode
        }
    }
}