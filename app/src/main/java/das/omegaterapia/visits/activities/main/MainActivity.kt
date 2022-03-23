package das.omegaterapia.visits.activities.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import das.omegaterapia.visits.R
import das.omegaterapia.visits.activities.main.composables.BottomNavBar
import das.omegaterapia.visits.activities.main.composables.MainFloatingActionButton
import das.omegaterapia.visits.activities.main.screens.*
import das.omegaterapia.visits.ui.components.generic.CenteredColumn
import das.omegaterapia.visits.ui.components.generic.DrawerButton
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

    val scaffoldState = rememberScaffoldState()

    var isScrolling by rememberSaveable { mutableStateOf(false) }

    //-------------------------------------------------------------------
    // Navigation booleans
    val enableNavigation = currentRoute?.destination?.route != MainActivityScreens.AddVisit.route
    val enableBottomNavigation = enableNavigation && windowSize.width == WindowSizeFormat.Compact
    val enableRailNavigation = enableNavigation && !enableBottomNavigation

    val showBottomNavigation = enableBottomNavigation && !isScrolling
    //--------------------------------------------------------------------


    val drawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)
    val gesturesEnabled = drawerState.currentValue != BottomDrawerValue.Closed

    LaunchedEffect(windowSize.width) {
        if (windowSize.width != WindowSizeFormat.Compact) {
            scope.launch { drawerState.close() }
        }
    }

    @Composable
    fun FAB() {
        MainFloatingActionButton(
            onClick = { navController.navigate(MainActivityScreens.AddVisit.route) },
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
                items(MainActivityScreens.navigableScreens) {
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
                        onMenuOpen = { scope.launch { drawerState.open() } }
                    )
                }
            },
        )
        { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (enableRailNavigation) {
                    NavigationRail(
                        header = { Box(Modifier.padding(horizontal = 8.dp)) { FAB() } }
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
                                    Icons.Filled.AccountCircle,
                                    contentDescription = "User Settings"
                                )
                            },
                            label = null,
                            selected = false,
                            onClick = { /*TODO*/ }
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


@Composable
private fun NavDrawerHeader(
    currentUser: String,
    modifier: Modifier = Modifier,
    onClose: () -> Unit
) {
    Row(
        modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.mipmap.ic_launcher_foreground),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(MaterialTheme.colors.secondary),
            modifier = Modifier
                .height(50.dp)
                .padding(end = 8.dp)
        )
        Text(currentUser, style = MaterialTheme.typography.h6)
        Spacer(Modifier.weight(1f, true))
        IconButton(onClick = onClose) {
            Icon(Icons.Filled.Close, contentDescription = "Close navigation drawer.")
        }
    }
}