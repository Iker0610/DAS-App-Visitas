package das.omegaterapia.visits

import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import das.omegaterapia.visits.ui.theme.OmegaterapiaTheme


enum class OmegaterapiaVisitsScreen(
    val icon: ImageVector,
) {
    Login(
        icon = Icons.Filled.PieChart,
    ),
    TodaysVisits(
        icon = Icons.Filled.AttachMoney,
    ),
    AllVisits(
        icon = Icons.Filled.MoneyOff,
    );

    companion object {
        fun fromRoute(route: String?): OmegaterapiaVisitsScreen =
            when (route?.substringBefore("/")) {
                Login.name -> Login
                TodaysVisits.name -> TodaysVisits
                AllVisits.name -> AllVisits
                null -> TodaysVisits
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}


@Composable
fun OmegaterapiaVisitsApp() {
    OmegaterapiaTheme() {
        val navController = rememberNavController()

        val allScreens = OmegaterapiaVisitsScreen.values().toList()
        val backstackEntry = navController.currentBackStackEntryAsState()
        val currentScreen = OmegaterapiaVisitsScreen.fromRoute(backstackEntry.value?.destination?.route)
        Scaffold(
            topBar = { /* TODO */ }
        ){

        }
    }
}