package das.omegaterapia.visits.activities.main.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import das.omegaterapia.visits.activities.main.VisitsViewModel
import das.omegaterapia.visits.activities.main.composables.VisitList
import das.omegaterapia.visits.data.visitList
import das.omegaterapia.visits.ui.components.generic.CenteredColumn
import das.omegaterapia.visits.ui.theme.OmegaterapiaTheme
import kotlinx.coroutines.launch

@Composable
fun AllVisitsScreen(visitViewModel: VisitsViewModel) {
    val scope = rememberCoroutineScope()

    // A surface container using the 'background' color from the theme
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        val groupedVisits = visitViewModel.allVisits.collectAsState(emptyMap()).value
        OmegaterapiaTheme {
            Surface {
                CenteredColumn(Modifier.fillMaxSize()) {
                    VisitList(groupedVisitCards = groupedVisits)
                }
            }
        }
    }
}