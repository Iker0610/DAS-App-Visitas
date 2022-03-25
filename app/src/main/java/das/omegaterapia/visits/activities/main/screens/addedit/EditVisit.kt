package das.omegaterapia.visits.activities.main.screens.addedit

import androidx.activity.compose.BackHandler
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import das.omegaterapia.visits.activities.main.composables.form.VisitForm
import das.omegaterapia.visits.model.entities.VisitCard
import das.omegaterapia.visits.ui.components.navigation.BackArrowTopBar

@Composable
fun EditVisitScreen(
    visitCard: VisitCard,
    onEditVisitCard: suspend (VisitCard) -> Boolean,
    title: String,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {},
    onSuccessfulSubmit: () -> Unit = onBackPressed,
) {
    BackHandler(onBack = onBackPressed)

    Scaffold(topBar = { BackArrowTopBar(title, onBackPressed) }) {
        VisitForm(
            initialVisitCard = visitCard,
            modifier = modifier,
            submitVisitCard = onEditVisitCard,
            onSuccessfulSubmit = onSuccessfulSubmit
        )
    }
}