package das.omegaterapia.visits.activities.main.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import das.omegaterapia.visits.activities.main.composables.VisitList
import das.omegaterapia.visits.data.visitList
import das.omegaterapia.visits.ui.theme.OmegaterapiaTheme

@Composable
fun AllVisitsScreen() {
    // A surface container using the 'background' color from the theme
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        val groupedVisits = mapOf(
            "Lunes" to visitList.subList(0, 5),
            "Martes" to visitList.subList(5, 12),
            "Miércoles" to emptyList(),
            "Jueves" to visitList.subList(12, 20),
            "Viernes" to visitList.subList(20, visitList.size),
        )
        OmegaterapiaTheme {
            Surface {
                VisitList(groupedVisitCards = groupedVisits)
            }
        }
    }
}