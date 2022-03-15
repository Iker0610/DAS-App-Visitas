package das.omegaterapia.visits.ui.components

import android.content.Context
import android.widget.Toast
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.dateTimePicker
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun selectDateTime(context: Context, onDateTimeSelected: (LocalDateTime) -> Unit) {
    MaterialDialog(context).show {
        dateTimePicker(requireFutureDateTime = true) { _, dateTime ->
            onDateTimeSelected(LocalDateTime.ofInstant(dateTime.toInstant(), dateTime.timeZone.toZoneId()))
        }
    }
}

@Composable
fun OutlinedDateTimeField(
    modifier: Modifier = Modifier,

    date: LocalDateTime = LocalDateTime.now(),
    onDateTimeSelected: (LocalDateTime) -> Unit = {},
    dateFormatPattern: String = "dd/MM/yyyy hh:mm",

    label: @Composable () -> Unit = { Text(text = "Date-Time") },
    placeholder: @Composable (() -> Unit)? = { Text(text = dateFormatPattern) },
    trailingIcon: @Composable (() -> Unit)? = null,

    enabled: Boolean = true,
) {
    val dateTimeText = date.format(DateTimeFormatter.ofPattern(dateFormatPattern))
    val context = LocalContext.current

    OutlinedTextField(
        modifier = modifier,

        label = label,
        leadingIcon = {
            IconButton(onClick = {
                selectDateTime(context) {
                    Toast
                        .makeText(context, it.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }) {
                Icon(Icons.Default.Event, contentDescription = "Visit Date")
            }
        },
        placeholder = placeholder,

        value = dateTimeText!!,
        onValueChange = {},

        trailingIcon = trailingIcon,

        readOnly = true,
        singleLine = true,
        maxLines = 1
    )
}