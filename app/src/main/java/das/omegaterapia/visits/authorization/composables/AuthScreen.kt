package das.omegaterapia.visits.authorization.composables

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import das.omegaterapia.visits.ui.components.generic.CenteredColumn
import das.omegaterapia.visits.ui.components.generic.CenteredRow
import das.omegaterapia.visits.ui.theme.OmegaterapiaTheme
import das.omegaterapia.visits.utils.WindowSizeFormat
import das.omegaterapia.visits.utils.WindowsSize
import das.omegaterapia.visits.authorization.AuthViewModel

@Composable
fun AuthScreen(authViewModel: AuthViewModel = viewModel(), windowSizeFormatClass: WindowsSize) {
    Scaffold { padding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .horizontalScroll(rememberScrollState())
        ) {
            if (windowSizeFormatClass.width == WindowSizeFormat.Expanded) {
                Card(elevation = 8.dp) {
                    CenteredRow(Modifier
                        .height(IntrinsicSize.Max)
                        .padding(horizontal = 32.dp, vertical = 16.dp)
                    ) {
                        LogInSection(authViewModel)

                        Divider(modifier = Modifier
                            .padding(horizontal = 64.dp)
                            .fillMaxHeight()
                            .width(1.dp)
                        )

                        SignInSection(authViewModel)
                    }
                }

            } else {

                AnimatedVisibility(authViewModel.isLogin, enter = fadeIn(), exit = fadeOut()) {
                    CenteredColumn(Modifier.width(IntrinsicSize.Max)
                    ) {
                        LogInCard(authViewModel)

                        Divider(modifier = Modifier.padding(top = 32.dp, bottom = 24.dp))

                        Text(text = "Don't have an account?", style = MaterialTheme.typography.body2)
                        TextButton(onClick = authViewModel::switchScreen) {
                            Text(text = "Sign In")
                        }
                    }

                }

                AnimatedVisibility(!authViewModel.isLogin, enter = fadeIn(), exit = fadeOut()) {
                    CenteredColumn(Modifier.width(IntrinsicSize.Max)
                    ) {
                        SignInCard(authViewModel)

                        Divider(modifier = Modifier.padding(top = 32.dp, bottom = 24.dp))

                        Text(text = "Already have an account?", style = MaterialTheme.typography.body2)
                        TextButton(onClick = authViewModel::switchScreen) {
                            Text(text = "Login")
                        }
                    }
                }
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
        AuthScreen(windowSizeFormatClass = WindowsSize(WindowSizeFormat.Compact, WindowSizeFormat.Compact, isLandscape = false))
    }
}

@Preview(widthDp = 851, heightDp = 393)
@Preview(widthDp = 851, heightDp = 393, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AuthScreenLandscapePreview() {
    OmegaterapiaTheme {
        AuthScreen(windowSizeFormatClass = WindowsSize(WindowSizeFormat.Compact, WindowSizeFormat.Compact, isLandscape = true))
    }
}
