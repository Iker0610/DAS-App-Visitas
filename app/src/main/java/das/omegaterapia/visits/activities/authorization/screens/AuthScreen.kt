package das.omegaterapia.visits.activities.authorization.screens

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import das.omegaterapia.visits.activities.authorization.AuthViewModel
import das.omegaterapia.visits.activities.authorization.DeviceBiometricsSupport
import das.omegaterapia.visits.activities.authorization.composables.LoginCard
import das.omegaterapia.visits.activities.authorization.composables.LoginSection
import das.omegaterapia.visits.activities.authorization.composables.SignInCard
import das.omegaterapia.visits.activities.authorization.composables.SignInSection
import das.omegaterapia.visits.ui.components.generic.CenteredColumn
import das.omegaterapia.visits.ui.components.generic.CenteredRow
import das.omegaterapia.visits.ui.theme.OmegaterapiaTheme
import das.omegaterapia.visits.ui.theme.getMaterialRectangleShape
import das.omegaterapia.visits.utils.WindowSizeFormat
import das.omegaterapia.visits.utils.WindowsSize

@Composable
fun AuthScreen(
    authViewModel: AuthViewModel = viewModel(),
    windowSizeFormatClass: WindowsSize,
    biometricSupportChecker: () -> DeviceBiometricsSupport = { DeviceBiometricsSupport.UNSUPPORTED },
    onSuccessfulLogin: (String) -> Unit = {},
    onSuccessfulSignIn: (String) -> Unit = {},
    onSuccessfulBiometricLogin: () -> Unit = {},
) {
    Scaffold { padding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .horizontalScroll(rememberScrollState())
        ) {
            if (windowSizeFormatClass.width != WindowSizeFormat.Compact) {
                Card(elevation = 8.dp) {
                    CenteredRow(Modifier
                        .height(IntrinsicSize.Max)
                        .padding(horizontal = 32.dp, vertical = 16.dp)
                    ) {
                        LoginSection(
                            authViewModel,
                            biometricSupportChecker = biometricSupportChecker,
                            onLoginSuccessful = onSuccessfulLogin,
                            onSuccessfulBiometricLogin = onSuccessfulBiometricLogin
                        )

                        Divider(modifier = Modifier
                            .padding(horizontal = 64.dp)
                            .fillMaxHeight()
                            .width(1.dp)
                        )

                        SignInSection(authViewModel, onSignInSuccessful = onSuccessfulSignIn)
                    }
                }

            } else {
                val animationTime = 275

                AnimatedVisibility(
                    authViewModel.isLogin,
                    enter = slideInHorizontally(
                        initialOffsetX = { -2 * it },
                        animationSpec = tween(
                            durationMillis = animationTime,
                            easing = LinearEasing
                        )
                    ),
                    exit = slideOutHorizontally(
                        targetOffsetX = { -2 * it },
                        animationSpec = tween(
                            durationMillis = animationTime,
                            easing = LinearEasing
                        )
                    )
                ) {
                    CenteredColumn(Modifier.width(IntrinsicSize.Max)
                    ) {
                        LoginCard(
                            authViewModel,
                            biometricSupportChecker = biometricSupportChecker,
                            onLoginSuccessful = onSuccessfulLogin,
                            onSuccessfulBiometricLogin = onSuccessfulBiometricLogin
                        )

                        Divider(modifier = Modifier.padding(top = 32.dp, bottom = 24.dp))

                        Text(text = "Don't have an account?", style = MaterialTheme.typography.body2)
                        TextButton(onClick = authViewModel::switchScreen, shape = getMaterialRectangleShape()) {
                            Text(text = "Sign In")
                        }
                    }

                }

                AnimatedVisibility(
                    !authViewModel.isLogin,
                    modifier = Modifier.fillMaxSize(),
                    enter = slideInHorizontally(
                        initialOffsetX = { 2 * it },
                        animationSpec = tween(
                            durationMillis = animationTime,
                            easing = LinearEasing
                        )
                    ),
                    exit = slideOutHorizontally(
                        targetOffsetX = { 2 * it },
                        animationSpec = tween(
                            durationMillis = animationTime,
                            easing = LinearEasing
                        )
                    )
                ) {
                    CenteredColumn(Modifier.width(IntrinsicSize.Max)
                    ) {
                        SignInCard(authViewModel, onSignInSuccessful = onSuccessfulSignIn)

                        Divider(modifier = Modifier.padding(top = 32.dp, bottom = 24.dp))

                        Text(text = "Already have an account?", style = MaterialTheme.typography.body2)
                        TextButton(onClick = authViewModel::switchScreen, shape = getMaterialRectangleShape()) {
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
