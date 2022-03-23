package das.omegaterapia.visits.activities.main.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import das.omegaterapia.visits.R
import das.omegaterapia.visits.ui.components.generic.CenteredRow

private val FABShape = CutCornerShape(50)

@Composable
fun MainFloatingActionButton(
    onClick: () -> Unit = {},
) {
    FloatingActionButton(onClick = onClick, shape = FABShape) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.fab_icon_description)
            )
        }
    }
}

@Composable
fun BottomNavBar(currentScreenTitle: String, onMenuOpen: () -> Unit) {

    BottomAppBar(cutoutShape = FABShape) {
        // Leading icons should typically have a high content alpha
        Surface(
            shape = CutCornerShape(topEndPercent = 80),
            color = Color.Transparent,
            modifier = Modifier
                .fillMaxWidth(0.5f)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable(onClick = onMenuOpen)) {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                    IconButton(onClick = onMenuOpen) {
                        Icon(Icons.Filled.Menu, contentDescription = "Localized description")
                    }
                    Text(text = currentScreenTitle, style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold))
                }
            }
        }

        // The actions should be at the end of the BottomAppBar. They use the default medium
        // content alpha provided by BottomAppBar
        Row {
            Spacer(Modifier.weight(1f, true))
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(Icons.Filled.AccountCircle, contentDescription = "Localized description")
            }
        }
    }
}