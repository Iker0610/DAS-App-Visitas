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
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.BottomDrawer
import androidx.compose.material.BottomDrawerValue
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.rememberBottomDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import das.omegaterapia.visits.activities.main.screens.AddVisitScreen
import das.omegaterapia.visits.activities.main.screens.AllVisitsScreen
import das.omegaterapia.visits.activities.main.screens.MainActivityScreens
import das.omegaterapia.visits.activities.main.screens.TodaysVisitsScreen
import das.omegaterapia.visits.activities.main.screens.VIPVisitsScreen
import das.omegaterapia.visits.ui.components.generic.DrawerButton
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
    windowSize: WindowSize,
) {
    val scope = rememberCoroutineScope()

    val navController = rememberAnimatedNavController()
    val currentRoute by navController.currentBackStackEntryFlow.collectAsState(initial = navController.currentBackStackEntry)

    val scaffoldState = rememberScaffoldState()

    var isScrolling by rememberSaveable { mutableStateOf(false) } // TODO

    val drawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)
    val gesturesEnabled = drawerState.currentValue != BottomDrawerValue.Closed


    BottomDrawer(
        gesturesEnabled = gesturesEnabled,
        drawerState = drawerState,
        drawerShape = CutCornerShape(topStartPercent = 7, topEndPercent = 7),
        drawerContent = {
            LazyColumn(Modifier.padding(top = 16.dp, bottom = 22.dp)) {
                item {
                    NavDrawerHeader(currentUser = visitViewModel.currentUser, Modifier.padding(horizontal = 16.dp)) {
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
                    currentRoute?.destination?.route != MainActivityScreens.AddVisit.route && !isScrolling,
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
                    currentRoute?.destination?.route != MainActivityScreens.AddVisit.route && !isScrolling,
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


@Composable
private fun NavDrawerHeader(currentUser: String, modifier: Modifier = Modifier, onClose: () -> Unit) {
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