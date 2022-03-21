package das.omegaterapia.visits.ui.components.datetime

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun OutlinedDateTimeField(
    modifier: Modifier = Modifier,

    date: ZonedDateTime = ZonedDateTime.now(),
    onDateTimeSelected: (ZonedDateTime) -> Unit = {},
    dateFormatPattern: String = "dd/MM/yyyy hh:mm",
    requireFutureDateTime: Boolean = false,

    label: @Composable () -> Unit = { Text(text = "Date-Time") },
    placeholder: @Composable (() -> Unit)? = { Text(text = dateFormatPattern) },
    leadingIcon: @Composable (() -> Unit)? = { Icon(Icons.Default.Event, contentDescription = "Date and Time") },
    trailingIcon: @Composable (() -> Unit)? = null,

    dialogTitle: String = "Choose a Date and Time",

    enabled: Boolean = true,
) {
    val dateTimeText = date.format(DateTimeFormatter.ofPattern(dateFormatPattern))!!
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        modifier = modifier.onFocusChanged {
            if (it.isFocused) {
                openDateTimePickerDialog(context,dialogTitle, requireFutureDateTime, onDismissAction = { focusManager.clearFocus() }) { dateTime ->
                    onDateTimeSelected(dateTime)
                    focusManager.clearFocus()
                }
            }
        },

        label = label,
        leadingIcon = leadingIcon,
        placeholder = placeholder,

        value = dateTimeText,
        onValueChange = {},

        trailingIcon = trailingIcon,

        readOnly = true,
        singleLine = true,
        maxLines = 1,

        enabled = enabled
    )
}


@Composable
fun AlternativeOutlinedDateTimeField(
    modifier: Modifier = Modifier,

    date: ZonedDateTime = ZonedDateTime.now(),
    onDateTimeSelected: (ZonedDateTime) -> Unit = {},
    dateFormatPattern: String = "dd/MM/yyyy",
    timeFormatPattern: String = "hh:mm",
    requireFutureDateTime: Boolean = false,

    dateLabel: @Composable () -> Unit = { Text(text = "Date") },
    datePlaceholder: @Composable (() -> Unit)? = { Text(text = dateFormatPattern) },
    dateLeadingIcon: @Composable (() -> Unit)? = { Icon(Icons.Default.Event, contentDescription = "Date") },
    dateTrailingIcon: @Composable (() -> Unit)? = null,

    timeLabel: @Composable () -> Unit = { Text(text = "Time") },
    timePlaceholder: @Composable (() -> Unit)? = { Text(text = timeFormatPattern) },
    timeLeadingIcon: @Composable (() -> Unit)? = { Icon(Icons.Default.Schedule, contentDescription = "Time") },
    timeTrailingIcon: @Composable (() -> Unit)? = null,

    dialogTitle: String = "Choose a Date and Time",

    enabled: Boolean = true,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedDateTimeField(
            modifier = Modifier.weight(1.5f),

            date = date,
            onDateTimeSelected = onDateTimeSelected,
            dateFormatPattern = dateFormatPattern,
            requireFutureDateTime = requireFutureDateTime,

            label = dateLabel,
            placeholder = datePlaceholder,
            leadingIcon = dateLeadingIcon,
            trailingIcon = dateTrailingIcon,

            dialogTitle = dialogTitle,

            enabled = enabled,
        )

        OutlinedDateTimeField(
            modifier = Modifier.weight(1f),

            date = date,
            onDateTimeSelected = onDateTimeSelected,
            dateFormatPattern = timeFormatPattern,
            requireFutureDateTime = requireFutureDateTime,

            label = timeLabel,
            placeholder = timePlaceholder,
            leadingIcon = timeLeadingIcon,
            trailingIcon = timeTrailingIcon,

            dialogTitle = dialogTitle,

            enabled = enabled,
        )
    }
}