package das.omegaterapia.visits.model.repositories

import android.database.sqlite.SQLiteConstraintException
import das.omegaterapia.visits.model.dao.AuthDao
import das.omegaterapia.visits.model.entities.AuthUser
import javax.inject.Inject

class UserRepository @Inject constructor(private val authDao: AuthDao) {
    suspend fun getUserPassword(username: String): String? {
        return try {
            authDao.getUserPassword(username)
        } catch (e: SQLiteConstraintException) {
            null
        }
    }

    suspend fun createUser(authUser: AuthUser): Boolean {
        return try {
            authDao.createUser(authUser)
            true
        } catch (e: SQLiteConstraintException) {
            false
        }
    }

}