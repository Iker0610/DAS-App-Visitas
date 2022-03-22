package das.omegaterapia.visits.activities.main.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import das.omegaterapia.visits.activities.main.composables.form.VisitForm
import das.omegaterapia.visits.model.entities.VisitCard

@Composable
fun AddVisitScreen(
    addVisitCard: suspend (VisitCard) -> Boolean,
    modifier: Modifier = Modifier,
    onSuccessfulSubmit: () -> Unit = {},
) {
    VisitForm(
        modifier = modifier,
        submitVisitCard = addVisitCard,
        onSuccessfulSubmit = onSuccessfulSubmit
    )
}