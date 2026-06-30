package org.map_bd.surveycalculator.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.map_bd.surveycalculator.AudioActivity
import org.map_bd.surveycalculator.screens.*
import org.map_bd.surveycalculatorr.screens.HomePage

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
//        startDestination = NavScreens.IntroPage.route
        startDestination = NavScreens.HomePage.route
    )
    {
//        composable(route = NavScreens.IntroPage.route) {
//            IntroPage(navController)
//        }
        composable(route = NavScreens.HomePage.route) {
            HomePage(navController)
        }
        composable(route = NavScreens.RecorderPlayerPage.route) {
            RecorderPlayerPage(navController)
        }
        composable(route = NavScreens.AudioPlayerPage.route) {
            AudioPlayerPage(navController)
        }
    }
}