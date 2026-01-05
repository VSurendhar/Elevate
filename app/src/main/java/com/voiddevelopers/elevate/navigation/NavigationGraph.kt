package com.voiddevelopers.elevate.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.voiddevelopers.elevate.presentation.screens.main.MainScreen
import com.voiddevelopers.elevate.presentation.screens.onBoarding.OnboardingScreen
import com.voiddevelopers.elevate.presentation.screens.progressBuilder.ProgressBuilderScreen
import com.voiddevelopers.elevate.presentation.screens.progressviewer.ProgressViewerScreen
import com.voiddevelopers.elevate.presentation.screens.widgetselector.WidgetSelectorScreen

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "onboarding") {
        composable("onboarding") {
            OnboardingScreen(navController)
        }
        composable("main") {
            MainScreen(navController)
        }
        composable("progress_builder") {
            ProgressBuilderScreen(navController)
        }
        composable("progress_viewer") {
            ProgressViewerScreen(navController)
        }
        composable("widget_selector") {
            WidgetSelectorScreen(navController)
        }
    }
}