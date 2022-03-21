package das.omegaterapia.visits.activities.main.composables

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import das.omegaterapia.visits.data.visitList
import das.omegaterapia.visits.model.entities.VisitCard
import das.omegaterapia.visits.ui.theme.OmegaterapiaTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VisitList(
    groupedVisitCards: Map<String, List<VisitCard>>,
    modifier: Modifier = Modifier,
    selectedVisit: VisitCard? = null,
    lazyListState: LazyListState = rememberLazyListState(),
) {
    val (selectedVisitCardId, setSelectedVisitCardId) = rememberSaveable { mutableStateOf(selectedVisit?.id) }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = lazyListState
    ) {
        groupedVisitCards.forEach { (groupTitle, groupVisitCards) ->
            stickyHeader { VisitGroupHeader(groupTitle) }

            items(groupVisitCards) { visitCardData ->
                VisitCardItem(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    visitCard = visitCardData,
                    isExpanded = visitCardData.id == selectedVisitCardId,
                    onClick = {
                        if (selectedVisitCardId != it.id) setSelectedVisitCardId(it.id)
                        else setSelectedVisitCardId(null)
                    }
                )
            }
        }
    }
}

@Composable
fun VisitGroupHeader(groupTitle: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colors.primary.copy(alpha = 0.85f).compositeOver(MaterialTheme.colors.surface),
        contentColor = MaterialTheme.colors.onPrimary,
        elevation = 16.dp
    ) {
        Text(
            text = groupTitle,
            style = MaterialTheme.typography.subtitle2.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
    }
}


@Preview(showBackground = true)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun VisitListPreview() {
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
