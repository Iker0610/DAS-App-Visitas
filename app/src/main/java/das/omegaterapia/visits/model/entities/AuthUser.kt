package das.omegaterapia.visits.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


//------------------------------------------------------------------------------
// User entity
@Entity(tableName = "User")
data class AuthUser(
    @PrimaryKey val username: String,
    @ColumnInfo(name = "hashed_password") val hashedPassword: String,
)