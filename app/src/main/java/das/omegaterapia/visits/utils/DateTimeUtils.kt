package das.omegaterapia.visits.utils

import java.time.DayOfWeek.MONDAY
import java.time.DayOfWeek.SUNDAY
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.util.*


enum class TemporalConverter(val converter: (ZonedDateTime) -> String) {
    MONTH({ it.with(TemporalAdjusters.firstDayOfMonth()).format(DateTimeFormatter.ofPattern("yyyy - MMMM")).uppercase() }),
    WEEK(::getWeek),
    DAY(::getDay),
    HOUR(::getHour),
    HOUR_WITH_DAY({
        getHour(it, "EEEE dd - HH:mm")
            .replaceFirstChar { char -> if (char.isLowerCase()) char.titlecase(Locale.getDefault()) else char.toString() }
    }),
    HOUR_WITH_FULL_DATE({ getHour(it, "yyyy MMM dd (EE) - HH:mm") })
    ;

    fun <T> groupDates(list: List<T>, key: (T) -> ZonedDateTime): Map<String, List<T>> = list.groupBy { this.converter(key(it)) }
}

private fun getWeek(dateTime: ZonedDateTime): String {
    val mondayOfWeek = dateTime.with(TemporalAdjusters.previousOrSame(MONDAY))
    val initialString = mondayOfWeek.format(DateTimeFormatter.ofPattern("yyyy MMM dd"))

    return (initialString + mondayOfWeek.with(TemporalAdjusters.next(SUNDAY)).format(DateTimeFormatter.ofPattern(" - MMM dd"))).uppercase()
}

private fun getDay(dateTime: ZonedDateTime): String {
    val date = dateTime.with(TemporalAdjusters.ofDateAdjuster { d: LocalDate -> d })
    val dateString = date.format(DateTimeFormatter.ofPattern("yyyy - MMM dd")).uppercase()
    val dayOfWeek = date.format(DateTimeFormatter.ofPattern("EEEE"))
        .replaceFirstChar { char -> if (char.isLowerCase()) char.titlecase(Locale.getDefault()) else char.toString() }

    return "$dateString, $dayOfWeek"
}

private fun getHour(dateTime: ZonedDateTime, pattern: String = "HH:mm"): String =
    dateTime.truncatedTo(ChronoUnit.HOURS).format(DateTimeFormatter.ofPattern(pattern))
