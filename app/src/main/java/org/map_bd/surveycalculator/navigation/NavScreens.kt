package org.map_bd.surveycalculator.navigation

sealed class NavScreens(val route: String) {
    object IntroPage : NavScreens("IntroPage")
    object HomePage : NavScreens("HomePage")
    object RecorderPlayerPage : NavScreens("RecorderPlayerPage")
    object AudioPlayerPage : NavScreens("AudioPlayerPage")
}