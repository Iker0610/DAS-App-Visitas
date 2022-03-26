package das.omegaterapia.visits.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import das.omegaterapia.visits.ui.components.generic.CenteredRow
import das.omegaterapia.visits.ui.theme.getButtonShape
import java.time.DayOfWeek.MONDAY
import java.time.DayOfWeek.SUNDAY
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.util.*


enum class TemporalConverter(val configName: String, val example: String, val converter: (ZonedDateTime) -> String) {
    MONTH("Month", "2022 - January", { it.with(TemporalAdjusters.firstDayOfMonth()).format(DateTimeFormatter.ofPattern("yyyy - MMMM")).uppercase() }),
    WEEK("Week", "2022 FEB 28 - MAR 06", ::getWeek),
    DAY("Day", "2000 - JAN 02, Tuesday", ::getDay),
    HOUR("Hour", "17:45", ::getHour),
    HOUR_WITH_DAY("Hour with day", "Monday 26 - 17:45", {
        getHour(it, "EEEE dd - HH:mm")
            .replaceFirstChar { char -> if (char.isLowerCase()) char.titlecase(Locale.getDefault()) else char.toString() }
    }),
    HOUR_WITH_FULL_DATE("Full date-time", "2000 JAN 02 (Mon) - 17:45", { getHour(it, "yyyy MMM dd (EE) - HH:mm") })
    ;

    fun <T> groupDates(list: List<T>, key: (T) -> ZonedDateTime): Map<String, List<T>> = list.groupBy { this.converter(key(it)) }

    companion object {
        val oneDayDefault = HOUR_WITH_DAY
        val multipleDayDefault = WEEK

        val oneDayConverters = listOf(HOUR, HOUR_WITH_DAY, HOUR_WITH_FULL_DATE)
        val multipleDaysConverters = listOf(MONTH, WEEK, DAY)
    }
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


@Composable
fun DayConverterPickerDialog(
    selectedConverter: String,
    onConverterSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    title: String,
) {
    ConverterPickerDialog(
        formatList = TemporalConverter.oneDayConverters,
        selectedConverter = selectedConverter,
        onConverterSelected = onConverterSelected,
        title = title,
        onDismiss = onDismiss
    )
}

@Composable
fun MultipleDaysConverterPickerDialog(
    selectedConverter: String,
    onConverterSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    title: String,
) {
    ConverterPickerDialog(
        formatList = TemporalConverter.multipleDaysConverters,
        selectedConverter = selectedConverter,
        onConverterSelected = onConverterSelected,
        title = title,
        onDismiss = onDismiss
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ConverterPickerDialog(
    formatList: List<TemporalConverter>,
    selectedConverter: String,
    onConverterSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    title: String,
) {
    var selected by rememberSaveable { mutableStateOf(selectedConverter) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RectangleShape,
            color = MaterialTheme.colors.surface,
            contentColor = contentColorFor(SnackbarDefaults.backgroundColor),
        ) {
            Column {
                Column(Modifier
                    .padding(horizontal = 24.dp)
                    .height(64.dp), verticalArrangement = Arrangement.Center) {
                    Text(text = title, style = MaterialTheme.typography.h6)
                }

                Divider()

                Column(Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
                ) {
                    formatList.forEach { format ->
                        ListItem(
                            modifier = Modifier.clickable { selected = format.name },
                            trailing = { Checkbox(checked = selected == format.name, onCheckedChange = { selected = format.name }) },
                            text = { Text(text = format.configName, style = MaterialTheme.typography.body1) },
                            secondaryText = { Text(text = format.example) }
                        )
                    }
                }

                Divider()

                CenteredRow(Modifier
                    .fillMaxWidth()
                    .padding(8.dp), horizontalArrangement = Arrangement.End) {
                    CenteredRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TextButton(onClick = onDismiss, shape = getButtonShape()) { Text(text = "CANCEL") }
                        TextButton(onClick = { onConverterSelected(selected) },
                            shape = getButtonShape()) { Text(text = "APPLY") }
                    }
                }
            }
        }
    }
}