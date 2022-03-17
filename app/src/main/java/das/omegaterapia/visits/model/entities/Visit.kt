package das.omegaterapia.visits.model.entities

import java.time.LocalDateTime
import java.util.*


data class VisitCard(
    val id: String = UUID.randomUUID().toString(),

    var mainClient: Client,
    var companions: MutableList<String> = mutableListOf(),

    var visitDate: LocalDateTime,
    var observations: String = "",

    var isVIP: Boolean = false,
)

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