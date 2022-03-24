package das.omegaterapia.visits.activities.main.screens.addedit

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import das.omegaterapia.visits.activities.main.composables.form.VisitForm
import das.omegaterapia.visits.model.entities.VisitCard

@Composable
fun AddVisitScreen(
    addVisitCard: suspend (VisitCard) -> Boolean,
    modifier: Modifier = Modifier,
    onBackPressed: ()->Unit = {},
    onSuccessfulSubmit: () -> Unit = onBackPressed,
) {
    BackHandler(onBack = onBackPressed)

    VisitForm(
        modifier = modifier,
        submitVisitCard = addVisitCard,
        onSuccessfulSubmit = onSuccessfulSubmit
    )
}