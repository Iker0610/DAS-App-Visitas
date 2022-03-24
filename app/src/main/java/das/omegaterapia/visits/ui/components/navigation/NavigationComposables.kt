package das.omegaterapia.visits.ui.components.navigation

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import das.omegaterapia.visits.R
import das.omegaterapia.visits.activities.main.screens.MainActivityScreens

private val FABShape = CutCornerShape(50)

@Composable
fun AddFloatingActionButton(
    onAdd: () -> Unit = {},
) {
    FloatingActionButton(onClick = onAdd, shape = FABShape) {
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
fun BottomNavBar(currentScreenTitle: String, onMenuOpen: () -> Unit, onSettings: () -> Unit) {

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
                        Icon(Icons.Filled.Menu, contentDescription = "Open navigation menu")
                    }
                    Text(text = currentScreenTitle, style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold))
                }
            }
        }

        // The actions should be at the end of the BottomAppBar. They use the default medium
        // content alpha provided by BottomAppBar
        Row {
            Spacer(Modifier.weight(1f, true))
            IconButton(onClick = onSettings) {
                Icon(
                    MainActivityScreens.Account.icon,
                    contentDescription = MainActivityScreens.Account.title
                )
            }
        }
    }
}


@Composable
fun NavDrawerHeader(
    currentUser: String,
    modifier: Modifier = Modifier,
    onClose: () -> Unit,
) {
    Row(
        modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.mipmap.ic_launcher_foreground),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(MaterialTheme.colors.secondary),
            modifier = Modifier
                .height(50.dp)
                .padding(end = 8.dp)
        )
        Text(currentUser, style = MaterialTheme.typography.h6)
        Spacer(Modifier.weight(1f, true))
        IconButton(onClick = onClose) {
            Icon(Icons.Filled.Close, contentDescription = "Close navigation drawer.")
        }
    }
}