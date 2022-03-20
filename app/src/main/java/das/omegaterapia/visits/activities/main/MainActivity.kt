package das.omegaterapia.visits.activities.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import das.omegaterapia.visits.data.visitList
import das.omegaterapia.visits.activities.main.composables.VisitList
import das.omegaterapia.visits.activities.main.screens.AllVisitsScreen
import das.omegaterapia.visits.ui.theme.OmegaterapiaTheme
import das.omegaterapia.visits.utils.rememberWindowSizeClass


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /*--------------------------------------------------
    |            Activity Lifecycle Methods            |
    --------------------------------------------------*/
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val username = intent.getStringExtra("LOGGED_USERNAME") ?: "NOT USERNAME GIVEN"

        setContent {
            OmegaterapiaTheme {
                val windowSizeClass = rememberWindowSizeClass()

                val navController = rememberAnimatedNavController()
                AnimatedNavHost(
                    navController = navController,
                    startDestination = "all_visits_screen"
                ) {
                    composable(route = "all_visits_screen") {
                        AllVisitsScreen()
                    }
                }
            }
        }
    }
}