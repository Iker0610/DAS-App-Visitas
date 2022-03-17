package das.omegaterapia.visits.model

import androidx.room.Database
import androidx.room.RoomDatabase
import das.omegaterapia.visits.model.dao.UserDao
import das.omegaterapia.visits.model.entities.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class OmegaterapiaVisitsDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}