package das.omegaterapia.visits.activities.main.screens.visitlists

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import das.omegaterapia.visits.R
import das.omegaterapia.visits.activities.main.VisitsViewModel
import das.omegaterapia.visits.activities.main.composables.VisitList
import das.omegaterapia.visits.model.entities.VisitCard
import das.omegaterapia.visits.model.entities.VisitId
import das.omegaterapia.visits.ui.theme.getButtonShape


@Composable
fun TodaysVisitsScreen(
    visitViewModel: VisitsViewModel,
    modifier: Modifier = Modifier,
    onItemEdit: (VisitCard) -> Unit = visitViewModel::currentToEditVisit::set,
    onItemDelete: (VisitId) -> Unit = { visitViewModel.deleteVisitCard(it) },
    lazyListState: LazyListState = rememberLazyListState(),
    onScrollStateChange: (Boolean) -> Unit = {},
    paddingAtBottom: Boolean = false,
) {
    val context = LocalContext.current
    var showExitAlertDialog by rememberSaveable { mutableStateOf(false) }

    BackHandler { showExitAlertDialog = true }

    if (showExitAlertDialog) {
        AlertDialog(
            title = { Text(stringResource(R.string.app_exit_dialog_text)) },
            confirmButton = {
                TextButton(onClick = { (context as Activity).finish() }, shape = getButtonShape()) {
                    Text(text = stringResource(id = R.string.exit_button))
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitAlertDialog = false }, shape = getButtonShape()) {
                    Text(text = stringResource(id = R.string.dismiss_button))
                }
            },
            onDismissRequest = { showExitAlertDialog = false },
            shape = RectangleShape,
        )
    }

    val groupedVisits by visitViewModel.todaysVisits.collectAsState(emptyMap())
    VisitList(
        groupedVisitCards = groupedVisits,
        modifier = modifier.fillMaxSize(),

        onItemEdit = onItemEdit,
        onItemDelete = onItemDelete,

        lazyListState = lazyListState,
        onScrollStateChange = onScrollStateChange,

        paddingAtBottom = paddingAtBottom
    )
}