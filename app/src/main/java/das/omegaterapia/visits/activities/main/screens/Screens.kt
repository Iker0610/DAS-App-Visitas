package das.omegaterapia.visits.activities.main.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.RecentActors
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Today
import androidx.compose.ui.graphics.vector.ImageVector


enum class MainActivityScreens(var title: String, var route: String, var icon: ImageVector) {
    TodaysVisits("Today's Visits", "todays_visits", Icons.Filled.Today),
    AllVisits("All Visits", "all_visits", Icons.Filled.RecentActors),
    VIPs("VIP Visits", "vip_visits", Icons.Filled.Star),
    AddVisit("Add Visit", "add_visit", Icons.Filled.Add);
}

val navigableScreens = listOf(MainActivityScreens.TodaysVisits, MainActivityScreens.AllVisits, MainActivityScreens.VIPs)

