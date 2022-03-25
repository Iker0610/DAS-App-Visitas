package das.omegaterapia.visits.activities.main.screens.visitlists

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import das.omegaterapia.visits.activities.main.VisitsViewModel
import das.omegaterapia.visits.activities.main.composables.VisitList
import das.omegaterapia.visits.model.entities.VisitCard
import das.omegaterapia.visits.model.entities.VisitId
import das.omegaterapia.visits.utils.WindowSize


@Composable
fun VIPVisitsScreen(
    visitViewModel: VisitsViewModel,
    modifier: Modifier = Modifier,
    onItemEdit: (VisitCard) -> Unit = visitViewModel::currentToEditVisit::set,
    onItemDelete: (VisitId) -> Unit = { visitViewModel.deleteVisitCard(it) },
    lazyListState: LazyListState = rememberLazyListState(),
    onScrollStateChange: (Boolean) -> Unit = {},
    paddingAtBottom: Boolean = false,
    ) {
    val groupedVisits by visitViewModel.vipVisits.collectAsState(emptyMap())
    VisitList(
        groupedVisitCards = groupedVisits,
        modifier = modifier.fillMaxSize(),

        onItemEdit = onItemEdit,
        onItemDelete = onItemDelete,

        lazyListState = lazyListState,
        onScrollStateChange = onScrollStateChange,

        paddingAtBottom = paddingAtBottom
    )
}