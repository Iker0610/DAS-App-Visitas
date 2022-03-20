package das.omegaterapia.visits.model

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import das.omegaterapia.visits.model.dao.AuthDao
import das.omegaterapia.visits.model.dao.VisitsDao
import das.omegaterapia.visits.model.entities.AuthUser
import das.omegaterapia.visits.model.entities.Client
import das.omegaterapia.visits.model.entities.VisitCard
import das.omegaterapia.visits.model.entities.VisitData

@Database(entities = [AuthUser::class, VisitData::class, Client::class], version = 1, exportSchema = false)
@TypeConverters(ZonedDateTimeConverter::class)
abstract class OmegaterapiaVisitsDatabase : RoomDatabase() {
    abstract fun authDao(): AuthDao
    abstract fun visitsDao(): VisitsDao
}