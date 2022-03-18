package das.omegaterapia.visits.model

import androidx.room.Database
import androidx.room.RoomDatabase
import das.omegaterapia.visits.model.dao.AuthDao
import das.omegaterapia.visits.model.entities.AuthUser

@Database(entities = [AuthUser::class], version = 1, exportSchema = false)
abstract class OmegaterapiaVisitsDatabase : RoomDatabase() {
    abstract fun authDao(): AuthDao
}