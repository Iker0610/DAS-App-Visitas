package das.omegaterapia.visits.activities.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import das.omegaterapia.visits.activities.main.composables.MainFloatingActionButton
import das.omegaterapia.visits.activities.main.screens.AllVisitsScreen
import das.omegaterapia.visits.activities.main.screens.VisitForm
import das.omegaterapia.visits.ui.theme.OmegaterapiaTheme
import das.omegaterapia.visits.utils.isScrollingUp
import das.omegaterapia.visits.utils.rememberWindowSizeClass


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val visitViewModel: VisitsViewModel by viewModels()

    /*--------------------------------------------------
    |            Activity Lifecycle Methods            |
    --------------------------------------------------*/
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OmegaterapiaTheme {
                val windowSize = rememberWindowSizeClass()

                val navController = rememberAnimatedNavController()
                val currentRoute by navController.currentBackStackEntryFlow.collectAsState(initial = navController.currentBackStackEntry)

                val scaffoldState = rememberScaffoldState()

                val allVisitsLazyListState = rememberLazyListState()

                Scaffold(
                    scaffoldState = scaffoldState,
                    floatingActionButton = {
                        AnimatedVisibility(
                            currentRoute?.destination?.route != "add",
                            enter = scaleIn(),
                            exit = scaleOut()
                        ) {
                            MainFloatingActionButton(
                                onClick = { navController.navigate("add") },
                                extended = allVisitsLazyListState.isScrollingUp()
                            )
                        }
                    }
                )
                { innerPadding ->
                    AnimatedNavHost(
                        navController = navController,
                        startDestination = "all_visits_screen"
                    ) {
                        composable(route = "all_visits_screen") {
                            AllVisitsScreen(
                                visitViewModel = visitViewModel,
                                windowSize = windowSize,
                                lazyListState = allVisitsLazyListState,
                            )
                        }
                        composable(
                            route = "add",
                            enterTransition = {
                                slideInVertically(
                                    initialOffsetY = { 2 * it },
                                    animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessVeryLow)
                                )
                            },
                            exitTransition = {
                                slideOutVertically(
                                    targetOffsetY = { 2 * it },
                                    animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessVeryLow)
                                )
                            },
                        ) {
                            VisitForm(
                                modifier = Modifier.padding(innerPadding),
                                submitForm = visitViewModel::addVisitCard,
                                onSuccessfulSubmit = {
                                    if (!navController.popBackStack()) {
                                        navController.navigate("all_visits_screen")
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}