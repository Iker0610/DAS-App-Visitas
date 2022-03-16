package das.omegaterapia.visits.model

import androidx.room.Entity
import androidx.room.PrimaryKey


//-------------------------------------------------------------------

//                USER FOR ROOM DB

//-------------------------------------------------------------------

// User entity
@Entity(tableName = "user")
data class User(
    @PrimaryKey val username: String,
    val hashedPassword: String,
)

