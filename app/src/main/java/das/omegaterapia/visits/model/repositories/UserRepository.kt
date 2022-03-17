package das.omegaterapia.visits.model.repositories

import das.omegaterapia.visits.model.dao.UserDao
import javax.inject.Inject

class UserRepository @Inject constructor(private val userDao: UserDao) {
    suspend fun getUserPassword() {}
}