package das.omegaterapia.visits.activities.main.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomDrawer
import androidx.compose.material.BottomDrawerValue
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberBottomDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import das.omegaterapia.visits.R
import kotlinx.coroutines.launch

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
            Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(R.string.fab_icon_description))
        }
    }
}

@Composable
fun BottomNavBar() {

    BottomAppBar(cutoutShape = FABShape) {
        // Leading icons should typically have a high content alpha
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(Icons.Filled.Menu, contentDescription = "Localized description")
            }
        }
        // The actions should be at the end of the BottomAppBar. They use the default medium
        // content alpha provided by BottomAppBar
        Spacer(Modifier.weight(1f, true))
        IconButton(onClick = { /* doSomething() */ }) {
            Icon(Icons.Filled.Favorite, contentDescription = "Localized description")
        }
        IconButton(onClick = { /* doSomething() */ }) {
            Icon(Icons.Filled.Favorite, contentDescription = "Localized description")
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomNavigationDrawer() {
    val (gesturesEnabled, toggleGesturesEnabled) = remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .toggleable(
                    value = gesturesEnabled,
                    onValueChange = toggleGesturesEnabled
                )
        ) {
            Checkbox(gesturesEnabled, null)
            Text(text = if (gesturesEnabled) "Gestures Enabled" else "Gestures Disabled")
        }

        val drawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)
        BottomDrawer(
            gesturesEnabled = gesturesEnabled,
            drawerState = drawerState,
            drawerContent = {
                Button(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp),
                    onClick = { scope.launch { drawerState.close() } },
                    content = { Text("Close Drawer") }
                )
                LazyColumn {
                    items(25) {
                        ListItem(
                            text = { Text("Item $it") },
                            icon = {
                                Icon(
                                    Icons.Default.Favorite,
                                    contentDescription = "Localized description"
                                )
                            }
                        )
                    }
                }
            },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val openText = if (gesturesEnabled) "▲▲▲ Pull up ▲▲▲" else "Click the button!"
                Text(text = if (drawerState.isClosed) openText else "▼▼▼ Drag down ▼▼▼")
                Spacer(Modifier.height(20.dp))
                Button(onClick = { scope.launch { drawerState.open() } }) {
                    Text("Click to open")
                }
            }
        }

    }
}