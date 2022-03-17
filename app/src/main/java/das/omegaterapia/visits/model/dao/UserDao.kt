package das.omegaterapia.visits.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import das.omegaterapia.visits.model.entities.User


@Dao
interface UserDao {
    @Insert
    suspend fun createUser(user: User)

    @Query("SELECT hashedPassword FROM user WHERE username = :username ")
    suspend fun getUserPassword(username: String): String


    @Query("DELETE FROM user where username = :username")
    suspend fun deleteUser(username: String)
}