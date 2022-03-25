package das.omegaterapia.visits.preferences

import kotlinx.coroutines.flow.Flow


interface IUserPreferences {
    fun userLanguage(user: String): Flow<String>
    suspend fun setUserLanguage(user: String, langCode: String)
}