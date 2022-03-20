package das.omegaterapia.visits.activities.authorization.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import das.omegaterapia.visits.R
import das.omegaterapia.visits.ui.theme.BlueGrey600
import kotlinx.coroutines.delay
import kotlin.math.max
import kotlin.math.min

@Composable
fun AnimatedSplashScreen(onAnimationFinished: () -> Unit) {
    var startAnimation by rememberSaveable { mutableStateOf(false) }
    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(delayMillis = 150, durationMillis = 2000)
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(3000)
        onAnimationFinished()
    }
    Splash(alpha = alphaAnim)
}

@Composable
fun Splash(alpha: Float) {
    Box(
        modifier = Modifier
            .background(BlueGrey600)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier
                .size(200.dp)
                .alpha(alpha = min(max(alpha - 0.15f, 0f), 0.6f))
                .padding(top = 10.dp, start = 8.dp),
            painter = painterResource(id = R.mipmap.ic_launcher_foreground),
            contentDescription = null,
            tint = Color.DarkGray
        )
        Icon(
            modifier = Modifier
                .size(200.dp)
                .alpha(alpha = alpha),
            painter = painterResource(id = R.mipmap.ic_launcher_foreground),
            contentDescription = null,
            tint = Color.White
        )
    }
}

@Composable
@Preview
fun SplashScreenPreview() {
    Splash(alpha = 1f)
}

@Composable
@Preview(uiMode = UI_MODE_NIGHT_YES)
fun SplashScreenDarkPreview() {
    Splash(alpha = 1f)
}