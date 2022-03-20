package das.omegaterapia.visits.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime


class ZonedDateTimeConverter {
    @TypeConverter
    fun fromTimestamp(value: Long): ZonedDateTime {
        return ZonedDateTime.ofInstant(Instant.ofEpochSecond(value), ZoneId.systemDefault())
    }

    @TypeConverter
    fun dateToTimestamp(date: ZonedDateTime): Long {
        return date.toEpochSecond()
    }

    @TypeConverter
    fun listToJson(value: List<String>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String): MutableList<String> = Gson().fromJson(value, Array<String>::class.java).toMutableList()
}
