package das.omegaterapia.visits.activities.main.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import das.omegaterapia.visits.R

@Composable
fun MainFloatingActionButton(
    extended: Boolean = false,
    onClick: () -> Unit = {},
) {
    val percent by animateIntAsState(targetValue = if (extended) 30 else 50)

    FloatingActionButton(onClick = onClick, shape = CutCornerShape(percent)) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(R.string.fab_icon_description))

            // Toggle the visibility of the content with animation.
            AnimatedVisibility(visible = extended) {
                Text(text = stringResource(R.string.fab_add), modifier = Modifier.padding(start = 8.dp, top = 3.dp))
            }
        }
    }
}