package das.omegaterapia.visits.activities.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.BottomDrawer
import androidx.compose.material.BottomDrawerValue
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.NavigationRail
import androidx.compose.material.NavigationRailItem
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.rememberBottomDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import das.omegaterapia.visits.activities.main.screens.AddVisitScreen
import das.omegaterapia.visits.activities.main.screens.AllVisitsScreen
import das.omegaterapia.visits.activities.main.screens.EditVisitScreen
import das.omegaterapia.visits.activities.main.screens.MainActivityScreens
import das.omegaterapia.visits.activities.main.screens.TodaysVisitsScreen
import das.omegaterapia.visits.activities.main.screens.VIPVisitsScreen
import das.omegaterapia.visits.ui.components.generic.CenteredColumn
import das.omegaterapia.visits.ui.components.generic.DrawerButton
import das.omegaterapia.visits.ui.components.navigation.AddFloatingActionButton
import das.omegaterapia.visits.ui.components.navigation.BottomNavBar
import das.omegaterapia.visits.ui.components.navigation.NavDrawerHeader
import das.omegaterapia.visits.ui.theme.OmegaterapiaTheme
import das.omegaterapia.visits.utils.WindowSize
import das.omegaterapia.visits.utils.WindowSizeFormat
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
    windowSize: WindowSize,
) {
    val scope = rememberCoroutineScope()

    val navController = rememberAnimatedNavController()
    val currentRoute by navController.currentBackStackEntryFlow.collectAsState(initial = navController.currentBackStackEntry)


    //-------------------------------------------------------------------
    // Navigation booleans
    val enableNavigation = MainActivityScreens.isNavigable(currentRoute?.destination?.route)
    val enableBottomNavigation = enableNavigation && windowSize.width == WindowSizeFormat.Compact
    val enableRailNavigation = enableNavigation && !enableBottomNavigation

    var isScrolling by rememberSaveable { mutableStateOf(false) }
    val showBottomNavigation = enableBottomNavigation && !isScrolling
    //--------------------------------------------------------------------

    val scaffoldState = rememberScaffoldState()
    val drawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)
    val gesturesEnabled = drawerState.currentValue != BottomDrawerValue.Closed

    LaunchedEffect(enableBottomNavigation) {
        if (!enableBottomNavigation && drawerState.currentValue != BottomDrawerValue.Closed) {
            scope.launch { drawerState.close() }
        }
    }

    @Composable
    fun FAB() {
        AddFloatingActionButton(
            onAdd = { navController.navigate(MainActivityScreens.AddVisit.route) },
        )
    }

    BottomDrawer(
        gesturesEnabled = gesturesEnabled,
        drawerState = drawerState,
        drawerShape = CutCornerShape(topStartPercent = 7, topEndPercent = 7),
        drawerContent = {
            LazyColumn(Modifier.padding(top = 16.dp, bottom = 22.dp)) {
                item {
                    NavDrawerHeader(
                        currentUser = visitViewModel.currentUser,
                        Modifier.padding(horizontal = 16.dp)
                    ) {
                        scope.launch { drawerState.close() }
                    }
                    Divider(
                        Modifier
                            .padding(top = 8.dp, bottom = 8.dp)
                            .fillMaxWidth()
                    )
                }
                items(MainActivityScreens.navigableScreens.toList()) {
                    DrawerButton(
                        icon = it.icon,
                        label = it.title,
                        isSelected = currentRoute?.destination?.route == it.route,
                        action = {
                            if (currentRoute?.destination?.route != it.route) {
                                scope.launch { drawerState.close() }
                            }
                            navController.navigate(it.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    )
                }
            }
        },
    ) {
        Scaffold(
            scaffoldState = scaffoldState,
            floatingActionButton = {
                AnimatedVisibility(
                    showBottomNavigation,
                    enter = scaleIn(),
                    exit = scaleOut()
                ) {
                    FAB()
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            isFloatingActionButtonDocked = true,
            bottomBar = {
                AnimatedVisibility(
                    showBottomNavigation,
                    enter = slideInVertically(initialOffsetY = { 2 * it }),
                    exit = slideOutVertically(targetOffsetY = { 2 * it })
                ) {
                    BottomNavBar(
                        MainActivityScreens.fromRoute(currentRoute?.destination?.route).title,
                        onMenuOpen = { scope.launch { drawerState.open() } },
                        onSettings = {
                            navController.navigate(MainActivityScreens.Account.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            },
        )
        {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedVisibility(
                    enableRailNavigation,
                    enter = slideInHorizontally(initialOffsetX = { -it }) + expandHorizontally(),
                    exit = slideOutHorizontally(targetOffsetX = { -it }) + shrinkHorizontally()
                ) {
                    NavigationRail(
                        header = { Box(Modifier.padding(8.dp)) { FAB() } },
                    ) {
                        CenteredColumn(Modifier.weight(1f, true)) {
                            MainActivityScreens.navigableScreens.forEach { screen ->
                                NavigationRailItem(
                                    icon = { Icon(screen.icon, contentDescription = screen.title) },
                                    label = null,
                                    selected = currentRoute?.destination?.route == screen.route,
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                        NavigationRailItem(
                            icon = {
                                Icon(
                                    MainActivityScreens.Account.icon,
                                    contentDescription = MainActivityScreens.Account.title
                                )
                            },
                            label = null,
                            selected = currentRoute?.destination?.route == MainActivityScreens.Account.route,
                            onClick = {
                                navController.navigate(MainActivityScreens.Account.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }

                AnimatedNavHost(
                    navController = navController,
                    startDestination = MainActivityScreens.TodaysVisits.route
                ) {

                    composable(route = MainActivityScreens.TodaysVisits.route) {
                        TodaysVisitsScreen(
                            visitViewModel = visitViewModel,
                            onScrollStateChange = { isScrolling = it },

                            )
                    }


                    composable(route = MainActivityScreens.AllVisits.route) {
                        AllVisitsScreen(
                            visitViewModel = visitViewModel,
                            onScrollStateChange = { isScrolling = it },
                        )
                    }


                    composable(route = MainActivityScreens.VIPs.route) {
                        VIPVisitsScreen(
                            visitViewModel = visitViewModel,
                            onScrollStateChange = { isScrolling = it },
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
                            addVisitCard = visitViewModel::addVisitCard,
                            onBackPressed = {
                                scope.launch(Dispatchers.Main) {
                                    if (!navController.popBackStack()) {
                                        navController.navigate(MainActivityScreens.TodaysVisits.route)
                                    }
                                }
                            }
                        )
                    }


                    composable(
                        route = MainActivityScreens.EditVisit.route,
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
                        if (visitViewModel.currentToEditVisit == null) {
                            if (currentRoute?.destination?.route == MainActivityScreens.EditVisit.route
                                && !navController.popBackStack()
                            ) {
                                navController.navigate(MainActivityScreens.TodaysVisits.route)
                            }
                        } else
                            EditVisitScreen(
                                visitCard = visitViewModel.currentToEditVisit!!,
                                onEditVisitCard = visitViewModel::updateVisitCard,
                                onBackPressed = {
                                    visitViewModel.currentToEditVisit = null
                                    scope.launch(Dispatchers.Main) {
                                        if (!navController.popBackStack()) {
                                            navController.navigate(MainActivityScreens.TodaysVisits.route)
                                        }
                                    }
                                }
                            )
                    }
                }
            }
        }
    }

    LaunchedEffect(visitViewModel.currentToEditVisit) {
        Log.d("edit", "Ha cambiado el estado. ${visitViewModel.currentToEditVisit}")
        if (visitViewModel.currentToEditVisit != null && currentRoute?.destination?.route != MainActivityScreens.EditVisit.route) {
            navController.navigate(MainActivityScreens.EditVisit.route)
        }
    }
}


