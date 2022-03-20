package das.omegaterapia.visits.model.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


data class Direction(
    var address: String,
    var town: String,
    var zip: String,
) {
    override fun toString(): String {
        return "$address, $town $zip"
    }
}


@Entity(indices = [Index(value = ["town", "zip"])])
data class Client(
    var name: String,
    var surname: String,

    @ColumnInfo(name = "phone_number")
    @PrimaryKey var phoneNum: String,

    @Embedded
    var direction: Direction,
) {
    override fun toString(): String {
        return "$name $surname"
    }
}

