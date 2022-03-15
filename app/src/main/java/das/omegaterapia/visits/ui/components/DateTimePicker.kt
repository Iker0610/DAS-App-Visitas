package das.omegaterapia.visits.ui.components

import android.content.Context
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import com.afollestad.materialdialogs.DialogBehavior
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.ModalDialog
import com.afollestad.materialdialogs.datetime.dateTimePicker
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateTimeDialogBehavior(val onDismissAction: () -> Unit) : DialogBehavior by ModalDialog {
    override fun onDismiss(): Boolean {
        this.onDismissAction()
        return false
    }
}


fun selectDateTime(
    context: Context,
    requireFutureDateTime: Boolean = false,
    onDismissAction: () -> Unit = {},
    onDateTimeSelected: (LocalDateTime) -> Unit,
) {
    MaterialDialog(context, DateTimeDialogBehavior(onDismissAction)).show {
        dateTimePicker(requireFutureDateTime = requireFutureDateTime) { _, dateTime ->
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
    requireFutureDateTime: Boolean = false,

    label: @Composable () -> Unit = { Text(text = "Date-Time") },
    placeholder: @Composable (() -> Unit)? = { Text(text = dateFormatPattern) },
    leadingIcon: @Composable (() -> Unit)? = { Icon(Icons.Default.Event, contentDescription = "Visit Date") },
    trailingIcon: @Composable (() -> Unit)? = null,

    enabled: Boolean = true,
) {
    val dateTimeText = date.format(DateTimeFormatter.ofPattern(dateFormatPattern))!!
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        modifier = modifier.onFocusChanged {
            if (it.isFocused) {
                selectDateTime(context, requireFutureDateTime, onDismissAction = { focusManager.clearFocus() }) { dateTime ->
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