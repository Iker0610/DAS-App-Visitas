package das.omegaterapia.visits.ui.components.datetime

import android.content.Context
import com.afollestad.materialdialogs.DialogBehavior
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.ModalDialog
import com.afollestad.materialdialogs.datetime.dateTimePicker
import java.time.LocalDateTime


class DateTimeDialogBehavior(val onDismissAction: () -> Unit) : DialogBehavior by ModalDialog {
    override fun onDismiss(): Boolean {
        this.onDismissAction()
        return false
    }
}


fun openDateTimePickerDialog(
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