package das.omegaterapia.visits.model.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import das.omegaterapia.visits.model.User
import das.omegaterapia.visits.model.dao.UserDao

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
