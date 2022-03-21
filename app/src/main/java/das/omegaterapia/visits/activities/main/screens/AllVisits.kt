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
        val groupedVisits = mapOf(
            "Lunes" to visitViewModel.allVisits.collectAsState(emptyList()).value,
            "Martes" to visitList.subList(5, 12),
            "Miércoles" to emptyList(),
            "Jueves" to visitList.subList(12, 20),
            "Viernes" to visitList.subList(20, visitList.size),
        )
        OmegaterapiaTheme {
            Surface {
                CenteredColumn(Modifier.fillMaxSize()) {
                    Button(onClick = {
                        scope.launch {
                            visitViewModel.addVisitCard(visitList[5].also {
                                it.user = visitViewModel.currentUser; it.visitData.mainClientPhone = it.client.phoneNum
                            })
                        }
                    })
                    { Text(text = "Add") }
                    VisitList(groupedVisitCards = groupedVisits)
                }
            }
        }
    }
}