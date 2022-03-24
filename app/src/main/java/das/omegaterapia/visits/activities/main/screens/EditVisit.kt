package das.omegaterapia.visits.activities.main.screens

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import das.omegaterapia.visits.activities.main.composables.form.VisitForm
import das.omegaterapia.visits.model.entities.VisitCard

@Composable
fun EditVisitScreen(
    visitCard: VisitCard,
    onEditVisitCard: suspend (VisitCard) -> Boolean,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {},
    onSuccessfulSubmit: () -> Unit = onBackPressed,
) {
    BackHandler(onBack = onBackPressed)

    VisitForm(
        initialVisitCard = visitCard,
        modifier = modifier,
        submitVisitCard = onEditVisitCard,
        onSuccessfulSubmit = onSuccessfulSubmit
    )
}