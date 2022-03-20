package das.omegaterapia.visits.model.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.LocalDateTime
import java.util.*


@Entity(foreignKeys = [])
data class VisitData(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "main_client_phone")
    var mainClientPhone: String,

    var companions: MutableList<String> = mutableListOf(),

    @ColumnInfo(name = "visit_date")
    var visitDate: LocalDateTime,

    var observations: String = "",

    @ColumnInfo(name = "is_VIP")
    var isVIP: Boolean = false,
)


data class VisitCard(
    @Embedded var visitData: VisitData,

    @Relation(parentColumn = "main_client_phone", entityColumn = "phone_number", entity = Client::class)
    @ColumnInfo(name = "main_client")
    var mainClient: Client,
) {
    val id by visitData::id
    val companions by visitData::companions
    val visitDate by visitData::visitDate
    val observations by visitData::observations
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
