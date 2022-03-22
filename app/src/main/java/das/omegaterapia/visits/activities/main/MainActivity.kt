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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import das.omegaterapia.visits.activities.main.composables.MainFloatingActionButton
import das.omegaterapia.visits.activities.main.screens.AllVisitsScreen
import das.omegaterapia.visits.activities.main.screens.AddVisitScreen
import das.omegaterapia.visits.activities.main.screens.MainActivityScreens
import das.omegaterapia.visits.ui.theme.OmegaterapiaTheme
import das.omegaterapia.visits.utils.isScrollingUp
import das.omegaterapia.visits.utils.rememberWindowSizeClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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

                val scope = rememberCoroutineScope()

                Scaffold(
                    scaffoldState = scaffoldState,
                    floatingActionButton = {
                        AnimatedVisibility(
                            currentRoute?.destination?.route != MainActivityScreens.AddVisit.route,
                            enter = scaleIn(),
                            exit = scaleOut()
                        ) {
                            MainFloatingActionButton(
                                onClick = { navController.navigate(MainActivityScreens.AddVisit.route) },
                                extended = allVisitsLazyListState.isScrollingUp()
                            )
                        }
                    }
                )
                { innerPadding ->
                    AnimatedNavHost(
                        navController = navController,
                        startDestination = MainActivityScreens.AllVisits.route
                    ) {
                        composable(route = MainActivityScreens.AllVisits.route) {
                            AllVisitsScreen(
                                visitViewModel = visitViewModel,
                                windowSize = windowSize,
                                lazyListState = allVisitsLazyListState,
                            )
                        }
                        composable(
                            route = MainActivityScreens.AddVisit.route,
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
                            AddVisitScreen(
                                modifier = Modifier.padding(innerPadding),
                                addVisitCard = visitViewModel::addVisitCard,
                                onSuccessfulSubmit = {
                                    scope.launch(Dispatchers.Main) {
                                        if (!navController.popBackStack()) {
                                            navController.navigate(MainActivityScreens.AllVisits.route)
                                        }
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