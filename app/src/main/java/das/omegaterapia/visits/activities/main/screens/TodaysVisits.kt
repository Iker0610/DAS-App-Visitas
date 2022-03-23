package das.omegaterapia.visits.activities.main.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import das.omegaterapia.visits.activities.main.VisitsViewModel
import das.omegaterapia.visits.activities.main.composables.VisitList
import das.omegaterapia.visits.utils.WindowSize


@Composable
fun TodaysVisitsScreen(
    visitViewModel: VisitsViewModel,
    windowSize: WindowSize,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
) {
    val groupedVisits by visitViewModel.todaysVisits.collectAsState(emptyMap())
    VisitList(
        groupedVisitCards = groupedVisits,
        modifier = modifier.fillMaxSize(),
        lazyListState = lazyListState,
    )
}