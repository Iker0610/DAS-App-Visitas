package das.omegaterapia.visits.model.entities

import java.util.*

data class Client(
    // @PrimaryKey(autoGenerate = true)
    val id: String = UUID.randomUUID().toString(),
    var name: String,
    var surname: String,
    var direction: Direction,
    var phoneNum: String,
) {
    override fun toString(): String {
        return "$name $surname"
    }
}

// Mirar de sustituirlo por la clase Adress de android
data class Direction(
    var address: String,
    var town: String,
    var zip: String,
)