package das.omegaterapia.visits.model.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.ZonedDateTime
import java.util.*


@Entity(
    foreignKeys = [
        ForeignKey(entity = AuthUser::class, parentColumns = ["username"], childColumns = ["user"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Client::class, parentColumns = ["phone_number"], childColumns = ["main_client_phone"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index(value = ["user", "visit_date"]), Index(value = ["main_client_phone"])],
)
data class VisitData(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),

    var user: String = "",

    @ColumnInfo(name = "main_client_phone")
    var mainClientPhone: String,

    var companions: List<String> = listOf(),

    @ColumnInfo(name = "visit_date")
    var visitDate: ZonedDateTime,

    var observations: String = "",

    @ColumnInfo(name = "is_VIP")
    var isVIP: Boolean = false,
)


data class VisitCard(
    @Embedded var visitData: VisitData,

    @Relation(parentColumn = "main_client_phone", entityColumn = "phone_number", entity = Client::class)
    var client: Client,
) {
    @delegate:Ignore
    val id by visitData::id

    @delegate:Ignore
    var user by visitData::user

    @delegate:Ignore
    val companions by visitData::companions

    @delegate:Ignore
    val visitDate by visitData::visitDate

    @delegate:Ignore
    val observations by visitData::observations

    @delegate:Ignore
    val isVIP by visitData::isVIP
}


/*
data class VisitRegistry(
    val registryTimestamp: LocalDateTime,
    val state: VisitRegistryState,
    var observations: String = "",
)

data class Visit(
    val visitCard: VisitCard,
    val registry: MutableList<VisitRegistry>,
)


enum class VisitRegistryState(val displayName: String) {
    Null("Null"),
    Absent("Absent"),
    OK("OK"),
    OnlySurvey("Only Survey"),
    Sale("Sale"),
    Reconfirm("Reconfirm"),
    OnHold("On Hold"),
}
*/
