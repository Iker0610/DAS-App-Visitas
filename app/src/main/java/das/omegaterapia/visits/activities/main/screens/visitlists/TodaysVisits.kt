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
import das.omegaterapia.visits.utils.WindowSize


@Composable
fun TodaysVisitsScreen(
    visitViewModel: VisitsViewModel,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    onScrollStateChange: (Boolean) -> Unit = {},
    paddingAtBottom: Dp = 0.dp,
) {
    val groupedVisits by visitViewModel.todaysVisits.collectAsState(emptyMap())
    VisitList(
        groupedVisitCards = groupedVisits,
        modifier = modifier.fillMaxSize(),

        onItemEdit = visitViewModel::currentToEditVisit::set,
        onItemDelete = visitViewModel::deleteVisitCard,

        lazyListState = lazyListState,
        onScrollStateChange = onScrollStateChange,

        paddingAtBottom = paddingAtBottom
    )
}