package das.omegaterapia.visits.activities.main.screens.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import das.omegaterapia.visits.ui.components.navigation.BackArrowTopBar

@Composable
fun UserProfileScreen(
    title: String,
    onBackPressed: () -> Unit = {},
) {
    Scaffold(topBar = { BackArrowTopBar(title, onBackPressed) }) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Estamos en proceso....", style = MaterialTheme.typography.h4)
        }
    }
}