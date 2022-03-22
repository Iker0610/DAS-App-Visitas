package das.omegaterapia.visits.activities.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import das.omegaterapia.visits.activities.main.composables.BottomNavBar
import das.omegaterapia.visits.activities.main.composables.MainFloatingActionButton
import das.omegaterapia.visits.activities.main.screens.AddVisitScreen
import das.omegaterapia.visits.activities.main.screens.AllVisitsScreen
import das.omegaterapia.visits.activities.main.screens.MainActivityScreens
import das.omegaterapia.visits.activities.main.screens.navigableScreens
import das.omegaterapia.visits.ui.theme.OmegaterapiaTheme
import das.omegaterapia.visits.utils.WindowSize
import das.omegaterapia.visits.utils.rememberWindowSizeClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val visitViewModel: VisitsViewModel by viewModels()

    /*--------------------------------------------------
    |            Activity Lifecycle Methods            |
    --------------------------------------------------*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OmegaterapiaTheme {
                val windowSize = rememberWindowSizeClass()
                MainActivityScreen(visitViewModel, windowSize = windowSize)
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
private fun MainActivityScreen(
    visitViewModel: VisitsViewModel = viewModel(),
    windowSize: WindowSize
) {
    val scope = rememberCoroutineScope()

    val navController = rememberAnimatedNavController()
    val currentRoute by navController.currentBackStackEntryFlow.collectAsState(initial = navController.currentBackStackEntry)

    val scaffoldState = rememberScaffoldState()
    val allVisitsLazyListState = rememberLazyListState()

    val drawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)
    val gesturesEnabled = drawerState.currentValue != BottomDrawerValue.Closed

    BottomDrawer(
        gesturesEnabled = gesturesEnabled,
        drawerState = drawerState,
        drawerShape = CutCornerShape(5),
        drawerContent = {

            LazyColumn(Modifier.padding(vertical = 24.dp)) {
                item {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(visitViewModel.currentUser, style = MaterialTheme.typography.h6)
                    }
                    Divider(
                        Modifier
                            .padding(top = 22.dp, bottom = 8.dp)
                            .fillMaxWidth()
                    )
                }
                items(navigableScreens) {
                    ListItem(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = { Text(it.title) },
                        icon = {
                            Icon(
                                it.icon,
                                contentDescription = "Navigate to " + it.title
                            )
                        }
                    )
                }
            }
        },
    ) {


        Scaffold(
            scaffoldState = scaffoldState,
            floatingActionButton = {
                AnimatedVisibility(
                    currentRoute?.destination?.route != MainActivityScreens.AddVisit.route && !allVisitsLazyListState.isScrollInProgress,
                    enter = scaleIn(),
                    exit = scaleOut()
                ) {
                    MainFloatingActionButton(
                        onClick = { navController.navigate(MainActivityScreens.AddVisit.route) },
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            isFloatingActionButtonDocked = true,
            bottomBar = {
                AnimatedVisibility(
                    currentRoute?.destination?.route != MainActivityScreens.AddVisit.route && !allVisitsLazyListState.isScrollInProgress,
                    enter = slideInVertically(initialOffsetY = { 2 * it }),
                    exit = slideOutVertically(targetOffsetY = { 2 * it })
                ) {
                    BottomNavBar(onMenuOpen = { scope.launch { drawerState.open() } })
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
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioNoBouncy,
                                stiffness = Spring.StiffnessVeryLow
                            )
                        )
                    },
                    exitTransition = {
                        slideOutVertically(
                            targetOffsetY = { 2 * it },
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioNoBouncy,
                                stiffness = Spring.StiffnessVeryLow
                            )
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