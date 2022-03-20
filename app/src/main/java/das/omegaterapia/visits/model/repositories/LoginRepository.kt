package das.omegaterapia.visits.model.repositories

import android.database.sqlite.SQLiteConstraintException
import das.omegaterapia.visits.model.dao.AuthenticationDao
import das.omegaterapia.visits.model.entities.AuthUser
import das.omegaterapia.visits.preferences.ILoginSettings
import javax.inject.Inject

interface ILoginRepository : ILoginSettings {
    suspend fun getUserPassword(username: String): String?
    suspend fun createUser(authUser: AuthUser): Boolean
}

class LoginRepository @Inject constructor(
    private val authDao: AuthenticationDao,
    private val loginSettings: ILoginSettings,
) : ILoginRepository {
    override suspend fun getUserPassword(username: String): String? {
        return try {
            authDao.getUserPassword(username)
        } catch (e: SQLiteConstraintException) {
            null
        }
    }

    override suspend fun createUser(authUser: AuthUser): Boolean {
        return try {
            authDao.createUser(authUser)
            true
        } catch (e: SQLiteConstraintException) {
            false
        }
    }

    override suspend fun getLastLoggedUser(): String? = loginSettings.getLastLoggedUser()
    override suspend fun setLastLoggedUser(value: String) = loginSettings.setLastLoggedUser(value)
}