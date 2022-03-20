package das.omegaterapia.visits.data

import das.omegaterapia.visits.model.entities.VisitCard
import das.omegaterapia.visits.model.entities.VisitData
import java.time.LocalDateTime
import kotlin.random.Random

val getYear = { Random.nextInt(2018, 2022) }
val getMonth = { Random.nextInt(1, 12) }
val getDay = { Random.nextInt(1, 28) }
val getHour = { Random.nextInt(8, 19) }
val getMinute = { Random.nextInt(0, 59) }
val getVisitDate = { LocalDateTime.of(getYear(), getMonth(), getDay(), getHour(), getMinute()) }

val getRandomClient = { clients[Random.nextInt(clients.size)] }
val getRandomCompanion = { clients[Random.nextInt(clients.size)].toString() }

const val observation =
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam felis elit, mattis scelerisque orci sed, consequat facilisis leo. In at mi diam. In at vestibulum massa. Nulla viverra, magna quis auctor blandit, dui massa efficitur neque, nec placerat felis mauris non purus. Etiam vestibulum accumsan tristique. Vivamus aliquet dui nec nulla eleifend, in rutrum nulla blandit. Suspendisse et elementum elit. Sed varius mattis facilisis. "

val visitList = listOf(

    VisitCard(
        mainClient = getRandomClient(),
        visitData = VisitData(
            mainClientPhone = "",
            companions = mutableListOf(getRandomCompanion(), getRandomCompanion(), getRandomCompanion()),
            visitDate = getVisitDate(),
            observations = observation,
            isVIP = true
        )
    ),

    VisitCard(
        mainClient = getRandomClient(),
        visitData = VisitData(
            mainClientPhone = "",
            visitDate = getVisitDate(),
        )
    ),

    VisitCard(
        mainClient = getRandomClient(),
        visitData = VisitData(
            mainClientPhone = "",
            visitDate = getVisitDate(),
            observations = observation
        )
    ),

    VisitCard(
        mainClient = getRandomClient(),
        visitData = VisitData(
            mainClientPhone = "",
            companions = mutableListOf(getRandomCompanion(), getRandomCompanion()),
            visitDate = getVisitDate(),
        )
    ),

    VisitCard(
        mainClient = getRandomClient(),
        visitData = VisitData(
            mainClientPhone = "",
            companions = mutableListOf(getRandomCompanion()),
            visitDate = getVisitDate(),
            observations = observation
        )
    ),

    VisitCard(
        mainClient = getRandomClient(),
        visitData = VisitData(
            mainClientPhone = "",
            companions = mutableListOf(getRandomCompanion()),
            visitDate = getVisitDate(),
        )
    ),
)
