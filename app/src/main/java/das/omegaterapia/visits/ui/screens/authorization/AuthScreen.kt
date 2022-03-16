package das.omegaterapia.visits.ui.screens.authorization

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import das.omegaterapia.visits.ui.components.generic.CenteredRow
import das.omegaterapia.visits.ui.theme.OmegaterapiaTheme
import das.omegaterapia.visits.utils.WindowSizeFormat
import das.omegaterapia.visits.utils.WindowsSize
import das.omegaterapia.visits.viewmodel.AuthViewModel

@Composable
fun AuthScreen(authViewModel: AuthViewModel, windowSizeFormatClass: WindowsSize) {
    Scaffold(
    ) { padding ->
        CenteredRow() {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                LogInCard()
            }
        }
    }

}

//----------------------------------------------------------------------------------------------------------

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AuthScreenPreview() {
    OmegaterapiaTheme {
        AuthScreen(AuthViewModel(), WindowsSize(WindowSizeFormat.Compact, WindowSizeFormat.Compact, isLandscape = false))
    }
}
