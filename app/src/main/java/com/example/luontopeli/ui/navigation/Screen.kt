package com.example.luontopeli.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Sealed class sovelluksen näkymien (screen) määrittelyyn.
 */
sealed class Screen(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Map : Screen("map", "Kartta", Icons.Filled.Map)
    object Camera : Screen("camera", "Kamera", Icons.Filled.CameraAlt)
    object Discover : Screen("discover", "Löydöt", Icons.Filled.Explore)
    object Stats : Screen("stats", "Tilastot", Icons.Filled.BarChart)
    object Profile : Screen("profile", "Profiili", Icons.Filled.Person)

    companion object {
        val bottomNavScreens = listOf(Map, Camera, Discover, Stats, Profile)
    }
}
