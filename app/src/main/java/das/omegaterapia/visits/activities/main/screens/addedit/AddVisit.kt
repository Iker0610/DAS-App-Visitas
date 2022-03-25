package das.omegaterapia.visits.activities.main.screens.addedit

import androidx.activity.compose.BackHandler
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import das.omegaterapia.visits.activities.main.composables.form.VisitForm
import das.omegaterapia.visits.model.entities.VisitCard
import das.omegaterapia.visits.ui.components.navigation.BackArrowTopBar
import das.omegaterapia.visits.ui.theme.OmegaterapiaTheme

@Composable
fun AddVisitScreen(
    addVisitCard: suspend (VisitCard) -> Boolean,
    title: String,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {},
    onSuccessfulSubmit: () -> Unit = onBackPressed,
) {
    BackHandler(onBack = onBackPressed)

    Scaffold(
        topBar = { BackArrowTopBar(title, onBackPressed) }
    ) {
        VisitForm(
            modifier = modifier,
            submitVisitCard = addVisitCard,
            onSuccessfulSubmit = onSuccessfulSubmit
        )
    }
}