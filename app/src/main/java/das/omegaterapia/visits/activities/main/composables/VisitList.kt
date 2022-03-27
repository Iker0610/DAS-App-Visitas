package das.omegaterapia.visits.activities.main.composables

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import das.omegaterapia.visits.R
import das.omegaterapia.visits.data.visitList
import das.omegaterapia.visits.model.entities.VisitCard
import das.omegaterapia.visits.model.entities.VisitId
import das.omegaterapia.visits.ui.theme.OmegaterapiaTheme
import das.omegaterapia.visits.ui.theme.getButtonShape

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VisitList(
    groupedVisitCards: Map<String, List<VisitCard>>,
    modifier: Modifier = Modifier,
    onItemEdit: (VisitCard) -> Unit = {},
    onItemDelete: (VisitId) -> Unit = {},
    lazyListState: LazyListState = rememberLazyListState(),
    onScrollStateChange: (Boolean) -> Unit = {},
    paddingAtBottom: Boolean = false,
) {
    val (expandedVisitCardId, setExpandedVisitCardId) = rememberSaveable { mutableStateOf<String?>(null) }
    val (swipedVisitCardId, setSwipedVisitCardId) = rememberSaveable { mutableStateOf<String?>(null) }

    var toDeleteItemId by rememberSaveable { mutableStateOf<String?>(null) }

    if (toDeleteItemId != null) {
        val dismissCallback = { toDeleteItemId = null }
        AlertDialog(
            title = { Text(text = stringResource(R.string.delete_visit_card_dialog_title)) },
            confirmButton = {
                TextButton(onClick = { onItemDelete(VisitId(toDeleteItemId!!)); toDeleteItemId = null }, shape = getButtonShape()) {
                    Text(text = stringResource(R.string.delete_button))
                }
            },
            dismissButton = {
                TextButton(onClick = dismissCallback, shape = getButtonShape()) {
                    Text(text = stringResource(R.string.dismiss_button))
                }
            },
            onDismissRequest = dismissCallback,
            shape = RectangleShape,
        )
    }

    val paddingAtBottomValue = if (paddingAtBottom) 90.dp else 16.dp

    LazyColumn(
        modifier = modifier.clipToBounds()/*Importante el clip para cuando hagamos swipe de las cards*/,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        state = lazyListState,
        contentPadding = PaddingValues(bottom = paddingAtBottomValue)
    ) {
        groupedVisitCards.forEach { (groupTitle, groupVisitCards) ->
            stickyHeader { VisitGroupHeader(groupTitle) }

            items(groupVisitCards, key = { visitCard -> visitCard.hashCode() }
            ) { visitCard ->
                SwipeableVisitCardItem(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    visitCard = visitCard,
                    isExpanded = visitCard.id == expandedVisitCardId,
                    canBeSwippedToSide = visitCard.id == swipedVisitCardId || swipedVisitCardId == null,

                    onEdit = onItemEdit,
                    onDelete = { toDeleteItemId = visitCard.id },

                    onClick = {
                        if (expandedVisitCardId != it.id) {
                            setExpandedVisitCardId(it.id)
                            setSwipedVisitCardId(it.id)
                        } else {
                            setExpandedVisitCardId(null)
                        }
                    },

                    onSwipe = {
                        if (swipedVisitCardId != it.id) {
                            setSwipedVisitCardId(it.id)
                            setExpandedVisitCardId(null)
                        }
                    }
                )
            }
        }
        //item { Spacer(modifier = Modifier.height(80.dp)) }
    }

    LaunchedEffect(lazyListState.isScrollInProgress) {
        onScrollStateChange(lazyListState.isScrollInProgress)
    }
}

@Composable
fun VisitGroupHeader(groupTitle: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colors.primary.copy(alpha = 1f).compositeOver(MaterialTheme.colors.surface),
        contentColor = MaterialTheme.colors.onPrimary,
        elevation = 8.dp
    ) {
        Text(
            text = groupTitle,
            style = MaterialTheme.typography.subtitle2.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
    }
}

//------------------------------------------------------------------------------------------------------------------

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
