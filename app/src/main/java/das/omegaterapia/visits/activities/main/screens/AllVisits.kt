package das.omegaterapia.visits.activities.main.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import das.omegaterapia.visits.activities.main.VisitsViewModel
import das.omegaterapia.visits.activities.main.composables.VisitList
import das.omegaterapia.visits.utils.WindowSize


@Composable
fun AllVisitsScreen(
    visitViewModel: VisitsViewModel,
    windowSize: WindowSize,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
) {
    val groupedVisits = visitViewModel.allVisits.collectAsState(emptyMap()).value
    VisitList(
        groupedVisitCards = groupedVisits,
        modifier = modifier.fillMaxSize(),
        lazyListState = lazyListState,
    )
}