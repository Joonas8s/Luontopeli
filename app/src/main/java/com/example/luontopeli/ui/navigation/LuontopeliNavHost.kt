package com.example.luontopeli.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.luontopeli.ui.map.MapScreen
import com.example.luontopeli.camera.CameraScreen
import com.example.luontopeli.ui.discover.DiscoverScreen
import com.example.luontopeli.ui.profile.ProfileScreen
import com.example.luontopeli.ui.stats.StatsScreen

/**
 * Sovelluksen navigointikomponentti (NavHost).
 */
@Composable
fun LuontopeliNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Map.route,
        modifier = modifier
    ) {
        composable(Screen.Map.route) { MapScreen() }
        composable(Screen.Camera.route) { CameraScreen() }
        composable(Screen.Discover.route) { DiscoverScreen() }
        composable(Screen.Stats.route) { StatsScreen() }
        composable(Screen.Profile.route) { ProfileScreen() }
    }
}
