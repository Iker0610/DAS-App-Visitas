package das.omegaterapia.visits.ui.components.generic

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.offset
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeableState
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableItem(
    modifier: Modifier = Modifier,
    state: SwipeableState<Int> = rememberSwipeableState(initialValue = 0),
    swipeEnabled: Boolean = true,
    background: @Composable RowScope.() -> Unit,
    dismissContent: @Composable RowScope.() -> Unit,
) {
    val squareSize = 110.dp
    val sizePx = with(LocalDensity.current) { squareSize.toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1, -sizePx to 2)

    Box(
        modifier = modifier
            .swipeable(
                state = state,
                anchors = anchors,
                orientation = Orientation.Horizontal,
                enabled = swipeEnabled
            ),
    ) {
        Row(
            content = background,
            modifier = Modifier.matchParentSize()
        )
        Row(
            content = dismissContent,
            modifier = Modifier.offset { IntOffset(state.offset.value.roundToInt(), 0) }
        )
    }
}

