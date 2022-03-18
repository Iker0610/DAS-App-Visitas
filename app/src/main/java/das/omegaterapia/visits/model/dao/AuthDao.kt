package das.omegaterapia.visits.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import das.omegaterapia.visits.model.entities.AuthUser


@Dao
interface AuthDao {
    @Insert
    suspend fun createUser(authUser: AuthUser)

    @Query("SELECT hashedPassword FROM user WHERE username = :username ")
    suspend fun getUserPassword(username: String): String
}