package das.omegaterapia.visits.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


//------------------------------------------------------------------------------
// User entity
@Entity(tableName = "user")
data class AuthUser(
    @PrimaryKey val username: String,
    val hashedPassword: String,
)